package org.xiscript.xi.nodes;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;

public interface Node {

	DataType evaluate(VariableCache cache);

	int numChildren();

	void addChild(Node node);

	void clear();

}