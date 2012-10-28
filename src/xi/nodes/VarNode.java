package xi.nodes;

import xi.VariableCache;
import xi.datatypes.DataType;

public class VarNode implements Node {
	
	private VariableCache cache;
	private String id; 
	
	public VarNode(String id, VariableCache cache) {
		this.cache = cache;
		this.id = id;
	}
	
	public VarNode(String id) {
		this(id, null);
	}
	
	public String id() {
		return id;
	}
	
	@Override
	public void addChild(Node node) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int numChildren() {
		return 0;
	}
	
	@Override
	public DataType evaluate() {
		return cache.get(id);
	}
	
}