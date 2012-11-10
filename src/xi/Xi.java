package xi;

import java.io.File;
import java.io.FileNotFoundException;

import xi.core.XiEnvironment;

public class Xi {
	
	public static void main(String[] args) {
		runFromFile(args[0]);
	}
	
	public static void runFromFile(String file) {
		try {
			XiEnvironment env = new XiEnvironment(new File(file));
			env.close();
		} catch (FileNotFoundException fnfe) {
			System.err.println("File " + file + " was not found.");
		}
	}
	
}