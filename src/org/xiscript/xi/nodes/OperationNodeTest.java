package org.xiscript.xi.nodes;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.Test;
import org.xiscript.xi.operations.BuiltInOperation;

public class OperationNodeTest {

	@Test
	public void testHashCode() {
		fail("Not yet implemented");
	}
	@Test
	public void testOperationNodeListOfNodeOperation() {
		String sb=new String("-");
		Node node = new OperationNode(BuiltInOperation.parse(sb));
		assertTrue(node instanceof OperationNode);
	/*I don't understand  here  what  kind of  method junit4 want test!!! */
	}

	@Test
	public void testOperationNodeNodeArrayOperation() {
		fail("Not yet implemented");
	}

	@Test
	public void testOperationNodeOperation() {
		fail("Not yet implemented");
	}

	@Test
	public void testChildren() {
		fail("Not yet implemented");
	}

	@Test
	public void testOp() {
		String sb=new String("-");
		Node node=new OperationNode(BuiltInOperation.parse(sb));
		assertNotNull(((OperationNode) node).op());
		assertEquals(((OperationNode) node).op().toString(),"-");
	}

	@Test
	public void testAddChild() {
/*here i  can't assert nothing because addChild method  return void*/
	/*	String sb=new String("-");
		Node node=new OperationNode(BuiltInOperation.parse(sb));
		Queue<Character> source = new LinkedList<Character>();
		String n1=new String("12");
		//System.out.print(input);
		for (int i=0; i<n1.length() ;i++){
			source.add(n1.charAt(i));
		}
		//Node x=readNum(source);
		
		node.addChild(Node(n1));
		*/
		fail("Not yet implemented");
	}

	@Test
	public void testNumChildren() {
		fail("Not yet implemented");
	}

	@Test
	public void testLiteralize() {
		fail("Not yet implemented");
	}

	@Test
	public void testEqualsObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

}
