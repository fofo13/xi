package xi.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import xi.datatypes.XiBlock;
import xi.datatypes.XiNull;
import xi.datatypes.XiFunc;
import xi.datatypes.collections.XiList;
import xi.datatypes.collections.XiString;
import xi.datatypes.numeric.XiFloat;
import xi.datatypes.numeric.XiInt;
import xi.nodes.DataNode;
import xi.nodes.Node;
import xi.nodes.OperationNode;
import xi.nodes.VarNode;

public class Parser {

	public static String[] splitOnSemiColons(String exp) {
		List<String> split = new ArrayList<String>(Arrays.asList(exp.split(";")));

		List<String> tokens = new ArrayList<String>();

		while (!split.isEmpty()) {
			String token = split.remove(0);
			while (isIncomplete(token)) {
				token += ";" + split.remove(0);
			}
			tokens.add(token.trim());
		}

		return tokens.toArray(new String[tokens.size()]);
	}

	public static String[] tokenize(String exp) {
		List<String> split = new ArrayList<String>(Arrays.asList(exp.split("\\s+")));

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

	public static Node parseNode(String exp, VariableCache cache) {
		if (exp.equals("null"))
			return new DataNode<XiNull>(XiNull.instance());
		if (exp.matches("-?\\d+"))
			return new DataNode<XiInt>(XiInt.parse(exp));
		if (exp.matches("-?\\d+.\\d+"))
			return new DataNode<XiFloat>(XiFloat.parse(exp));
		if (exp.startsWith("["))
			return new DataNode<XiList>(XiList.parse(exp, cache));
		if (exp.startsWith("{")) {
			XiBlock block = new XiBlock(exp);
			block.addVars(cache);
			return new DataNode<XiBlock>(block);
		}
		if (exp.startsWith("\""))
			return new DataNode<XiString>(new XiString(exp));
		if (IntrinsicOperation.idExists(exp))
			return new OperationNode(IntrinsicOperation.parse(exp), cache);
		if (exp.matches("\\D.*+")) {
			if (cache.get(exp) instanceof XiFunc)
				return new OperationNode((XiFunc)cache.get(exp), cache);
			return new VarNode(exp, cache);
		}
		throw new RuntimeException("Cannot parse expression: " + exp);
	}

}