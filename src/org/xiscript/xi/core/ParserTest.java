package org.xiscript.xi.core;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.Test;
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
		//String output="Hello";
		//XiProgram.program=new SyntaxTree(Parser.genNodeQueue(source));
		Node node=nodes.poll();
		assertTrue(node instanceof OperationNode);
		node=nodes.poll();
		assertNotNull(node);
		String s=new String(node.toString());
		assertEquals(s,"Hello");
		assertTrue(nodes.isEmpty());

	}

	@Test
	public void testGenNodeQueueCharSequence() {
		fail("Not yet implemented");
	}

}
