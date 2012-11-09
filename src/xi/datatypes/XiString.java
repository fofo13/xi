package xi.datatypes;

import java.util.ArrayList;
import java.util.List;

public class XiString extends ListWrapper {

	private static class XiChar extends DataType {
		
		private char val;
		
		public XiChar(char val) { 
			this.val = val; 
		}
		
		public char val() {
			return val;
		}
		
		@Override
		public boolean isEmpty() {
			return val == 0;
		}
		
		@Override
		public int compareTo(DataType other) {
			if (! (other instanceof XiChar))
				return 0;
			return (new Character(val)).compareTo(new Character(((XiChar)other).val()));
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
			list.add(new XiChar(c));
	}

	@Override
	public String toString() {
		String val = "";
		for (DataType c : list)
			val += c.toString();
		return val;
	}
	
	@Override
	public ListWrapper instantiate(List<DataType> list) {
		return new XiString(list);
	}

}