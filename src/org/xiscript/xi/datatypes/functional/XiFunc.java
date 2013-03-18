package org.xiscript.xi.datatypes.functional;

import org.xiscript.xi.datatypes.XiType;
import org.xiscript.xi.datatypes.collections.XiTuple;
import org.xiscript.xi.operations.Operation;

public class XiFunc extends Function implements Operation {

	public XiFunc(XiTuple list, XiBlock body) {
		super(list, body);
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
		return XiType.valueOf(XiType.Type.LAMBDA);
	}

}