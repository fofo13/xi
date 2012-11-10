package xi.datatypes;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

	public Set<DataType> set() {
		return collection;
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