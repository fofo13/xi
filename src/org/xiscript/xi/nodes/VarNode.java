package org.xiscript.xi.nodes;

import java.util.regex.Pattern;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiAttribute;

public class VarNode implements Node {

	public static final Pattern DOT = Pattern.compile("\\.");

	private String id;

	public VarNode(String id) {
		this.id = id;
	}

	public String id() {
		return id;
	}

	@Override
	public void addChild(Node node) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int numChildren() {
		return 0;
	}

	@Override
	public DataType evaluate(VariableCache cache) {
		String[] split = DOT.split(id);

		DataType value = cache.get(split[0]);

		for (int i = 1; i < split.length; i++)
			value = value.getAttribute(XiAttribute.valueOf(split[i]));

		return value;
	}

	@Override
	public void clear() {
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof VarNode) && id.equals(((VarNode) o).id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public String toString() {
		return id;
	}

}