package xi.datatypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import xi.Parser;

public class XiList extends DataType {

	private List<DataType> list;

	public XiList(List<DataType> list) {
		this.list = list;
	}

	public XiList() {
		this(new ArrayList<DataType>());
	}

	public XiList(int n) {
		this(new ArrayList<DataType>(n));
		for (int i = 0; i < n; i++)
			list.add(new XiNum(i));
	}

	public static XiList parse(String exp) {
		if (exp.equals("[]"))
			return new XiList();

		String[] split = Parser.tokenize(exp.substring(1, exp.length() - 1));
		List<DataType> list = new ArrayList<DataType>(split.length);
		for (String s : split)
			list.add(DataType.create(s));
		return new XiList(list);
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

	/*
	 * public DataType max() { return Collections.max(list); }
	 * 
	 * public int min() { return Collections.min(list); }
	 */

	public XiList shuffle() {
		List<DataType> newList = new ArrayList<DataType>(list.size());
		for (DataType data : list)
			newList.add(data);
		Collections.shuffle(newList);
		return new XiList(newList);
	}

	public XiList map(XiBlock block) {
		List<DataType> newList = new ArrayList<DataType>(list.size());
		for (DataType a : list) {
			block.updateLocal(new XiVar(".", a));
			newList.add(block.evaluate());
		}
		return new XiList(newList);
	}

	public XiList filter(XiBlock block) {
		List<DataType> newList = new ArrayList<DataType>(list.size());
		for (DataType a : list) {
			block.updateLocal(new XiVar(".", a));
			if (!block.evaluate().isEmpty())
				newList.add(a);
		}
		return new XiList(newList);
	}

	public XiNum sum() {
		int n = 0;
		for (DataType data : list)
			n += ((XiNum) data).val();
		return new XiNum(n);
	}

	public XiList append(DataType data) {
		List<DataType> newList = new ArrayList<DataType>(list.size());
		for (DataType d : list)
			newList.add(d);
		newList.add(data);
		return new XiList(newList);
	}

	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public String toString() {
		return list.toString().replaceAll(",", "");
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