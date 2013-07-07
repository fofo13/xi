package org.xiscript.xi.nodes;

import java.io.Serializable;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;

public interface Node extends Serializable {

	DataType evaluate(VariableCache cache);

	int numChildren();

	void addChild(Node node);

	void clear();

	void literalize();

}