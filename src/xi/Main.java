package xi;

import java.io.File;
import java.io.FileNotFoundException;

import xi.core.XiEnvironment;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {

		XiEnvironment env = new XiEnvironment(new File("test/scratchpad.xi"));
		env.close();

	}
}