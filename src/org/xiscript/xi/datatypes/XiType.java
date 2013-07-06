package org.xiscript.xi.datatypes;

import java.util.HashMap;
import java.util.Map;

public class XiType extends DataType {

	public static enum Type {

		TYPE(), MODULE(), NULL(), SYS(), ATTRIBUTE(), VAR(),

		INT(), LONG(), FLOAT(), COMPLEX(), REAL(INT, LONG, FLOAT), NUM(REAL,
				COMPLEX),

		REGEX(), TUPLE(), SET(), STRING(REGEX), LIST(STRING, TUPLE), COLLECTION(
				LIST, SET),

		FILE(), GENERATOR(), ITER(FILE, COLLECTION, GENERATOR),

		DICT(),

		READER(), WRITER(), STREAM(READER, WRITER),

		FUNCTION(), BLOCK(), LAMBDA();

		private Type[] subtypes;

		private Type(Type... subtypes) {
			this.subtypes = subtypes;
		}

		public boolean containsSubtype(Type type) {
			for (Type t : subtypes)
				if (t == type || t.containsSubtype(type))
					return true;

			return false;
		}
	}

	private static final Map<Type, XiType> types = new HashMap<Type, XiType>(
			Type.values().length);

	static {
		for (Type type : Type.values())
			types.put(type, new XiType(type));
	}

	private final Type type;

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

		if (type.containsSubtype(t))
			return 1;
		if (t.containsSubtype(type))
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