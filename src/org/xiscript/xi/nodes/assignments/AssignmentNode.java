package org.xiscript.xi.nodes.assignments;

import org.xiscript.xi.core.Parser;
import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiAttribute;
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
		String[] v = VarNode.DOT.split(((VarNode) children.get(0)).id());

		if (v.length == 1) {
			cache.put(v[0], rhs);
		} else {
			DataType lhs = cache.get(v[0]);
			for (int i = 1; i < v.length - 1; i++)
				lhs = lhs.getAttribute(XiAttribute.valueOf(v[i]));

			lhs.setAttribute(XiAttribute.valueOf(v[v.length - 1]), rhs);
		}
	}

	@Override
	public DataType evaluate(VariableCache cache) {
		DataType rhs = children.get(1).evaluate(cache);
		assign(rhs, cache);
		return rhs;
	}

	@Override
	public String toString() {
		return Parser.ASSIGNMENT;
	}

}