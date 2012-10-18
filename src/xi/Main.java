package xi;

public class Main {
	public static void main(String[] args) {
		
		XiEnvironment env = new XiEnvironment();
		env.put("a := + 3 2");
		env.put("a := , a");
		env.put("println a");
		
	}
}