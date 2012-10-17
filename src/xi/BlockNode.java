package xi;

public class BlockNode extends Node {
	
	private Block block;
	
	public BlockNode(String exp) {
		block = new Block(exp);
	}
	
	@Override
	public DataType evaluate() {
		return block;
	}
	
}
