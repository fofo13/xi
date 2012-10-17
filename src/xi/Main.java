package xi;

public class Main {
	public static void main(String[] args) {
		String exp = "println = $ @ , 503 {= % 503 + . 1 0} 2";
		(new SyntaxTree(exp)).evaluate();
	}
}