package org.xiscript.xi.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiAttribute;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;
import org.xiscript.xi.nodes.VarNode;

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
		return cache.containsKey(new XiVar(VarNode.DOT.split(id, 2)[0]));
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
		String[] split = VarNode.DOT.split(id);

		DataType value = cache.get(new XiVar(split[0]));

		if (value == null)
			ErrorHandler.invokeError(ErrorType.IDNETIFIER_NOT_FOUND, id);

		for (int i = 1; i < split.length; i++)
			value = value.getAttribute(XiAttribute.valueOf(split[i]));

		return value;
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

	public VariableCache getPersistents() {
		Map<XiVar, DataType> m = new HashMap<XiVar, DataType>();

		for (Entry<? extends XiVar, ? extends DataType> entry : entrySet()) {
			if (entry.getKey().persistent())
				m.put(entry.getKey(), entry.getValue());
		}

		return new VariableCache(m);
	}

}