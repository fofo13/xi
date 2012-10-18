package xi;

public class XiEnvironment {

	private VariableCache globals;

	private DataType last;
	
	public XiEnvironment() {
		globals = new VariableCache();
	}

	public VariableCache globals() {
		return globals;
	}
	
	public void put(String statement) {
		if (statement.contains(";"))
			throw new IllegalArgumentException("Argument is not a single statement.");
		
		if (statement.contains(":=")) { 
			String[] split = statement.split(":=");
			globals.put(new XiVar(split[0].trim(), new SyntaxTree(split[1].trim(), globals).evaluate()));
		}
		
		else
			last = (new SyntaxTree(statement, globals)).evaluate();
	}
	
	public DataType last() { return last; }
	
}