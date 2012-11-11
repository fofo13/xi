package xi.datatypes;

public class XiInt extends DataType {

	private int val;

	public XiInt(int val) {
		this.val = val;
	}

	public XiInt(boolean val) {
		this.val = val ? 1 : 0;
	}

	public int val() {
		return val;
	}

	public static XiInt parse(String exp) {
		return new XiInt(Integer.parseInt(exp));
	}

	@Override
	public boolean isEmpty() {
		return val == 0;
	}

	@Override
	public int compareTo(DataType other) {
		if (other instanceof XiInt)
			return (new Integer(val)).compareTo(((XiInt)other).val());
		return 0;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof XiInt)
			return val == ((XiInt) o).val();
		return false;
	}

	@Override
	public int hashCode() {
		return val;
	}

	@Override
	public String toString() {
		return "" + val;
	}

	public XiInt add(XiInt other) {
		return new XiInt(val + other.val());
	}

	public XiInt sub(XiInt other) {
		return new XiInt(val - other.val());
	}

	public XiInt mul(XiInt other) {
		return new XiInt(val * other.val());
	}

	public XiInt div(XiInt other) {
		return new XiInt(val / other.val());
	}

	public XiInt mod(XiInt other) {
		return new XiInt(val % other.val());
	}

	public XiInt pow(XiInt other) {
		return new XiInt((int) Math.pow(val, other.val()));
	}

}