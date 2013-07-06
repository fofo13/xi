package org.xiscript.xi.datatypes.collections;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiType;
import org.xiscript.xi.datatypes.XiVar;

public final class ArgumentList extends DataType {

	public static final ArgumentList EMPTY = new ArgumentList(new XiVar[0]);

	private final XiVar[] array;

	public ArgumentList(XiVar[] array) {
		this.array = array;
	}

	@Override
	public XiType type() {
		return null;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public int compareTo(DataType other) {
		return -1;
	}

	@Override
	public XiVar[] getJavaAnalog() {
		return array;
	}
}