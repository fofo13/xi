package org.xiscript.xi.datatypes;

import java.util.HashMap;
import java.util.Map;

import org.xiscript.xi.datatypes.numeric.XiInt;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;

public abstract class DataType implements Comparable<DataType> {

	protected Map<XiAttribute, DataType> attributes = new HashMap<XiAttribute, DataType>();

	public abstract boolean isEmpty();

	public int length() {
		return toString().length();
	}

	public abstract Object getJavaAnalog();

	protected void refreshAttributes() {
		attributes.put(new XiAttribute("hash"), new XiInt(hashCode()));
	}

	public DataType getAttribute(XiAttribute attribute) {
		refreshAttributes();
		DataType d = attributes.get(attribute);
		if (d == null)
			ErrorHandler.invokeError(ErrorType.INVALID_ATTRIBUTE, this,
					attribute);
		return d;
	}

	public void setAttribute(XiAttribute attribute, DataType data) {
		attributes.put(attribute, data);
	}

	public void delete() {
	}

	public abstract XiType type();

}