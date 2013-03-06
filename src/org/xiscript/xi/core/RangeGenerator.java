package org.xiscript.xi.core;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.numeric.XiInt;
import org.xiscript.xi.util.Range;

public class RangeGenerator extends XiGenerator {

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

}