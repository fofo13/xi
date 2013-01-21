package org.xiscript.xi.datatypes.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.datatypes.functional.XiBlock;
import org.xiscript.xi.datatypes.functional.XiLambda;
import org.xiscript.xi.datatypes.numeric.XiInt;
import org.xiscript.xi.operations.IntrinsicOperation;

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

	public CollectionWrapper<T> map(XiBlock block, boolean deep) {
		Collection<DataType> col = new ArrayList<DataType>(collection.size());
		int index = 0;
		for (DataType a : collection) {
			block.updateLocal(new XiVar(".", a));
			block.updateLocal(new XiVar("_", new XiInt(index)));
			col.add((deep && (a instanceof CollectionWrapper<?>)) ? ((CollectionWrapper<?>) a)
					.map(block, true) : block.evaluate());
			index++;
		}
		return instantiate(col);
	}

	public CollectionWrapper<T> map(XiLambda block, boolean deep,
			VariableCache globals) {
		Collection<DataType> col = new ArrayList<DataType>(collection.size());
		for (DataType a : collection) {
			col.add((deep && (a instanceof CollectionWrapper<?>)) ? ((CollectionWrapper<?>) a)
					.map(block, true, globals) : block.evaluate(new XiTuple(
					Arrays.asList(a)), globals));
		}
		return instantiate(col);
	}

	public CollectionWrapper<T> filter(XiBlock block, boolean deep) {
		Collection<DataType> col = new ArrayList<DataType>(collection.size());
		int index = 0;
		for (DataType a : collection) {
			block.updateLocal(new XiVar(".", a));
			block.updateLocal(new XiVar("_", new XiInt(index)));

			if (deep && (a instanceof CollectionWrapper<?>))
				col.add(((CollectionWrapper<?>) a).filter(block, deep));
			else if (!block.evaluate().isEmpty())
				col.add(a);

			index++;
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
			d = IntrinsicOperation.ADD.evaluate(
					new DataType[] { d, list.get(i) }, null);
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

	public XiTuple asTuple() {
		return new XiTuple(new ArrayList<DataType>(collection));
	}

	@Override
	public Object getJavaAnalog() {
		return collection;
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