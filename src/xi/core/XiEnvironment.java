package xi.core;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import xi.datatypes.DataType;
import xi.datatypes.XiNull;
import xi.datatypes.XiVar;
import xi.exceptions.BreakException;
import xi.exceptions.ContinueException;
import xi.exceptions.ControlFlowException;
import xi.exceptions.ReturnException;

public class XiEnvironment implements Closeable {

	private static final Map<String, VariableCache> stdlib = new HashMap<String, VariableCache>();

	static {
		File dir = new File("src/xi/modules/");
		for (File child : dir.listFiles()) {
			XiEnvironment sub = null;
			try {
				sub = new XiEnvironment(child, false);
				sub.run();
			} catch (FileNotFoundException fnfe) {
				System.err.println("Internal error occured.");
				System.exit(-1);
			}
			stdlib.put(child.getName().split("\\.")[0], sub.globals());
			sub.close();
		}
	}

	private List<String> statements;

	private VariableCache globals;
	private DataType last;
	private boolean primary;
	private boolean closed;

	public XiEnvironment(VariableCache cache) {
		statements = new ArrayList<String>();

		globals = cache;
		last = XiNull.instance();
		primary = false;
	}

	public XiEnvironment() {
		this(new VariableCache());
	}

	private XiEnvironment(File file, boolean primary)
			throws FileNotFoundException {
		this();

		this.primary = primary;
		if (primary) {
			load("const");
			load("stdlib");
		}

		statements = compile(file);
	}

	public XiEnvironment(File file) throws FileNotFoundException {
		this(file, true);
	}

	public VariableCache globals() {
		return globals;
	}

	public void run() {
		for (String statement : statements) {
			try {
				for (String exp : Parser.splitOnSemiColons(statement)) {
					if (exp.isEmpty())
						continue;
					process(exp);
				}
			} catch (ControlFlowException cfe) {
				throw cfe;
			} catch (Exception e) {
				System.err.println("Error: " + e.getMessage());
			}
		}
	}

	public void put(String statement) {
		if (closed)
			throw new RuntimeException("XiEnvironment is closed.");

		statements.add(statement);
	}

	private void process(String exp) throws BreakException {
		exp = exp.trim();

		if (exp.matches("import\\s+.*")) {
			load(exp.trim().split("\\s+", 2)[1].replace(".", "/"));
			return;
		}
		if (exp.equals("break")) {
			if (primary)
				throw new RuntimeException("break statement misplaced");

			throw new BreakException();
		}
		if (exp.equals("continue")) {
			if (primary)
				throw new RuntimeException("break statement misplaced");

			throw new ContinueException();
		}
		if (exp.matches("return\\s+.*")) {
			if (primary)
				throw new RuntimeException("return statement misplaced");

			throw new ReturnException((new SyntaxTree(exp.split("\\s+", 2)[1],
					globals)).evaluate());
		}

		if (Parser.containsAssignment(exp)) {
			String[] split = exp.split(":=", 2);
			globals.add(new XiVar(split[0].trim(), new SyntaxTree(split[1]
					.trim(), globals).evaluate()));
		} else {
			last = (new SyntaxTree(exp, globals)).evaluate();
		}

	}

	private void load(String name) {
		if (stdlib.containsKey(name)) {
			globals.addAll(stdlib.get(name));
			return;
		}
		try {
			File file = new File(name + ".xi");
			XiEnvironment env = new XiEnvironment(file);
			globals.addAll(env.globals());
			env.close();
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException("Import could not be resolved: " + name);
		}
	}

	private static List<String> compile(File file) throws FileNotFoundException {
		List<String> statements = new ArrayList<String>();

		Scanner scan = new Scanner(file);
		while (scan.hasNext()) {
			String exp = scan.nextLine();
			while (Parser.isIncomplete(exp)) {
				exp += scan.nextLine();
				if (!exp.endsWith(";"))
					exp += ";";
			}
			statements.add(exp);
		}
		scan.close();

		return statements;
	}

	public DataType last() {
		return last;
	}

	@Override
	public void close() {
		closed = true;
	}

}