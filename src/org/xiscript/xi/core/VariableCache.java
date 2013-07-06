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
	public VariableCache parent;

	private VariableCache(Map<XiVar, DataType> cache, VariableCache parent) {
		this.cache = cache;
		this.parent = parent;
	}

	public VariableCache(VariableCache parent) {
		this(new HashMap<XiVar, DataType>(), parent);
	}

	public VariableCache() {
		this(null);
	}

	@Override
	public DataType put(XiVar id, DataType data) {
		String[] split = VarNode.DOT.split(id.id());

		if (split.length == 1) {
			if ((!id.isTemporary()) && (!id.isPersistent())
					&& (parent != null && parent.containsKey(id)))
				return parent.put(id, data);

			return cache.put(id, data);
		}

		DataType d = get(new XiVar(split[0]));
		for (int i = 1; i < split.length - 1; i++)
			d = d.getAttribute(XiAttribute.valueOf(split[i]));
		d.setAttribute(XiAttribute.valueOf(split[split.length - 1]), data);

		return null;
	}

	@Override
	public void putAll(Map<? extends XiVar, ? extends DataType> m) {
		for (Entry<? extends XiVar, ? extends DataType> entry : m.entrySet())
			if (!entry.getKey().isTemporary())
				put(entry.getKey(), entry.getValue());
	}

	public void putAll(Map<? extends XiVar, ? extends DataType> m,
			boolean addTemps) {
		if (!addTemps) {
			putAll(m);
			return;
		}

		for (Entry<? extends XiVar, ? extends DataType> entry : m.entrySet())
			put(entry.getKey(), entry.getValue());
	}

	@Override
	public boolean containsKey(Object key) {
		return cache.containsKey(key)
				|| (parent != null && parent.containsKey(key));
	}

	@Override
	public boolean containsValue(Object value) {
		return cache.containsValue(value)
				|| (parent != null && parent.containsValue(value));
	}

	public boolean containsId(String id) {
		XiVar var = new XiVar(VarNode.DOT.split(id, 2)[0]);
		return cache.containsKey(var)
				|| (parent != null && parent.containsId(id));
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

		if (value == null) {
			if (parent == null)
				ErrorHandler.invokeError(ErrorType.IDNETIFIER_NOT_FOUND, key);

			return parent.get(key);
		}

		return value;
	}

	public DataType get(String id) {
		String[] split = VarNode.DOT.split(id);

		XiVar v = new XiVar(split[0]);
		DataType value = cache.get(v);

		if (value == null) {
			if (parent == null) {
				if (XiEnvironment.globals.containsKey(v))
					return XiEnvironment.globals.get(split[0]);

				ErrorHandler.invokeError(ErrorType.IDNETIFIER_NOT_FOUND, id);
			}

			return parent.get(id);
		}

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
		return cache.toString() + " "
				+ ((parent == null) ? "" : parent.toString());
	}

	// shallow copy:
	@Override
	public VariableCache clone() {
		return new VariableCache(new HashMap<XiVar, DataType>(cache), null);
	}

	public void setTo(VariableCache other) {
		cache = new HashMap<XiVar, DataType>(other.cache);
	}

	public int length() {
		return parent == null ? 1 : 1 + parent.length();
	}

}