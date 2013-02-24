package org.xiscript.xi.nodes.shortcircuit;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.numeric.XiInt;
import org.xiscript.xi.nodes.OperationNode;

public class AndNode extends OperationNode {

	public AndNode(VariableCache cache) {
		super(null, cache);
	}

	@Override
	public int numChildren() {
		return 2;
	}

	@Override
	public DataType evaluate() {
		return new XiInt(!children.get(0).evaluate().isEmpty()
				&& !children.get(1).evaluate().isEmpty());
	}

}