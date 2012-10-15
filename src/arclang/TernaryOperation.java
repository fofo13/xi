package arclang;

public enum TernaryOperation implements Operation {
	
	TERN("?");
	
	private final String id;
	
	private static final String[] ALL_IDS = new String[values().length];
	static {
		for (int i = 0 ; i < ALL_IDS.length ; i++)
			ALL_IDS[i] = values()[i].id();
	}
	
	private TernaryOperation(String id) {
		this.id = id;
	}
	
	@Override
	public String id() {
		return id;
	}
	
	public static TernaryOperation parse(String id) {
		for (TernaryOperation op : values())
			if (op.id().equals(id))
				return op;
		throw new IllegalArgumentException();
	}
	
	public int perform(int a, int b, int c) {
		switch (ordinal()) {
		case 0:
			return a != 0 ? b : c;
		}
		throw new RuntimeException("Something went really wrong.");
	}
	
}