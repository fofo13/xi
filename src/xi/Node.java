package xi;

public abstract class Node {
	
	protected Node left;
	protected Node right;

	public abstract DataType evaluate();

	public void setLeft(Node node) {
		left = node;
	}

	public void setRight(Node node) {
		right = node;
	}
	
	public static Node create(String exp) {
		if (exp.matches("-?\\d+"))
			return new NumNode(Integer.parseInt(exp));
		if (Operation.idExists(exp))
			return new OperationNode(Operation.parse(exp));
		throw new RuntimeException();
	}
	
}