package org.xiscript.xi.datatypes.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiType;
import org.xiscript.xi.datatypes.iterable.XiGenerator;

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

	@Override
	public CollectionWrapper<List<DataType>> instantiate(
			Collection<DataType> col) {
		return new XiList(new ArrayList<DataType>(col));
	}

	@Override
	public XiType type() {
		return XiType.valueOf(XiType.Type.LIST);
	}

}