package org.xiscript.xi.datatypes.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.xiscript.xi.core.Parser;
import org.xiscript.xi.datatypes.DataType;


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
		exp = exp.replace("\"", "");
		for (char c : exp.toCharArray())
			collection.add((new XiChar(c)).toXiString());
	}

	public XiList toList() {
		return new XiList(collection);
	}
	
	public XiString cut(XiString other) {
		return new XiString(toString().replace(other.toString(), ""));
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
		if (! (other instanceof XiString))
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