package org.xiscript.xi.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiNull;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.exceptions.ControlFlowException;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;
import org.xiscript.xi.nodes.Node;

public class XiEnvironment {

	private Queue<Character> source;
	private VariableCache globals;

	protected XiEnvironment(InputStream file, boolean primary) {
		globals = new VariableCache();

		if (primary) {
			globals.addAll(ModuleLoader.stdlib.get("stdlib").contents());
		}

		source = compile(file);
	}

	public XiEnvironment(File file) throws FileNotFoundException {
		this(new FileInputStream(file), true);
	}

	public VariableCache globals() {
		return globals;
	}

	public DataType run() {
		DataType last = XiNull.instance();
		Queue<Node> nodes = Parser.genNodeQueue(source);
		try {
			while (!nodes.isEmpty()) {
				SyntaxTree tree = new SyntaxTree(nodes, globals);
				last = tree.evaluate();
				nodes = tree.nodes();
			}
		} catch (ControlFlowException cfe) {
			throw cfe;
		}
		return last;
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
		for (XiVar v : globals)
			v.delete();
	}

}