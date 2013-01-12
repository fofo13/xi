package org.xiscript.xi.core;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

import org.xiscript.xi.datatypes.collections.XiList;
import org.xiscript.xi.datatypes.collections.XiRegex;
import org.xiscript.xi.datatypes.collections.XiString;
import org.xiscript.xi.datatypes.collections.XiTuple;
import org.xiscript.xi.datatypes.functional.XiBlock;
import org.xiscript.xi.datatypes.functional.XiFunc;
import org.xiscript.xi.datatypes.numeric.XiComplex;
import org.xiscript.xi.datatypes.numeric.XiFloat;
import org.xiscript.xi.datatypes.numeric.XiInt;
import org.xiscript.xi.datatypes.numeric.XiLong;
import org.xiscript.xi.nodes.DataNode;
import org.xiscript.xi.nodes.Node;
import org.xiscript.xi.nodes.OperationNode;
import org.xiscript.xi.nodes.VarNode;
import org.xiscript.xi.operations.IntrinsicOperation;

public class Parser {

	public static String[] splitOnSemiColons(String exp) {
		return generateTokenArray(
				new ArrayList<String>(Arrays.asList(exp.split(";"))), ";");
	}

	public static String[] tokenize(String exp) {
		return generateTokenArray(
				new ArrayList<String>(Arrays.asList(exp.split("\\s+"))), " ");
	}

	private static String[] generateTokenArray(List<String> split, String delim) {
		List<String> tokens = new ArrayList<String>();
		Queue<String> queue = new ArrayDeque<String>(split);

		while (!queue.isEmpty()) {
			String token = queue.poll();
			while (isIncomplete(token)) {
				token += delim + queue.poll();
			}
			tokens.add(token.trim());
		}

		return tokens.toArray(new String[tokens.size()]);
	}

	public static boolean containsAssignment(String exp) {
		String[] split = exp.split(":=(?![^\\{]*\\})");

		if (split.length == 1)
			return false;

		for (String line : split)
			if ((line.replace("}", "").length()
					- line.replace("{", "").length() == 0)
					&& ((line.length() - line.replace("\"", "").length()) % 2 == 0))
				return true;

		return false;
	}

	public static boolean isIncomplete(String exp) {
		String mod = exp.replace("\"\"", "").replaceAll("\"[^\"]+\"", "");
		return (exp.length() - exp.replace("\"", "").length()) % 2 == 1
				|| mod.replace("]", "").length()
						- mod.replace("[", "").length() != 0
				|| mod.replace(")", "").length()
						- mod.replace("(", "").length() != 0
				|| mod.replace("}", "").length()
						- mod.replace("{", "").length() != 0;
	}

	public static Node parseNode(String exp, VariableCache cache) {
		if (exp.matches("-?\\d+"))
			return new DataNode<XiInt>(XiInt.parse(exp));
		if (exp.matches("-?\\d+[lL]"))
			return new DataNode<XiLong>(XiLong.parse(exp));
		if (exp.matches("-?\\d+\\.\\d*"))
			return new DataNode<XiFloat>(XiFloat.parse(exp));
		if (exp.matches("-?\\d+(\\.\\d+)*i"))
			return new DataNode<XiComplex>(XiComplex.parseIm(exp));
		if (exp.startsWith("["))
			return new DataNode<XiList>(XiList.parse(exp, cache));
		if (exp.startsWith("("))
			return new DataNode<XiTuple>(XiTuple.parse(exp, cache));
		if (exp.startsWith("{")) {
			XiBlock block = new XiBlock(exp);
			block.addVars(cache);
			return new DataNode<XiBlock>(block);
		}
		if (exp.startsWith("\""))
			return new DataNode<XiString>(new XiString(exp));
		if (exp.startsWith("re\""))
			return new DataNode<XiRegex>(new XiRegex(exp));
		if (IntrinsicOperation.idExists(exp))
			return new OperationNode(IntrinsicOperation.parse(exp), cache);
		if (exp.matches("[\\.\\p{Alpha}_]\\w*")) {
			if (cache.get(exp) instanceof XiFunc)
				return new OperationNode((XiFunc) cache.get(exp), cache);
			return new VarNode(exp, cache);
		}
		throw new RuntimeException("Cannot parse expression: " + exp);
	}

	public static String unescapeJava(String str) {
		if (str == null) {
			return null;
		}
		StringWriter writer = new StringWriter(str.length());
		try {
			unescapeJava(writer, str);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return writer.toString();
	}

	private static void unescapeJava(Writer out, String str) throws IOException {
		int sz = str.length();
		StringBuilder unicode = new StringBuilder(4);
		boolean hadSlash = false;
		boolean inUnicode = false;
		for (int i = 0; i < sz; i++) {
			char ch = str.charAt(i);
			if (inUnicode) {
				// if in unicode, then we're reading unicode
				// values in somehow
				unicode.append(ch);
				if (unicode.length() == 4) {
					// unicode now contains the four hex digits
					// which represents our unicode character
					try {
						int value = Integer.parseInt(unicode.toString(), 16);
						out.write((char) value);
						unicode.setLength(0);
						inUnicode = false;
						hadSlash = false;
					} catch (NumberFormatException nfe) {
						throw new RuntimeException(
								"Unable to parse unicode value: " + unicode,
								nfe);
					}
				}
				continue;
			}
			if (hadSlash) {
				// handle an escaped value
				hadSlash = false;
				switch (ch) {
				case '\\':
					out.write('\\');
					break;
				case '\'':
					out.write('\'');
					break;
				case '\"':
					out.write('"');
					break;
				case 'r':
					out.write('\r');
					break;
				case 'f':
					out.write('\f');
					break;
				case 't':
					out.write('\t');
					break;
				case 'n':
					out.write('\n');
					break;
				case 'b':
					out.write('\b');
					break;
				case 'u': {
					// uh-oh, we're in unicode country....
					inUnicode = true;
					break;
				}
				default:
					out.write(ch);
					break;
				}
				continue;
			} else if (ch == '\\') {
				hadSlash = true;
				continue;
			}
			out.write(ch);
		}
		if (hadSlash) {
			// then we're in the weird case of a \ at the end of the
			// string, let's output it anyway.
			out.write('\\');
		}
	}

}