package org.xiscript.xi.datatypes.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xiscript.xi.compilation.Type;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiType;
import org.xiscript.xi.datatypes.functional.HiddenFunc;
import org.xiscript.xi.datatypes.numeric.XiInt;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;

public class XiRegex extends XiString {

	private static class XiMatcher extends HiddenFunc {

		private static final long serialVersionUID = 0L;

		private Matcher matcher;

		public XiMatcher(Matcher matcher) {
			super(1);
			this.matcher = matcher;
			matcher.find();
		}

		@Override
		public XiString evaluate(DataType... args) { // TODO: named capturing
														// groups
			return new XiString(matcher.group(((XiInt) args[0]).val()));
		}

		@Override
		public int length() {
			return matcher.groupCount();
		}

		@Override
		public Matcher getJavaAnalog() {
			return matcher;
		}

		@Override
		public XiType type() {
			return XiType.valueOf(Type.MATCHER);
		}

		@Override
		public boolean isEmpty() {
			return !matcher.matches();
		}

		@Override
		public int compareTo(DataType d) {
			ErrorHandler.invokeError(ErrorType.UNCOMPARABLE, type());
			return 0;
		}

		@Override
		public String toString() {
			return matcher.group();
		}

	}

	private static final long serialVersionUID = 0L;

	private Pattern pattern;

	public XiRegex(List<DataType> list) {
		super(list);
		pattern = Pattern.compile(toString());
	}

	public XiRegex(String expr) {
		super(expr);
		pattern = Pattern.compile(expr);
	}

	public Pattern pattern() {
		return pattern;
	}

	public XiMatcher match(XiString str) {
		return new XiMatcher(pattern.matcher(str));
	}

	@Override
	public XiList useToSplit(XiString str) {
		String[] result = pattern.split(str);

		List<DataType> list = new ArrayList<DataType>(result.length);
		for (String s : result)
			list.add(new XiString(s));

		return new XiList(list);
	}

	@Override
	public ListWrapper instantiate(Collection<DataType> col) {
		return new XiRegex(new ArrayList<DataType>(col));
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof XiString)
			return pattern.matcher((XiString) o).matches();
		return o instanceof XiRegex && pattern.equals(((XiRegex) o).pattern);
	}

	@Override
	public String toString() {
		return pattern.pattern();
	}

}