package org.xiscript.xi.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiNull;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;
import org.xiscript.xi.nodes.Node;
import org.xiscript.xi.nodes.OperationNode;
import org.xiscript.xi.nodes.singletons.StopNode;
import org.xiscript.xi.operations.BuiltInOperation;

public class SyntaxTree {

	private Node[] statements;

	public SyntaxTree(Queue<Node> nodes) {
		Iterator<Node> iter = nodes.iterator();

		while (iter.hasNext()) {
			Node node = iter.next();
			if (node instanceof OperationNode) {
				OperationNode opnode = (OperationNode) node;
				if (opnode.op() == BuiltInOperation.DEF) {
					iter.next().literalize();
					iter.next().literalize();
				} else if (opnode.op() == BuiltInOperation.LAMBDA) {
					iter.next().literalize();
				} else if (opnode.op() == BuiltInOperation.FOR) {
					iter.next().literalize();
				}
			}
		}

		List<Node> statements = new ArrayList<Node>();
		while (!nodes.isEmpty())
			statements.add(create(nodes));

		this.statements = statements.toArray(new Node[statements.size()]);
	}

	public Node[] statements() {
		return statements;
	}

	public DataType evaluate(VariableCache scope) {
		DataType last = XiNull.instance();
		for (Node node : statements)
			last = node.evaluate(scope);
		return last;
	}

	private static Node create(Queue<Node> nodes) {
		if (nodes.isEmpty()) {
			ErrorHandler.invokeError(ErrorType.INCOMPLETE_EXPRESSION);
		}

		Node node = nodes.poll();

		if (node.numChildren() > -1) {
			for (int i = 0; i < node.numChildren(); i++) {
				Node next = create(nodes);
				if (next == StopNode.instance())
					break;
				node.addChild(next);
			}
		} else {
			Node child = null;
			while ((child = create(nodes)) != StopNode.instance()) {
				node.addChild(child);
			}
		}

		return node;
	}
}