package org.xiscript.xi.nodes.assignments;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.nodes.VarNode;
import org.xiscript.xi.operations.BuiltInOperation;

public class CompoundAssignmentNode extends AssignmentNode {

	private BuiltInOperation op;

	public CompoundAssignmentNode(BuiltInOperation op) {
		this.op = op;
	}

	@Override
	public DataType evaluate(VariableCache cache) {
		String id = ((VarNode) children.get(0)).id();
		DataType rhs = op.evaluate(cache.get(id),
				children.get(1).evaluate(cache));
		XiVar var = new XiVar(id, rhs);
		cache.add(var);
		return rhs;
	}

}
