package org.xiscript.xi.datatypes.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xiscript.xi.core.Parser;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiAttribute;
import org.xiscript.xi.datatypes.XiType;
import org.xiscript.xi.datatypes.numeric.XiInt;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;

public class XiString extends ListWrapper implements CharSequence {

	private static final long serialVersionUID = 0L;

	private static class XiChar extends DataType {

		private static final long serialVersionUID = 0L;

		private char val;

		public XiChar(char val) {
			this.val = val;
		}

		public char val() {
			return val;
		}

		public XiString toXiString() {
			List<DataType> l = new ArrayList<DataType>();
			l.add(new XiChar(val));
			return new XiString(l);
		}

		@Override
		public Character getJavaAnalog() {
			return val;
		}

		@Override
		public boolean isEmpty() {
			return val == 0;
		}

		@Override
		public int compareTo(DataType other) {
			if (!(other instanceof XiChar))
				return 0;
			return (new Character(val)).compareTo(new Character(
					((XiChar) other).val()));
		}

		@Override
		public String toString() {
			return String.valueOf(val);
		}

		@Override
		public XiType type() {
			ErrorHandler.invokeError(ErrorType.INTERNAL);
			return null;
		}

	}

	private boolean raw;

	public XiString(List<DataType> list) {
		super(new ArrayList<DataType>(list.size()));

		for (DataType d : list) {
			if (d instanceof XiChar)
				collection.add(d);
			else if (d instanceof XiString)
				for (char c : d.toString().toCharArray())
					collection.add(new XiChar(c));
			else
				ErrorHandler.invokeError(ErrorType.INTERNAL);
		}
	}

	public XiString(String expr, boolean raw) {
		this(new ArrayList<DataType>());

		for (char c : expr.toCharArray())
			collection.add((new XiChar(c)).toXiString());

		this.raw = raw;
	}

	public XiString(String expr) {
		this(expr, false);
	}

	@Override
	public DataType get(int index) {
		return new XiString(super.get(index).toString());
	}

	public XiList useToSplit(XiString str) {
		String[] result = str.toString().split(Pattern.quote(toString()));

		List<DataType> list = new ArrayList<DataType>(result.length);
		for (String s : result)
			list.add(new XiString(s));

		return new XiList(list);
	}

	public XiString cut(XiString other) {
		if (other instanceof XiRegex)
			return new XiString(toString().replaceAll(other.toString(), ""));
		return new XiString(toString().replace(other.toString(), ""));
	}

	public XiString replace(XiString sub, XiString rep) {
		if (sub instanceof XiRegex)
			return new XiString(toString().replaceAll(sub.toString(),
					rep.toString()));
		return new XiString(toString().replace(sub.toString(), rep.toString()));
	}

	@Override
	public DataType getAttribute(XiAttribute a) {
		if (a.equals(ADD)) {
			ErrorHandler.invokeError(ErrorType.INVALID_ATTRIBUTE, type(), ADD);
		}

		return super.getAttribute(a);
	}

	@Override
	public char charAt(int index) {
		return toString().charAt(index);
	}

	@Override
	public CharSequence subSequence(int beginIndex, int endIndex) {
		return toString().substring(beginIndex, endIndex);
	}

	@Override
	public DataType find(DataType data) {
		if (data instanceof XiRegex) {
			List<DataType> matches = new ArrayList<DataType>();

			Pattern p = Pattern.compile(data.toString());
			Matcher m = p.matcher(toString());

			while (m.find())
				matches.add(new XiString(m.group()));

			return new XiList(matches);
		}

		if (data instanceof XiString) {
			return new XiInt(toString().indexOf(data.toString()));
		}

		return super.find(data);
	}

	@Override
	public String getJavaAnalog() {
		return toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (DataType c : collection)
			sb.append(c.toString());
		return raw ? sb.toString() : Parser.unescapeJava(sb.toString());
	}

	@Override
	public ListWrapper instantiate(Collection<DataType> col) {
		return new XiString(new ArrayList<DataType>(col));
	}

	@Override
	public boolean contains(Object data) {
		if (data instanceof XiRegex)
			return toString().matches(".*" + data + ".*");
		if (data instanceof XiString)
			return toString().contains(data.toString());
		return false;
	}

	@Override
	public int compareTo(DataType other) {
		if (!(other instanceof XiString))
			return 0;
		return toString().compareTo(other.toString());
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof XiRegex)
			return toString().matches(o.toString());
		return o instanceof XiString && toString().equals(o.toString());
	}

	@Override
	public XiType type() {
		return XiType.valueOf(XiType.Type.STRING);
	}

}