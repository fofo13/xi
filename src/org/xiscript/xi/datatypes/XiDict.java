package org.xiscript.xi.datatypes;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.xiscript.xi.compilation.Type;
import org.xiscript.xi.datatypes.collections.CollectionWrapper;
import org.xiscript.xi.datatypes.collections.XiSet;
import org.xiscript.xi.datatypes.collections.XiTuple;

public class XiDict extends DataType implements Map<DataType, DataType> {

	private static final long serialVersionUID = 0L;

	private Map<DataType, DataType> map;

	public XiDict(Map<DataType, DataType> map) {
		this.map = map;
	}

	public XiDict(CollectionWrapper<?> col) {
		this();
		for (DataType data : col) {
			XiTuple l = (XiTuple) data;
			put(l.get(0), l.get(1));
		}
	}

	public XiDict() {
		this(new HashMap<DataType, DataType>());
	}

	public Map<DataType, DataType> map() {
		return map;
	}

	@Override
	public DataType put(DataType key, DataType value) {
		return map.put(key, value);
	}

	@Override
	public void putAll(Map<? extends DataType, ? extends DataType> m) {
		map.putAll(m);
	}

	@Override
	public DataType remove(Object key) {
		return map.remove(key);
	}

	@Override
	public boolean containsKey(Object o) {
		return map.containsKey(o);
	}

	@Override
	public boolean containsValue(Object o) {
		return map.containsValue(o);
	}

	@Override
	public DataType get(Object key) {
		if (!map.containsKey(key))
			return XiNull.instance();
		return map.get(key);
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public Collection<DataType> values() {
		return map.values();
	}

	@Override
	public Set<Map.Entry<DataType, DataType>> entrySet() {
		return map.entrySet();
	}

	@Override
	public int size() {
		return length();
	}

	public XiSet keySet() {
		return new XiSet(map.keySet());
	}

	public XiSet itemSet() {
		Set<DataType> set = new HashSet<DataType>(length());
		for (DataType key : map.keySet())
			set.add(new XiTuple(key, map.get(key)));
		return new XiSet(set);
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
		if (!(other instanceof XiDict))
			return 0;
		return Integer.valueOf(length()).compareTo(other.length());
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof XiDict && map.equals(((XiDict) o).map());
	}

	@Override
	public int hashCode() {
		return map.hashCode();
	}

	@Override
	public String toString() {
		return map.toString();
	}

	@Override
	public XiType type() {
		return XiType.valueOf(Type.DICT);
	}

}