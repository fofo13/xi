package xi.datatypes.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import xi.core.IntrinsicOperation;
import xi.datatypes.DataType;
import xi.datatypes.XiBlock;
import xi.datatypes.XiVar;
import xi.datatypes.numeric.XiInt;

public abstract class CollectionWrapper<T extends Collection<DataType>> extends
		DataType implements Iterable<DataType> {

	protected T collection;

	protected abstract CollectionWrapper<T> instantiate(Collection<DataType> col);

	public CollectionWrapper(T collection) {
		this.collection = collection;
	}

	public T collection() {
		return collection;
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

	public DataType sum() {
		if (isEmpty())
			return new XiInt(0);
		List<DataType> list = new ArrayList<DataType>(collection);
		DataType d = list.get(0);
		for (int i = 1; i < collection.size(); i++)
			d = IntrinsicOperation.ADD.evaluate(new DataType[] { d, list.get(i) },
					null);
		return d;
	}
	
	public boolean contains(DataType data) {
		return collection.contains(data);
	}
	
	public int size() {
		return length();
	}

	public XiList asList() {
		return new XiList(new ArrayList<DataType>(collection));
	}
	
	public XiSet asSet() {
		return new XiSet(new HashSet<DataType>(collection));
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
	public Iterator<DataType> iterator() {
		return collection.iterator();
	}
	
	@Override
	public int hashCode() {
		return collection.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof CollectionWrapper)
			return collection.equals(((CollectionWrapper<?>) o).collection());
		return false;
	}

	@Override
	public String toString() {
		return collection.toString();
	}

}