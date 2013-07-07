package org.xiscript.xi.nodes.assignments;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.nodes.VarNode;
import org.xiscript.xi.operations.BuiltInOperation;

public class CompoundAssignmentNode extends AssignmentNode {

	private static final long serialVersionUID = 0L;

	private BuiltInOperation op;

	public CompoundAssignmentNode(BuiltInOperation op) {
		this.op = op;
	}

	@Override
	public DataType evaluate(VariableCache cache) {
		String id = ((VarNode) children.get(0)).id();
		DataType rhs = op.evaluate(cache.get(id),
				children.get(1).evaluate(cache));
		assign(rhs, cache);
		return rhs;
	}

}
