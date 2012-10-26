package xi.nodes;

import xi.datatypes.DataType;
import xi.datatypes.XiBlock;

public class BlockNode extends DataNode {
	
	public BlockNode(XiBlock data) {
		this.data = data;
	}
	
	public BlockNode(String exp) {
		this(new XiBlock(exp));
	}
	
	@Override
	public DataType evaluate() {
		return data;
	}
	
}
