package org.xiscript.xi.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiNull;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;
import org.xiscript.xi.nodes.Node;
import org.xiscript.xi.nodes.OperationNode;
import org.xiscript.xi.nodes.assignments.AssignmentNode;
import org.xiscript.xi.nodes.singletons.StopNode;
import org.xiscript.xi.operations.BuiltInOperation;

public class SyntaxTree {

	private Node[] statements;

	public SyntaxTree(Node[] statements) {
		this.statements = statements;
	}
/*iterates a syntax tree */
	public SyntaxTree(Queue<Node> nodes) {
		Iterator<Node> iter = nodes.iterator();

		while (iter.hasNext()) {
			Node node = iter.next();

			if (node instanceof AssignmentNode) {
				iter.next().literalize();
			} else if (node instanceof OperationNode) {   /*check if a node is an operation and which operation regards*/
				OperationNode opnode = (OperationNode) node;
				if (opnode.op() == BuiltInOperation.DEF) {
					iter.next().literalize();
					iter.next().literalize();
				} else if (opnode.op() == BuiltInOperation.LAMBDA) {
					iter.next().literalize();
				} else if (opnode.op() == BuiltInOperation.FOR) {
					iter.next().literalize();
				}
			}
		}

		List<Node> statements = new ArrayList<Node>();
		while (!nodes.isEmpty())
			statements.add(create(nodes));

		this.statements = statements.toArray(new Node[statements.size()]);
	}

	public Node[] statements() {
		return statements;
	}

	public DataType evaluate(VariableCache scope) {
		DataType last = XiNull.instance();
		for (Node node : statements)   /*for  each node of statements(of syntax three)*/
			last = node.evaluate(scope);
		return last;
	}

	private static Node create(Queue<Node> nodes) {
		if (nodes.isEmpty()) {
			ErrorHandler.invokeError(ErrorType.INCOMPLETE_EXPRESSION);
		}

		Node node = nodes.poll();

		if (node.numChildren() > -1) {
			for (int i = 0; i < node.numChildren(); i++) {
				Node next = create(nodes);
				if (next == StopNode.instance())
					break;
		/*if we are analyzing a node with children her we add them (for  example node is +
		 * we add he to add_node a  number 3) */
				node.addChild(next);
			}
		} else {
			Node child = null;
			while ((child = create(nodes)) != StopNode.instance()) {
				node.addChild(child);
			}
		}

		return node;
	}

	protected void writeTo(File file) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(statements);
			oos.close();
			fos.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	protected static SyntaxTree readFrom(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			SyntaxTree st = new SyntaxTree((Node[]) ois.readObject());
			ois.close();
			fis.close();
			return st;
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}

		return null;
	}

}