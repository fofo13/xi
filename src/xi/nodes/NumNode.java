package xi.nodes;

import xi.datatypes.DataType;
import xi.datatypes.XiNum;

public class NumNode extends DataNode {

	public NumNode(int n) {
		data = new XiNum(n);
	}

	public NumNode(String n) {
		this(Integer.parseInt(n));
	}

	public DataType evaluate() {
		return data;
	}
	
}