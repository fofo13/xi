package xi;

import java.util.ArrayList;
import java.util.List;

class SyntaxTree {
	
	private Node head;
	private String[] tokens;
	
	public SyntaxTree(String exp) {
		// tokens = exp.replaceAll("[\\(\\)]", "").split("\\s+(?![^\\[]*\\])(?![^\\{]*\\})");
		tokens = (new Parser(exp)).tokens();
		head = parse();
	}
	
	public DataType evaluate() {
		return head.evaluate();
	}
	
	private Node parse() {
		ArrayList<Node> nodes = new ArrayList<Node>(tokens.length);
		for (String tok : tokens)
			nodes.add(Node.create(tok));
		return create(nodes);
	}
	
	private static Node create(List<Node> nodes) {
		
		if (nodes.get(0) instanceof OperationNode) {
			OperationNode node = (OperationNode)nodes.remove(0);
			Operation op = node.op();
			
			for (int i = 0 ; i < op.numArgs() ; i++)
				node.addChild(create(nodes));
			
			return node;
		}
		
		return nodes.remove(0);
	}
	
}