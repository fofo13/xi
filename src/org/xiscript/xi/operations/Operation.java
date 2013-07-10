package org.xiscript.xi.operations;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.functional.XiLambda;

public interface Operation {

	DataType evaluate(VariableCache globals, DataType... args);

	int numArgs();

	XiLambda asLambda();

}