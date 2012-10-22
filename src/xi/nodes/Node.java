package xi.nodes;

import xi.Operation;
import xi.VariableCache;
import xi.datatypes.DataType;
import xi.datatypes.XiList;

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
	
	public static Node create(String exp, VariableCache cache) {
		if (exp.equals("null"))
			return new NullNode();
		if (exp.matches("-?\\d+"))
			return new NumNode(Integer.parseInt(exp));
		if (exp.startsWith("["))
			return new ListNode(XiList.parse(exp, cache));
		if (exp.startsWith("{"))
			return new BlockNode(exp);
		if (exp.startsWith("\""))
			return new StringNode(exp);
		if (Operation.idExists(exp))
			return new OperationNode(Operation.parse(exp), cache);
		if (exp.matches("\\D.*+"))
			return new VarNode(exp);
		throw new RuntimeException("Cannot parse expression: " + exp);
	}
	
}