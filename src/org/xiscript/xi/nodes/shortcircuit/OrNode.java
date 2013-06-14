package org.xiscript.xi.nodes.shortcircuit;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.numeric.XiInt;
import org.xiscript.xi.nodes.OperationNode;
import org.xiscript.xi.operations.ShortCircuitOperation;

public class OrNode extends OperationNode {

	public OrNode() {
		super(null);
	}

	@Override
	public int numChildren() {
		return 2;
	}

	@Override
	public DataType evaluate(VariableCache cache) {
		return new XiInt(!children.get(0).evaluate(cache).isEmpty()
				|| !children.get(1).evaluate(cache).isEmpty());
	}

	@Override
	public String toString() {
		return ShortCircuitOperation.OR.toString();
	}

}