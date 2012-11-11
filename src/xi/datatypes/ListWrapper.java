package xi.datatypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
		List<DataType> newList = new ArrayList<DataType>(collection.size() * n.val());
		for (int i = 0 ; i < n.val() ; i++)
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
	
	public CollectionWrapper<List<DataType>> cut(XiList params) {
		int m = ((XiInt)params.get(0)).val();
		int n = ((XiInt)params.get(1)).val();
		n = n < 0 ? collection.size() + n : n;
		m = m < 0 ? collection.size() + m : m;
		List<DataType> newList = new ArrayList<DataType>(n);
		for (int i = m ; i < n ; i++)
			newList.add(get(i));
		return instantiate(newList);
	}
	
}