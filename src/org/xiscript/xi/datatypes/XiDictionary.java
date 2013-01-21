package org.xiscript.xi.datatypes;

import java.util.HashMap;
import java.util.Map;

import org.xiscript.xi.datatypes.collections.CollectionWrapper;
import org.xiscript.xi.datatypes.collections.XiSet;
import org.xiscript.xi.datatypes.collections.XiTuple;

public class XiDictionary extends DataType {

	private Map<DataType, DataType> map;

	public XiDictionary(Map<DataType, DataType> map) {
		this.map = map;
	}

	public XiDictionary(CollectionWrapper<?> col) {
		this();
		for (DataType data : col) {
			XiTuple l = (XiTuple) data;
			put(l.get(0), l.get(1));
		}
	}

	public XiDictionary() {
		this(new HashMap<DataType, DataType>());
	}

	public Map<DataType, DataType> map() {
		return map;
	}

	public void put(DataType key, DataType value) {
		map.put(key, value);
	}

	public void del(DataType key) {
		map.remove(key);
	}

	public DataType get(DataType key) {
		if (!map.containsKey(key))
			return XiNull.instance();
		return map.get(key);
	}

	public XiSet keySet() {
		return new XiSet(map.keySet());
	}

	public boolean containsKey(DataType d) {
		return map.containsKey(d);
	}

	@Override
	public Map<DataType, DataType> getJavaAnalog() {
		return map;
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public int length() {
		return map.size();
	}

	@Override
	public int compareTo(DataType other) {
		if (!(other instanceof XiDictionary))
			return 0;
		return (new Integer(length())).compareTo(other.length());
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof XiDictionary
				&& map.equals(((XiDictionary) o).map());
	}

	@Override
	public int hashCode() {
		return map.hashCode();
	}

	@Override
	public String toString() {
		return map.toString();
	}

}