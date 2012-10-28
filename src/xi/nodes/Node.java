package xi.nodes;

import xi.datatypes.DataType;

public abstract class Node {

	public abstract DataType evaluate();
	public abstract int numChildren();
	public abstract void addChild(Node node);
	
}