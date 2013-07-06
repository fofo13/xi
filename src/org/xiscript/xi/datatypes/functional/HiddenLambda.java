package org.xiscript.xi.datatypes.functional;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.collections.ArgumentList;
import org.xiscript.xi.datatypes.collections.XiTuple;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;

public abstract class HiddenLambda extends XiLambda {

	private int numArgs;

	public HiddenLambda(int numArgs) {
		super(ArgumentList.EMPTY, null);
		this.numArgs = numArgs;
	}

	public abstract DataType evaluate(DataType... dataTypes);

	@Override
	public DataType evaluate(XiTuple args, VariableCache globals) {
		return evaluate(args.collection().toArray(new DataType[args.length()]));
	}

	@Override
	public DataType evaluate(DataType[] args, VariableCache globals) {
		return evaluate(args);
	}

	@Override
	public int length() {
		return numArgs;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public int compareTo(DataType other) {
		ErrorHandler.invokeError(ErrorType.UNCOMPARABLE, type());
		return 0;
	}

}