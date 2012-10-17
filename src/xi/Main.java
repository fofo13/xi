package xi;

//import java.util.*;

// test stuff
public class Main {
	public static void main(String[] args) {
		String exp = "= * 2 2 + 2 3";
		System.out.println((new ExpressionTree(exp)).evaluate());
		
	}
}