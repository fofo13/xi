package org.xiscript.xi.operations;

import static org.junit.Assert.*;

import org.junit.Test;



public class BuiltInOperationTest {

	@Test 
	public void testId() {
		String sb=new String("+");
		//StringBuilder s=new StringBuilder();
		//Node node = new OperationNode(BuiltInOperation.parse(sb.toString()));
		//assertTrue(node instanceof OperationNode);
		//String s=new String()-
		//String idop=new String();
		//BuiltInOperation.idExists(sb.toString());
		String op= new String(BuiltInOperation.ADD.id());
		assertEquals(sb,op);
		op= new String(BuiltInOperation.SUB.id());
		sb="-";
		assertEquals(sb,op);
		op=new String(BuiltInOperation.LSHIFT.id());
		sb="<<";
		assertEquals(sb,op);
		
	}
	
	
	

	@Test
	public void testNumArgs() {
		
		int args= (BuiltInOperation.PRINTLN.numArgs());
		int num=1;
		assertEquals(args,num);
		args=BuiltInOperation.DIV.numArgs();
		num=2;
		assertEquals(args,num);
		args=BuiltInOperation.SETATTR.numArgs();
		num=3;
		assertEquals(args,num);
	
		
	}

	@Test
	public void testEvaluateVariableCacheDataTypeArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testEvaluateDataTypeArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testAsLambda() {
		fail("Not yet implemented");
	}

	@Test
	public void testIdExists() {
		fail("Not yet implemented");
	}

	@Test
	public void testParse() {
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

}
