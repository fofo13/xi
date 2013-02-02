package org.xiscript.xi.datatypes.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xiscript.xi.core.Parser;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiAttribute;
import org.xiscript.xi.datatypes.functional.HiddenLambda;

public class XiString extends ListWrapper {

	private static class XiChar extends DataType {

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

	}

	public XiString(List<DataType> list) {
		super(list);
	}

	public XiString(String exp) {
		this(new ArrayList<DataType>());

		for (char c : exp.toCharArray())
			collection.add((new XiChar(c)).toXiString());
	}

	public XiList toList() {
		return new XiList(collection);
	}

	public XiString cut(XiString other) {
		return new XiString(toString().replace(other.toString(), ""));
	}

	public XiString replace(XiString sub, XiString rep) {
		if (sub instanceof XiRegex)
			return new XiString(toString().replaceAll(sub.toString(),
					rep.toString()));
		return new XiString(toString().replace(sub.toString(), rep.toString()));
	}

	@Override
	protected void refreshAttributes() {
		final String str = toString();

		attributes.put(new XiAttribute("regex"), new XiRegex(str));

		String[] split = Parser.whitespace.split(toString());
		List<DataType> l = new ArrayList<DataType>(split.length);

		for (String s : split)
			l.add(new XiString(s));

		attributes.put(new XiAttribute("split"), new XiList(l));

		HiddenLambda splitter = new HiddenLambda(1) {
			@Override
			public DataType evaluate(DataType... args) {

				String delim = args[0].toString();
				if (!(args[0] instanceof XiRegex))
					delim = Pattern.quote(delim);

				String[] split = str.split(delim);
				List<DataType> l = new ArrayList<DataType>(split.length);

				for (String s : split)
					l.add(new XiString(s));

				return new XiList(l);
			}
		};

		attributes.put(new XiAttribute("splitter"), splitter);

		super.refreshAttributes();
	}

	@Override
	public DataType find(DataType data) {
		if (data instanceof XiRegex) {
			Pattern p = Pattern.compile(((XiRegex) data).toString());
			Matcher m = p.matcher(toString());

			XiList matches = new XiList();
			while (m.find())
				matches = (XiList) matches.add(new XiString(m.group()));

			return matches;
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
		return Parser.unescapeJava(sb.toString());
	}

	@Override
	public ListWrapper instantiate(Collection<DataType> col) {
		return new XiString(new ArrayList<DataType>(col));
	}

	@Override
	public boolean contains(DataType data) {
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

}