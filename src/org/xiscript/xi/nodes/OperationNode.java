package org.xiscript.xi.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.objectweb.asm.MethodVisitor;
import org.xiscript.xi.compilation.Type;
import org.xiscript.xi.compilation.VariableSuite;
import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;
import org.xiscript.xi.operations.Operation;

public class OperationNode implements Node {

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

	@Override
	public void emitBytecode(MethodVisitor mv, VariableSuite vs) {
		op.emitBytecode(mv, vs, children.toArray(new Node[children.size()]));
	}

	@Override
	public Type inferType(VariableSuite vs) {
		Type[] argTypes = new Type[children.size()];
		for (int i = 0; i < argTypes.length; i++)
			argTypes[i] = children.get(i).inferType(vs);

		return op.resultingType(argTypes);
	}

}