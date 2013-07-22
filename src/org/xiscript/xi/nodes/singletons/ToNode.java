package org.xiscript.xi.nodes.singletons;

import org.objectweb.asm.MethodVisitor;
import org.xiscript.xi.compilation.Type;
import org.xiscript.xi.compilation.VariableSuite;
import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiNull;
import org.xiscript.xi.nodes.Node;

public class ToNode implements Node {

	private static final long serialVersionUID = 0L;

	private static final ToNode instance = new ToNode();

	private ToNode() {
	}

	public static ToNode instance() {
		return instance;
	}

	@Override
	public void literalize() {
	}

	@Override
	public DataType evaluate(VariableCache cache) {
		return XiNull.instance();
	}

	@Override
	public int numChildren() {
		return 0;
	}

	@Override
	public void addChild(Node node) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof StopNode);
	}

	@Override
	public int hashCode() {
		return Boolean.FALSE.hashCode();
	}

	@Override
	public void emitBytecode(MethodVisitor mv, VariableSuite vs) {
		// TODO Auto-generated method stub

	}

	@Override
	public Type inferType(VariableSuite vs) {
		// TODO Auto-generated method stub
		return null;
	}

}