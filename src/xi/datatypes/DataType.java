package xi.datatypes;


public abstract class DataType {
	
	public abstract boolean isEmpty();
	
	public static DataType create(String exp) {
		if (exp.matches("-?\\d+"))
			return new XiNum(Integer.parseInt(exp));
		if (exp.startsWith("["))
			return XiList.parse(exp);
		if (exp.startsWith("{"))
			return new XiBlock(exp);
		if (exp.startsWith("\""))
			return new XiString(exp);
		if (exp.matches("[a-zA-Z]\\w+"))
			return new XiVar(exp, null);
		throw new RuntimeException();
	}
	
}