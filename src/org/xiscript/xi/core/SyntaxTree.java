package org.xiscript.xi.core;

import java.util.ArrayDeque;
import java.util.Queue;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.functional.XiFunc;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;
import org.xiscript.xi.nodes.Node;
import org.xiscript.xi.nodes.OperationNode;
import org.xiscript.xi.nodes.StopNode;
import org.xiscript.xi.nodes.VarNode;

public class SyntaxTree {

	private Queue<Node> nodes;
	private VariableCache globals;
	private Node head;

	public SyntaxTree(Queue<Node> nodes, VariableCache globals) {
		this.globals = globals;

		Queue<Node> newNodes = new ArrayDeque<Node>(nodes.size());

		for (Node node : nodes) {
			if (node instanceof VarNode) {
				VarNode vnode = (VarNode) node;

				if (globals.containsId(vnode.id())
						&& globals.get(vnode.id()) instanceof XiFunc) {
					newNodes.add(new OperationNode((XiFunc) globals.get(vnode
							.id())));
				} else {
					newNodes.add(node);
				}
			} else {
				newNodes.add(node);
			}
		}

		this.nodes = newNodes;
		head = create(this.nodes);
	}

	public Queue<Node> nodes() {
		return nodes;
	}

	public VariableCache globals() {
		return globals;
	}

	public DataType evaluate() {
		return head.evaluate(globals);
	}

	public static Node create(Queue<Node> nodes) {
		if (nodes.isEmpty()) {
			ErrorHandler.invokeError(ErrorType.INCOMPLETE_EXPRESSION);
		}

		Node node = nodes.poll();

		if (node.numChildren() > -1) {
			for (int i = 0; i < node.numChildren(); i++) {
				Node next = create(nodes);
				if (next instanceof StopNode)
					break;
				node.addChild(next);
			}
		} else {
			Node child = null;
			while (!(child = create(nodes)).equals(StopNode.instance)) {
				node.addChild(child);
			}
		}

		return node;
	}

}