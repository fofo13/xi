package xi;

public class Variable {
	
	private String id;
	private DataType data;
	
	public Variable(String id, DataType val) {
		this.id = id;
		this.data = val;
	}
	
	public String id() {
		return id;
	}
	
	public DataType val() {
		return data;
	}
	
	@Override
	public String toString() {
		return id;
	}
	
	@Override
	public boolean equals(Object o) {
		Variable v = (Variable)o;
		return id.equals(v.id());
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
}