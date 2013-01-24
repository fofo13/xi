package org.xiscript.xi.datatypes.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.xiscript.xi.datatypes.DataType;

public class XiRegex extends XiString {

	public XiRegex(List<DataType> list) {
		super(list);
	}

	public XiRegex(String exp) {
		super(exp);
	}

	@Override
	public ListWrapper instantiate(Collection<DataType> col) {
		return new XiRegex(new ArrayList<DataType>(col));
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof XiString)
			return o.toString().matches(toString());
		return o instanceof XiRegex && toString().equals(o.toString());
	}

}