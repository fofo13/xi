package org.xiscript.xi.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;
import org.xiscript.xi.operations.Operation;

public class OperationNode implements Node { /*OperationNode implements Node(interface) that extends Serializable (interface)*/

	private static final long serialVersionUID = 0L;

	protected Operation op;
	protected List<Node> children;

	public OperationNode(List<Node> children, Operation op) {
		this.children = children;
		this.op = op;
	}

	public OperationNode(Node[] children, Operation op) {
		this(Arrays.asList(children), op);
	}

	public OperationNode(Operation op) {
		
		/*Create a linked_list_node if op not is null or  his number of arguments are less then 0 or
		 * an arraylist_node with 10 as number of  arguments if op is null else with 
		 * right number of arguments necessary for that operation 
		 * It's necessary to build an op-node:a field children(is a List<Node>) for arguments 
		 * of operation-node and a field op specify the operation.*/
		
		this((op != null && op.numArgs() < 0) ? new LinkedList<Node>()
				: new ArrayList<Node>((op == null) ? 10 : op.numArgs()), op);
	}

	public List<Node> children() {
		return children;
	}

	public Operation op() {
		return op;
	}

	@Override
	public void addChild(Node node) {
		children.add(node);
	}

	@Override
	public int numChildren() {
		return op.numArgs();
	}

	protected DataType[] processChildren(VariableCache cache) {
		DataType[] arr = new DataType[children.size()];
		for (int i = 0; i < arr.length; i++) {
			try {
				arr[i] = children.get(i).evaluate(cache);
			} catch (ClassCastException cce) {
				ErrorHandler.invokeError(ErrorType.ARGUMENT, children.get(i));
			}
		}
		return arr;
	}

	@Override
	public DataType evaluate(VariableCache cache) {
		try {
			return op.evaluate(cache, processChildren(cache));
		} catch (ClassCastException cce) {
			ErrorHandler.invokeError(ErrorType.ARGUMENT, op);
			return null;
		}
	}

	@Override
	public void literalize() {
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