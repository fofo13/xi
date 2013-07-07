package org.xiscript.xi.datatypes;

import java.io.Serializable;

import org.xiscript.xi.datatypes.numeric.XiInt;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;

public abstract class DataType implements Comparable<DataType>, Serializable {

	private static final long serialVersionUID = 0L;

	private static final XiAttribute HASH = XiAttribute.valueOf("hash");
	private static final XiAttribute TYPE = XiAttribute.valueOf("type");

	public abstract boolean isEmpty();

	public int length() {
		return toString().length();
	}

	public DataType getAttribute(XiAttribute a) {
		if (a.equals(HASH))
			return new XiInt(hashCode());
		if (a.equals(TYPE))
			return type();

		ErrorHandler.invokeError(ErrorType.INVALID_ATTRIBUTE, type(), a);
		return null;
	}

	public void setAttribute(XiAttribute a, DataType value) {
		ErrorHandler.invokeError(ErrorType.UNASSIGNABLE_ATTRIBUTE, type(), a);
	}

	public abstract Object getJavaAnalog();

	public void delete() {
	}

	public abstract XiType type();

	@Override
	public String toString() {
		return "<" + type().toString().toLowerCase() + " at "
				+ Integer.toHexString(hashCode()) + ">";
	}

}