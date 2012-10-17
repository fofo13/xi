package xi;

public class ListNode extends DataNode {
	
	public ListNode(XiList list) {
		data = list;
	}
	
	public XiList val() {
		return (XiList)data;
	}
	
	@Override
	public DataType evaluate() {
		return data;
	}
	
}