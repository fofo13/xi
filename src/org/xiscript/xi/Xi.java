package org.xiscript.xi;

import java.io.File;
import java.io.FileNotFoundException;

import org.xiscript.xi.core.XiProgram;

public class Xi {

	public static void main(String... args) {
		runFromFile(args[0]);
	}

	public static void runFromFile(String file) {
		try {
			XiProgram env = new XiProgram(new File(file));
			/*XiProgram has  got several fields,the most important  is statements that is
			 * syntax three*/
			env.run();
		} catch (FileNotFoundException fnfe) {
			System.err.printf("File %s was not found.%n", file);
		}
	    //System.out.print("ciao");

	}

}