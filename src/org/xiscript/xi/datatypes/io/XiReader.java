package org.xiscript.xi.datatypes.io;

import java.util.Scanner;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.collections.XiString;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;

public class XiReader extends DataType {

	private Scanner in;

	public XiReader(Scanner in) {
		this.in = in;
	}

	@Override
	public Scanner getJavaAnalog() {
		return in;
	}

	@Override
	public int compareTo(DataType other) {
		ErrorHandler.invokeError(ErrorType.UNCOMPARABLE, type());
		return 0;
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

}