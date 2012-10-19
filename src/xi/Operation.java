package xi;

public enum Operation {

	NOT("!", 1), BITNOT("~", 1), ABS("\\", 1), ADD("+", 2), SUBTRACT("-", 2), MULTIPLY(
			"*", 2), DIVIDE("/", 2), MODULUS("%", 2), EQ("=", 2), NEQ("!=", 2), GREATER(
			">", 2), LESS("<", 2), GREATER_EQ(">=", 2), LESS_EQ("<=", 2), AND(
			"&", 2), OR("|", 2), XOR("^", 2), RSHIFT(">>", 2), LSHIFT("<<", 2), POW(
			"**", 2), TERN("?", 3),

	MAP("@", 2), RANGE(",", 1), SUM("$", 1),
	
	PRINT("print", 1), PRINTLN("println", 1);
	
	private String id;
	private int numArgs;

	private Operation(String id, int numArgs) {
		this.id = id;
		this.numArgs = numArgs;
	}

	public String id() {
		return id;
	}

	public int numArgs() {
		return numArgs;
	}

	public DataType evaluate(DataType[] args) {
		switch (this) {
		case NOT:
			return new XiNum(args[0].isEmpty() ? 0 : 1);
		case BITNOT:
			return new XiNum(~((XiNum) args[0]).val());
		case ABS:
			return new XiNum(Math.abs(((XiNum) args[0]).val()));
		case ADD:
			if (args[0] instanceof XiString || args[1] instanceof XiString)
				return new XiString(args[0].toString() + args[1].toString());
			return ((XiNum) args[0]).add((XiNum) args[1]);
		case SUBTRACT:
			return ((XiNum) args[0]).sub((XiNum) args[1]);
		case MULTIPLY:
			return ((XiNum) args[0]).mul((XiNum) args[1]);
		case DIVIDE:
			return ((XiNum) args[0]).div((XiNum) args[1]);
		case MODULUS:
			return ((XiNum) args[0]).mod((XiNum) args[1]);
		case EQ:
			return new XiNum(args[0].equals(args[1]) ? 1 : 0);
		case NEQ:
			return new XiNum(args[0].equals(args[1]) ? 0 : 1);
		case GREATER:
			return new XiNum(
					((XiNum) args[0]).val() > ((XiNum) args[1]).val() ? 1 : 0);
		case LESS:
			return new XiNum(
					((XiNum) args[0]).val() < ((XiNum) args[1]).val() ? 1 : 0);
		case GREATER_EQ:
			return new XiNum(
					((XiNum) args[0]).val() >= ((XiNum) args[1]).val() ? 1 : 0);
		case LESS_EQ:
			return new XiNum(
					((XiNum) args[0]).val() <= ((XiNum) args[1]).val() ? 1 : 0);
		case AND:
			return new XiNum(((XiNum) args[0]).val() & ((XiNum) args[1]).val());
		case OR:
			return new XiNum(((XiNum) args[0]).val() | ((XiNum) args[1]).val());
		case XOR:
			return new XiNum(((XiNum) args[0]).val() ^ ((XiNum) args[1]).val());
		case RSHIFT:
			return new XiNum(((XiNum) args[0]).val() >> ((XiNum) args[1]).val());
		case LSHIFT:
			return new XiNum(((XiNum) args[0]).val() << ((XiNum) args[1]).val());
		case POW:
			return ((XiNum) args[0]).pow((XiNum) args[1]);
		case TERN:
			return new XiNum(args[0].isEmpty() ? 0 : 1);
		case MAP:
			return ((XiList)args[0]).map((XiBlock)args[1]);
		case RANGE:
			return new XiList(((XiNum)args[0]).val());
		case SUM:
			return ((XiList)args[0]).sum();
		case PRINT:
			System.out.print(args[0]);
			return args[0];
		case PRINTLN:
			System.out.println(args[0]);
			return args[0];
		default:
			throw new RuntimeException();
		}
	}
	
	public static boolean idExists(String id) {
		for (Operation op : values())
			if (id.equals(op.id()))
				return true;
		return false;
	}
	
	public static Operation parse(String id) {
		for (Operation op : values())
			if (id.equals(op.id()))
				return op;
		throw new IllegalArgumentException("Invalid identifier");
	}

}