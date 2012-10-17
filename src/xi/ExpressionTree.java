package xi;

import java.util.ArrayList;
import java.util.List;

class ExpressionTree {
	
	private Node head;
	private String[] tokens;
	
	public ExpressionTree(String exp) {
		tokens = exp.replaceAll("[\\(\\)]", "").split("\\s+(?![^\\[]*\\])(?![^\\{]*\\})");
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
		
		if (nodes.get(0) instanceof NumNode)
			return nodes.remove(0);
		
		if (nodes.get(0) instanceof OperationNode) {
			OperationNode node = (OperationNode)nodes.remove(0);
			Operation op = node.op();
			
			for (int i = 0 ; i < op.numArgs() ; i++)
				node.addChild(create(nodes));
			
			return node;
		}
		
		throw new RuntimeException();
	}
	
}