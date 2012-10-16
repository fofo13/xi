package xi;

import java.util.Arrays;

//import java.util.*;

public class Main {
	public static void main(String[] args) {
		//String exp = "+ 3 - * 1 2 1";
		//System.out.println((new ExpressionTree(exp)).evaluate());
		
		String exp = "@ [1 2 3] {* . 3}";
		System.out.println(Arrays.toString(exp.split("\\s+(?![^\\[]*\\])")));
	}
}