package xi.nodes;

import xi.datatypes.XiString;

public class StringNode extends DataNode {
	
	public StringNode(XiString data) {
		this.data = data;
	}
	
	public StringNode(String str) {
		this(new XiString(str));
	}
	
}