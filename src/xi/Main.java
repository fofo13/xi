package xi;

import java.io.File;

public class Main {
	public static void main(String[] args) throws Exception {

		XiEnvironment env = new XiEnvironment(new File("tests/scratchpad.xi"));
		env.close();

	}
}