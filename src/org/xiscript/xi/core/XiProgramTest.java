package org.xiscript.xi.core;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;

@SuppressWarnings("unused")
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
			assertNotNull("il file non è nullo",env);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		//fail("Not yet implemented");
	}

	@Test
	public void testScope() {
		String s=new String();
		s="C:\\Users\\alfonso\\Desktop\\input.txt";
		
		File f= new File(s);
		
		XiProgram env;
		env=XiProgram.readFrom(f);
		assertNotNull(env.scope());
		assertTrue(env.scope() instanceof VariableCache);
		
	}

	@Test
	public void testRun() throws FileNotFoundException {
		String s=new String();
		s="C:\\Users\\alfonso\\Desktop\\input.txt";
		
		File f= new File(s);
		
		XiProgram env;
		env=new XiProgram(f);
		assertNotNull(env.run());
		assertTrue(env.run() instanceof DataType);
		
	}

	@Test
	public void testWriteTo() throws IOException {
		String s=new String();
		s="C:\\Users\\alfonso\\Desktop\\input.txt";
		
		File f= new File(s);
		
		XiProgram env =new XiProgram(f);
		
		String s2=new String();
		s2="C:\\Users\\alfonso\\Desktop\\test1.txt";
		
		File f2= new File(s2);
		env.writeTo(f2);
		
		String s3=new String();
		s3="C:\\Users\\alfonso\\Desktop\\test2.txt";
		
		File f3= new File(s3);
		env.writeTo(f3);

		String a=new String();
		String b=new String();
            // open file
            FileReader filein = new FileReader("C:\\Users\\alfonso\\Desktop\\test1.txt");
            
            int next;
        
            do {
                next = filein.read(); // read next char
                
                if (next != -1) { 
                    a= a + (char)next;
                				}
            	} while (next != -1);
        	
        	filein.close(); // close file

            // open file
            FileReader filein2 = new FileReader("C:\\Users\\alfonso\\Desktop\\test2.txt");
            
            
        
            do {
                next = filein2.read(); 
                if (next != -1) { 
                    b= b + (char)next;
                				}
            	} while (next != -1);
            filein2.close(); 

		assertEquals(a,b);
		
		
	}

	@Test
	public void testReadFrom() {
		String s=new String();
		s="C:\\Users\\alfonso\\Desktop\\input.txt";
		
		File f= new File(s);
		
		XiProgram env;
		env=XiProgram.readFrom(f);
		assertTrue(env instanceof XiProgram);
		assertNotNull(env);
		}

}
