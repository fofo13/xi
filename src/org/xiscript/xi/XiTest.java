package org.xiscript.xi;

//import java.io.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class XiTest extends Xi{


	@Test
	public void testRunFromFile() {
		String s1=new String();
		s1="C:\\Users\\alfonso\\Desktop\\input.txt";
		
		runFromFile(s1);
		assertNotNull(s1);
		/*in the  first  case s1 is  a  real file input for Xi project*/
		
		String s2=new String();
		s2="C:\\Users\\alfonso\\Desktop\\fittizio.txt";		
		runFromFile(s2);
		assertNotNull(s2);		
		
		/*in this  case instead s2 not is  a  real input file  for xi project
		 * it's necessary to throw an exception into runFromFile method and cover
		 * all line of  it*/
		
	}

}
