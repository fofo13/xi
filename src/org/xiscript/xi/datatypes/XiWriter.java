package org.xiscript.xi.datatypes;

import java.io.PrintStream;

public class XiWriter extends DataType {

	private PrintStream out;

	public XiWriter(PrintStream out) {
		this.out = out;
	}

	@Override
	public boolean isEmpty() {
		return out.checkError();
	}

	@Override
	public int compareTo(DataType other) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public PrintStream getJavaAnalog() {
		return out;
	}
	
	public void print(Object o) {
		out.print(o);
	}
	
	public void println(Object o) {
		out.print(o);
	}

}