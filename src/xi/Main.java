package xi;

//import java.util.*;

// test stuff
public class Main {
	public static void main(String[] args) {
		String exp = "println $ @ , 13 {= % 13 + . 1 0}";
		(new SyntaxTree(exp)).evaluate();
	}
}