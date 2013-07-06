package org.xiscript.xi.nodes;

import java.util.regex.Pattern;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiVar;

public class VarNode implements Node {

	public static final Pattern DOT = Pattern.compile("\\.");

	private boolean literal;
	private XiVar id;

	public VarNode(XiVar id) {
		this.id = id;
		literal = false;
	}

	public VarNode(String id) {
		this(new XiVar(id));
	}

	public String id() {
		return id.id();
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
		return literal ? id : cache.get(id.id());
	}

	@Override
	public void clear() {
	}

	@Override
	public boolean equals(Object o) {
		return id.equals(o);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public String toString() {
		return id.toString();
	}

}