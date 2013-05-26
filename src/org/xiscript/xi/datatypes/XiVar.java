package org.xiscript.xi.datatypes;

public class XiVar extends DataType {

	public static final XiVar D = new XiVar(".", true);
	public static final XiVar U = new XiVar("_", true);

	private String id;
	private boolean temporary;
	private boolean persistent;

	public XiVar(String id, boolean temporary, boolean persistent) {
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

	public boolean temporary() {
		return temporary;
	}

	public boolean persistent() {
		return persistent;
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