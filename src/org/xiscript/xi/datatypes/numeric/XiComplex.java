package org.xiscript.xi.datatypes.numeric;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiAttribute;
import org.xiscript.xi.datatypes.XiType;

public class XiComplex extends XiNum {

	private static final XiAttribute RE = XiAttribute.valueOf("re");
	private static final XiAttribute IM = XiAttribute.valueOf("im");

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

	public static XiComplex parseIm(String im) {
		return new XiComplex(0, Double.parseDouble(im));
	}

	public XiComplex conj() {
		return new XiComplex(re, -im);
	}

	@Override
	public XiComplex neg() {
		return new XiComplex(-re, -im);
	}

	@Override
	public XiComplex inv() {
		double r = re * re + im * im;
		return new XiComplex(re / r, -im / r);
	}

	@Override
	public XiReal<?> abs() {
		return new XiFloat(Math.sqrt(re * re + im * im));
	}

	@Override
	public XiComplex add(XiNum other) {
		if (other instanceof XiComplex) {
			XiComplex c = (XiComplex) other;
			return new XiComplex(re + c.re(), im + c.im());
		}
		return new XiComplex(re + ((XiReal<?>) other).num().doubleValue(), im);
	}

	@Override
	public XiComplex sub(XiNum other) {
		if (other instanceof XiComplex) {
			XiComplex c = (XiComplex) other;
			return new XiComplex(re - c.re(), im - c.im());
		}
		return new XiComplex(re - ((XiReal<?>) other).num().doubleValue(), im);
	}

	@Override
	public XiComplex mul(XiNum other) {
		if (other instanceof XiComplex) {
			XiComplex c = (XiComplex) other;
			return new XiComplex(re * c.re() - im * c.im(), re * c.im() + im
					* c.re());
		}
		double d = ((XiReal<?>) other).num().doubleValue();
		return new XiComplex(re * d, im * d);
	}

	@Override
	public XiComplex div(XiNum other) {
		if (other instanceof XiReal) {
			double r = ((XiReal<?>) other).num().doubleValue();
			return new XiComplex(re / r, im / r);
		}
		XiComplex c = (XiComplex) other;
		return mul(c.conj()).div(c.abs()).div(c.abs());
	}

	@Override
	public XiNum pow(XiNum other) {
		XiComplex a = (this.log()).mul(other);
		return a.exp();
	}

	public XiComplex log() {
		double rpart = Math.sqrt(re * re + im * im);
		double ipart = Math.atan2(im, re);
		if (ipart > Math.PI)
			ipart = ipart - 2.0 * StrictMath.PI;
		return new XiComplex(Math.log(rpart), ipart);
	}

	public XiComplex exp() {
		double exp_x = Math.exp(re);
		return new XiComplex(exp_x * StrictMath.cos(im), exp_x * Math.sin(im));
	}

	@Override
	public Object getJavaAnalog() {
		return this;
	}

	@Override
	public int compareTo(DataType other) {
		if (other instanceof XiComplex) {
			Double a = ((XiFloat) (abs())).num().doubleValue();
			Double b = ((XiFloat) (((XiComplex) other).abs())).num()
					.doubleValue();
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
			XiComplex c = ((XiComplex) o);
			return re == c.re() && im == c.im();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (new double[] { re, im }).hashCode();
	}

	@Override
	public String toString() {
		if (Math.abs(im) < 1E-10)
			return Double.toString(re);
		if (Math.abs(re) < 1E-10)
			return im + "i";
		if (im < 0)
			return re + " - " + (-im) + "i";
		return re + " + " + im + "i";
	}

	@Override
	public DataType getAttribute(XiAttribute a) {
		if (a.equals(RE))
			return new XiFloat(re);
		if (a.equals(IM))
			return new XiFloat(im);

		return super.getAttribute(a);
	}

	@Override
	public XiType type() {
		return XiType.valueOf(XiType.Type.COMPLEX);
	}

}