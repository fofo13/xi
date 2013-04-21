package org.xiscript.xi.nodes.assignments;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.numeric.XiInt;
import org.xiscript.xi.nodes.VarNode;
import org.xiscript.xi.operations.BuiltInOperation;

public class MinusMinusNode extends AssignmentNode {

	@Override
	public int numChildren() {
		return 1;
	}

	@Override
	public DataType evaluate(VariableCache cache) {
		String id = ((VarNode) children.get(0)).id();
		DataType rhs = BuiltInOperation.SUBTRACT.evaluate(cache.get(id),
				new XiInt(1));
		cache.put(id, rhs);
		return rhs;
	}

}