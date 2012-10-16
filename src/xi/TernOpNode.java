package xi;

public class TernOpNode extends Node {
	
	private TernaryOperation op;
	private Node center;  // 3 total children nodes
	
	public TernOpNode(TernaryOperation op) {
		this.op = op;
	}
	
	public TernOpNode(String exp) {
		this(TernaryOperation.parse(exp));
	}
	
	public void setCenter(Node node) {
		center = node;
	}
	
	public int evaluate() {
		return op.perform(left.evaluate(), center.evaluate(), right.evaluate());
	}
	
}