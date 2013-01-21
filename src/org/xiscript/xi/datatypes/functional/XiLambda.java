package org.xiscript.xi.datatypes.functional;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiAttribute;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.datatypes.collections.XiTuple;
import org.xiscript.xi.exceptions.ReturnException;

public class XiLambda extends DataType {

	private String[] identifiers;
	private XiBlock body;

	public XiLambda(XiTuple list, XiBlock body) {
		if (list == null)
			return;

		identifiers = new String[list.length()];
		for (int i = 0; i < identifiers.length; i++) {
			if (!(list.get(i) instanceof XiAttribute))
				throw new RuntimeException(
						"Argument-tuple must consist of only attribute identifiers.");
			identifiers[i] = ((XiAttribute) list.get(i)).toString();
		}
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

		try {
			return body.evaluate();
		} catch (ReturnException re) {
			return re.data();
		}
	}

	@Override
	public Object getJavaAnalog() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int length() {
		return identifiers.length;
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