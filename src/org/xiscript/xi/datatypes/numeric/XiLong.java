package org.xiscript.xi.datatypes.numeric;

import java.math.BigInteger;

import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;

public class XiLong extends XiReal<BigInteger> {

	public XiLong(BigInteger val) {
		super(val);
	}

	public XiLong(long val) {
		this(BigInteger.valueOf(val));
	}

	public static XiLong parse(String exp) {
		char last = exp.charAt(exp.length() - 1);
		if (last == 'l' || last == 'L')
			exp = exp.substring(0, exp.length() - 1);
		try {
			return new XiLong(new BigInteger(exp));
		} catch (NumberFormatException nfe) {
			ErrorHandler.invokeError(ErrorType.NUMBER_FORMAT, exp);
			return null;
		}
	}

	@Override
	public XiLong asLong() {
		return this;
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
		return new XiLong(val.add(((XiReal<?>) other).asLong().val));
	}

	@Override
	public XiLong sub(XiNum other) {
		return new XiLong(val.subtract(((XiReal<?>) other).asLong().val));
	}

	@Override
	public XiLong mul(XiNum other) {
		return new XiLong(val.multiply(((XiReal<?>) other).asLong().val));
	}

	@Override
	public XiLong div(XiNum other) {
		return new XiLong(val.divide(((XiReal<?>) other).asLong().val));
	}

	@Override
	public XiLong pow(XiNum other) {
		return new XiLong(val.pow(((XiReal<?>) other).num().intValue()));
	}

	public XiLong mod(XiReal<?> other) {
		return new XiLong(val.mod(other.asLong().val));
	}

}