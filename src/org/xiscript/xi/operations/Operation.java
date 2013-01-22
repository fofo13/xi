package org.xiscript.xi.operations;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;

public interface Operation {

	DataType evaluate(DataType[] args, VariableCache globals);

	int numArgs();

}