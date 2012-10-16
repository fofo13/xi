package xi;

public class NumNode extends Node {
	private Xi n;

	public NumNode(int n) {
		this.n = new Xi(n);
	}

	public NumNode(String n) {
		this(Integer.parseInt(n));
	}

	public int evaluate() {
		return n.val();
	}
}