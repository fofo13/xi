package org.xiscript.xi.datatypes.functional;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.datatypes.collections.XiString;
import org.xiscript.xi.datatypes.collections.XiTuple;
import org.xiscript.xi.exceptions.ReturnException;
import org.xiscript.xi.operations.Operation;


public class XiFunc extends DataType implements Operation {

	private String[] identifiers;
	private XiBlock body;

	public XiFunc(XiTuple list, XiBlock body) {
		identifiers = new String[list.length()];
		for (int i = 0; i < identifiers.length; i++)
			identifiers[i] = ((XiString) list.get(i)).toString();
		this.body = body;
	}

	public XiBlock body() {
		return body;
	}

	@Override
	public Object getJavaAnalog() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int numArgs() {
		return identifiers.length;
	}

	@Override
	public DataType evaluate(DataType[] args, VariableCache globals) {
		body.addVars(globals);
		for (int i = 0; i < args.length; i++)
			body.updateLocal(new XiVar(identifiers[i], args[i]));
		try {
			return body.evaluate();
		} catch (ReturnException re) {
			return re.data();
		}
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