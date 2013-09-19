package org.xiscript.xi;

//import java.io.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class XiTest extends Xi{


	@Test
	public void testRunFromFile() {
		String s=new String();
		s="C:\\Users\\alfonso\\Desktop\\input.txt";
		
		runFromFile(s);
		assertNotNull(s);
		
		
	}

}
