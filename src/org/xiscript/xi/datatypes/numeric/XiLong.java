package org.xiscript.xi.datatypes.numeric;

import java.math.BigInteger;

public class XiLong extends XiReal {

	public XiLong(BigInteger val) {
		super(val);
	}

	public XiLong(long val) {
		this(BigInteger.valueOf(val));
	}

	public static XiLong parse(String exp) {
		if (exp.endsWith("l") || exp.endsWith("L"))
			exp = exp.substring(0, exp.length() - 1);
		return new XiLong(new BigInteger(exp));
	}

	@Override
	public boolean isEmpty() {
		return val.equals(BigInteger.ZERO);
	}

	@Override
	public String toString() {
		return val.toString();
	}

	@Override
	public XiNum neg() {
		return new XiLong(((BigInteger) val).negate());
	}

	@Override
	public XiNum inv() {
		return new XiInt(1 / val.intValue());
	}

	@Override
	public XiNum abs() {
		return new XiInt(Math.abs(val.intValue()));
	}

	@Override
	public XiNum add(XiNum other) {
		return new XiLong(
				((BigInteger) val).add((BigInteger) ((XiLong) other).val));
	}

	@Override
	public XiNum sub(XiNum other) {
		return new XiLong(
				((BigInteger) val).subtract((BigInteger) ((XiLong) other).val));
	}

	@Override
	public XiNum mul(XiNum other) {
		return new XiLong(
				((BigInteger) val).multiply((BigInteger) ((XiLong) other).val));
	}

	@Override
	public XiNum div(XiNum other) {
		return new XiLong(
				((BigInteger) val).divide((BigInteger) ((XiLong) other).val));
	}

	@Override
	public XiNum pow(XiNum other) {
		return new XiLong(((BigInteger) val).pow(((XiInt) other).val()));
	}

	public XiLong mod(XiReal other) {
		return new XiLong(((BigInteger) val).mod((BigInteger) other.val));
	}

}