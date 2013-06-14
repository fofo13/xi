package org.xiscript.xi.nodes.shortcircuit;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.nodes.OperationNode;
import org.xiscript.xi.operations.ShortCircuitOperation;

public class TernNode extends OperationNode {

	public TernNode() {
		super(null);
	}

	@Override
	public int numChildren() {
		return 3;
	}

	@Override
	public DataType evaluate(VariableCache cache) {
		return children.get(0).evaluate(cache).isEmpty() ? children.get(2)
				.evaluate(cache) : children.get(1).evaluate(cache);
	}

	@Override
	public String toString() {
		return ShortCircuitOperation.TERN.toString();
	}

}