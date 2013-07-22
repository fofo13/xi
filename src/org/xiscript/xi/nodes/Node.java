package org.xiscript.xi.nodes;

import java.io.Serializable;

import org.objectweb.asm.MethodVisitor;
import org.xiscript.xi.compilation.Type;
import org.xiscript.xi.compilation.VariableSuite;
import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;

public interface Node extends Serializable {

	DataType evaluate(VariableCache cache);

	int numChildren();

	void addChild(Node node);

	void literalize();

	void emitBytecode(MethodVisitor mv, VariableSuite vs);

	Type inferType(VariableSuite vs);

}