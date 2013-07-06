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
import org.xiscript.xi.nodes.Node;

public class XiEnvironment {

	private Queue<Character> source;

	public static final VariableCache globals = new VariableCache();
	private VariableCache locals;

	protected XiEnvironment(InputStream file, boolean primary) {
		if (primary) {
			globals.putAll(ModuleLoader.get("stdlib").contents());
		} else {
			locals = new VariableCache();
		}

		source = compile(file);
	}

	public XiEnvironment(File file) throws FileNotFoundException {
		this(new FileInputStream(file), true);
	}

	public VariableCache cache() {
		return locals == null ? globals : locals;
	}

	public DataType run() {
		Queue<Node> nodes = Parser.genNodeQueue(source);

		return new SyntaxTree(nodes).evaluate(cache());
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

	public void delete() {
		(locals == null ? globals : locals).clear();
	}

}