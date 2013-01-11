package org.xiscript.xi.operations;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;


public interface Operation {
	
	public DataType evaluate(DataType[] args, VariableCache globals);
	public int numArgs();
	
}