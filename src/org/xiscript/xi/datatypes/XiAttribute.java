package org.xiscript.xi.datatypes;

public class XiAttribute extends DataType {

	private String val;
	private boolean setable;

	public XiAttribute(String val, boolean setable) {
		this.val = val.replace("'", "");
		if (!this.val.matches("\\w*"))
			throw new RuntimeException("Invalid attribute name: " + this.val);

		this.setable = setable;
	}

	public XiAttribute(String val) {
		this(val, false);
	}
	
	public boolean setable() {
		return setable;
	}

	@Override
	public int compareTo(DataType other) {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return val.isEmpty();
	}

	@Override
	public String getJavaAnalog() {
		return val;
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof XiAttribute) && ((XiAttribute) o).val.equals(val);
	}

	@Override
	public int hashCode() {
		return val.hashCode();
	}

	@Override
	public String toString() {
		return val;
	}

}