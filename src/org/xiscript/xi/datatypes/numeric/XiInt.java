package org.xiscript.xi.datatypes.numeric;

import org.xiscript.xi.datatypes.XiType;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;

public class XiInt extends XiReal<Integer> {

	private static final long serialVersionUID = 0L;

	public XiInt(int val) {
		super(val);
	}

	public XiInt(boolean val) {
		super(val ? 1 : 0);
	}

	public int val() {
		return val;
	}

	public static XiInt parse(String exp) {
		try {
			return new XiInt(Integer.parseInt(exp));
		} catch (NumberFormatException nfe) {
			ErrorHandler.invokeError(ErrorType.NUMBER_FORMAT, exp);
			return null;
		}
	}

	@Override
	public boolean isEmpty() {
		return val == 0;
	}

	@Override
	public String toString() {
		return Integer.toString(val);
	}

	@Override
	public XiNum neg() {
		return new XiInt(-val);
	}

	@Override
	public XiNum inv() {
		return new XiInt(1 / val);
	}

	@Override
	public XiNum abs() {
		return new XiInt(Math.abs(val));
	}

	@Override
	public XiNum add(XiNum other) {
		if (other instanceof XiComplex || other instanceof XiFloat)
			return other.add(this);
		return new XiInt(val + ((XiReal<?>) other).num().intValue());
	}

	@Override
	public XiNum sub(XiNum other) {
		if (other instanceof XiComplex || other instanceof XiFloat)
			return other.sub(this).neg();
		return new XiInt(val - ((XiReal<?>) other).num().intValue());
	}

	@Override
	public XiNum mul(XiNum other) {
		if (other instanceof XiComplex || other instanceof XiFloat)
			return other.mul(this);
		return new XiInt(val * ((XiReal<?>) other).num().intValue());
	}

	@Override
	public XiNum div(XiNum other) {
		if (other instanceof XiComplex || other instanceof XiFloat)
			return other.div(this).inv();
		return new XiInt(val / ((XiReal<?>) other).num().intValue());
	}

	@Override
	public XiNum pow(XiNum other) {
		if (!(other instanceof XiInt))
			return new XiFloat(val.doubleValue()).pow(other);

		double pow = Math.pow(val, ((XiReal<?>) other).num().intValue());

		if (Double.isNaN(pow)) {
			return new XiComplex(val, 0).pow(other);
		}

		return new XiInt((int) pow);
	}

	public XiInt mod(XiInt other) {
		return new XiInt(val % other.num().intValue());
	}

	@Override
	public XiType type() {
		return XiType.valueOf(XiType.Type.INT);
	}

}