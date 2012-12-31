package xi.datatypes;

import xi.core.VariableCache;
import xi.datatypes.collections.XiString;
import xi.datatypes.collections.XiTuple;

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