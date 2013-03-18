package org.xiscript.xi.datatypes.io;

import java.io.Closeable;
import java.util.Scanner;

import org.xiscript.xi.datatypes.XiType;
import org.xiscript.xi.datatypes.collections.XiString;

public class XiReader extends XiStream implements Closeable {

	private Scanner in;

	public XiReader(Scanner in) {
		this.in = in;
	}

	public boolean hasNext() {
		return in.hasNext();
	}

	public boolean hasNextLine() {
		return in.hasNextLine();
	}

	@Override
	public void close() {
		in.close();
	}

	@Override
	public Scanner getJavaAnalog() {
		return in;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public String toString() {
		return in.toString();
	}

	public XiString read() {
		return new XiString(in.next());
	}

	public XiString readln() {
		return new XiString(in.nextLine());
	}

	@Override
	public XiType type() {
		return XiType.valueOf(XiType.Type.READER);
	}

}