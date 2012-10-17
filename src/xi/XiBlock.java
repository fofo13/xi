package xi;

public class XiBlock extends DataType {
	
	private String exp;
	
	public XiBlock(String exp) {
		this.exp = exp.replaceAll("[\\{\\}]", "");
	}
	
	@Override
	public boolean isEmpty() {
		return exp.isEmpty();
	}
	
	public DataType evaluate(int a) {
		return (new SyntaxTree(exp.replaceAll("\\.", "" + a))).evaluate();
	}
	
}