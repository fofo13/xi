package org.xiscript.xi.datatypes.io;

import java.io.Closeable;
import java.io.PrintStream;

import org.xiscript.xi.datatypes.XiType;
/*this is a xi class for output*/
public class XiWriter extends XiStream implements Closeable {

	private static final long serialVersionUID = 0L;

	private PrintStream out;

	public XiWriter(PrintStream out) {
		this.out = out;
	}

	@Override
	public void close() {
		out.close();
	}

	@Override
	public boolean isEmpty() {
		return out.checkError();
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

	@Override
	public XiType type() {
		return XiType.valueOf(XiType.Type.WRITER);
	}

}