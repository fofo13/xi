package xi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OperationNode extends Node {
	
	private Operation op;
	private List<Node> children;
	
	public OperationNode(List<Node> children, Operation op) {
		this.op = op;
		this.children = children;
	}
	
	public OperationNode(Node[] children, Operation op) {
		this(Arrays.asList(children), op);
	}
	
	public OperationNode(Operation op) {
		this(new ArrayList<Node>(), op);
	}
	
	public Operation op() { return op; }
	
	public List<Node> children() { return children; }
	
	public void addChild(Node node) {
		children.add(node);
	}
	
	public DataType evaluate() {
		DataType[] arr = new DataType[children.size()];
		for (int i = 0 ; i < arr.length ; i++)
			arr[i] = children.get(i).evaluate();
		return op.evaluate(arr);
	}
	
}