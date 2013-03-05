package org.xiscript.xi.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Range implements Iterator<Integer> {

	private int min, max, step;
	private int i;

	public Range(int min, int max, int step) {
		this.min = min;
		this.max = max;
		this.step = step;
		i = min;
	}

	public Range(int min, int max) {
		this(min, max, 1);
	}

	public Range(int max) {
		this(0, max, 1);
	}

	@Override
	public Integer next() {
		if (!hasNext())
			throw new NoSuchElementException();

		int i0 = i;
		i += step;
		return i0;
	}

	@Override
	public boolean hasNext() {
		return (step > 0 && i < max) || (step < 0 && i > max);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	public void reset() {
		i = min;
	}

}