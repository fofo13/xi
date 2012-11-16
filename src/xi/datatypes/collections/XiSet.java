package xi.datatypes.collections;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import xi.datatypes.DataType;

public class XiSet extends CollectionWrapper<Set<DataType>> {

	public XiSet(Set<DataType> set) {
		super(set);
	}

	public XiSet(ListWrapper list) {
		this(new HashSet<DataType>(list.list()));
	}

	public XiSet() {
		this(new HashSet<DataType>());
	} 
	
	public XiSet union(XiSet other) {
		Set<DataType> newSet = new HashSet<DataType>(collection);
		newSet.addAll(other.collection());
		return new XiSet(newSet);
	}
	
	public XiSet intersection(XiSet other) {
		Set<DataType> newSet = new HashSet<DataType>(collection);
		newSet.retainAll(other.collection());
		return new XiSet(newSet);
	}
	
	public XiSet difference(XiSet other, boolean symmetric) {
		Set<DataType> newSet = new HashSet<DataType>(collection);
		if (symmetric) {
			newSet.addAll(other.collection());
			Set<DataType> tmp = new HashSet<DataType>(collection);
			tmp.retainAll(other.collection());
			newSet.removeAll(tmp);
		}
		else {
			newSet.removeAll(other.collection());
		}
		return new XiSet(newSet);
	}
	
	public boolean superset(XiSet other) {
		return collection.containsAll(other.collection());
	}
	
	public boolean subset(XiSet other) {
		return other.collection().containsAll(collection);
	}
	
	@Override
	public String toString() {
		String s = collection.toString();
		return "{" + s.substring(1, s.length() - 1) + "}";
	}

	@Override
	public XiSet instantiate(Collection<DataType> col) {
		return new XiSet(new HashSet<DataType>(col));
	}

}