package org.xiscript.xi.exceptions;

public abstract class LoopControlException extends ControlFlowException {

	private static final long serialVersionUID = 0L;

	protected int n;

	public LoopControlException(int n) {
		this.n = n;
	}

	public int n() {
		return n;
	}

	public void decrement() {
		n--;
	}

}