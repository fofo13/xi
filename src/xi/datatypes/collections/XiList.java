package xi.datatypes.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import xi.core.Parser;
import xi.core.SyntaxTree;
import xi.core.VariableCache;
import xi.datatypes.DataType;
import xi.datatypes.numeric.XiInt;

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
			collection.add(new XiInt(i));
	}

	public static XiList parse(String exp, VariableCache cache) {
		if (exp.matches("[\\[\\(]\\s+[\\)\\]]"))
			return new XiList();

		String[] split = Parser.splitOnSemiColons(exp.substring(1,
				exp.length() - 1).trim());
		StringBuilder result = new StringBuilder(split.length);
		for (String s : split)
			result.append(s + " ");

		SyntaxTree t = new SyntaxTree(result.toString(), cache);

		List<DataType> list = new ArrayList<DataType>();
		do {
			list.add(t.evaluate());
		} while (!t.nodes().isEmpty());

		return new XiList(list);
	}

	public XiList abs() {
		List<DataType> newList = new ArrayList<DataType>();
		for (DataType data : collection)
			if (data instanceof XiList)
				newList.addAll(((XiList) data).abs().list());
			else
				newList.add(data);
		return new XiList(newList);
	}

	@Override
	public CollectionWrapper<List<DataType>> instantiate(
			Collection<DataType> col) {
		return new XiList(new ArrayList<DataType>(col));
	}

	public XiTuple asTuple() {
		return new XiTuple(collection);
	}

}