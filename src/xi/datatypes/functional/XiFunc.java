package xi.datatypes.functional;

import xi.core.VariableCache;
import xi.datatypes.DataType;
import xi.datatypes.XiVar;
import xi.datatypes.collections.XiTuple;
import xi.datatypes.collections.XiString;
import xi.operations.Operation;

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
	public int numArgs() {
		return identifiers.length;
	}

	@Override
	public DataType evaluate(DataType[] args, VariableCache globals) {
		body.addVars(globals);
		for (int i = 0; i < args.length; i++)
			body.updateLocal(new XiVar(identifiers[i], args[i]));
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

}