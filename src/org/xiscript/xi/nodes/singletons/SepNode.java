package org.xiscript.xi.nodes.singletons;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiNull;
import org.xiscript.xi.nodes.Node;

public class SepNode implements Node {

	private static final long serialVersionUID = 0L;

	private static final SepNode instance = new SepNode();

	private SepNode() {
	}

	public static SepNode instance() {
		return instance;
	}

	@Override
	public void clear() {
	}

	@Override
	public void literalize() {
	}

	@Override
	public DataType evaluate(VariableCache cache) {
		return XiNull.instance();
	}

	@Override
	public int numChildren() {
		return 0;
	}

	@Override
	public void addChild(Node node) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof StopNode);
	}

	@Override
	public int hashCode() {
		return Boolean.FALSE.hashCode();
	}

}