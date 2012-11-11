package xi.datatypes.numeric;

import xi.datatypes.DataType;

public class XiFloat extends XiReal {

	public XiFloat(double val) {
		super(val);
	}

	public static XiFloat parse(String exp) {
		return new XiFloat(Double.parseDouble(exp));
	}
	
	@Override
	public XiNum abs() {
		return new XiFloat(Math.abs(val.doubleValue()));
	}
	
	@Override
	public XiNum add(XiNum other) {
		if (other instanceof XiComplex)
			return other.add(this);
		return new XiFloat(val.doubleValue()
				+ ((XiReal) other).num().doubleValue());
	}

	@Override
	public XiNum sub(XiNum other) {
		if (other instanceof XiComplex)
			return other.sub(this);
		return new XiFloat(val.doubleValue()
				- ((XiReal) other).num().doubleValue());
	}

	@Override
	public XiNum mul(XiNum other) {
		if (other instanceof XiComplex)
			return other.mul(this);
		return new XiFloat(val.doubleValue()
				* ((XiReal) other).num().doubleValue());
	}

	@Override
	public XiNum div(XiNum other) {
		if (other instanceof XiComplex)
			return other.mul(this);
		return new XiFloat(val.doubleValue()
				/ ((XiReal) other).num().doubleValue());
	}

	@Override
	public XiNum pow(XiNum other) {
		if (other instanceof XiComplex)
			return other.mul(this);
		return new XiFloat(Math.pow(val.doubleValue(), ((XiReal) other).num()
				.doubleValue()));
	}
	
	@Override
	public int compareTo(DataType other) {
		if (other instanceof XiFloat) {
			Double a = val.doubleValue();
			Double b = ((XiFloat)other).num().doubleValue();
			return a.compareTo(b);
		}
		return 0;
	}
	
	@Override
	public boolean isEmpty() {
		return val.doubleValue() == 0;
	}
	
	@Override
	public int hashCode() {
		return val.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof XiFloat && val.equals(((XiFloat)o).num());
	}
	
	@Override
	public String toString() {
		return "" + val.doubleValue();
	}

}