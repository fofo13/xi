package org.xiscript.xi.nodes;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiDict;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.datatypes.collections.ListWrapper;
import org.xiscript.xi.datatypes.functional.Function;
import org.xiscript.xi.datatypes.numeric.XiInt;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;

public class FunctionNode extends OperationNode {

	private static final long serialVersionUID = 0L;

	private XiVar id;

	public FunctionNode(XiVar id) {
		super(null);
		this.id = id;
	}

	@Override
	public int numChildren() {
		return -1;
	}

	@Override
	public DataType evaluate(VariableCache cache) {
		DataType d = cache.get(id);
		DataType[] args = processChildren(cache);

		if (d instanceof Function)
			return ((Function) d).evaluate(cache, args);

		if (d instanceof ListWrapper) {
			ListWrapper list = (ListWrapper) d;

			switch (args.length) {
			case 1:
				return list.get(((XiInt) args[0]).val());
			case 2:
				return list.get(((XiInt) args[0]).val(),
						((XiInt) args[1]).val());
			case 3:
				return list.get(((XiInt) args[0]).val(),
						((XiInt) args[1]).val(), ((XiInt) args[2]).val());
			}

			ErrorHandler.invokeError(ErrorType.ARGUMENT, id);
		}

		if (d instanceof XiDict)
			return ((XiDict) d).get(args[0]);

		ErrorHandler.invokeError(ErrorType.NOT_CALLABLE, id, d.type());
		return null;
	}
}