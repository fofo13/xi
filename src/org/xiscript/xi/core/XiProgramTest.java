package org.xiscript.xi.core;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

public class XiProgramTest{

	@Test
	public void testXiProgramInputStream() {
		fail("Not yet implemented");
	}

	@Test
	public void testXiProgramFile() {
		String s=new String();
		s="C:\\Users\\alfonso\\Desktop\\input.txt";
		try {
			XiProgram env = new XiProgram(new File(s));
			assertNotNull("il file è nullo",env);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		//fail("Not yet implemented");
	}

	@Test
	public void testScope() {
		fail("Not yet implemented");
	}

	@Test
	public void testRun() {
		fail("Not yet implemented");
	}

	@Test
	public void testWriteTo() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadFrom() {
		fail("Not yet implemented");
	}

}
