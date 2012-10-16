package xi;

public class BinOpNode extends Node {
	private BinaryOperation op;
	
	public BinOpNode(BinaryOperation op) {
		this.op = op;
	}
	
	public BinOpNode(String exp) {
		this(BinaryOperation.parse(exp));
	}
	
	public int evaluate() {
		return op.perform(left.evaluate(), right.evaluate());
	}
}