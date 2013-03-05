package org.xiscript.xi.datatypes.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.xiscript.xi.core.Parser;
import org.xiscript.xi.core.SyntaxTree;
import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.core.XiGenerator;
import org.xiscript.xi.datatypes.DataType;

public class XiList extends ListWrapper {

	private static final Pattern EMPTY_LIST = Pattern
			.compile("\\s*\\[\\s*\\]\\s*");

	public XiList(List<DataType> list) {
		super(list);
	}

	public XiList() {
		this(new ArrayList<DataType>());
	}

	public XiList(XiGenerator iter) {
		this();
		for (DataType d : iter)
			add(d);
	}

	public static XiList parse(String exp, VariableCache cache) {
		if (EMPTY_LIST.matcher(exp).matches())
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

}