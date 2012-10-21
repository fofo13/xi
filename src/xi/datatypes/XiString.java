package xi.datatypes;


public class XiString extends DataType implements Comparable<XiString> {
	
	private String val;
	
	public XiString(String exp) {
		val = exp.replaceAll("\"", "");
	}
	
	public String val() {
		return val;
	}
	
	public XiString append(XiString other) {
		return new XiString(val + other.val());
	}
	
	public XiNum indexOf(XiString other) {
		return new XiNum(val.indexOf(other.val()));
	}
	
	@Override
	public boolean isEmpty() {
		return val.isEmpty();
	}
	
	@Override
	public int compareTo(XiString other) {
		return val.compareTo(other.val());
	}
	
	@Override
	public String toString() {
		return val;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof XiString)
			return val.equals(((XiString)o).val());
		return false;
	}
	
	@Override
	public int hashCode() {
		return val.hashCode();
	}
	
}