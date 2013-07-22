package org.xiscript.xi.nodes;

import org.objectweb.asm.MethodVisitor;
import org.xiscript.xi.compilation.Type;
import org.xiscript.xi.compilation.VariableSuite;
import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.datatypes.functional.XiFunc;
import org.xiscript.xi.datatypes.functional.XiLambda;

public class FunctionConverterNode implements Node {

	private static final long serialVersionUID = 0L;

	private XiVar id;

	public FunctionConverterNode(XiVar id) {
		this.id = id;
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
	public XiLambda evaluate(VariableCache cache) {
		return ((XiFunc) cache.get(id)).asLambda();
	}

	@Override
	public void literalize() {
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
