package org.xiscript.xi.datatypes.io;

import java.io.PrintStream;

import org.xiscript.xi.datatypes.DataType;

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
		out.println(o);
	}

	public void printf(String format, Object... args) {
		System.out.printf(format, args);
	}

}