package xi.datatypes.numeric;

public class XiInt extends XiReal {

	public XiInt(int val) {
		super(val);
	}

	public XiInt(boolean val) {
		super(val ? 1 : 0);
	}

	public int val() {
		return val.intValue();
	}

	public static XiInt parse(String exp) {
		return new XiInt(Integer.parseInt(exp));
	}

	@Override
	public boolean isEmpty() {
		return val.intValue() == 0;
	}

	@Override
	public String toString() {
		return "" + val.intValue();
	}

	@Override
	public XiNum neg() {
		return new XiInt(-val.intValue());
	}

	@Override
	public XiNum inv() {
		return new XiInt(1 / val.intValue());
	}

	@Override
	public XiNum abs() {
		return new XiFloat(Math.abs(val.intValue()));
	}

	@Override
	public XiNum add(XiNum other) {
		if (other instanceof XiComplex || other instanceof XiFloat)
			return other.add(this);
		return new XiInt(val.intValue() + ((XiReal) other).num().intValue());
	}

	@Override
	public XiNum sub(XiNum other) {
		if (other instanceof XiComplex || other instanceof XiFloat)
			return other.sub(this).neg();
		return new XiInt(val.intValue() - ((XiReal) other).num().intValue());
	}

	@Override
	public XiNum mul(XiNum other) {
		if (other instanceof XiComplex || other instanceof XiFloat)
			return other.mul(this);
		return new XiInt(val.intValue() * ((XiReal) other).num().intValue());
	}

	@Override
	public XiNum div(XiNum other) {
		if (other instanceof XiComplex || other instanceof XiFloat)
			return other.div(this).inv();
		return new XiInt(val.intValue() / ((XiReal) other).num().intValue());
	}

	@Override
	public XiNum pow(XiNum other) {
		if (other instanceof XiFloat)
			return new XiFloat(Math.pow(val.doubleValue(), ((XiFloat) other)
					.num().doubleValue()));
		return new XiInt((int) Math.pow(val.intValue(), ((XiReal) other).num()
				.intValue()));
	}

	public XiInt mod(XiInt other) {
		return new XiInt(val.intValue() % other.num().intValue());
	}

}