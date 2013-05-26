package org.xiscript.xi.datatypes.iterable;

import java.util.Iterator;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.datatypes.functional.XiBlock;
import org.xiscript.xi.datatypes.functional.XiLambda;
import org.xiscript.xi.datatypes.numeric.XiInt;
import org.xiscript.xi.operations.BuiltInOperation;

public abstract class XiIterable extends DataType implements Iterable<DataType> {

	public XiIterable map(final XiBlock block) {
		final Iterator<DataType> iter = iterator();

		return new XiGenerator() {
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

	public XiIterable map(final XiLambda lambda, final VariableCache globals) {
		final Iterator<DataType> iter = iterator();

		return new XiGenerator() {
			@Override
			public DataType next() {
				if (!iter.hasNext())
					return null;

				return lambda.evaluate(globals, iter.next());
			}
		};
	}

	public XiIterable filter(final XiBlock block) {
		final Iterator<DataType> iter = iterator();

		return new XiGenerator() {
			int index = 0;

			@Override
			public DataType next() {
				if (!iter.hasNext())
					return null;

				DataType next = iter.next();
				block.updateLocal(new XiVar(".", true), next);
				block.updateLocal(new XiVar("_", true), new XiInt(index));
				index++;

				return block.evaluate().isEmpty() ? next() : next;
			}
		};
	}

	public XiIterable filter(final XiLambda lambda, final VariableCache globals) {
		final Iterator<DataType> iter = iterator();

		return new XiGenerator() {
			@Override
			public DataType next() {
				if (!iter.hasNext())
					return null;

				DataType next = iter.next();
				return lambda.evaluate(globals, next).isEmpty() ? next() : next;
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

}