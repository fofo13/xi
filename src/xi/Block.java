package xi;

public class Block extends DataType {
	
	private String exp;
	
	public Block(String exp) {
		this.exp = exp.replaceAll("[\\{\\}]", "");
	}
	
	@Override
	public boolean isEmpty() {
		return exp.equals("");
	}
	
	public DataType evaluate(int a) {
		return (new SyntaxTree(exp.replaceAll("\\.", "" + a))).evaluate();
	}
	
}