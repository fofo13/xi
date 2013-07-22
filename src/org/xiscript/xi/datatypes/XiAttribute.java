package org.xiscript.xi.datatypes;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.xiscript.xi.compilation.Type;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;

public class XiAttribute extends DataType {

	private static final long serialVersionUID = 0L;

	private static final Pattern FORM = Pattern.compile("\\w*");

	private static final Map<String, XiAttribute> cache = new HashMap<String, XiAttribute>();

	private String val;

	private XiAttribute(String val) {
		this.val = val;
	}

	public static XiAttribute valueOf(String val) {
		if (cache.containsKey(val))
			return cache.get(val);

		if (!FORM.matcher(val).matches())
			ErrorHandler.invokeError(ErrorType.INVALID_IDENTIFIER, val);

		return new XiAttribute(val);
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

	@Override
	public XiType type() {
		return XiType.valueOf(Type.ATTRIBUTE);
	}

}