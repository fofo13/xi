package xi;

import java.util.ArrayList;
import java.util.List;

import xi.datatypes.DataType;
import xi.datatypes.XiBlock;
import xi.datatypes.XiList;
import xi.datatypes.XiNull;
import xi.datatypes.XiNum;
import xi.datatypes.XiString;
import xi.datatypes.XiVar;
import xi.nodes.BlockNode;
import xi.nodes.ListNode;
import xi.nodes.Node;
import xi.nodes.NullNode;
import xi.nodes.NumNode;
import xi.nodes.OperationNode;
import xi.nodes.StringNode;
import xi.nodes.VarNode;

public class Parser {

	// TODO - this can be made better
	public static String[] splitOnSemiColons(String exp) {
		String[] arr = exp.split(";");
		List<String> split = new ArrayList<String>(arr.length);

		for (String s : arr)
			split.add(s);

		List<String> tokens = new ArrayList<String>();

		int a = 0, b = 0;
		for (int i = 0; i < split.size(); i++) {
			String line = split.get(i);
			a += line.replace("}", "").length()
					- line.replace("{", "").length();
			b += line.length() - line.replace("\"", "").length();
			if (a == 0 && b % 2 == 0) {
				String newLine = "";
				for (int j = 0; j < i + 1; j++)
					newLine += split.remove(0) + (i == j ? "" : ";");
				tokens.add(newLine.trim());
				i = -1;
			}
		}

		return tokens.toArray(new String[tokens.size()]);
	}

	public static String[] tokenize(String exp) {
		String[] arr = exp.split("\\s+");
		List<String> split = new ArrayList<String>(arr.length);

		for (String s : arr)
			split.add(s);

		List<String> tokens = new ArrayList<String>();

		while (!split.isEmpty()) {
			String token = split.remove(0);
			while (isIncomplete(token)) {
				token += " " + split.remove(0);
			}
			tokens.add(token.trim());
		}

		return tokens.toArray(new String[tokens.size()]);
	}

	// TODO - this is in bad shape now, will fix it later
	public static boolean containsAssignment(String exp) {
		String[] split = exp.split(":=(?![^\\{]*\\})");

		if (split.length == 1)
			return false;

		int a = 0, b = 0;
		for (int i = 0; i < split.length; i++) {
			String line = split[i];
			a += line.replace("}", "").length()
					- line.replace("{", "").length();
			b += line.length() - line.replace("\"", "").length();
			if (a == 0 && b % 2 == 0) {
				return true;
			}
			a = b = 0;
		}

		return false;
	}

	public static boolean isIncomplete(String exp) {
		String mod = exp.replaceAll("\"[^\"]+\"", "");
		return (exp.length() - exp.replace("\"", "").length()) % 2 == 1
				|| mod.replace("]", "").length()
						- mod.replace("[", "").length() != 0
				|| mod.replace("}", "").length()
						- mod.replace("{", "").length() != 0;
	}
	
	public static DataType parseDataType(String exp, VariableCache cache) {
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
	
	public static Node parseNode(String exp, VariableCache cache) {
		if (exp.equals("null"))
			return new NullNode();
		if (exp.matches("-?\\d+"))
			return new NumNode(Integer.parseInt(exp));
		if (exp.startsWith("["))
			return new ListNode(XiList.parse(exp, cache));
		if (exp.startsWith("{"))
			return new BlockNode(exp);
		if (exp.startsWith("\""))
			return new StringNode(exp);
		if (Operation.idExists(exp))
			return new OperationNode(Operation.parse(exp), cache);
		if (exp.matches("\\D.*+"))
			return new VarNode(exp);
		throw new RuntimeException("Cannot parse expression: " + exp);
	}

}