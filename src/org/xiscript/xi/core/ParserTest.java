package org.xiscript.xi.core;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.Test;
import org.xiscript.xi.nodes.DataNode;
import org.xiscript.xi.nodes.Node;
import org.xiscript.xi.nodes.OperationNode;

public class ParserTest extends Parser{

	@Test
	public void testGenNodeQueueQueueOfCharacter() {
		Queue<Character> source = new LinkedList<Character>();
		Queue<Node> nodes ;
		String input=new String();
		input="println Hello";
		//System.out.print(input);
		for (int i=0; i<input.length() ;i++){
			source.add(input.charAt(i));
		}
		nodes=genNodeQueue(source);
		assertNotNull(nodes);
		assertTrue(nodes instanceof Queue<?>);

		Node node=nodes.poll();
		assertTrue(node instanceof OperationNode);
		node=nodes.poll();
		assertNotNull(node);
		String s=new String(node.toString());
		assertEquals(s,"Hello");
		assertTrue(nodes.isEmpty());
		
		/*Change simple Hello input into "Hello" */
		input="println \"Hello\"";
		//System.out.print(input);
		for (int i=0; i<input.length() ;i++){
			source.add(input.charAt(i));
		}
		nodes=genNodeQueue(source);
		assertNotNull(nodes);
		assertTrue(nodes instanceof Queue<?>);
		node=nodes.poll();
		assertTrue(node instanceof OperationNode);
		node=nodes.poll();
		assertNotNull(node);
		s=new String(node.toString());
		assertEquals(s,"Hello");
		assertTrue(nodes.isEmpty());


	}
	@Test
	public void testGenNodeQueueQueueOfCharacter_PrintlnAdd_2_2() {
		Queue<Character> source = new LinkedList<Character>();
		Queue<Node> nodes ;
		String input=new String();
		input="println + 2 2";
		//System.out.print(input);
		for (int i=0; i<input.length() ;i++){
			source.add(input.charAt(i));
		}
		nodes=genNodeQueue(source);
		assertNotNull(nodes);
		assertTrue(nodes instanceof Queue<?>);

		Node node=nodes.poll();
		assertTrue(node instanceof OperationNode);
		node=nodes.poll();
		assertTrue(node instanceof OperationNode);
		node=nodes.poll();
		assertTrue(node instanceof DataNode);
		node=nodes.poll();
		assertTrue(node instanceof DataNode);
		assertNotNull(node);
		assertNotEquals(node,"2");
		assertEquals(node.toString(),"2");
//		String s=new String(node.toString());
//		assertEquals(s,"Hello");
//		assertTrue(nodes.isEmpty());

	}

	@Test
	public void testGenNodeQueueCharSequence() {
		String s=new String ("println + 3,5 4i");
		Queue<Node> nodes = genNodeQueue(s);
		Node node=nodes.poll();
		assertTrue(node instanceof OperationNode);
		node=nodes.poll();
		assertTrue(node instanceof OperationNode);
		node=nodes.poll();
		assertTrue(node instanceof DataNode);
		node=nodes.poll();
		assertFalse(node instanceof DataNode);
		assertNotNull(node);
/* This  node is DataNode<XiComplex> and his correct appearance
 	for  this  script language is 4.0i */
		assertNotEquals(node,"4i");
		assertNotEquals(node,"4.0i");
		assertNotEquals(node.toString(),"4i");
		assertNotEquals(node.toString(),"4.0i");

	}

}
