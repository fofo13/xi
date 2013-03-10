package org.xiscript.xi.core;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.functional.XiFunc;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;
import org.xiscript.xi.nodes.Node;
import org.xiscript.xi.nodes.OperationNode;
import org.xiscript.xi.nodes.VarNode;

public class SyntaxTree {

	private Queue<Node> nodes;
	private VariableCache globals;

	private Node head;

	public SyntaxTree(Queue<Node> nodes, VariableCache globals) {
		this.globals = globals;

		List<Node> nodesList = new ArrayList<Node>(nodes.size());

		for (Node node : nodes) {
			if (node instanceof VarNode) {
				VarNode vnode = (VarNode) node;
				if (globals.containsId(vnode.id())
						&& globals.get(vnode.id()) instanceof XiFunc)
					nodesList.add(new OperationNode((XiFunc) globals.get(vnode
							.id())));
				else
					nodesList.add(node);
			} else
				nodesList.add(node);
		}

		this.nodes = new ArrayDeque<Node>(nodesList);
		head = create(this.nodes);
	}

	public SyntaxTree(String exp, VariableCache globals) {
		this(Parser.genNodeQueue(exp), globals);
	}

	public Queue<Node> nodes() {
		return nodes;
	}

	public DataType evaluate() {
		return head.evaluate(globals);
	}

	public static Node create(Queue<Node> nodes) {
		if (nodes.isEmpty()) {
			ErrorHandler.invokeError(ErrorType.INCOMPLETE_EXPRESSION);
		}

		Node node = nodes.poll();

		for (int i = 0; i < node.numChildren(); i++) {
			node.addChild(create(nodes));
		}

		return node;
	}

}