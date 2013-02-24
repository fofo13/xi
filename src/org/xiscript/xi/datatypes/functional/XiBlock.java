package org.xiscript.xi.datatypes.functional;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.core.XiEnvironment;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiNull;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.exceptions.ControlFlowException;

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
		for (XiVar var : cache)
			locals.add(var);
	}

	public VariableCache locals() {
		return locals;
	}

	@Override
	public Object getJavaAnalog() {
		throw new UnsupportedOperationException();
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