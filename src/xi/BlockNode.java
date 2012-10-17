package xi;

public class BlockNode extends Node {
	
	private XiBlock block;
	
	public BlockNode(String exp) {
		block = new XiBlock(exp);
	}
	
	@Override
	public DataType evaluate() {
		return block;
	}
	
}
