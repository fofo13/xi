package org.xiscript.xi.datatypes.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.xiscript.xi.core.XiGenerator;
import org.xiscript.xi.datatypes.DataType;

public class XiList extends ListWrapper {

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