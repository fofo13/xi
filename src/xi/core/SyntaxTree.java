package xi.core;

import java.util.ArrayDeque;
import java.util.Queue;

import xi.datatypes.DataType;
import xi.nodes.Node;

public class SyntaxTree {

	private Node head;
	private String[] tokens;

	private VariableCache cache;

	public SyntaxTree(String exp, VariableCache cache) {
		tokens = Parser.tokenize(exp);
		this.cache = cache;
		head = parse();
	}

	public SyntaxTree(String exp) {
		this(exp, new VariableCache());
	}

	public DataType evaluate() {
		return head.evaluate();
	}

	private Node parse() {
		Queue<Node> nodes = new ArrayDeque<Node>(tokens.length);
		for (String tok : tokens)
			nodes.add(Parser.parseNode(tok, cache));
		return create(nodes);
	}

	private static Node create(Queue<Node> nodes) {
		if (nodes.isEmpty())
			throw new RuntimeException("Invalid number of arguments specified.");

		Node node = nodes.poll();

		for (int i = 0; i < node.numChildren(); i++)
			node.addChild(create(nodes));

		return node;
	}

}