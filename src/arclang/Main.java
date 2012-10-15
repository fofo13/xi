package arclang;

//import java.util.*;

public class Main {
	public static void main(String[] args) {
		String exp = "+ 1 2";
		System.out.println((new ExpressionTree(exp)).evaluate());
	}
}