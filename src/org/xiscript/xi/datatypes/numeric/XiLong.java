package org.xiscript.xi.datatypes.numeric;

import java.math.BigInteger;

public class XiLong extends XiReal<BigInteger> {

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
	public XiLong neg() {
		return new XiLong(val.negate());
	}

	@Override
	public XiInt inv() {
		return new XiInt(1 / val.intValue());
	}

	@Override
	public XiLong abs() {
		return new XiLong(val.abs());
	}

	@Override
	public XiLong add(XiNum other) {
		return new XiLong(val.add(BigInteger.valueOf(((XiReal<?>) other).num()
				.longValue())));
	}

	@Override
	public XiLong sub(XiNum other) {
		return new XiLong(val.subtract(BigInteger.valueOf(((XiReal<?>) other)
				.num().longValue())));
	}

	@Override
	public XiLong mul(XiNum other) {
		return new XiLong(val.multiply(BigInteger.valueOf(((XiReal<?>) other)
				.num().longValue())));
	}

	@Override
	public XiLong div(XiNum other) {
		return new XiLong(val.divide(BigInteger.valueOf(((XiReal<?>) other)
				.num().longValue())));
	}

	@Override
	public XiLong pow(XiNum other) {
		return new XiLong(val.pow(((XiReal<?>) other).num().intValue()));
	}

	public XiLong mod(XiReal<?> other) {
		return new XiLong(val.mod(BigInteger.valueOf(other.val.longValue())));
	}

}