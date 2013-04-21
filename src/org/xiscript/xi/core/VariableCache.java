package org.xiscript.xi.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;

public class VariableCache implements Map<XiVar, DataType>, Cloneable {

	private Map<XiVar, DataType> cache;

	private VariableCache(Map<XiVar, DataType> cache) {
		this.cache = cache;
	}

	public VariableCache() {
		this(new HashMap<XiVar, DataType>());
	}

	@Override
	public DataType put(XiVar id, DataType data) {
		return cache.put(id, data);
	}

	public void put(String id, DataType data) {
		put(new XiVar(id), data);
	}

	@Override
	public void putAll(Map<? extends XiVar, ? extends DataType> m) {
		for (Entry<? extends XiVar, ? extends DataType> entry : m.entrySet())
			if (!entry.getKey().temporary())
				cache.put(entry.getKey(), entry.getValue());
	}

	public void putAll(Map<? extends XiVar, ? extends DataType> m,
			boolean addTemps) {
		if (!addTemps) {
			putAll(m);
			return;
		}

		for (Entry<? extends XiVar, ? extends DataType> entry : m.entrySet())
			cache.put(entry.getKey(), entry.getValue());
	}

	@Override
	public boolean containsKey(Object key) {
		return cache.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return cache.containsValue(value);
	}

	public boolean containsId(String id) {
		return cache.containsKey(new XiVar(id));
	}

	@Override
	public int size() {
		return cache.size();
	}

	@Override
	public void clear() {
		for (DataType data : values())
			data.delete();
		cache.clear();
	}

	@Override
	public Set<Entry<XiVar, DataType>> entrySet() {
		return cache.entrySet();
	}

	@Override
	public DataType get(Object key) {
		DataType value = cache.get(key);

		if (value == null)
			ErrorHandler.invokeError(ErrorType.IDNETIFIER_NOT_FOUND, key);

		return value;
	}

	public DataType get(String id) {
		return get(new XiVar(id));
	}

	@Override
	public DataType remove(Object key) {
		return cache.remove(key);
	}

	public DataType remove(String id) {
		return cache.remove(new XiVar(id));
	}

	@Override
	public boolean isEmpty() {
		return cache.isEmpty();
	}

	@Override
	public Collection<DataType> values() {
		return cache.values();
	}

	@Override
	public Set<XiVar> keySet() {
		return cache.keySet();
	}

	@Override
	public String toString() {
		return cache.toString();
	}

	@Override
	public VariableCache clone() {
		return new VariableCache(new HashMap<XiVar, DataType>(cache));
	}

}