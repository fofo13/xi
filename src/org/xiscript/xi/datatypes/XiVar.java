package org.xiscript.xi.datatypes;

public class XiVar extends DataType {

	private String id;
	private boolean temporary;

	public XiVar(String id, boolean temporary) {
		this.id = id;
		this.temporary = temporary;
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