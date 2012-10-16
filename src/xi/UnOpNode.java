package xi;

public class UnOpNode extends Node {
	private UnaryOperation op;
	
	public UnOpNode(UnaryOperation op) {
		this.op = op;
	}
	
	public UnOpNode(String exp) {
		this(UnaryOperation.parse(exp));
	}
	
	public int evaluate() {
		return op.perform(left.evaluate());
	}
}