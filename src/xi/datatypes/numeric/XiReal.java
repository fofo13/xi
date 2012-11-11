package xi.datatypes.numeric;

public abstract class XiReal extends XiNum {
	
	protected Number val;
	
	public XiReal(Number val) {
		this.val = val;
	}
	
	public Number num() {
		return val;
	}
	
	@Override
	public int hashCode() {
		return val.intValue();
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof XiReal && val.equals(((XiInt) o).num());
	}
	
}