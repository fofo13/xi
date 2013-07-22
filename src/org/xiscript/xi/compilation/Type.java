package org.xiscript.xi.compilation;

public enum Type {

	TYPE(), MODULE(), NULL(), SYS(), ATTRIBUTE(), VAR(),

	INT(), LONG(), FLOAT(), COMPLEX(), REAL(INT, LONG, FLOAT), NUM(REAL,
			COMPLEX),

	REGEX(), MATCHER(), TUPLE(), SET(), STR(REGEX), LIST(STR, TUPLE), COLLECTION(
			LIST, SET),

	FILE(), GENERATOR(), ITER(FILE, COLLECTION, GENERATOR),

	DICT(),

	READER(), WRITER(), STREAM(READER, WRITER),

	FUNCTION(), BLOCK(), LAMBDA();

	private Type[] subtypes;

	private Type(Type... subtypes) {
		this.subtypes = subtypes;
	}

	public boolean isParentOf(Type type) {
		for (Type t : subtypes)
			if (t == type || t.isParentOf(type))
				return true;

		return false;
	}

	public org.objectweb.asm.Type getJavaAnalog() {
		switch (this) {
		case INT:
			return org.objectweb.asm.Type.INT_TYPE;
		default:
			return null;
		}
	}

}