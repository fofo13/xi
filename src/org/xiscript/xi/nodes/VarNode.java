package org.xiscript.xi.nodes;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiVar;

public class VarNode implements Node {

	private static final long serialVersionUID = 0L;

	private boolean literal;
	private XiVar var;

	public VarNode(XiVar var) {
		this.var = var;
		literal = false;
	}

	public VarNode(String id) {
		this(new XiVar(id));
	}

	public XiVar var() {
		return var;
	}

	@Override
	public void literalize() {
		literal = true;
	}

	@Override
	public void addChild(Node node) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int numChildren() {
		return 0;
	}

	@Override
	public DataType evaluate(VariableCache cache) {
		return literal ? var : cache.get(var);
	}

	@Override
	public boolean equals(Object o) {
		return var.equals(o);
	}

	@Override
	public int hashCode() {
		return var.hashCode();
	}

	@Override
	public String toString() {
		return var.toString();
	}

}