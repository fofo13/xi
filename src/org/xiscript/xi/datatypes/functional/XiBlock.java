package org.xiscript.xi.datatypes.functional;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.xiscript.xi.core.Parser;
import org.xiscript.xi.core.SyntaxTree;
import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.core.XiEnvironment;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiNull;
import org.xiscript.xi.datatypes.XiType;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.exceptions.ControlFlowException;
import org.xiscript.xi.nodes.Node;

public class XiBlock extends DataType {

	private VariableCache locals;
	private List<SyntaxTree> statements;
	private String exp;

	public XiBlock(String exp, VariableCache locals) {
		this.locals = locals;
		this.exp = exp;
	}

	public XiBlock(String exp) {
		this(exp, new VariableCache());
	}

	private void init() {
		if (statements == null) {
			locals.putAll(XiEnvironment.globals);

			Queue<Node> nodes = Parser.genNodeQueue(exp);

			statements = new LinkedList<SyntaxTree>();

			while (!nodes.isEmpty()) {
				SyntaxTree tree = new SyntaxTree(nodes, locals);
				statements.add(tree);
				nodes = tree.nodes();
			}
		}
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
		return statements.isEmpty();
	}

	public DataType evaluate() {
		init();
		DataType last = XiNull.instance();
		VariableCache pers = locals.getPersistents();

		try {
			for (SyntaxTree st : statements) {
				last = st.evaluate();
				locals.putAll(st.globals());
			}
		} catch (ControlFlowException cfe) {
			throw cfe;
		}

		locals.putAll(pers);
		return last;
	}

	@Override
	public XiType type() {
		return XiType.valueOf(XiType.Type.BLOCK);
	}

}