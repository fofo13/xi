package xi.core;

import xi.datatypes.DataType;

public interface Operation {
	
	public DataType evaluate(DataType[] args, VariableCache globals);
	public int numArgs();
	
}