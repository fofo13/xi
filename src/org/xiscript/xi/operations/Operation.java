package org.xiscript.xi.operations;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.functional.XiLambda;

public interface Operation {

	DataType evaluate(DataType[] args, VariableCache globals);

	int numArgs();

	XiLambda asLambda();

}