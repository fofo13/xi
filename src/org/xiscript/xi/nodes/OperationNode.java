package org.xiscript.xi.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;
import org.xiscript.xi.operations.Operation;

public class OperationNode implements Node {

	protected Operation op;
	protected List<Node> children;

	public OperationNode(List<Node> children, Operation op) {
		this.op = op;
		this.children = children;
	}

	public OperationNode(Node[] children, Operation op) {
		this(Arrays.asList(children), op);
	}

	public OperationNode(Operation op) {
		this(new ArrayList<Node>((op == null) ? 10 : op.numArgs()), op);
	}

	public List<Node> children() {
		return children;
	}

	protected void checkAndUnpack(VariableCache cache) {
		if ((!children.isEmpty()) && children.get(0) instanceof PackedDataNode)
			children = ((PackedDataNode) children.get(0)).contents(cache);
	}

	@Override
	public void addChild(Node node) {
		children.add(node);
	}

	@Override
	public int numChildren() {
		return op.numArgs();
	}

	@Override
	public DataType evaluate(VariableCache cache) {
		checkAndUnpack(cache);
		DataType[] arr = new DataType[children.size()];
		for (int i = 0; i < arr.length; i++)
			try {
				arr[i] = children.get(i).evaluate(cache);
			} catch (ClassCastException cce) {
				ErrorHandler.invokeError(ErrorType.ARGUMENT, children.get(i));
			}
		try {
			return op.evaluate(arr, cache);
		} catch (ClassCastException cce) {
			ErrorHandler.invokeError(ErrorType.ARGUMENT, op);
			return null;
		}
	}

	@Override
	public void clear() {
		children.clear();
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof OperationNode)
				&& op.equals(((OperationNode) o).op);
	}

	@Override
	public int hashCode() {
		return op.hashCode();
	}

	@Override
	public String toString() {
		return (op == null) ? "op-node" : op.toString();
	}

}