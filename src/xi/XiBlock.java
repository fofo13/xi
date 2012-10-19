package xi;

public class XiBlock extends DataType {
	
	private String exp;
	private VariableCache locals;
	
	public XiBlock(String exp) {
		this.exp = exp.substring(1, exp.length() - 1);
		locals = new VariableCache();
	}
	
	@Override
	public boolean isEmpty() {
		return exp.isEmpty();
	}
	
	public DataType evaluate(int a) {
		return (new SyntaxTree(exp.replaceAll("\\.", "" + a), locals)).evaluate();
	}
	
}