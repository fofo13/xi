package org.xiscript.xi.nodes;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;

public class StopNode implements Node {

	public static final StopNode instance = new StopNode();

	private StopNode() {
	}

	@Override
	public void clear() {
	}

	@Override
	public DataType evaluate(VariableCache cache) {
		throw new UnsupportedOperationException();
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