package org.xiscript.xi.datatypes.iterable;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiType;

public abstract class XiGenerator extends XiIterable {

	private static final long serialVersionUID = 0L;

	public abstract DataType next();

	public void reset() {
	}

	@Override
	public Iterator<DataType> iterator() {
		final XiGenerator gen = this;

		return new Iterator<DataType>() {
			private DataType val = gen.next();

			@Override
			public boolean hasNext() {
				return val != null;
			}

			@Override
			public DataType next() {
				try {
					if (!hasNext())
						throw new NoSuchElementException();
					return val;
				} finally {
					val = gen.next();
				}
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public Iterator<DataType> getJavaAnalog() {
		return iterator();
	}

	@Override
	public XiType type() {
		return XiType.valueOf(XiType.Type.GENERATOR);
	}

}
