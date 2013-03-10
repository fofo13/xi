package org.xiscript.xi.nodes.assignments;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.nodes.OperationNode;
import org.xiscript.xi.nodes.VarNode;

public class AssignmentNode extends OperationNode {

	public AssignmentNode() {
		super(null);
	}

	@Override
	public int numChildren() {
		return 2;
	}

	@Override
	public DataType evaluate(VariableCache cache) {
		DataType rhs = children.get(1).evaluate(cache);
		XiVar var = new XiVar(((VarNode) children.get(0)).id(), rhs);
		cache.add(var);
		return rhs;
	}

}