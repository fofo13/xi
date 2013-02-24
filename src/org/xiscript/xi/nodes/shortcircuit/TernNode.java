package org.xiscript.xi.nodes.shortcircuit;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.nodes.OperationNode;

public class TernNode extends OperationNode {

	public TernNode(VariableCache cache) {
		super(null, cache);
	}

	@Override
	public int numChildren() {
		return 3;
	}

	@Override
	public DataType evaluate() {
		return children.get(0).evaluate().isEmpty() ? children.get(2)
				.evaluate() : children.get(1).evaluate();
	}

}