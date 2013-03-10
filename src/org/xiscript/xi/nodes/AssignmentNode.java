package org.xiscript.xi.nodes;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiNull;
import org.xiscript.xi.datatypes.XiVar;

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
		XiVar var = new XiVar(((VarNode) children.get(0)).id(), children.get(1)
				.evaluate(cache));
		cache.add(var);
		return XiNull.instance();
	}

}