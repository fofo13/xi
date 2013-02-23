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
import java.util.regex.Pattern;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiNull;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.exceptions.BreakException;
import org.xiscript.xi.exceptions.ContinueException;
import org.xiscript.xi.exceptions.ControlFlowException;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;
import org.xiscript.xi.exceptions.ReturnException;
import org.xiscript.xi.operations.IntrinsicOperation;

public class XiEnvironment implements Closeable {

	private static final Pattern returnStatement = Pattern
			.compile("return\\s+.*");

	private static final Pattern assignOp = Pattern.compile(":=");

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
			}
		}
	}

	public void put(String statement) {
		if (closed)
			throw new IllegalStateException();

		statements.add(statement);
	}

	private void process(String exp) throws BreakException {
		exp = exp.trim();

		if (exp.equals("break")) {
			if (primary)
				ErrorHandler
						.invokeError(ErrorType.MISPLACED_STATEMENT, "break");

			throw new BreakException();
		}

		if (exp.equals("continue")) {
			if (primary)
				ErrorHandler.invokeError(ErrorType.MISPLACED_STATEMENT,
						"continue");

			throw new ContinueException();
		}

		if (returnStatement.matcher(exp).matches()) {
			if (primary)
				ErrorHandler.invokeError(ErrorType.MISPLACED_STATEMENT, exp);

			throw new ReturnException((new SyntaxTree(Parser.whitespace.split(
					exp, 2)[1], globals)).evaluate());
		}

		if (Parser.containsAssignment(exp)) {
			String[] split = assignOp.split(exp, 2);
			split[0] = split[0].trim();

			if (IntrinsicOperation.idExists(split[0]))
				ErrorHandler.invokeError(ErrorType.INVALID_OVERRIDE, split[0]);

			if (!Parser.identifier.matcher(split[0]).matches())
				ErrorHandler
						.invokeError(ErrorType.INVALID_IDENTIFIER, split[0]);

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
			StringBuilder exp = new StringBuilder(scan.nextLine());
			while (Parser.isIncomplete(exp.toString())) {
				try {
					exp.append(scan.nextLine());
				} catch (NoSuchElementException nsee) {
					ErrorHandler
							.invokeError(ErrorHandler.ErrorType.INCOMPLETE_EXPRESSION);
				}

				if (!exp.toString().endsWith(";"))
					exp.append(';');
			}
			statements.add(exp.toString());
		}
		scan.close();

		return statements;
	}

	public DataType last() {
		return last;
	}

	public void delete() {
		for (XiVar v : globals)
			v.delete();
	}

	@Override
	public void close() {
		closed = true;
	}

}