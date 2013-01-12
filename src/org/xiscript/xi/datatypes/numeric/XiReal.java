package org.xiscript.xi.datatypes.numeric;

import org.xiscript.xi.datatypes.DataType;

public abstract class XiReal extends XiNum {
	
	protected Number val;
	
	public XiReal(Number val) {
		this.val = val;
	}
	
	public Number num() {
		return val;
	}
	
	@Override
	public Number getJavaAnalog() {
		return val;
	}
	
	@Override
	public int compareTo(DataType other) {
		if (! (other instanceof XiReal))
			return 0;
		return new Double(val.doubleValue()).compareTo(((XiReal)other).num().doubleValue());
	}
	
	@Override
	public int hashCode() {
		return val.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof XiReal && val.equals(((XiInt) o).num());
	}
	
}