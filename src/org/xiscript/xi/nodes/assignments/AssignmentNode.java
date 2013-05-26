package org.xiscript.xi.nodes.assignments;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;
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
		if (!(children.get(0) instanceof VarNode))
			ErrorHandler.invokeError(ErrorType.FUNCTION_REASSIGN);

		DataType rhs = children.get(1).evaluate(cache);
		cache.put(((VarNode) children.get(0)).id(), rhs);
		return rhs;
	}

	@Override
	public String toString() {
		return ":=";
	}

}