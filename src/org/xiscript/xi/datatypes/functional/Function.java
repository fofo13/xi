package org.xiscript.xi.datatypes.functional;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiAttribute;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.datatypes.collections.XiTuple;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;
import org.xiscript.xi.exceptions.ReturnException;

public abstract class Function extends DataType {

	protected String[] identifiers;
	protected XiBlock body;

	public Function(String[] identifiers, XiBlock body) {
		this.identifiers = identifiers;
		this.body = body;
	}

	public Function(XiTuple list, XiBlock body) {
		this(new String[list == null ? 0 : list.length()], body);

		for (int i = 0; i < identifiers.length; i++) {
			if (!(list.get(i) instanceof XiAttribute))
				ErrorHandler.invokeError(ErrorType.INVALID_ATTRIBUTE_TUPLE,
						list);

			identifiers[i] = ((XiAttribute) list.get(i)).toString();
		}
	}

	public DataType evaluate(DataType[] args, VariableCache globals) {
		VariableCache scope = new VariableCache(globals); // TODO: This can
															// create extremely
															// long cache chains
															// with recursive
															// functions, and
															// can result in a
															// StackOverflowError

		for (int i = 0; i < args.length; i++) {
			scope.put(new XiVar(identifiers[i], false, true), args[i]);
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

	public DataType evaluate(VariableCache globals, DataType... args) {
		return evaluate(args, globals);
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