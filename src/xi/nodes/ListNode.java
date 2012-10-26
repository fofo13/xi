package xi.nodes;

import xi.datatypes.DataType;
import xi.datatypes.XiList;

public class ListNode extends DataNode {
	
	public ListNode(XiList data) {
		this.data = data;
	}
	
	public XiList val() {
		return (XiList)data;
	}
	
	@Override
	public DataType evaluate() {
		return data;
	}
	
}