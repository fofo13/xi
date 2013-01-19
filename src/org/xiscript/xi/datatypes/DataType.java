package org.xiscript.xi.datatypes;

import java.util.HashMap;
import java.util.Map;

import org.xiscript.xi.datatypes.collections.XiString;
import org.xiscript.xi.datatypes.numeric.XiInt;

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

	protected void refreshAttributes() {
		attributes.put(new XiAttribute("hash"), new XiInt(hashCode()));
		attributes.put(new XiAttribute("type"), new XiString(type()));
	}

	public DataType getAttribute(XiAttribute attribute) {
		refreshAttributes();
		DataType d = attributes.get(attribute);
		if (d == null)
			throw new RuntimeException(toString() + " has no attribute: "
					+ attribute);
		return d;
	}

}