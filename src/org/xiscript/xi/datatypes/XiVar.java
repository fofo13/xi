package org.xiscript.xi.datatypes;

import java.util.Arrays;
import java.util.regex.Pattern;

import org.xiscript.xi.compilation.Type;

public class XiVar extends DataType {

	private static final long serialVersionUID = 0L;

	private static final Pattern DOT = Pattern.compile("\\.");

	public static final XiVar SPEC_VAR = new XiVar("_", true);
	public static final XiVar INDEX_VAR = new XiVar("__", true);

	private String[] components;
	private boolean temporary;
	private boolean persistent;

	private XiVar(String s, boolean temporary, boolean persistent) {
		components = DOT.split(s);
		this.temporary = temporary;
		this.persistent = persistent;
	}

	public XiVar(String id, boolean temporary) {
		this(id, temporary, false);
	}

	public XiVar(String id) {
		this(id, false);
	}

	public String id() {
		return components[0];
	}

	public String component(int i) {
		return components[i];
	}

	public boolean isTemporary() {
		return temporary;
	}

	public boolean isPersistent() {
		return persistent;
	}

	public void setTemporary(boolean val) {
		temporary = val;
	}

	public void setPersistent(boolean val) {
		persistent = val;
	}

	@Override
	public int length() {
		return components.length;
	}

	@Override
	public XiVar getJavaAnalog() {
		return this;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public int compareTo(DataType other) {
		return 0;
	}

	@Override
	public String toString() {
		if (components.length == 1)
			return id();

		StringBuilder sb = new StringBuilder((components[0].length() + 1)
				* components.length);
		for (int i = 0; i < components.length; i++) {
			if (i > 0)
				sb.append('.');
			sb.append(components[i]);
		}

		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof XiVar)
				&& Arrays.equals(components, (((XiVar) o).components));
	}

	@Override
	public int hashCode() {
		return components[0].hashCode();
	}

	@Override
	public XiType type() {
		return XiType.valueOf(Type.VAR);
	}

}