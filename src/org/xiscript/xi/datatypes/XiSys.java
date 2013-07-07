package org.xiscript.xi.datatypes;

import java.util.Scanner;

import org.xiscript.xi.datatypes.io.XiReader;
import org.xiscript.xi.datatypes.io.XiWriter;

public class XiSys extends DataType {

	private static final long serialVersionUID = 0L;

	private static final XiAttribute STDOUT = XiAttribute.valueOf("stdout");
	private static final XiAttribute STDIN = XiAttribute.valueOf("stdin");
	private static final XiAttribute STDERR = XiAttribute.valueOf("stderr");

	public static XiWriter stdout = new XiWriter(System.out);
	public static XiReader stdin = new XiReader(new Scanner(System.in));
	public static XiWriter stderr = new XiWriter(System.err);

	private static final XiSys instance = new XiSys();

	private XiSys() {
	}

	public static XiSys instance() {
		return instance;
	}

	public XiWriter stdout() {
		return stdout;
	}

	public XiReader stdin() {
		return stdin;
	}

	@Override
	public DataType getAttribute(XiAttribute a) {
		if (a.equals(STDOUT))
			return stdout;
		if (a.equals(STDIN))
			return stdin;
		if (a.equals(STDERR))
			return stderr;

		return super.getAttribute(a);
	}

	@Override
	public void setAttribute(XiAttribute a, DataType value) {
		if (a.equals(STDOUT))
			stdout = (XiWriter) value;
		else if (a.equals(STDIN))
			stdin = (XiReader) value;
		else if (a.equals(STDERR))
			stderr = (XiWriter) value;
		else
			super.setAttribute(a, value);
	}

	@Override
	public Runtime getJavaAnalog() {
		return Runtime.getRuntime();
	}

	@Override
	public int compareTo(DataType other) {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public XiType type() {
		return XiType.valueOf(XiType.Type.SYS);
	}

}