package xi.datatypes.numeric;

import xi.datatypes.DataType;

public class XiComplex extends XiNum {

	protected double re;
	protected double im;

	public XiComplex(double re, double im) {
		this.re = re;
		this.im = im;
	}

	public double re() {
		return re;
	}

	public double im() {
		return im;
	}

	public XiComplex conj() {
		return new XiComplex(re, -im);
	}
	
	@Override
	public XiNum abs() {
		return new XiFloat(Math.sqrt(re * re + im * im));
	}

	@Override
	public XiNum add(XiNum other) {
		if (other instanceof XiComplex) {
			XiComplex c = (XiComplex) other;
			return new XiComplex(re + c.re(), im + c.im());
		}
		return new XiComplex(re + ((XiReal) other).num().doubleValue(), im);
	}

	@Override
	public XiNum sub(XiNum other) {
		if (other instanceof XiComplex) {
			XiComplex c = (XiComplex) other;
			return new XiComplex(re - c.re(), im - c.im());
		}
		return new XiComplex(re - ((XiReal) other).num().doubleValue(), im);
	}

	@Override
	public XiNum mul(XiNum other) {
		if (other instanceof XiComplex) {
			XiComplex c = (XiComplex) other;
			return new XiComplex(re * c.re() - im * c.im(), re * c.im() + im
					* c.re());
		}
		double d = ((XiReal) other).num().doubleValue();
		return new XiComplex(re * d, im * d);
	}
	
	@Override
	public XiNum div(XiNum other) {
		XiComplex c = (XiComplex) other;
		return mul(c.conj()).div(c.abs());
	}
	
	@Override
	public XiNum pow(XiNum other) {
		XiComplex c = new XiComplex(re, im);
		int n = ((XiReal)other).num().intValue();
		for (int i = 0 ; i < n ; i++)
			c = (XiComplex)c.mul(c);
		return c;
	}
	
	@Override
	public int compareTo(DataType other) {
		if (other instanceof XiComplex) {
			Double a = ((XiFloat)(abs())).num().doubleValue();
			Double b = ((XiFloat)(((XiComplex)other).abs())).num().doubleValue();
			return a.compareTo(b);
		}
		return 0;
	}
	
	@Override
	public boolean isEmpty() {
		return re == 0 && im == 0;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof XiComplex) {
			XiComplex c = ((XiComplex)o);
			return re == c.re() && im == c.im();
		}
		return false;
	}
	
	@Override
	public String toString() {
		return re + " + " + im + "i";
	}

}