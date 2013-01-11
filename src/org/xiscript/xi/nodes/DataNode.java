package org.xiscript.xi.nodes;

import org.xiscript.xi.datatypes.DataType;

public class DataNode<T extends DataType> implements Node {
	
	private T data;
	
	public DataNode(T data) {
		this.data = data;
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
	public DataType evaluate() {
		return data;
	}
	
}