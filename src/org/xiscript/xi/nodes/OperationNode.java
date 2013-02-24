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
	protected VariableCache cache;

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

	public List<Node> children() {
		return children;
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
	public DataType evaluate() {
		DataType[] arr = new DataType[children.size()];
		for (int i = 0; i < arr.length; i++)
			try {
				arr[i] = children.get(i).evaluate();
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
	public String toString() {
		return op.toString();
	}

}