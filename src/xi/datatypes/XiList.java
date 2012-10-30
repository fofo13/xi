package xi.datatypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import xi.Operation;
import xi.Parser;
import xi.VariableCache;

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

	public static XiList parse(String exp, VariableCache cache) {
		if (exp.equals("[]"))
			return new XiList();
		
		String[] split = Parser.tokenize(exp.substring(1, exp.length() - 1).trim());
		List<DataType> list = new ArrayList<DataType>(split.length);
		for (String s : split)
			list.add(Parser.parseNode(s, cache).evaluate());
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

	public DataType sum() {
		if (isEmpty())
			return new XiNum(0);
		DataType d = list.get(0);
		for (int i = 1 ; i < list.size() ; i++)
			d = Operation.ADD.evaluate(new DataType[]{d, list.get(i)}, null);
		return d;
	}

	public XiList add(DataType data) {
		List<DataType> newList = new ArrayList<DataType>(list.size() + 1);
		newList.addAll(list);
		newList.add(data);
		return new XiList(newList);
	}

	public XiList remove(XiNum n) {
		int index = n.val();
		List<DataType> newList = new ArrayList<DataType>(list);
		newList.remove(index < 0 ? size() + index : index);
		return new XiList(newList);
	}
	
	public XiList mul(XiNum n) {
		List<DataType> newList = new ArrayList<DataType>(list.size() * n.val());
		for (int i = 0 ; i < n.val() ; i++)
			newList.addAll(list);
		return new XiList(newList);
	}
	
	public XiList abs() {
		List<DataType> newList = new ArrayList<DataType>();
		for (DataType data : list)
			if (data instanceof XiList)
				newList.addAll(((XiList)data).abs().list());
			else
				newList.add(data);
		return new XiList(newList);
	}
	
	public int size() {
		return list.size();
	}

	public XiList lshift(XiNum n) {
		List<DataType> newList = new ArrayList<DataType>(list);
		Collections.rotate(newList, -n.val());
		return new XiList(newList);
	}
	
	public XiList rshift(XiNum n) {
		List<DataType> newList = new ArrayList<DataType>(list);
		Collections.rotate(newList, n.val());
		return new XiList(newList);
	}
	
	public XiList zip() {
		int n = ((XiList)list.get(0)).length();
		List<DataType> newList = new ArrayList<DataType>(n);
		for (int i = 0 ; i < n ; i++) {
			List<DataType> sub = new ArrayList<DataType>(length());
			for (int j = 0 ; j < length() ; j++)
				sub.add(((XiList)list.get(j)).get(i));
			newList.add(new XiList(sub));
		}
		return new XiList(newList);
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
	public String toString() {
		return list.toString();
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