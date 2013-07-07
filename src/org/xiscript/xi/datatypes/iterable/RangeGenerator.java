package org.xiscript.xi.datatypes.iterable;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.numeric.XiInt;
import org.xiscript.xi.util.Range;

public class RangeGenerator extends XiGenerator {

	private static final long serialVersionUID = 0L;

	private Range range;

	public RangeGenerator(int min, int max, int step) {
		range = new Range(min, max, step);
	}

	public RangeGenerator(int min, int max) {
		this(min, max, 1);
	}

	public RangeGenerator(int max) {
		this(0, max, 1);
	}

	@Override
	public DataType next() {
		if (range.hasNext())
			return new XiInt(range.next());

		range.reset();
		return null;
	}

	@Override
	public void reset() {
		range.reset();
	}

}