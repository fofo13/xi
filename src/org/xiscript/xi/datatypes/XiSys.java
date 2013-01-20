package org.xiscript.xi.datatypes;

import org.xiscript.xi.datatypes.io.XiWriter;

public class XiSys extends DataType {

	private static final XiAttribute stdout = new XiAttribute("stdout", true);
	private static final XiSys instance = new XiSys();

	private XiSys() {
		attributes.put(stdout, new XiWriter(System.out));
	}
	
	public static XiSys instance() {
		return instance;
	}

	public XiWriter stdout() {
		return (XiWriter) attributes.get(stdout);
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
	public String toString() {
		return attributes.toString();
	}

}