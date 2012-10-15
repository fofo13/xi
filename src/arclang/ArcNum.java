package arclang;

public class ArcNum extends DataType {
	
	private int val;
	
	public ArcNum(int val) {
		this.val = val;
	}
	
	public int val() {
		return val;
	}
	
	public ArcNum parse(String exp) {
		return new ArcNum(Integer.parseInt(exp));
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