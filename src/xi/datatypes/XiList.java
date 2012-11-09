package xi.datatypes;

import java.util.ArrayList;
import java.util.List;

import xi.core.Operation;
import xi.core.Parser;
import xi.core.VariableCache;

public class XiList extends ListWrapper {

	public XiList(List<DataType> list) {
		super(list);
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

	public DataType sum() {
		if (isEmpty())
			return new XiNum(0);
		DataType d = list.get(0);
		for (int i = 1 ; i < list.size() ; i++)
			d = Operation.ADD.evaluate(new DataType[]{d, list.get(i)}, null);
		return d;
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
	public String toString() {
		return list.toString();
	}
	
	@Override
	public ListWrapper instantiate(List<DataType> list) {
		return new XiList(list);
	}

}