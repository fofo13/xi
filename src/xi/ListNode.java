package xi;

public class ListNode extends Node {
	
	private XiList val;
	
	public ListNode(XiList val) {
		this.val = val;
	}
	
	public XiList val() {
		return val;
	}
	
	@Override
	public DataType evaluate() {
		return val;
	}
	
}