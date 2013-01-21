package org.xiscript.xi.datatypes;

import org.xiscript.xi.core.VariableCache;

public class XiModule extends DataType {

	private VariableCache contents;

	public XiModule(VariableCache contents) {
		this.contents = contents;
		for (XiVar var : contents)
			attributes.put(new XiAttribute(var.id()), var.val());
	}

	public VariableCache contents() {
		return contents;
	}

	public void addVar(String id, DataType val) {
		contents.add(new XiVar(id, val));
		attributes.put(new XiAttribute(id), val);
	}

	@Override
	public Object getJavaAnalog() {
		return null;
	}

	@Override
	public int length() {
		return attributes.size();
	}

	@Override
	public int compareTo(DataType other) {
		if (other instanceof XiModule)
			return Integer.valueOf(length()).compareTo(
					((XiModule) other).length());
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return attributes.isEmpty();
	}

}