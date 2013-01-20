package org.xiscript.xi.datatypes;

import java.util.Scanner;

import org.xiscript.xi.datatypes.io.XiReader;
import org.xiscript.xi.datatypes.io.XiWriter;

public class XiSys extends DataType {

	private static final XiAttribute stdout = new XiAttribute("stdout", true);
	private static final XiAttribute stdin = new XiAttribute("stdin", true);
	private static final XiSys instance = new XiSys();

	private XiSys() {
		attributes.put(stdout, new XiWriter(System.out));
		attributes.put(stdin, new XiReader(new Scanner(System.in)));
	}

	public static XiSys instance() {
		return instance;
	}

	public XiWriter stdout() {
		return (XiWriter) attributes.get(stdout);
	}

	public XiReader stdin() {
		return (XiReader) attributes.get(stdin);
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

}