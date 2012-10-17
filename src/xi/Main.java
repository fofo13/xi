package xi;

//import java.util.*;

// test stuff
public class Main {
	public static void main(String[] args) {
		String exp = "@ , 10 {** . 2}";
		System.out.println((new SyntaxTree(exp)).evaluate());
	}
}