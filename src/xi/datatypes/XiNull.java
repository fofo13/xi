package xi.datatypes;


public class XiNull extends DataType implements Comparable<XiNull> {
	
	@Override
	public boolean isEmpty() {
		return true;
	}
	
	@Override
	public int compareTo(XiNull other) {
		return 0;
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