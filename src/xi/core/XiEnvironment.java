package xi.core;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import xi.datatypes.DataType;
import xi.datatypes.XiVar;

public class XiEnvironment implements Closeable {

	private VariableCache globals;
	private DataType last;
	private boolean closed;

	public XiEnvironment(VariableCache cache) {
		globals = cache;
	}

	public XiEnvironment() {
		this(new VariableCache());
	}

	public XiEnvironment(File file) throws FileNotFoundException {
		this();
		Scanner scan = new Scanner(file);
		while (scan.hasNext()) {
			String exp = scan.nextLine();
			while (Parser.isIncomplete(exp)) {
				exp += scan.nextLine();
				if (! exp.endsWith(";"))
					exp += ";";
			}
			put(exp);
		}
		scan.close();
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
			if (exp.startsWith("import ")) {
				String name = exp.split("\\s+")[1].replace(".", "/") + ".xi";
				try {
					File file = new File(name);
					XiEnvironment env = new XiEnvironment(file);
					globals.addAll(env.globals());
					env.close();
				} catch (FileNotFoundException fnfe) {
					throw new RuntimeException("Import could not be resolved: " + name);
				}
				return;
			}
			if (Parser.containsAssignment(exp)) {
				int n = exp.indexOf(":=");
				String[] split = new String[] { exp.substring(0, n),
						exp.substring(n + 2) };
				globals.add(new XiVar(split[0].trim(), new SyntaxTree(split[1]
						.trim(), globals).evaluate()));
			} else
				last = (new SyntaxTree(exp, globals)).evaluate();
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