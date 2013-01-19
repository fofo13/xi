package org.xiscript.xi.datatypes.numeric;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiAttribute;

public class XiComplex extends XiNum {

	protected double re;
	protected double im;

	public XiComplex(double re, double im) {
		this.re = re;
		this.im = im;
		attributes.put(new XiAttribute("re"), new XiFloat(re));
		attributes.put(new XiAttribute("im"), new XiFloat(im));
	}

	public double re() {
		return re;
	}

	public double im() {
		return im;
	}

	public static XiComplex parseIm(String im) {
		if (!im.endsWith("i"))
			throw new IllegalArgumentException();
		return new XiComplex(0, Double.parseDouble(im.substring(0,
				im.length() - 1)));
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
	public XiReal abs() {
		return new XiFloat(Math.sqrt(re * re + im * im));
	}

	@Override
	public XiComplex add(XiNum other) {
		if (other instanceof XiComplex) {
			XiComplex c = (XiComplex) other;
			return new XiComplex(re + c.re(), im + c.im());
		}
		return new XiComplex(re + ((XiReal) other).num().doubleValue(), im);
	}

	@Override
	public XiComplex sub(XiNum other) {
		if (other instanceof XiComplex) {
			XiComplex c = (XiComplex) other;
			return new XiComplex(re - c.re(), im - c.im());
		}
		return new XiComplex(re - ((XiReal) other).num().doubleValue(), im);
	}

	@Override
	public XiComplex mul(XiNum other) {
		if (other instanceof XiComplex) {
			XiComplex c = (XiComplex) other;
			return new XiComplex(re * c.re() - im * c.im(), re * c.im() + im
					* c.re());
		}
		double d = ((XiReal) other).num().doubleValue();
		return new XiComplex(re * d, im * d);
	}

	@Override
	public XiComplex div(XiNum other) {
		if (other instanceof XiReal) {
			double r = ((XiReal) other).num().doubleValue();
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
		throw new UnsupportedOperationException();
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
		return (float) re + " + " + (float) im + "i";
	}

}