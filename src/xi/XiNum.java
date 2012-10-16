package xi;

public class XiNum extends DataType {
	
	private int val;
	
	public XiNum(int val) {
		this.val = val;
	}
	
	public int val() {
		return val;
	}
	
	public XiNum parse(String exp) {
		return new XiNum(Integer.parseInt(exp));
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