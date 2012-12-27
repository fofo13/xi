package xi.core;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import xi.datatypes.DataType;
import xi.datatypes.XiNull;
import xi.datatypes.XiVar;

public class XiEnvironment implements Closeable {

	private static final Map<String, VariableCache> stdlib = new HashMap<String, VariableCache>();
	static {
		File dir = new File("src/modules/");
		for (File child : dir.listFiles()) {
			XiEnvironment sub = null;
			try {
				sub = new XiEnvironment(child, false);
			} catch (FileNotFoundException fnfe) {
				System.err.println("Internal error occured.");
				System.exit(-1);
			}
			stdlib.put(child.getName().split("\\.")[0], sub.globals());
			sub.close();
		}
	}

	private VariableCache globals;
	private DataType last;
	private boolean closed;

	public XiEnvironment(VariableCache cache) {
		globals = cache;
		last = XiNull.instance();
	}

	public XiEnvironment() {
		this(new VariableCache());
	}

	private XiEnvironment(File file, boolean stdImports) throws FileNotFoundException {
		this();
		
		if (stdImports) {
			load("const");
			load("stdlib");
		}
		
		LineNumberReader r = new LineNumberReader(new FileReader(file));
		Scanner scan = new Scanner(r);
		while (scan.hasNext()) {
			String exp = scan.nextLine();
			while (Parser.isIncomplete(exp)) {
				exp += scan.nextLine();
				if (!exp.endsWith(";"))
					exp += ";";
			}
			try {
				put(exp);
			} catch (Exception e) {
				System.err.println("Error: " + e.getMessage());
			}
		}
		scan.close();
	}
	
	public XiEnvironment(File file) throws FileNotFoundException {
		this(file, true);
	}

	public VariableCache globals() {
		return globals;
	}

	public void put(String statement) {
		if (closed)
			throw new RuntimeException("XiEnvironment is closed.");

		for (String exp : Parser.splitOnSemiColons(statement)) {
			if (exp.isEmpty())
				continue;
			process(exp);
		}
	}

	private void process(String exp) {
		if (exp.matches("\\s?+import\\s+.*")) {
			load(exp.trim().split("\\s+")[1].replace(".", "/"));
			return;
		}
		if (Parser.containsAssignment(exp)) {
			int n = exp.indexOf(":=");
			String[] split = new String[] { exp.substring(0, n),
					exp.substring(n + 2) };
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

	public DataType last() {
		return last;
	}

	@Override
	public void close() {
		closed = true;
	}

}