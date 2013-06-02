package org.xiscript.xi.datatypes.functional;

import java.util.LinkedList;
import java.util.List;
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

	private VariableCache scope;
	private List<Node> statements;
	private CharSequence expr;

	public XiBlock(CharSequence expr) {
		this.expr = expr;
	}

	private void init() {
		if (statements == null) {
			Queue<Node> nodes = Parser.genNodeQueue(expr);

			statements = new LinkedList<Node>();

			while (!nodes.isEmpty()) {
				SyntaxTree tree = new SyntaxTree(nodes, scope);
				statements.add(tree.head());
				nodes = tree.nodes();
			}
		}
	}

	public void updateLocal(XiVar id, DataType val) {
		scope.put(id, val);
	}

	public void setOuterScope(VariableCache cache) {
		if (scope != null)
			scope.clear();
		scope = new VariableCache(cache);
	}

	public VariableCache locals() {
		return scope;
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
		return statements.isEmpty();
	}

	public DataType evaluate() {
		init();
		DataType last = XiNull.instance();

		try {
			for (Node node : statements) {
				last = node.evaluate(scope);
			}
		} catch (ControlFlowException cfe) {
			throw cfe;
		}

		return last;
	}

	@Override
	public XiType type() {
		return XiType.valueOf(XiType.Type.BLOCK);
	}

}