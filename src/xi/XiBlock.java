package xi;

public class XiBlock extends DataType {
	
	private String exp;
	private VariableCache locals;
	
	public XiBlock(String exp, VariableCache locals) {
		this.exp = exp.substring(1, exp.length() - 1);
		this.locals = locals;
	}
	
	public XiBlock(String exp) {
		this(exp, new VariableCache());
	}
	
	@Override
	public boolean isEmpty() {
		return exp.isEmpty();
	}
	
	public DataType evaluate(DataType data) {
		return (new SyntaxTree(exp.replaceAll("\\.", data.toString()), locals)).evaluate();
	}
	
}