package org.xiscript.xi.datatypes.functional;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiType;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.datatypes.collections.ArgumentList;
import org.xiscript.xi.datatypes.collections.XiTuple;

public class XiLambda extends Function {

	public XiLambda(ArgumentList list, XiBlock body) {
		super(list.getJavaAnalog(), body);
	}

	public XiLambda(XiVar[] identifiers, XiBlock body) {
		super(identifiers, body);
	}

	public DataType evaluate(XiTuple args, VariableCache globals) {
		return evaluate(args.toArray(new DataType[args.length()]), globals);
	}

	@Override
	public XiType type() {
		return XiType.valueOf(XiType.Type.LAMBDA);
	}

}