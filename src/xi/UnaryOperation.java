package xi;
public enum UnaryOperation implements Operation {
	
	NOT("!"), BITNOT("~"), ABS("\\");
	
	private final String id;
	
	private static final String[] ALL_IDS = new String[values().length];
	static {
		for (int i = 0 ; i < ALL_IDS.length ; i++)
			ALL_IDS[i] = values()[i].id();
	}
	
	private UnaryOperation(String id) {
		this.id = id;
	}
	
	@Override
	public String id() {
		return id;
	}
	
	public static UnaryOperation parse(String id) {
		for (UnaryOperation op : values())
			if (op.id().equals(id))
				return op;
		throw new IllegalArgumentException();
	}
	
	public int perform(int a) {
		switch (ordinal()) {
		case 0:
			return a == 0 ? 1 : 0;
		case 1:
			return ~a;
		case 2:
			return a < 0 ? -a : a;
		}
		throw new RuntimeException("Something went really wrong.");
	}
	
}