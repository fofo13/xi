package xi.nodes;

import xi.datatypes.DataType;

public interface Node {

	public DataType evaluate();
	public int numChildren();
	public void addChild(Node node);
	
}