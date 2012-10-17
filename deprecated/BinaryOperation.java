package tmp;

public enum BinaryOperation {
	ADD("+"), SUBTRACT("-"), MULTIPLY("*"), DIVIDE("/"), MODULUS("%"),
	EQ("="), NEQ("!="), GREATER(">"), LESS("<"), GREATER_EQ(">="),
	LESS_EQ("<="), AND("&"), OR("|"), XOR("^"), RSHIFT(">>"), 
	LSHIFT("<<"), POW("**");

	private final String id;
	
	private static final String[] ALL_IDS = new String[values().length];
	static {
		for (int i = 0 ; i < ALL_IDS.length ; i++)
			ALL_IDS[i] = values()[i].id();
	}
	
	private BinaryOperation(String id) {
		this.id = id;
	}
	
	public String id() {
		return id;
	}
	
	public static BinaryOperation parse(String id) {
		for (BinaryOperation op : values())
			if (op.id().equals(id))
				return op;
		throw new IllegalArgumentException();
	}

	public static BinaryOperation parseBinaryOperation(char op) {
		return parse("" + op);
	}

	public int perform(int a, int b) {
		switch (ordinal()) {
		case 0:
			return a + b;
		case 1:
			return a - b;
		case 2:
			return a * b;
		case 3:
			return a / b;
		case 4:
			return a % b;
		case 5:
			return a == b ? 1 : 0;
		case 6:
			return a != b ? 1 : 0;
		case 7:
			return a > b ? 1 : 0;
		case 8:
			return a < b ? 1 : 0;
		case 9:
			return a >= b ? 1 : 0;
		case 10:
			return a <= b ? 1 : 0;
		case 11:
			return a & b;
		case 12:
			return a | b;
		case 13:
			return a ^ b;
		case 14:
			return a >> b;
		case 15:
			return a << b;
		case 16:
			return (int) Math.pow(a, b);
		default:
			throw new RuntimeException("Something went really wrong.");
		}
	}

	@Override
	public String toString() {
		return ALL_IDS[ordinal()];
	}
}