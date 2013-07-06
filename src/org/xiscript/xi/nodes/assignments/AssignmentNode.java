package org.xiscript.xi.nodes.assignments;

import org.xiscript.xi.core.Parser;
import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.datatypes.collections.ArgumentList;
import org.xiscript.xi.datatypes.collections.ListWrapper;
import org.xiscript.xi.nodes.Node;
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

	protected void assign(DataType rhs, VariableCache cache) {
		cache.put(new XiVar(((VarNode) children.get(0)).id()), rhs);
	}

	@Override
	public DataType evaluate(VariableCache cache) {
		Node first = children.get(0);

		if (first instanceof VarNode) {
			DataType rhs = children.get(1).evaluate(cache);
			assign(rhs, cache);
			return rhs;
		} else {
			XiVar[] vars = ((ArgumentList) first.evaluate(cache))
					.getJavaAnalog();

			ListWrapper lw = (ListWrapper) children.get(1).evaluate(cache);

			for (int i = 0; i < vars.length; i++)
				cache.put(vars[i], lw.get(i));

			return lw;
		}
	}

	@Override
	public String toString() {
		return Parser.ASSIGNMENT;
	}

}