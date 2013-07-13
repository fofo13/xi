package org.xiscript.xi.datatypes.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiAttribute;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.datatypes.functional.Function;
import org.xiscript.xi.datatypes.functional.HiddenFunc;
import org.xiscript.xi.datatypes.functional.XiBlock;
import org.xiscript.xi.datatypes.iterable.XiIterable;
import org.xiscript.xi.datatypes.numeric.XiInt;

public abstract class CollectionWrapper<T extends Collection<DataType>> extends
		XiIterable implements Collection<DataType> {

	private static final long serialVersionUID = 0L;

	protected static final XiAttribute ADD = XiAttribute.valueOf("add");

	protected T collection;

	private Function adder;

	protected abstract CollectionWrapper<T> instantiate(Collection<DataType> col);

	public CollectionWrapper(T collection) {
		this.collection = collection;
	}

	private void initAdder() {
		adder = new HiddenFunc(1) {
			private static final long serialVersionUID = 0L;

			@Override
			public DataType evaluate(DataType... args) {
				return new XiInt(collection.add(args[0]));
			}
		};
	}

	public T collection() {
		return collection;
	}

	@Override
	public CollectionWrapper<T> map(final XiBlock block) {
		return map(block, false);
	}

	@Override
	public CollectionWrapper<T> map(Function f, VariableCache globals) {
		return map(f, false, globals);
	}

	@Override
	public CollectionWrapper<T> filter(final XiBlock block) {
		return filter(block, false);
	}

	@Override
	public CollectionWrapper<T> filter(Function f, VariableCache globals) {
		return filter(f, false, globals);
	}

	public CollectionWrapper<T> map(XiBlock block, boolean deep) {
		Collection<DataType> col = new ArrayList<DataType>(collection.size());
		int index = 0;
		for (DataType a : collection) {
			block.updateLocal(XiVar.SPEC_VAR, a);
			block.updateLocal(XiVar.INDEX_VAR, new XiInt(index));
			col.add((deep && (a instanceof CollectionWrapper<?>)) ? ((CollectionWrapper<?>) a)
					.map(block, true) : block.evaluate());
			index++;
		}
		return instantiate(col);
	}

	public CollectionWrapper<T> map(Function f, boolean deep,
			VariableCache globals) {
		Collection<DataType> col = new ArrayList<DataType>(collection.size());
		final boolean multiArg = f.length() > 1;
		for (DataType a : collection) {
			if (deep && (a instanceof CollectionWrapper<?>))
				col.add(((CollectionWrapper<?>) a).map(f, true, globals));

			col.add(multiArg ? f.evaluate(globals, (ListWrapper) a) : f
					.evaluate(globals, a));
		}
		return instantiate(col);
	}

	public CollectionWrapper<T> filter(XiBlock block, boolean deep) {
		Collection<DataType> col = new ArrayList<DataType>(collection.size());
		int index = 0;
		for (DataType data : collection) {
			block.updateLocal(XiVar.SPEC_VAR, data);
			block.updateLocal(XiVar.INDEX_VAR, new XiInt(index));

			if (deep && (data instanceof CollectionWrapper<?>))
				col.add(((CollectionWrapper<?>) data).filter(block, deep));
			else if (!block.evaluate().isEmpty())
				col.add(data);

			index++;
		}
		return instantiate(col);
	}

	public CollectionWrapper<T> filter(Function f, boolean deep,
			VariableCache globals) {

		Collection<DataType> col = new ArrayList<DataType>(collection.size());
		final boolean multiArg = f.length() > 1;

		for (DataType data : collection) {

			if (deep && (data instanceof CollectionWrapper<?>))
				col.add(((CollectionWrapper<?>) data).filter(f, deep, globals));

			else if (!(multiArg ? f.evaluate(globals, (ListWrapper) data) : f
					.evaluate(globals, data)).isEmpty())
				col.add(data);
		}

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
	public DataType getAttribute(XiAttribute a) {
		if (a.equals(ADD)) {
			if (adder == null)
				initAdder();

			return adder;
		}

		return super.getAttribute(a);
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