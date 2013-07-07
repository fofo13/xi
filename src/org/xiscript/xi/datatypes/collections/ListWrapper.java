package org.xiscript.xi.datatypes.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiAttribute;
import org.xiscript.xi.datatypes.numeric.XiInt;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;
import org.xiscript.xi.operations.BuiltInOperation;
import org.xiscript.xi.util.Range;

public abstract class ListWrapper extends CollectionWrapper<List<DataType>>
		implements List<DataType> {

	private static final long serialVersionUID = 0L;

	private static Pattern DPLUS = Pattern.compile("\\d+");

	private static XiAttribute REV = XiAttribute.valueOf("rev");

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

	public DataType get(int start, int end, int step) {
		if (start > end && step > 0)
			end += length();

		List<DataType> newList = new ArrayList<DataType>(
				(int) Math.ceil((double) Math.abs(end - start) / Math.abs(step)));

		Iterator<Integer> range = new Range(start, end, step);
		while (range.hasNext())
			newList.add(get(range.next()));

		return instantiate(newList);
	}

	public DataType get(int start, int end) {
		return get(start, end, 1);
	}

	public DataType get(XiTuple t) {
		int len = t.length();

		if (len == 1)
			return get(((XiInt) t.get(0)).val());

		int start = ((XiInt) t.get(0)).val();
		int end = ((XiInt) t.get(1)).val();
		int step = (len > 2) ? ((XiInt) t.get(2)).val() : 1;

		return get(start, end, step);
	}

	public DataType get(XiList l) {
		XiInt last = (XiInt) l.get(-1);

		if (l.length() == 1)
			return get(last);

		ListWrapper sub = (ListWrapper) get((XiInt) l.get(0));
		for (int i = 1; i < l.length() - 1; i++)
			sub = (ListWrapper) sub.get((XiInt) l.get(i));

		return sub.get(last);
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

	public void sort() {
		Collections.sort(collection);
	}

	public void sort(Comparator<DataType> cmp) {
		Collections.sort(collection, cmp);
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
			ErrorHandler.invokeError(ErrorType.ARGUMENT, BuiltInOperation.CUT);

		int m = ((XiInt) params.get(0)).val();
		int n = ((XiInt) params.get(1)).val();
		int step = 1;

		m = m < 0 ? collection.size() + m : m;
		n = n < 0 ? collection.size() + n : n;
		if (params.length() == 3)
			step = ((XiInt) params.get(2)).val();

		if (step == 0)
			ErrorHandler.invokeError(ErrorType.ARGUMENT, BuiltInOperation.CUT);

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
			ErrorHandler.invokeError(ErrorType.ARGUMENT, BuiltInOperation.DEL);

		int m = ((XiInt) params.get(0)).val();
		int n = ((XiInt) params.get(1)).val();
		int step = 1;

		m = m < 0 ? collection.size() + m : m;
		n = n < 0 ? collection.size() + n : n;
		if (params.length() == 3)
			step = ((XiInt) params.get(2)).val();

		if (step == 0)
			ErrorHandler.invokeError(ErrorType.ARGUMENT, BuiltInOperation.DEL);

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
			ErrorHandler
					.invokeError(ErrorType.ARGUMENT, BuiltInOperation.RANGE);

		int m = ((XiInt) params.get(0)).val();
		int n = ((XiInt) params.get(1)).val();
		int step = 1;

		if (params.length() == 3)
			step = ((XiInt) params.get(2)).val();

		return range(m, n, step);
	}

	private static XiList range(int m, int n, int step) {
		if (step == 0)
			ErrorHandler
					.invokeError(ErrorType.ARGUMENT, BuiltInOperation.RANGE);

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

	public CollectionWrapper<?> abs() {
		List<DataType> newList = new ArrayList<DataType>();
		for (DataType data : collection)
			if (data instanceof ListWrapper)
				newList.addAll(((ListWrapper) data).abs().collection);
			else
				newList.add(data);

		return instantiate(newList);
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

	@Override
	public DataType getAttribute(XiAttribute a) {
		if (a.equals(REV)) {
			List<DataType> rev = new ArrayList<DataType>(size());
			for (int i = size() - 1; i >= 0; i--)
				rev.add(collection.get(i));

			return instantiate(rev);
		}

		String s = a.toString();
		if (DPLUS.matcher(s).matches()) {
			return collection.get(Integer.parseInt(s));
		}

		return super.getAttribute(a);
	}

	@Override
	public void setAttribute(XiAttribute a, DataType value) {
		String s = a.toString();
		if (DPLUS.matcher(s).matches()) {
			collection.set(Integer.parseInt(s), value);
			return;
		}

		super.setAttribute(a, value);
	}

}