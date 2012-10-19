package xi;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class VariableCache implements Set<XiVar> {
	
	private Set<XiVar> cache;
	
	public VariableCache(Set<XiVar> cache) {
		this.cache = cache;
	}
	
	public VariableCache() {
		this(new HashSet<XiVar>());
	}
	
	public DataType get(String id) {
		for (XiVar v : cache)
			if (v.id().equals(id))
				return v.val();
		throw new IllegalArgumentException("Variable identifier not found in cache.");
	}
	
	public VariableCache merge(VariableCache other) {
		Set<XiVar> merged = new HashSet<XiVar>(size() + other.size());
		for (XiVar var : other)
			merged.add(var);
		for (XiVar var : this)
			merged.add(var);
		return new VariableCache(merged);
	}
	
	@Override
	public int size() {
		return cache.size();
	}
	
	@Override
	public boolean addAll(Collection<? extends XiVar> c) {
		return cache.addAll(c);
	}
	
	@Override
	public boolean retainAll(Collection<?> c) {
		return cache.retainAll(c);
	}
	
	@Override
	public boolean add(XiVar v) {
		if (! cache.add(v)) {
			cache.remove(v);
			cache.add(v);
		}
		return true;
	}
	
	@Override
	public void clear() {
		cache.clear();
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		return cache.retainAll(c);
	}
	
	@Override
	public <T> T[] toArray(T[] a) {
		return cache.toArray(a);
	}
	
	@Override
	public boolean containsAll(Collection<?> c) {
		return cache.containsAll(c);
	}
	
	@Override
	public Object[] toArray() {
		return cache.toArray();
	}
	
	@Override
	public boolean remove(Object o) {
		return cache.remove(o);
	}
	
	@Override
	public boolean contains(Object o) {
		return cache.contains(o);
	}
	
	@Override
	public boolean isEmpty() {
		return cache.isEmpty();
	}
	
	@Override
	public Iterator<XiVar> iterator() {
		return cache.iterator();
	}
	
	@Override
	public String toString() {
		return cache.toString();
	}
	
}