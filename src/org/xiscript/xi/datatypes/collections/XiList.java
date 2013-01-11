package org.xiscript.xi.datatypes.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.xiscript.xi.core.Parser;
import org.xiscript.xi.core.SyntaxTree;
import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.numeric.XiInt;


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
		if (exp.replaceAll("\\s+", "").length() == 2)
			return new XiList();

		String[] split = Parser.splitOnSemiColons(exp.substring(1,
				exp.length() - 1).trim());
		StringBuilder result = new StringBuilder(split.length);
		for (String s : split) {
			result.append(s);
			result.append(" ");
		}

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