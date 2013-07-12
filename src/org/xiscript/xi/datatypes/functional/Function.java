package org.xiscript.xi.datatypes.functional;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.datatypes.collections.ListWrapper;
import org.xiscript.xi.datatypes.collections.XiTuple;
import org.xiscript.xi.exceptions.ReturnException;

public abstract class Function extends DataType {

	private static final long serialVersionUID = 0L;

	protected XiVar[] identifiers;
	protected XiBlock body;

	public Function(XiVar[] identifiers, XiBlock body) {
		this.identifiers = identifiers;
		this.body = body;
	}

	public DataType evaluate(VariableCache globals, DataType... args) {
		VariableCache scope = new VariableCache(globals); // TODO: This can
															// create extremely
															// long cache chains
															// with recursive
															// functions, and
															// can result in a
															// StackOverflowError

		for (int i = 0; i < identifiers.length; i++) {
			scope.put(identifiers[i], args[i]);
		}

		body.setOuterScope(scope);

		try {
			return body.evaluate();
		} catch (ReturnException re) {
			return re.data();
		} finally {
			scope.clear();
		}
	}

	public DataType evaluate(VariableCache globals, ListWrapper args) {
		return evaluate(globals, args.toArray(new DataType[args.length()]));
	}

	public DataType evaluate(DataType... args) {
		return evaluate(VariableCache.EMPTY_CACHE, args);
	}

	public Function compose(final Function other) {
		return new HiddenFunc(other.length()) {
			private static final long serialVersionUID = 0L;

			@Override
			public DataType evaluate(DataType... args) {
				DataType d = other.evaluate(args);

				if (d instanceof XiTuple) {
					return Function.this.evaluate(((XiTuple) d)
							.toArray(new DataType[d.length()]));
				}

				return Function.this.evaluate(d);
			}
		};
	}

	@Override
	public Object getJavaAnalog() {
		return this;
	}

	@Override
	public int length() {
		return identifiers == null ? 0 : identifiers.length;
	}

	@Override
	public boolean isEmpty() {
		return body.isEmpty();
	}

	@Override
	public int compareTo(DataType other) {
		return body.compareTo(other);
	}

}