package xi;

public class NumNode extends Node {
	private XiNum n;

	public NumNode(int n) {
		this.n = new XiNum(n);
	}

	public NumNode(String n) {
		this(Integer.parseInt(n));
	}

	public int evaluate() {
		return n.val();
	}
}