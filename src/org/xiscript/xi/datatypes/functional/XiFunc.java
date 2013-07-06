package org.xiscript.xi.datatypes.functional;

import org.xiscript.xi.datatypes.XiType;
import org.xiscript.xi.datatypes.collections.ArgumentList;
import org.xiscript.xi.operations.Operation;

public class XiFunc extends Function implements Operation {

	public XiFunc(ArgumentList list, XiBlock body) {
		super(list.getJavaAnalog(), body);
	}

	@Override
	public int numArgs() {
		return length();
	}

	public XiLambda asLambda() {
		return new XiLambda(identifiers, body);
	}

	@Override
	public XiType type() {
		return XiType.valueOf(XiType.Type.FUNCTION);
	}

}