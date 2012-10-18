package xi;

public class VarNode extends DataNode {
	
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
	public DataType evaluate() {
		return cache.get(id);
	}
	
}