package org.xiscript.xi.nodes.assignments;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.datatypes.numeric.XiInt;
import org.xiscript.xi.nodes.VarNode;
import org.xiscript.xi.operations.IntrinsicOperation;

public class PlusPlusNode extends AssignmentNode {

	@Override
	public int numChildren() {
		return 1;
	}

	@Override
	public DataType evaluate(VariableCache cache) {
		String id = ((VarNode) children.get(0)).id();
		DataType rhs = IntrinsicOperation.ADD.evaluate(cache.get(id),
				new XiInt(1));
		XiVar var = new XiVar(id, rhs);
		cache.add(var);
		return rhs;
	}

}