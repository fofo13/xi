package xi;

public class XiNum extends DataType implements Comparable<XiNum> {

	private int val;

	public XiNum(int val) {
		this.val = val;
	}

	public int val() {
		return val;
	}

	public static XiNum parse(String exp) {
		return new XiNum(Integer.parseInt(exp));
	}
	
	@Override
	public boolean isEmpty() {
		return val != 0;
	}
	
	@Override
	public int compareTo(XiNum other) {
		return (new Integer(val)).compareTo(other.val());
	}
	
	@Override
	public boolean equals(Object o) {
		return val == ((XiNum)o).val();
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
		return new XiNum((int)Math.pow(val, other.val()));
	}

}