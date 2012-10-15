package arclang;

public abstract class Node {
	protected Node left;
	protected Node right;

	public abstract int evaluate();

	public void setLeft(Node node) {
		left = node;
	}

	public void setRight(Node node) {
		right = node;
	}
}