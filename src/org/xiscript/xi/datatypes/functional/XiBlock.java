package org.xiscript.xi.datatypes.functional;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiNull;
import org.xiscript.xi.datatypes.XiType;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.nodes.Node;

public class XiBlock extends DataType {

	private static final long serialVersionUID = 0L;

	private Node[] statements;
	private VariableCache scope;

	public XiBlock(Node[] statements) {
		this.statements = statements;
	}

	public void updateLocal(XiVar id, DataType val) {
		scope.put(id, val);
	}

	public void setOuterScope(VariableCache cache) {
		if (scope != null)
			scope.clear();
		scope = new VariableCache(cache);
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
		return statements.length == 0;
	}

	public DataType evaluate() {
		DataType last = XiNull.instance();

		for (Node node : statements)
			last = node.evaluate(scope);

		return last;
	}

	@Override
	public XiType type() {
		return XiType.valueOf(XiType.Type.BLOCK);
	}

}