package xi;

import java.util.HashSet;
import java.util.Set;

public class VariableCache {
	
	private Set<Variable> cache;
	
	public VariableCache() {
		cache = new HashSet<Variable>();
	}
	
	public void put(Variable v) {
		cache.add(v);
	}
	
	public DataType get(String id) {
		for (Variable v : cache)
			if (v.id().equals(id))
				return v.val();
		throw new IllegalArgumentException("Variable identifier not found in cache.");
	}
	
}