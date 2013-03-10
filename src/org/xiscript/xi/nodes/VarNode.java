package org.xiscript.xi.nodes;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;

public class VarNode implements Node {

	private String id;

	public VarNode(String id) {
		this.id = id;
	}

	public String id() {
		return id;
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
		return cache.get(id);
	}

	@Override
	public void clear() {

	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof VarNode) && id.equals(((VarNode) o).id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public String toString() {
		return id;
	}

}