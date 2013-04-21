package org.xiscript.xi.datatypes.functional;

import java.util.ArrayDeque;
import java.util.Queue;

import org.xiscript.xi.core.Parser;
import org.xiscript.xi.core.SyntaxTree;
import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiNull;
import org.xiscript.xi.datatypes.XiType;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.exceptions.ControlFlowException;
import org.xiscript.xi.nodes.Node;

public class XiBlock extends DataType {

	private VariableCache locals;
	private Queue<Node> nodes;

	public XiBlock(String exp, VariableCache locals) {
		this.locals = locals;
		nodes = Parser.genNodeQueue(exp.substring(1, exp.length() - 1));
	}

	public XiBlock(String exp) {
		this(exp, new VariableCache());
	}

	public void updateLocal(XiVar id, DataType val) {
		locals.put(id, val);
	}

	public void addVars(VariableCache cache) {
		locals.putAll(cache, true);
	}

	public VariableCache locals() {
		return locals;
	}

	@Override
	public Object getJavaAnalog() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int compareTo(DataType other) {
		if (other instanceof XiBlock)
			return 0;
		if (other instanceof XiNull)
			return 1;
		return -1;
	}

	@Override
	public boolean isEmpty() {
		return nodes.isEmpty();
	}

	public DataType evaluate() {
		VariableCache clone = locals.clone();
		DataType last = XiNull.instance();

		for (Node node : this.nodes)
			node.clear();

		Queue<Node> nodes = new ArrayDeque<Node>(this.nodes);

		try {
			while (!nodes.isEmpty()) {
				SyntaxTree tree = new SyntaxTree(nodes, clone);
				last = tree.evaluate();
				nodes = tree.nodes();
			}
		} catch (ControlFlowException cfe) {
			throw cfe;
		}

		locals.putAll(clone);
		return last;
	}

	@Override
	public XiType type() {
		return XiType.valueOf(XiType.Type.BLOCK);
	}

}