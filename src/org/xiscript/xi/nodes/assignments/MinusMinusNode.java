package org.xiscript.xi.nodes.assignments;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.datatypes.numeric.XiInt;
import org.xiscript.xi.nodes.VarNode;
import org.xiscript.xi.operations.BuiltInOperation;

public class MinusMinusNode extends AssignmentNode {

	private static final long serialVersionUID = 0L;

	@Override
	public int numChildren() {
		return 1;
	}

	@Override
	public DataType evaluate(VariableCache cache) {
		String id = ((VarNode) children.get(0)).id();
		DataType rhs = BuiltInOperation.SUB.evaluate(cache.get(id),
				new XiInt(1));
		cache.put(new XiVar(id), rhs);
		return rhs;
	}

}