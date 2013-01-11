package org.xiscript.xi.datatypes;


public class XiVar extends DataType {
	
	private String id;
	private DataType data;
	
	public XiVar(String id, DataType val) {
		this.id = id;
		this.data = val;
	}
	
	public String id() {
		return id;
	}
	
	public DataType val() {
		return data;
	}
	
	public void set(DataType data) {
		this.data = data;
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
	public String toString() {
		return id;
	}
	
	@Override
	public boolean equals(Object o) {
		XiVar v = (XiVar)o;
		return id.equals(v.id());
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
}