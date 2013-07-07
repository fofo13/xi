package org.xiscript.xi.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;

public class XiEnvironment {

	private Queue<Character> source;
	private VariableCache scope;

	protected XiEnvironment(InputStream file, boolean primary) {
		scope = new VariableCache();

		if (primary) {
			scope.putAll(ModuleLoader.get("stdlib").contents());
		}

		source = compile(file);
	}

	public XiEnvironment(File file) throws FileNotFoundException {
		this(new FileInputStream(file), true);
	}

	public VariableCache scope() {
		return scope;
	}

	public DataType run() {
		return new SyntaxTree(Parser.genNodeQueue(source)).evaluate(scope);
	}

	private static Queue<Character> compile(InputStream file) {
		Queue<Character> source = new LinkedList<Character>();

		int next = -1;

		try {
			while ((next = file.read()) != -1) {
				source.add((char) next);
			}
			file.close();
		} catch (IOException ioe) {
			ErrorHandler.invokeError(ErrorType.INTERNAL);
		}

		return source;
	}

}