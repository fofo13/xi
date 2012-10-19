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
		if (exp.startsWith("["))
			return new ListNode(XiList.parse(exp));
		if (exp.startsWith("{"))
			return new BlockNode(exp);
		if (exp.startsWith("\""))
			return new StringNode(exp);
		if (Operation.idExists(exp))
			return new OperationNode(Operation.parse(exp));
		if (exp.matches("[a-zA-Z]\\w+"))
			return new VarNode(exp);
		throw new RuntimeException();
	}
	
}