package xi.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import xi.Operation;
import xi.VariableCache;
import xi.datatypes.DataType;

public class OperationNode extends Node {
	
	private Operation op;
	private List<Node> children;
	private VariableCache cache;
	
	public OperationNode(List<Node> children, Operation op, VariableCache cache) {
		this.op = op;
		this.children = children;
		this.cache = cache;
	}
	
	public OperationNode(Node[] children, Operation op, VariableCache cache) {
		this(Arrays.asList(children), op, cache);
	}
	
	public OperationNode(Operation op, VariableCache cache) {
		this(new ArrayList<Node>(), op, cache);
	}
	
	public List<Node> children() { return children; }
	
	@Override
	public void addChild(Node node) {
		children.add(node);
	}
	
	@Override
	public int numChildren() {
		return op.numArgs();
	}
	
	@Override
	public DataType evaluate() {
		DataType[] arr = new DataType[children.size()];
		for (int i = 0 ; i < arr.length ; i++)
			arr[i] = children.get(i).evaluate();
		return op.evaluate(arr, cache);
	}
	
}