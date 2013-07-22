package org.xiscript.xi.datatypes.functional;

import org.objectweb.asm.MethodVisitor;
import org.xiscript.xi.compilation.Type;
import org.xiscript.xi.compilation.VariableSuite;
import org.xiscript.xi.datatypes.XiType;
import org.xiscript.xi.datatypes.collections.ArgumentList;
import org.xiscript.xi.nodes.Node;
import org.xiscript.xi.operations.Operation;

public class XiFunc extends Function implements Operation {

	private static final long serialVersionUID = 0L;

	public XiFunc(ArgumentList list, XiBlock body) {
		super(list.getJavaAnalog(), body);
	}

	@Override
	public int numArgs() {
		return length();
	}

	public XiLambda asLambda() {
		return new XiLambda(identifiers, body);
	}

	@Override
	public XiType type() {
		return XiType.valueOf(Type.FUNCTION);
	}

	@Override
	public Type resultingType(Type... argTypes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void emitBytecode(MethodVisitor mv, VariableSuite vs, Node... args) {
		// TODO Auto-generated method stub
	}

}