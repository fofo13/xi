package xi;

public class Main {
	public static void main(String[] args) {
		String exp = "println + \"hello\" 3";
		(new SyntaxTree(exp)).evaluate();
	}
}