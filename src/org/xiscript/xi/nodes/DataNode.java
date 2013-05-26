package org.xiscript.xi.nodes;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;

public class DataNode<T extends DataType> implements Node {

	protected T data;

	public DataNode(T data) {
		this.data = data;
	}

	public DataType data() {
		return data;
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
		return data;
	}

	@Override
	public void clear() {

	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof DataNode) && data.equals(((DataNode<?>) o).data);
	}

	@Override
	public int hashCode() {
		return data.hashCode();
	}

	@Override
	public String toString() {
		return data == null ? "null" : data.toString();
	}

}