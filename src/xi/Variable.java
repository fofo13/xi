package xi;
public class Variable {
	
	private String id;
	private DataType val;
	
	public Variable(String id, DataType val) {
		this.id = id;
		this.val = val;
	}
	
	public String id() {
		return id;
	}
	
	public DataType val() {
		return val;
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