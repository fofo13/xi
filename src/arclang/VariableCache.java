package arclang;
import java.util.HashSet;
import java.util.Set;

public class VariableCache {
	
	private Set<Variable> cache;
	
	public VariableCache() {
		cache = new HashSet<Variable>();
	}
	
	public void insert(Variable v) {
		cache.add(v);
	}
	
	public DataType lookup(String id) {
		for (Variable v : cache)
			if (v.id().equals(id))
				return v.val();
		throw new IllegalArgumentException("Variable identifier not found in cache.");
	}
	
}