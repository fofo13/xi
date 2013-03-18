package org.xiscript.xi.datatypes;

public class XiVar extends DataType {

	private String id;
	private DataType data;
	private boolean temporary;

	public XiVar(String id, DataType val, boolean temporary) {
		this.id = id;
		this.data = val;
		this.temporary = temporary;
	}

	public XiVar(String id, DataType val) {
		this(id, val, false);
	}

	public String id() {
		return id;
	}

	public DataType val() {
		return data;
	}

	public boolean temporary() {
		return temporary;
	}

	public void set(DataType data) {
		this.data = data;
	}

	@Override
	public String getJavaAnalog() {
		return id;
	}

	@Override
	public boolean isEmpty() {
		return data.isEmpty();
	}

	@Override
	public int compareTo(DataType other) {
		return 0;
	}

	@Override
	public void delete() {
		data.delete();
	}

	@Override
	public String toString() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		XiVar v = (XiVar) o;
		return id.equals(v.id());
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