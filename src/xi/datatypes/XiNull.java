package xi.datatypes;


public class XiNull extends DataType {
	
	private static final XiNull instance = new XiNull();
	
	private XiNull() {}
	
	public static XiNull instance() {
		return instance;
	}
	
	@Override
	public boolean isEmpty() {
		return true;
	}
	
	@Override
	public int compareTo(DataType other) {
		return other instanceof XiNull ? 0 : -1;
	}
	
	@Override
	public String toString() {
		return "null";
	}
	
	@Override
	public int length() {
		return 0;
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof XiNull;
	}
	
	@Override
	public int hashCode() {
		return Boolean.FALSE.hashCode();
	}
	
}