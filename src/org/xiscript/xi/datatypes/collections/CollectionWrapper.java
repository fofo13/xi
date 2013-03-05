package org.xiscript.xi.datatypes.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.core.XiIterable;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.datatypes.functional.XiBlock;
import org.xiscript.xi.datatypes.functional.XiLambda;
import org.xiscript.xi.datatypes.numeric.XiInt;

public abstract class CollectionWrapper<T extends Collection<DataType>> extends
		XiIterable implements Collection<DataType> {

	protected T collection;

	protected abstract CollectionWrapper<T> instantiate(Collection<DataType> col);

	public CollectionWrapper(T collection) {
		this.collection = collection;
	}

	public T collection() {
		return collection;
	}

	@Override
	public CollectionWrapper<T> map(final XiBlock block) {
		return map(block, false);
	}

	public CollectionWrapper<T> map(XiBlock block, boolean deep) {
		Collection<DataType> col = new ArrayList<DataType>(collection.size());
		int index = 0;
		for (DataType a : collection) {
			block.updateLocal(new XiVar(".", a, true));
			block.updateLocal(new XiVar("_", new XiInt(index), true));
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
		for (DataType data : collection) {
			block.updateLocal(new XiVar(".", data, true));
			block.updateLocal(new XiVar("_", new XiInt(index), true));

			if (deep && (data instanceof CollectionWrapper<?>))
				col.add(((CollectionWrapper<?>) data).filter(block, deep));
			else if (!block.evaluate().isEmpty())
				col.add(data);

			index++;
		}
		return instantiate(col);
	}

	public CollectionWrapper<T> plus(DataType data) {
		Collection<DataType> col = new ArrayList<DataType>(
				collection.size() + 1);
		col.addAll(collection);
		col.add(data);
		return instantiate(col);
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
	public boolean add(DataType data) {
		return collection.add(data);
	}

	@Override
	public boolean addAll(Collection<? extends DataType> c) {
		return collection.addAll(c);
	}

	@Override
	public boolean isEmpty() {
		return collection.isEmpty();
	}

	@Override
	public int size() {
		return length();
	}

	@Override
	public boolean contains(Object o) {
		return collection.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return collection.containsAll(c);
	}

	@Override
	public Iterator<DataType> iterator() {
		return collection.iterator();
	}

	@Override
	public boolean remove(Object o) {
		return collection.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return collection.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return collection.retainAll(c);
	}

	@Override
	public Object[] toArray() {
		return collection.toArray();
	}

	@Override
	public <U> U[] toArray(U[] a) {
		return collection.toArray(a);
	}

	@Override
	public void clear() {
		collection.clear();
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