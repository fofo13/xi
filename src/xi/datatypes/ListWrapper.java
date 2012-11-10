package xi.datatypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ListWrapper extends DataType {
	
	protected List<DataType> list;

	protected abstract ListWrapper instantiate(List<DataType> l);
	
	public ListWrapper(List<DataType> list) {
		this.list = list;
	}

	public List<DataType> list() {
		return list;
	}
	
	public DataType get(int index) {
		index %= list.size();
		return list.get(index < 0 ? list.size() + index : index);
	}
	
	public DataType get(XiNum index) {
		return get(index.val());
	}

	public ListWrapper shuffle() {
		List<DataType> newList = new ArrayList<DataType>(list.size());
		for (DataType data : list)
			newList.add(data);
		Collections.shuffle(newList);
		return instantiate(newList);
	}

	public ListWrapper map(XiBlock block) {
		List<DataType> newList = new ArrayList<DataType>(list.size());
		for (DataType a : list) {
			block.updateLocal(new XiVar(".", a));
			newList.add(block.evaluate());
		}
		return instantiate(newList);
	}

	public ListWrapper filter(XiBlock block) {
		List<DataType> newList = new ArrayList<DataType>(list.size());
		for (DataType a : list) {
			block.updateLocal(new XiVar(".", a));
			if (!block.evaluate().isEmpty())
				newList.add(a);
		}
		return instantiate(newList);
	}

	public ListWrapper add(DataType data) {
		List<DataType> newList = new ArrayList<DataType>(list.size() + 1);
		newList.addAll(list);
		newList.add(data);
		return instantiate(newList);
	}

	public ListWrapper remove(XiNum n) {
		int index = n.val();
		List<DataType> newList = new ArrayList<DataType>(list);
		newList.remove(index < 0 ? size() + index : index);
		return instantiate(newList);
	}
	
	public ListWrapper mul(XiNum n) {
		List<DataType> newList = new ArrayList<DataType>(list.size() * n.val());
		for (int i = 0 ; i < n.val() ; i++)
			newList.addAll(list);
		return instantiate(newList);
	}
	
	public ListWrapper sort() {
		List<DataType> newList = new ArrayList<DataType>(list);
		Collections.sort(newList);
		return instantiate(newList);
	}
	
	public int size() {
		return list.size();
	}

	public ListWrapper lshift(XiNum n) {
		List<DataType> newList = new ArrayList<DataType>(list);
		Collections.rotate(newList, -n.val());
		return instantiate(newList);
	}
	
	public ListWrapper rshift(XiNum n) {
		List<DataType> newList = new ArrayList<DataType>(list);
		Collections.rotate(newList, n.val());
		return instantiate(newList);
	}
	
	public ListWrapper cut(XiList params) {
		int m = ((XiNum)params.get(0)).val();
		int n = ((XiNum)params.get(1)).val();
		n = n < 0 ? list.size() + n : n;
		m = m < 0 ? list.size() + m : m;
		List<DataType> newList = new ArrayList<DataType>(n);
		for (int i = m ; i < n ; i++)
			newList.add(get(i));
		return instantiate(newList);
	}
	
	@Override
	public int compareTo(DataType other) {
		if (other instanceof XiList)
			return (new Integer(list.size())).compareTo(((XiList)other).size());
		return 0;
	}
	
	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public int length() {
		return list.size();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof XiList)
			return list.equals(((XiList) o).list());
		return false;
	}

	@Override
	public int hashCode() {
		return list.hashCode();
	}
	
}