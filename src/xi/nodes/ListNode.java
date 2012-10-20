package xi.nodes;

import xi.datatypes.DataType;
import xi.datatypes.XiList;

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