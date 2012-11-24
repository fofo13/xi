package xi.operations;

import xi.core.VariableCache;
import xi.datatypes.DataType;

public interface Operation {
	
	public DataType evaluate(DataType[] args, VariableCache globals);
	public int numArgs();
	
}