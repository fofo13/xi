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
	
	public void updateLocal(XiVar v) {
		locals.add(v);
	}
	
	@Override
	public boolean isEmpty() {
		return exp.isEmpty();
	}
	
	public DataType evaluate(DataType data) {
		return (new SyntaxTree(exp.replaceAll("\\.", data.toString()), locals)).evaluate();
	}
	
	public DataType evaluate() {
		XiEnvironment env = null;
		try {
			env = new XiEnvironment(locals);
			env.put(exp);
			return env.last();
		} finally {
			env.close();
		}
	}
	
}