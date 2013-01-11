package org.xiscript.xi.exceptions;

import org.xiscript.xi.datatypes.DataType;

public class ReturnException extends ControlFlowException {
	
	private static final long serialVersionUID = 0L;
	
	private DataType data;
	
	public ReturnException(DataType data) {
		this.data = data;
	}
	
	public DataType data() {
		return data;
	}
	
}