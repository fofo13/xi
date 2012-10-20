package xi.nodes;

import xi.datatypes.DataType;
import xi.datatypes.XiBlock;

public class BlockNode extends DataNode {
	
	public BlockNode(String exp) {
		data = new XiBlock(exp);
	}
	
	@Override
	public DataType evaluate() {
		return data;
	}
	
}
