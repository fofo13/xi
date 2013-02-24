package org.xiscript.xi.core;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.regex.Pattern;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiAttribute;
import org.xiscript.xi.datatypes.collections.CollectionWrapper;
import org.xiscript.xi.datatypes.collections.XiList;
import org.xiscript.xi.datatypes.collections.XiRegex;
import org.xiscript.xi.datatypes.collections.XiString;
import org.xiscript.xi.datatypes.collections.XiTuple;
import org.xiscript.xi.datatypes.functional.XiBlock;
import org.xiscript.xi.datatypes.functional.XiFunc;
import org.xiscript.xi.datatypes.functional.XiLambda;
import org.xiscript.xi.datatypes.numeric.XiComplex;
import org.xiscript.xi.datatypes.numeric.XiFloat;
import org.xiscript.xi.datatypes.numeric.XiInt;
import org.xiscript.xi.datatypes.numeric.XiLong;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;
import org.xiscript.xi.nodes.DataNode;
import org.xiscript.xi.nodes.Node;
import org.xiscript.xi.nodes.OperationNode;
import org.xiscript.xi.nodes.PackedDataNode;
import org.xiscript.xi.nodes.VarNode;
import org.xiscript.xi.operations.IntrinsicOperation;
import org.xiscript.xi.operations.Operation;
import org.xiscript.xi.operations.ShortCircuitOperation;

public class Parser {

	private static final Pattern intPattern = Pattern.compile("-?\\d+");

	private static final Pattern longPattern = Pattern.compile("-?\\d+[lL]");

	private static final Pattern floatPattern = Pattern
			.compile("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?");

	private static final Pattern imPattern = Pattern
			.compile("-?\\d+(\\.\\d+)*i");

	private static final Pattern assignmentSplit = Pattern
			.compile(":=(?![^\\{]*\\})");

	private static final Pattern quoteDel = Pattern
			.compile("\'[^\']*\'|\"[^\"]*\"");

	public static final Pattern identifier = Pattern
			.compile("[\\.\\p{Alpha}_]\\w*");

	public static final Pattern whitespace = Pattern.compile("\\s+");

	public static final Pattern semicolon = Pattern.compile(";");

	public static String[] splitOnSemiColons(String exp) {
		return generateTokenArray(
				new ArrayList<String>(Arrays.asList(semicolon.split(exp))), ";");
	}

	public static String[] tokenize(String exp) {
		return generateTokenArray(
				new ArrayList<String>(Arrays.asList(whitespace.split(exp))),
				" ");
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
		String[] split = assignmentSplit.split(exp);

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
		String mod = quoteDel.matcher(exp).replaceAll("");

		return (exp.length() - exp.replace("\"", "").length()) % 2 == 1
				|| (exp.length() - exp.replace("'", "").length()) % 2 == 1
				|| mod.replace("]", "").length()
						- mod.replace("[", "").length() != 0
				|| mod.replace(")", "").length()
						- mod.replace("(", "").length() != 0
				|| mod.replace("}", "").length()
						- mod.replace("{", "").length() != 0;
	}

	public static Node parseNode(String exp, VariableCache cache) {
		if (intPattern.matcher(exp).matches())
			return new DataNode<XiInt>(XiInt.parse(exp));
		if (longPattern.matcher(exp).matches())
			return new DataNode<XiLong>(XiLong.parse(exp));
		if (floatPattern.matcher(exp).matches())
			return new DataNode<XiFloat>(XiFloat.parse(exp));
		if (imPattern.matcher(exp).matches())
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
			return new DataNode<XiString>(new XiString(exp.substring(1,
					exp.length() - 1)));
		if (exp.startsWith("'"))
			return new DataNode<XiAttribute>(new XiAttribute(exp.substring(1,
					exp.length() - 1), true));
		if (exp.startsWith("re\""))
			return new DataNode<XiRegex>(new XiRegex(exp.substring(3,
					exp.length() - 1)));
		if (IntrinsicOperation.idExists(exp))
			return new OperationNode(IntrinsicOperation.parse(exp), cache);
		if (ShortCircuitOperation.idExists(exp))
			return ShortCircuitOperation.parse(exp).getNode(cache);
		if (identifier.matcher(exp).matches()) {
			if (cache.get(exp) instanceof XiFunc)
				return new OperationNode((XiFunc) cache.get(exp), cache);
			return new VarNode(exp, cache);
		}
		if (exp.startsWith("`")) {
			String id = exp.substring(1);

			if (IntrinsicOperation.idExists(id))
				return new DataNode<XiLambda>(IntrinsicOperation.parse(id)
						.asLambda());

			Node n = parseNode(id, cache);

			if (n instanceof OperationNode
					&& cache.get(id) instanceof Operation) {
				return new DataNode<XiLambda>(
						((Operation) cache.get(id)).asLambda());
			}

			DataType d = n.evaluate();

			if (!(d instanceof CollectionWrapper<?>))
				ErrorHandler.invokeError(ErrorType.UNPACKING_ERROR, d);

			return new PackedDataNode((CollectionWrapper<?>) d);
		}

		ErrorHandler.invokeError(ErrorType.PARSE_ERROR, exp);
		return null;
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
				unicode.append(ch);
				if (unicode.length() == 4) {
					try {
						int value = Integer.parseInt(unicode.toString(), 16);
						out.write((char) value);
						unicode.setLength(0);
						inUnicode = false;
						hadSlash = false;
					} catch (NumberFormatException nfe) {
						ErrorHandler.invokeError(ErrorType.UNICODE_ERROR, str);
					}
				}
				continue;
			}
			if (hadSlash) {
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
			out.write('\\');
		}
	}

}