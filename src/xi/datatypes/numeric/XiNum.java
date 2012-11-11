package xi.datatypes.numeric;

import xi.datatypes.DataType;

public abstract class XiNum extends DataType {
	
	public abstract XiNum add(XiNum other);
	
	public abstract XiNum sub(XiNum other);
	
	public abstract XiNum mul(XiNum other);
	
	public abstract XiNum div(XiNum other);
	
	public abstract XiNum pow(XiNum other);
	
	public abstract XiNum abs();
	
}