package org.xiscript.xi.core;

import java.util.ArrayDeque;
import java.util.Queue;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;
import org.xiscript.xi.nodes.Node;
import org.xiscript.xi.nodes.PackedDataNode;

public class SyntaxTree {

	private Node head;
	private Queue<Node> nodes;

	public SyntaxTree(String exp, VariableCache cache) {
		String[] tokens = Parser.tokenize(exp);
		nodes = new ArrayDeque<Node>(tokens.length);

		for (String tok : tokens) {
			Node node = Parser.parseNode(tok, cache);

			if (node instanceof PackedDataNode)
				nodes.addAll(((PackedDataNode) node).contents());
			else
				nodes.add(node);
		}
	}

	public SyntaxTree(String exp) {
		this(exp, new VariableCache());
	}

	public Queue<Node> nodes() {
		return nodes;
	}

	public DataType evaluate() {
		head = parse();
		return head.evaluate();
	}

	private Node parse() {
		return create();
	}

	private Node create() {
		if (nodes.isEmpty())
			ErrorHandler.invokeError(ErrorType.INCOMPLETE_EXPRESSION);

		Node node = nodes.poll();

		for (int i = 0; i < node.numChildren(); i++)
			node.addChild(create());

		return node;
	}

}