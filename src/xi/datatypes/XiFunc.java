package xi.datatypes;

import xi.core.Operation;
import xi.core.VariableCache;

public class XiFunc extends DataType implements Operation {
	
	private XiBlock body;
	private int numArgs;
	
	public XiFunc(XiBlock body, int numArgs) {
		this.body = body;
		this.numArgs = numArgs;
	}
	
	public XiBlock body() {
		return body;
	}
	
	@Override
	public int numArgs() {
		return numArgs;
	}
	
	@Override
	public DataType evaluate(DataType[] args, VariableCache globals) {
		body.addVars(globals);
		for (int i = 0 ; i < args.length ; i++)
			body.updateLocal(new XiVar("$" + (i + 1), args[i]));
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
		return "func " + body + " (" + numArgs + " args)";
	}
	
}