package org.xiscript.xi.datatypes.iterable;

import java.util.Iterator;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiType;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.datatypes.functional.Function;
import org.xiscript.xi.datatypes.functional.XiBlock;
import org.xiscript.xi.datatypes.numeric.XiInt;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;
import org.xiscript.xi.operations.BuiltInOperation;

public abstract class XiIterable extends DataType implements Iterable<DataType> {

	private static final long serialVersionUID = 0L;

	public XiIterable map(final XiBlock block) {
		final Iterator<DataType> iter = iterator();

		return new XiGenerator() {
			private static final long serialVersionUID = 0L;

			int index = 0;

			@Override
			public DataType next() {
				if (!iter.hasNext())
					return null;

				block.updateLocal(XiVar.SPEC_VAR, iter.next());
				block.updateLocal(XiVar.INDEX_VAR, new XiInt(index));
				index++;

				return block.evaluate();
			}
		};
	}

	public XiIterable map(final Function f, final VariableCache globals) {
		final Iterator<DataType> iter = iterator();

		return new XiGenerator() {
			private static final long serialVersionUID = 0L;

			@Override
			public DataType next() {
				if (!iter.hasNext())
					return null;

				return f.evaluate(globals, iter.next());
			}
		};
	}

	public XiIterable filter(final XiBlock block) {
		final Iterator<DataType> iter = iterator();

		return new XiGenerator() {
			private static final long serialVersionUID = 0L;

			int index = 0;

			@Override
			public DataType next() {
				if (!iter.hasNext())
					return null;

				DataType next = iter.next();
				block.updateLocal(XiVar.SPEC_VAR, next);
				block.updateLocal(XiVar.INDEX_VAR, new XiInt(index));
				index++;

				return block.evaluate().isEmpty() ? next() : next;
			}
		};
	}

	public XiIterable filter(final Function f, final VariableCache globals) {
		final Iterator<DataType> iter = iterator();

		return new XiGenerator() {
			private static final long serialVersionUID = 0L;

			@Override
			public DataType next() {
				if (!iter.hasNext())
					return null;

				DataType next = iter.next();
				return f.evaluate(globals, next).isEmpty() ? next() : next;
			}
		};
	}

	public DataType sum() {
		DataType result = null;
		for (DataType data : this)
			result = (result == null) ? data : BuiltInOperation.ADD.evaluate(
					result, data);
		return (result == null) ? new XiInt(0) : result;
	}

	@Override
	public int compareTo(DataType other) {
		ErrorHandler.invokeError(ErrorType.UNCOMPARABLE, type());
		return 0;
	}

	@Override
	public XiType type() {
		return XiType.valueOf(XiType.Type.ITER);
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

}