package xi;

import java.util.ArrayList;
import java.util.List;

public class SyntaxTree {
	
	private Node head;
	private String[] tokens;
	
	private VariableCache cache;
	
	public SyntaxTree(String exp, VariableCache cache) {
		tokens = (new Parser(exp)).tokens();
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
			nodes.add(Node.create(tok));
		return create(nodes);
	}
	
	private Node create(List<Node> nodes) {	
		if (nodes.get(0) instanceof OperationNode) {
			OperationNode node = (OperationNode)nodes.remove(0);
			Operation op = node.op();
			
			for (int i = 0 ; i < op.numArgs() ; i++)
				node.addChild(create(nodes));
			
			return node;
		}
		
		if (nodes.get(0) instanceof VarNode) {
			VarNode node = (VarNode)nodes.get(0);
			nodes.set(0, new VarNode(node.id(), cache));
		}
		
		return nodes.remove(0);
	}
	
}