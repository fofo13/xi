package xi.datatypes;

import java.util.ArrayList;
import java.util.Collection;

public abstract class CollectionWrapper<T extends Collection<DataType>> extends
		DataType {

	protected T collection;

	protected abstract CollectionWrapper<T> instantiate(Collection<DataType> col);

	public CollectionWrapper(T collection) {
		this.collection = collection;
	}

	public CollectionWrapper<T> map(XiBlock block) {
		Collection<DataType> col = new ArrayList<DataType>(collection.size());
		for (DataType a : collection) {
			block.updateLocal(new XiVar(".", a));
			col.add(block.evaluate());
		}
		return instantiate(col);
	}

	public CollectionWrapper<T> filter(XiBlock block) {
		Collection<DataType> col = new ArrayList<DataType>(collection.size());
		for (DataType a : collection) {
			block.updateLocal(new XiVar(".", a));
			if (!block.evaluate().isEmpty())
				col.add(a);
		}
		return instantiate(col);
	}

	public CollectionWrapper<T> add(DataType data) {
		Collection<DataType> col = new ArrayList<DataType>(
				collection.size() + 1);
		col.addAll(collection);
		col.add(data);
		return instantiate(col);
	}

	public int size() {
		return length();
	}

	@Override
	public boolean isEmpty() {
		return collection.isEmpty();
	}

	@Override
	public int length() {
		return collection.size();
	}

	@Override
	public int compareTo(DataType other) {
		if (other instanceof CollectionWrapper)
			return (new Integer(length())).compareTo(((XiList) other).length());
		return 0;
	}

	@Override
	public int hashCode() {
		return collection.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof XiList)
			return collection.equals(((XiList) o).list());
		return false;
	}

	@Override
	public String toString() {
		return collection.toString();
	}

}