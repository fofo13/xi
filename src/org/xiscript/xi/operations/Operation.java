package org.xiscript.xi.operations;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.xiscript.xi.compilation.Type;
import org.xiscript.xi.compilation.VariableSuite;
import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.functional.XiLambda;
import org.xiscript.xi.nodes.Node;

public interface Operation extends Opcodes {

	DataType evaluate(VariableCache globals, DataType... args);

	int numArgs();

	XiLambda asLambda();

	void emitBytecode(MethodVisitor mv, VariableSuite vs, Node... args);

	Type resultingType(Type... argTypes);

}