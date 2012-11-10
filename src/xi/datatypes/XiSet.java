package xi.datatypes;

import java.util.HashSet;
import java.util.Set;

public class XiSet extends DataType {
	
	private Set<DataType> set;
	
	public XiSet(Set<DataType> set) {
		this.set = set;
	}
	
	public XiSet(ListWrapper list) {
		this(new HashSet<DataType>(list.list()));
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
	
	@Override
	public int compareTo(DataType other) {
		return 0;
	}
	
	@Override
	public String toString() {
		return set.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof XiSet && ((XiSet)o).set().equals(set);
	}
	
	@Override
	public int hashCode() {
		return set.hashCode();
	}
	
}