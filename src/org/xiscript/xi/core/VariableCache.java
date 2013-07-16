package org.xiscript.xi.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiAttribute;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.exceptions.ErrorHandler;

public class VariableCache implements Map<String, DataType>, Cloneable {

	public static final VariableCache EMPTY_CACHE = new VariableCache(
			Collections.unmodifiableMap(new HashMap<String, DataType>()), null);

	private Map<String, DataType> cache;
	public VariableCache parent;

	private VariableCache(Map<String, DataType> cache, VariableCache parent) {
		this.cache = cache;
		this.parent = parent;
	}

	public VariableCache(VariableCache parent) {
		this(new HashMap<String, DataType>(), parent);
	}

	public VariableCache() {
		this(null);
	}

	public DataType put(XiVar var, DataType data) {
		if (var.length() == 1) {
			if ((!var.isTemporary()) && (!var.isPersistent())
					&& (parent != null && parent.containsKey(var.id())))
				return parent.put(var, data);

			return cache.put(var.id(), data);
		}

		DataType d = getFromString(var.id());
		for (int i = 1; i < var.length() - 1; i++)
			d = d.getAttribute(XiAttribute.valueOf(var.component(i)));

		d.setAttribute(XiAttribute.valueOf(var.component(var.length() - 1)),
				data);

		return null;
	}

	private DataType getFromString(String id) {
		DataType value = cache.get(id);

		if (value == null) {
			if (parent == null)
				return null;

			return parent.getFromString(id);
		}

		return value;
	}

	private DataType getFromVar(XiVar var) {
		DataType value = getFromString(var.id());

		if (value == null) {
			ErrorHandler.invokeError(
					ErrorHandler.ErrorType.IDNETIFIER_NOT_FOUND, var);
		}

		for (int i = 1; i < var.length(); i++)
			value = value.getAttribute(XiAttribute.valueOf(var.component(i)));

		return value;
	}

	@Override
	public DataType get(Object o) {
		return getFromVar((XiVar) o);
	}

	@Override
	public DataType put(String key, DataType value) {
		return cache.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends DataType> m) {
		cache.putAll(m);
	}

	@Override
	public boolean containsKey(Object key) {
		return cache.containsKey((String) key)
				|| (parent != null && parent.containsKey(key));
	}

	@Override
	public boolean containsValue(Object value) {
		return cache.containsValue(value)
				|| (parent != null && parent.containsValue(value));
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
	public DataType remove(Object key) {
		return cache.remove(key);
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
	public Set<String> keySet() {
		return cache.keySet();
	}

	@Override
	public Set<Entry<String, DataType>> entrySet() {
		return cache.entrySet();
	}

	@Override
	public String toString() {
		return cache.toString() + " "
				+ ((parent == null) ? "" : parent.toString());
	}

	// shallow copy:
	@Override
	public VariableCache clone() {
		return new VariableCache(new HashMap<String, DataType>(cache), null);
	}

	public int length() {
		return parent == null ? 1 : 1 + parent.length();
	}

}