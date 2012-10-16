package xi;

public class Xi extends DataType {
	
	private int val;
	
	public Xi(int val) {
		this.val = val;
	}
	
	public int val() {
		return val;
	}
	
	public Xi parse(String exp) {
		return new Xi(Integer.parseInt(exp));
	}
	
	@Override
	public DataType performOperation(UnaryOperation op) {
		// TODO
		return null;
	}
	
	@Override
	public DataType performOperation(BinaryOperation op, DataType data) {
		// TODO
		return null;
	}
	
}