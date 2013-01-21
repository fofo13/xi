package org.xiscript.xi.core;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiNull;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.exceptions.BreakException;
import org.xiscript.xi.exceptions.ContinueException;
import org.xiscript.xi.exceptions.ControlFlowException;
import org.xiscript.xi.exceptions.ReturnException;
import org.xiscript.xi.operations.IntrinsicOperation;

public class XiEnvironment implements Closeable {

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

	protected XiEnvironment(InputStream file, boolean primary)
			throws FileNotFoundException {
		this();

		this.primary = primary;
		if (primary) {
			globals.addAll(ModuleLoader.stdlib.get("stdlib").contents());
			globals.addAll(ModuleLoader.stdlib.get("const").contents());
		}

		statements = compile(file);
	}

	public XiEnvironment(File file) throws FileNotFoundException {
		this(new FileInputStream(file), true);
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
				System.exit(-1);
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
			split[0] = split[0].trim();

			if (IntrinsicOperation.idExists(split[0]))
				throw new RuntimeException(
						"Cannot redefine built-in function: " + split[0]);

			if (!split[0].matches("[\\.\\p{Alpha}_]\\w*"))
				throw new RuntimeException("Invalid variable identifier: "
						+ split[0]);

			globals.add(new XiVar(split[0].trim(), new SyntaxTree(split[1]
					.trim(), globals).evaluate()));
		} else {
			last = (new SyntaxTree(exp, globals)).evaluate();
		}

	}

	private static List<String> compile(InputStream file)
			throws FileNotFoundException {
		List<String> statements = new ArrayList<String>();

		Scanner scan = new Scanner(file);
		while (scan.hasNext()) {
			String exp = scan.nextLine();
			while (Parser.isIncomplete(exp)) {
				try {
					exp += scan.nextLine();
				} catch (NoSuchElementException nsee) {
					System.err
							.println("Error: Incomplete expression detected.");
					System.exit(-1);
				}
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