package org.xiscript.xi.datatypes.functional;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.datatypes.collections.XiString;
import org.xiscript.xi.datatypes.collections.XiTuple;


public class XiLambda extends DataType {

	private String[] identifiers;
	private XiBlock body;

	public XiLambda(XiTuple list, XiBlock body) {
		identifiers = new String[list.length()];
		for (int i = 0; i < identifiers.length; i++)
			identifiers[i] = ((XiString) list.get(i)).toString();
		this.body = body;
	}

	public DataType evaluate(XiTuple args, VariableCache globals) {
		if (identifiers.length != args.length())
			throw new RuntimeException(
					"Argument tuple length mismatch for lambda function: "
							+ this);

		body.addVars(globals);
		for (int i = 0; i < args.length(); i++)
			body.updateLocal(new XiVar(identifiers[i], args.get(i)));
		return body.evaluate();
	}

	@Override
	public boolean isEmpty() {
		return body.isEmpty();
	}

	@Override
	public int compareTo(DataType other) {
		return body.compareTo(other);
	}

	@Override
	public String toString() {
		return body.toString();
	}

}