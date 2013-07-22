package org.xiscript.xi.datatypes;

import java.util.HashMap;
import java.util.Map;

import org.xiscript.xi.compilation.Type;

public class XiType extends DataType {

	private static final long serialVersionUID = 0L;

	private static final Map<Type, XiType> types = new HashMap<Type, XiType>(
			Type.values().length);

	static {
		for (Type type : Type.values())
			types.put(type, new XiType(type));
	}

	public final Type type;

	private XiType(Type type) {
		this.type = type;
	}

	public static XiType valueOf(Type type) {
		return types.get(type);
	}

	public static XiType valueOf(XiAttribute attribute) {
		return valueOf(Type.valueOf(attribute.toString()));
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public Class<?> getJavaAnalog() {
		return getClass();
	}

	@Override
	public int compareTo(DataType data) {
		if (!(data instanceof XiType))
			return 0;

		Type t = ((XiType) data).type;

		if (type.isParentOf(t))
			return 1;
		if (t.isParentOf(type))
			return -1;

		return 0;
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof XiType) && type == ((XiType) o).type;
	}

	@Override
	public int hashCode() {
		return type.hashCode();
	}

	@Override
	public String toString() {
		return type.toString();
	}

	@Override
	public XiType type() {
		return valueOf(Type.TYPE);
	}

}