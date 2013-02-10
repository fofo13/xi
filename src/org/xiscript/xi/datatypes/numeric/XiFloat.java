package org.xiscript.xi.datatypes.numeric;

public class XiFloat extends XiReal<Double> {

	public XiFloat(double val) {
		super(val);
	}

	public static XiFloat parse(String exp) {
		return new XiFloat(Double.parseDouble(exp));
	}

	@Override
	public XiFloat neg() {
		return new XiFloat(-val);
	}

	@Override
	public XiFloat inv() {
		return new XiFloat(1.0 / val);
	}

	@Override
	public XiFloat abs() {
		return new XiFloat(Math.abs(val));
	}

	@Override
	public XiNum add(XiNum other) {
		if (other instanceof XiComplex)
			return other.add(this);
		return new XiFloat(val + ((XiReal<?>) other).num().doubleValue());
	}

	@Override
	public XiNum sub(XiNum other) {
		if (other instanceof XiComplex)
			return other.sub(this).neg();
		return new XiFloat(val - ((XiReal<?>) other).num().doubleValue());
	}

	@Override
	public XiNum mul(XiNum other) {
		if (other instanceof XiComplex)
			return other.mul(this);
		return new XiFloat(val * ((XiReal<?>) other).num().doubleValue());
	}

	@Override
	public XiNum div(XiNum other) {
		if (other instanceof XiComplex)
			return other.div(this).inv();
		return new XiFloat(val / ((XiReal<?>) other).num().doubleValue());
	}

	@Override
	public XiNum pow(XiNum other) {
		if (other instanceof XiReal) {
			double pow = Math.pow(val, ((XiReal<?>) other).num().doubleValue());

			if (!Double.isNaN(pow)) {
				return new XiFloat(pow);
			}
		}

		return new XiComplex(val, 0).pow(other);
	}

	@Override
	public boolean isEmpty() {
		return val == 0;
	}

	@Override
	public int hashCode() {
		return val.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof XiFloat && val.equals(((XiFloat) o).num());
	}

	@Override
	public String toString() {
		return Double.toString(val);
	}

}