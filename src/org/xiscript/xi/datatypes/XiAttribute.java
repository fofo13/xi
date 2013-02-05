package org.xiscript.xi.datatypes;

import java.util.regex.Pattern;

import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;

public class XiAttribute extends DataType {

	private static final Pattern FORM = Pattern.compile("\\w*");

	private String val;
	private boolean setable;

	public XiAttribute(String val, boolean setable) {
		if (!FORM.matcher(val).matches())
			ErrorHandler.invokeError(ErrorType.INVALID_IDENTIFIER, val);

		this.val = val;
		this.setable = setable;
	}

	public XiAttribute(String val) {
		this(val, false);
	}

	public boolean setable() {
		return setable;
	}

	@Override
	public int compareTo(DataType other) {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return val.isEmpty();
	}

	@Override
	public String getJavaAnalog() {
		return val;
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof XiAttribute) && ((XiAttribute) o).val.equals(val);
	}

	@Override
	public int hashCode() {
		return val.hashCode();
	}

	@Override
	public String toString() {
		return val;
	}

}