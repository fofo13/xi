package xi.datatypes.functional;

import xi.core.VariableCache;
import xi.core.XiEnvironment;
import xi.datatypes.DataType;
import xi.datatypes.XiNull;
import xi.datatypes.XiVar;
import xi.exceptions.ControlFlowException;

public class XiBlock extends DataType {
	
	private String exp;
	private VariableCache locals;
	
	public XiBlock(String exp, VariableCache locals) {
		this.exp = exp.substring(1, exp.length() - 1);
		this.locals = locals;
	}
	
	public XiBlock(String exp) {
		this(exp, new VariableCache());
	}
	
	public String exp() {
		return exp;
	}
	
	public void updateLocal(XiVar v) {
		locals.add(v);
	}
	
	public void addVars(VariableCache cache) {
		locals.addAll(cache);
	}
	
	public VariableCache locals() {
		return locals;
	}
	
	@Override
	public int compareTo(DataType other) {
		if (other instanceof XiBlock)
			return 0;
		if (other instanceof XiNull)
			return 1;
		return -1;
	}
	
	@Override
	public boolean isEmpty() {
		return exp.isEmpty();
	}
	
	@Override
	public String toString() {
		return "{" + exp + "}";
	}
	
	public DataType evaluate() throws ControlFlowException {
		XiEnvironment env = null;
		try {
			env = new XiEnvironment(locals);
			env.put(exp);
			env.run();
			return env.last();
		} finally {
			env.close();
		}
	}
	
}