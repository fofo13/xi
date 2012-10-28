package xi.nodes;

import xi.datatypes.DataType;

public class DataNode<T extends DataType> extends Node {
	
	private T data;
	
	public DataNode(T data) {
		this.data = data;
	}
	
	@Override
	public void addChild(Node node) {
		
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