package org.xiscript.xi.datatypes.functional;

import org.xiscript.xi.compilation.Type;
import org.xiscript.xi.datatypes.XiType;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.datatypes.collections.ArgumentList;

public class XiLambda extends Function {

	private static final long serialVersionUID = 0L;

	public XiLambda(ArgumentList list, XiBlock body) {
		super(list.getJavaAnalog(), body);
	}

	public XiLambda(XiVar[] identifiers, XiBlock body) {
		super(identifiers, body);
	}

	@Override
	public XiType type() {
		return XiType.valueOf(Type.LAMBDA);
	}

}