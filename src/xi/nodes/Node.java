package xi.nodes;

import xi.datatypes.DataType;

public abstract class Node {
	
	protected Node left;
	protected Node right;

	public abstract DataType evaluate();

	public void setLeft(Node node) {
		left = node;
	}

	public void setRight(Node node) {
		right = node;
	}
	
}