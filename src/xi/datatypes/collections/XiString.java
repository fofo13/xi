package xi.datatypes.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import xi.datatypes.DataType;

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
			return "" + val;
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

	@Override
	public String toString() {
		String val = "";
		for (DataType c : collection)
			val += c.toString();
		return val;
	}

	@Override
	public ListWrapper instantiate(Collection<DataType> col) {
		return new XiString(new ArrayList<DataType>(col));
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof XiString && toString().equals(o.toString());
	}

}