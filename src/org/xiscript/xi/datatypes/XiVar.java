package org.xiscript.xi.datatypes;

public class XiVar extends DataType {

	private static final long serialVersionUID = 0L;

	public static final XiVar SPEC_VAR = new XiVar("_", true);
	public static final XiVar INDEX_VAR = new XiVar("__", true);

	private String id;
	private boolean temporary;
	private boolean persistent;

	private XiVar(String id, boolean temporary, boolean persistent) {
		this.id = id;
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
		return id;
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
	public String getJavaAnalog() {
		return id;
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
	public void delete() {

	}

	@Override
	public String toString() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof XiVar) && id.equals(((XiVar) o).id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public XiType type() {
		return XiType.valueOf(XiType.Type.VAR);
	}

}