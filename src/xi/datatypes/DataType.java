package xi.datatypes;

public abstract class DataType {
	
	public abstract boolean isEmpty();
	
	public int length() {
		return toString().length();
	}
	
}