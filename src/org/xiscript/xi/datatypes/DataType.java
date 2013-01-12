package org.xiscript.xi.datatypes;

public abstract class DataType implements Comparable<DataType> {

	public abstract boolean isEmpty();

	public int length() {
		return toString().length();
	}

	public String type() {
		String[] split = getClass().toString().split("\\.");
		// type names should be of the form "Xi.*"
		return split[split.length - 1].substring(2).toLowerCase();
	}

	public abstract Object getJavaAnalog();

}