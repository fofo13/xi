package org.xiscript.xi.datatypes;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.xiscript.xi.core.VariableCache;

public class XiModule extends DataType {

	private static final long serialVersionUID = 0L;

	private Map<XiAttribute, DataType> attributes;
	private VariableCache contents;

	public XiModule(VariableCache contents) {
		this.contents = contents;
		attributes = new HashMap<XiAttribute, DataType>();

		for (Entry<String, DataType> entry : contents.entrySet())
			attributes.put(XiAttribute.valueOf(entry.getKey()),
					entry.getValue());
	}

	public XiModule(int size) {
		contents = new VariableCache();
		attributes = new HashMap<XiAttribute, DataType>(size);
	}

	public VariableCache contents() {
		return contents;
	}

	public void addVar(String id, DataType val) {
		contents.put(new XiVar(id), val);
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