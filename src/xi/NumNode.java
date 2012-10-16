package xi;

public class NumNode extends DataNode {

	public NumNode(int n) {
		data = new XiNum(n);
	}

	public NumNode(String n) {
		this(Integer.parseInt(n));
	}

	public int evaluate() {
		return ((XiNum)data).val();
	}
	
}