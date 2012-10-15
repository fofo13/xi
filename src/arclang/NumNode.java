package arclang;

public class NumNode extends Node {
	private int n;

	public NumNode(int n) {
		this.n = n;
	}

	public NumNode(String n) {
		this(Integer.parseInt(n));
	}

	public int evaluate() {
		return n;
	}
}