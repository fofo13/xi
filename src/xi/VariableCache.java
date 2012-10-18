package xi;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class VariableCache implements Iterable<XiVar> {
	
	private Set<XiVar> cache;
	
	public VariableCache() {
		cache = new HashSet<XiVar>();
	}
	
	public void put(XiVar v) {
		if (! cache.add(v)) {
			cache.remove(v);
			cache.add(v);
		}
	}
	
	public DataType get(String id) {
		for (XiVar v : cache)
			if (v.id().equals(id))
				return v.val();
		throw new IllegalArgumentException("Variable identifier not found in cache.");
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