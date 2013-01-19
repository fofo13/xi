package org.xiscript.xi.datatypes;

import java.util.HashMap;
import java.util.Map;

public abstract class DataType implements Comparable<DataType> {

	protected Map<XiAttribute, DataType> attributes = new HashMap<XiAttribute, DataType>();

	public abstract boolean isEmpty();

	public int length() {
		return toString().length();
	}

	public String type() {
		String[] split = getClass().toString().split("\\.");
		// type names should be of the form "Xi.*"
		return split[split.length - 1].substring(2).toLowerCase();
	}

	public abstract Object getJavaAnalog();

	public DataType getAttribute(XiAttribute attribute) {
		DataType d = attributes.get(attribute);
		if (d == null)
			throw new RuntimeException(toString() + " has no attribute: "
					+ attribute);
		return d;
	}

}