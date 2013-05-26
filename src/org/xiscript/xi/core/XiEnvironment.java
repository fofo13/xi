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
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;
import org.xiscript.xi.nodes.Node;
import org.xiscript.xi.nodes.OperationNode;
import org.xiscript.xi.nodes.assignments.AssignmentNode;
import org.xiscript.xi.operations.BuiltInOperation;

public class XiEnvironment {

	private Queue<Character> source;

	public static final VariableCache globals = new VariableCache();
	private VariableCache locals;

	protected XiEnvironment(InputStream file, boolean primary) {
		if (primary) {
			globals.putAll(ModuleLoader.stdlib.get("stdlib").contents());
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
		DataType last = XiNull.instance();

		Queue<Node> nodes = Parser.genNodeQueue(source);
		Queue<Node> refreshed = new LinkedList<Node>();

		while (!nodes.isEmpty()) {
			if (nodes.peek() instanceof AssignmentNode) {
				Node assign = nodes.poll();
				Node var = nodes.poll();
				Node op = nodes.poll();

				if (op instanceof OperationNode
						&& ((OperationNode) op).op() == BuiltInOperation.FUNC) {

					assign.addChild(var);
					assign.addChild(op);
					op.addChild(nodes.poll());
					op.addChild(nodes.poll());

					assign.evaluate(locals == null ? globals : locals);
				} else {
					refreshed.add(assign);
					refreshed.add(var);
					refreshed.add(op);
				}
			} else {
				refreshed.add(nodes.poll());
			}
		}

		while (!refreshed.isEmpty()) {
			SyntaxTree tree = new SyntaxTree(refreshed,
					locals == null ? globals : locals);
			last = tree.evaluate();
			refreshed = tree.nodes();
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
		(locals == null ? globals : locals).clear();
	}

}