package org.xiscript.xi.nodes;

import org.xiscript.xi.datatypes.DataType;

public interface Node {

	DataType evaluate();

	int numChildren();

	void addChild(Node node);

}