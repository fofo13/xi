package org.xiscript.xi.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.operations.Operation;


public class OperationNode implements Node {
	
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
			try {
				arr[i] = children.get(i).evaluate();
			} catch (ClassCastException cce) {
				throw new RuntimeException("Invalid argument types for operator: " + children.get(i));
			}
		return op.evaluate(arr, cache);
	}
	
	@Override
	public String toString() {
		return op.toString();
	}
	
}