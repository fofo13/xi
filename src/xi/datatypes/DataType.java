package xi.datatypes;

import xi.VariableCache;

public abstract class DataType {
	
	public abstract boolean isEmpty();
	
	public static DataType create(String exp, VariableCache cache) {
		if (exp.equals("null"))
			return new XiNull();
		if (exp.matches("-?\\d+"))
			return new XiNum(Integer.parseInt(exp));
		if (exp.startsWith("["))
			return XiList.parse(exp, cache);
		if (exp.startsWith("{")) {
			XiBlock block = new XiBlock(exp);
			block.addVars(cache);
			return block.evaluate();
		}
		if (exp.startsWith("\""))
			return new XiString(exp);
		if (exp.matches("\\D.*+"))
			return cache.containsId(exp) ? cache.get(exp) : new XiVar(exp, null);
		throw new RuntimeException("Could not parse: " + exp);
	}
	
}