package xi.datatypes;

public abstract class DataType implements Comparable<DataType> {
	
	public abstract boolean isEmpty();
	
	public int length() {
		return toString().length();
	}
	
}