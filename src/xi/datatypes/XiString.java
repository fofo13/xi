package xi.datatypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XiString extends DataType {

	private String val;

	public XiString(String exp) {
		val = exp.replaceAll("\"", "");
	}

	public String val() {
		return val;
	}

	public XiString add(XiString other) {
		return new XiString(val + other.val());
	}

	public XiString mul(XiNum n) {
		String s = "";
		for (int i = 0; i < n.val(); i++)
			s += val;
		return new XiString(s);
	}

	public XiNum indexOf(XiString other) {
		return new XiNum(val.indexOf(other.val()));
	}

	public XiString lshift(XiNum n) {
		List<Character> l = new ArrayList<Character>(val.length());
		for (char c : val.toCharArray())
			l.add(c);
		Collections.rotate(l, -n.val());
		char[] chars = new char[l.size()];
		for (int i = 0; i < chars.length; i++)
			chars[i] = l.get(i);
		return new XiString(new String(chars));
	}

	public XiString rshift(XiNum n) {
		List<Character> l = new ArrayList<Character>(val.length());
		for (char c : val.toCharArray())
			l.add(c);
		Collections.rotate(l, n.val());
		char[] chars = new char[l.size()];
		for (int i = 0; i < chars.length; i++)
			chars[i] = l.get(i);
		return new XiString(new String(chars));
	}

	@Override
	public boolean isEmpty() {
		return val.isEmpty();
	}

	@Override
	public int compareTo(DataType other) {
		if (other instanceof XiString)
			return val.compareTo(((XiString)other).val());
		return 0;
	}

	@Override
	public String toString() {
		return val;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof XiString)
			return val.equals(((XiString) o).val());
		return false;
	}

	@Override
	public int hashCode() {
		return val.hashCode();
	}

}