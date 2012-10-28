package xi.nodes;

import xi.datatypes.XiNum;

public class NumNode extends DataNode {

	public NumNode(XiNum data) {
		this.data = data;
	}
	
	public NumNode(int n) {
		this(new XiNum(n));
	}
	
}