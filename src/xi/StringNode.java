package xi;

public class StringNode extends DataNode {
	
	public StringNode(String str) {
		data = new XiString(str);
	}
	
	@Override
	public XiString evaluate() {
		return (XiString)data;
	}
	
}