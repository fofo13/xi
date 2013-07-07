package org.xiscript.xi.datatypes.numeric;

import org.xiscript.xi.datatypes.DataType;

public abstract class XiNum extends DataType {

	private static final long serialVersionUID = 0L;

	public abstract XiNum neg();

	public abstract XiNum inv();

	public abstract XiNum abs();

	public abstract XiNum add(XiNum other);

	public abstract XiNum sub(XiNum other);

	public abstract XiNum mul(XiNum other);

	public abstract XiNum div(XiNum other);

	public abstract XiNum pow(XiNum other);

}