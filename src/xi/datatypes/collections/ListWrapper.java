package xi.datatypes.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import xi.datatypes.DataType;
import xi.datatypes.numeric.XiInt;

public abstract class ListWrapper extends CollectionWrapper<List<DataType>> {

	public ListWrapper(List<DataType> list) {
		super(list);
	}

	public List<DataType> list() {
		return collection;
	}

	public DataType get(int index) {
		index %= collection.size();
		return collection.get(index < 0 ? collection.size() + index : index);
	}

	public DataType get(XiInt index) {
		return get(index.val());
	}

	public CollectionWrapper<List<DataType>> shuffle() {
		List<DataType> newList = new ArrayList<DataType>(collection.size());
		for (DataType data : collection)
			newList.add(data);
		Collections.shuffle(newList);
		return instantiate(newList);
	}

	public CollectionWrapper<List<DataType>> remove(XiInt n) {
		int index = n.val();
		List<DataType> newList = new ArrayList<DataType>(collection);
		newList.remove(index < 0 ? size() + index : index);
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
	
	public CollectionWrapper<List<DataType>> cut(XiList params) {
		if (params.length() != 2 && params.length() != 3)
			throw new RuntimeException(
					"Argument of cut function must be of length 2 or 3.");

		int m = ((XiInt) params.get(0)).val();
		int n = ((XiInt) params.get(1)).val();
		int step = 1;

		m = m < 0 ? collection.size() + m : m;
		n = n < 0 ? collection.size() + n : n;
		if (params.length() == 3)
			step = ((XiInt) params.get(2)).val();

		if (step == 0)
			throw new RuntimeException("Cannot have step length of 0.");

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

	public void del(XiList params) {
		if (params.length() != 2 && params.length() != 3)
			throw new RuntimeException(
					"Argument of cut function must be of length 2 or 3.");

		int m = ((XiInt) params.get(0)).val();
		int n = ((XiInt) params.get(1)).val();
		int step = 1;

		m = m < 0 ? collection.size() + m : m;
		n = n < 0 ? collection.size() + n : n;
		if (params.length() == 3)
			step = ((XiInt) params.get(2)).val();

		if (step == 0)
			throw new RuntimeException("Cannot have step length of 0.");
		
		List<Integer> indexes = new ArrayList<Integer>();
		for (int i = m; i < n; i += step)
			indexes.add(i);
		Collections.reverse(indexes);
		
		for (int i : indexes)
			collection.remove(i);
	}

	public void del(XiInt params) {
		collection.remove(params.val());
	}
	
	public static XiList range(XiList params) {
		if (params.length() != 2 && params.length() != 3)
			throw new RuntimeException(
					"Argument of cut function must be of length 2 or 3.");

		int m = ((XiInt) params.get(0)).val();
		int n = ((XiInt) params.get(1)).val();
		int step = 1;

		if (params.length() == 3)
			step = ((XiInt) params.get(2)).val();

		if (step == 0)
			throw new RuntimeException("Cannot have step length of 0.");
		
		List<DataType> list = new ArrayList<DataType>();
		for (int i = m; i < n; i += step)
			list.add(new XiInt(i));
		
		return new XiList(list);
	}
	
	public void put(XiInt index, DataType data) {
		collection.set(index.val(), data);
	}

}