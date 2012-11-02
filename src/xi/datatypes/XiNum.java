package xi.datatypes;

public class XiNum extends DataType {

	private int val;

	public XiNum(int val) {
		this.val = val;
	}

	public XiNum(boolean val) {
		this.val = val ? 1 : 0;
	}

	public int val() {
		return val;
	}

	public static XiNum parse(String exp) {
		return new XiNum(Integer.parseInt(exp));
	}

	@Override
	public boolean isEmpty() {
		return val == 0;
	}

	@Override
	public int compareTo(DataType other) {
		if (other instanceof XiNum)
			return (new Integer(val)).compareTo(((XiNum)other).val());
		return 0;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof XiNum)
			return val == ((XiNum) o).val();
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

	public XiNum add(XiNum other) {
		return new XiNum(val + other.val());
	}

	public XiNum sub(XiNum other) {
		return new XiNum(val - other.val());
	}

	public XiNum mul(XiNum other) {
		return new XiNum(val * other.val());
	}

	public XiNum div(XiNum other) {
		return new XiNum(val / other.val());
	}

	public XiNum mod(XiNum other) {
		return new XiNum(val % other.val());
	}

	public XiNum pow(XiNum other) {
		return new XiNum((int) Math.pow(val, other.val()));
	}

}