package org.xiscript.xi.datatypes.functional;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.collections.XiTuple;

public class XiLambda extends Function {

	public XiLambda(XiTuple list, XiBlock body) {
		super(list, body);
	}

	public DataType evaluate(XiTuple args, VariableCache globals) {
		return evaluate(args.collection().toArray(new DataType[args.length()]),
				globals);
	}

}