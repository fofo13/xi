package arclang;

import java.util.ArrayList;
import java.util.List;

class ExpressionTree {
	
	private Node head;
	private String[] tokens;
	
	public ExpressionTree(String exp) {
		tokens = exp.replaceAll("[\\(\\)]", "").split("\\s");
		head = parse();
	}
	
	public int evaluate() {
		return head.evaluate();
	}
	
	private Node parse() {
		ArrayList<Node> nodes = new ArrayList<Node>(tokens.length);
		for (String tok : tokens)
			nodes.add(tok.matches("\\?") ? new TernOpNode(tok) : tok.matches("-?\\d+") ? 
					new NumNode(tok) : tok.matches("[!~]") ? new UnOpNode(tok) : 
						new BinOpNode(tok));
		return create(nodes);
	}
	
	// this will likely be moved
	private static Node create(List<Node> nodes) {
		if (nodes.get(0) instanceof NumNode)
			return nodes.remove(0);
		
		if (nodes.get(0) instanceof UnOpNode) {
			UnOpNode node = (UnOpNode)nodes.remove(0);
			node.setLeft(create(nodes));
			return node;
		}
		
		if (nodes.get(0) instanceof BinOpNode) {
			BinOpNode node = (BinOpNode)nodes.remove(0);
			node.setLeft(create(nodes));
			node.setRight(create(nodes));
			return node;
		}
		
		
		if (nodes.get(0) instanceof TernOpNode) {
			TernOpNode node = (TernOpNode)nodes.remove(0);
			node.setLeft(create(nodes));
			node.setCenter(create(nodes));
			node.setRight(create(nodes));
		}
		
		throw new RuntimeException();
	}
	
}