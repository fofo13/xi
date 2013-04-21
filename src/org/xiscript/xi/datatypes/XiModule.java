package org.xiscript.xi.datatypes;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.xiscript.xi.core.VariableCache;

public class XiModule extends DataType {

	private Map<XiAttribute, DataType> attributes;

	private VariableCache contents;

	public XiModule(VariableCache contents) {
		this.contents = contents;
		attributes = new HashMap<XiAttribute, DataType>();

		for (Entry<XiVar, DataType> entry : contents.entrySet())
			attributes.put(XiAttribute.valueOf(entry.getKey().id()),
					entry.getValue());
	}

	public VariableCache contents() {
		return contents;
	}

	public void addVar(String id, DataType val) {
		contents.put(id, val);
		attributes.put(XiAttribute.valueOf(id), val);
	}

	@Override
	public DataType getAttribute(XiAttribute a) {
		DataType d = attributes.get(a);

		return (d == null) ? super.getAttribute(a) : d;
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

	@Override
	public XiType type() {
		return XiType.valueOf(XiType.Type.MODULE);
	}

}