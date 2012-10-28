package xi;

import java.util.ArrayList;
import java.util.List;

import xi.datatypes.DataType;
import xi.nodes.Node;
//import xi.nodes.OperationNode;

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
		ArrayList<Node> nodes = new ArrayList<Node>(tokens.length);
		for (String tok : tokens)
			nodes.add(Parser.parseNode(tok, cache));
		return create(nodes);
	}

	private Node create(List<Node> nodes) {
		Node node = nodes.remove(0);
		
		for (int i = 0; i < node.numChildren(); i++)
			node.addChild(create(nodes));

		return node;
	}

}