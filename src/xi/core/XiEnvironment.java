package xi.core;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.Scanner;

import xi.datatypes.DataType;
import xi.datatypes.XiNull;
import xi.datatypes.XiVar;

public class XiEnvironment implements Closeable {

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

	public XiEnvironment(File file) throws FileNotFoundException {
		this();
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
			load(exp.trim().split("\\s+")[1].replace(".", "/") + ".xi");
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
		try {
			File file = new File(name);
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