package arclang;

public enum Keywords {
	PRINT;
	
	public void execute(String[] args) {
		switch (ordinal()) {
		case 0:
			for (String s : args) System.out.print(s);  // SysOut(execute(s))
			System.out.println();
			break;
		}
	}
}