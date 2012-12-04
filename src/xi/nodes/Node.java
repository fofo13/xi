package xi.nodes;

import xi.datatypes.DataType;

public interface Node {

	DataType evaluate();
	int numChildren();
	void addChild(Node node);
	
}