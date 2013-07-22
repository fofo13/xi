package org.xiscript.xi.nodes;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.xiscript.xi.compilation.Type;
import org.xiscript.xi.compilation.VariableSuite;
import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;

public class VarNode implements Node {

	private static final long serialVersionUID = 0L;

	private boolean literal;
	private XiVar var;

	private VarNode(XiVar var) {
		this.var = var;
		literal = false;
	}

	public VarNode(String id) {
		this(new XiVar(id));
	}

	public XiVar var() {
		return var;
	}

	@Override
	public void literalize() {
		literal = true;
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
	public DataType evaluate(VariableCache cache) {
		return literal ? var : cache.get(var);
	}

	@Override
	public boolean equals(Object o) {
		return var.equals(o);
	}

	@Override
	public int hashCode() {
		return var.hashCode();
	}

	@Override
	public String toString() {
		return var.toString();
	}

	@Override
	public void emitBytecode(MethodVisitor mv, VariableSuite vs) { // TODO
		if (!vs.has(var))
			ErrorHandler.invokeError(ErrorType.IDNETIFIER_NOT_FOUND, var);

		Type t = vs.typeOf(var);

		switch (t) {
		case INT:
			mv.visitVarInsn(Opcodes.ILOAD, vs.indexOf(var));
			break;
		default:
			mv.visitVarInsn(Opcodes.ALOAD, vs.indexOf(var));
		}
	}

	@Override
	public Type inferType(VariableSuite vs) {
		if (!vs.has(var))
			ErrorHandler.invokeError(ErrorType.IDNETIFIER_NOT_FOUND, var);

		return vs.typeOf(var);
	}

}