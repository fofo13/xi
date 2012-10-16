package xi;

public abstract class DataType {
	
	public abstract DataType performOperation(UnaryOperation op);
	
	public abstract DataType performOperation(BinaryOperation op, DataType data);
	
}