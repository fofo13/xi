package org.xiscript.xi.datatypes.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.numeric.XiInt;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;
import org.xiscript.xi.operations.IntrinsicOperation;

public abstract class ListWrapper extends CollectionWrapper<List<DataType>>
		implements List<DataType> {

	public ListWrapper(List<DataType> list) {
		super(list);
	}

	public List<DataType> list() {
		return collection;
	}

	@Override
	public DataType get(int index) {
		index %= collection.size();
		return collection.get(index < 0 ? collection.size() + index : index);
	}

	public DataType get(XiInt index) {
		return get(index.val());
	}

	public DataType rnd() {
		List<DataType> newList = new ArrayList<DataType>(collection.size());
		for (DataType data : collection)
			newList.add(data);
		Collections.shuffle(newList);
		return instantiate(newList);
	}

	public CollectionWrapper<List<DataType>> mul(XiInt n) {
		List<DataType> newList = new ArrayList<DataType>(collection.size()
				* n.val());
		for (int i = 0; i < n.val(); i++)
			newList.addAll(collection);
		return instantiate(newList);
	}

	public CollectionWrapper<List<DataType>> sort() {
		List<DataType> newList = new ArrayList<DataType>(collection);
		Collections.sort(newList);
		return instantiate(newList);
	}

	public CollectionWrapper<List<DataType>> sort(Comparator<DataType> cmp) {
		List<DataType> newList = new ArrayList<DataType>(collection);
		Collections.sort(newList, cmp);
		return instantiate(newList);
	}

	public CollectionWrapper<List<DataType>> lshift(XiInt n) {
		List<DataType> newList = new ArrayList<DataType>(collection);
		Collections.rotate(newList, -n.val());
		return instantiate(newList);
	}

	public CollectionWrapper<List<DataType>> rshift(XiInt n) {
		List<DataType> newList = new ArrayList<DataType>(collection);
		Collections.rotate(newList, n.val());
		return instantiate(newList);
	}

	public CollectionWrapper<List<DataType>> zip() {
		int n = ((ListWrapper) collection.get(0)).length();
		List<DataType> newList = new ArrayList<DataType>(n);
		for (int i = 0; i < n; i++) {
			List<DataType> sub = new ArrayList<DataType>(length());
			for (int j = 0; j < length(); j++)
				sub.add(((ListWrapper) collection.get(j)).get(i));
			newList.add(instantiate(sub));
		}
		return instantiate(newList);
	}

	public CollectionWrapper<List<DataType>> cut(XiTuple params) {
		if (params.length() != 2 && params.length() != 3)
			ErrorHandler
					.invokeError(ErrorType.ARGUMENT, IntrinsicOperation.CUT);

		int m = ((XiInt) params.get(0)).val();
		int n = ((XiInt) params.get(1)).val();
		int step = 1;

		m = m < 0 ? collection.size() + m : m;
		n = n < 0 ? collection.size() + n : n;
		if (params.length() == 3)
			step = ((XiInt) params.get(2)).val();

		if (step == 0)
			ErrorHandler
					.invokeError(ErrorType.ARGUMENT, IntrinsicOperation.CUT);

		List<DataType> newList = new ArrayList<DataType>(n);
		for (int i = m; i < n; i += Math.abs(step))
			newList.add(get(i));

		if (step < 0)
			Collections.reverse(newList);

		return instantiate(newList);
	}

	public CollectionWrapper<List<DataType>> cut(XiInt params) {
		int n = params.val();
		n = n < 0 ? collection.size() + n : n;

		List<DataType> newList = new ArrayList<DataType>(size() - n);
		for (int i = n; i < size(); i++)
			newList.add(get(i));
		return instantiate(newList);
	}

	private CollectionWrapper<List<DataType>> delete(XiTuple params) {
		if (params.length() != 2 && params.length() != 3)
			ErrorHandler
					.invokeError(ErrorType.ARGUMENT, IntrinsicOperation.DEL);

		int m = ((XiInt) params.get(0)).val();
		int n = ((XiInt) params.get(1)).val();
		int step = 1;

		m = m < 0 ? collection.size() + m : m;
		n = n < 0 ? collection.size() + n : n;
		if (params.length() == 3)
			step = ((XiInt) params.get(2)).val();

		if (step == 0)
			ErrorHandler
					.invokeError(ErrorType.ARGUMENT, IntrinsicOperation.DEL);

		List<Integer> indexes = new ArrayList<Integer>();
		for (int i = m; i < n; i += step)
			indexes.add(i);
		Collections.reverse(indexes);

		List<DataType> newList = new ArrayList<DataType>(collection);

		for (int i : indexes)
			newList.remove(i);

		return instantiate(newList);
	}

	private CollectionWrapper<List<DataType>> delete(XiInt params) {
		int index = params.val() % collection.size();
		List<DataType> newList = new ArrayList<DataType>(collection);
		newList.remove(index < 0 ? newList.size() + index : index);

		return instantiate(newList);
	}

	public CollectionWrapper<List<DataType>> delete(DataType params) {
		if (params instanceof XiInt)
			return delete((XiInt) params);
		return delete((XiTuple) params);
	}

	public static XiList range(XiTuple params) {
		if (params.length() != 2 && params.length() != 3)
			ErrorHandler.invokeError(ErrorType.ARGUMENT,
					IntrinsicOperation.RANGE);

		int m = ((XiInt) params.get(0)).val();
		int n = ((XiInt) params.get(1)).val();
		int step = 1;

		if (params.length() == 3)
			step = ((XiInt) params.get(2)).val();

		return range(m, n, step);
	}

	private static XiList range(int m, int n, int step) {
		if (step == 0)
			ErrorHandler.invokeError(ErrorType.ARGUMENT,
					IntrinsicOperation.RANGE);

		int r = Math.abs(step);
		List<DataType> list = new ArrayList<DataType>();
		for (int i = m; i < n; i += r)
			list.add(new XiInt(i));

		if (step < 0)
			Collections.reverse(list);

		return new XiList(list);
	}

	@Override
	public DataType set(int index, DataType data) {
		return collection.set(index, data);
	}

	public void put(XiInt index, DataType data) {
		set(index.val(), data);
	}

	public DataType find(DataType data) {
		return new XiInt(indexOf(data));
	}

	public ListWrapper mod(int n) {
		int m = Math.abs(n);
		int size = length();
		int nlists = size / m + 1;
		List<DataType> newList = new ArrayList<DataType>(nlists);

		int r = 0;
		for (int i = 0; i < nlists; i++) {
			List<DataType> subList = new ArrayList<DataType>(m);
			for (int j = 0; j < m; j++) {
				if (r >= size)
					break;
				subList.add(collection.get(r));
				r++;
			}
			if (!subList.isEmpty())
				newList.add(instantiate(subList));
		}

		if (n < 0)
			Collections.reverse(newList);

		return new XiList(newList);
	}

	@Override
	public void add(int index, DataType data) {
		collection.add(index, data);
	}

	@Override
	public DataType remove(int index) {
		return collection.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return collection.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return collection.lastIndexOf(o);
	}

	@Override
	public boolean addAll(int index, Collection<? extends DataType> c) {
		return collection.addAll(index, c);
	}

	@Override
	public ListIterator<DataType> listIterator() {
		return collection.listIterator();
	}

	@Override
	public ListIterator<DataType> listIterator(int index) {
		return collection.listIterator(index);
	}

	@Override
	public List<DataType> subList(int fromIndex, int toIndex) {
		return collection.subList(fromIndex, toIndex);
	}

}