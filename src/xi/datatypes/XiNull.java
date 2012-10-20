package xi.datatypes;


public class XiNull extends DataType {
	
	@Override
	public boolean isEmpty() {
		return true;
	}
	
	@Override
	public String toString() {
		return "null";
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