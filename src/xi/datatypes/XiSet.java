package xi.datatypes;

import java.util.HashSet;
import java.util.Set;

public class XiSet extends DataType {
	
	private Set<DataType> set;
	
	public XiSet(Set<DataType> set) {
		this.set = set;
	}
	
	public XiSet() {
		this(new HashSet<DataType>());
	}
	
	public Set<DataType> set() {
		return set;
	}
	
	@Override
	public boolean isEmpty() {
		return set.isEmpty();
	}
	
	// TODO
	
	@Override
	public int compareTo(DataType other) {
		return 0;
	}
	
}