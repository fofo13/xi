package org.xiscript.xi.core;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Pattern;

import org.xiscript.xi.datatypes.XiAttribute;
import org.xiscript.xi.datatypes.collections.XiRegex;
import org.xiscript.xi.datatypes.collections.XiString;
import org.xiscript.xi.datatypes.functional.XiBlock;
import org.xiscript.xi.datatypes.functional.XiLambda;
import org.xiscript.xi.datatypes.numeric.XiComplex;
import org.xiscript.xi.datatypes.numeric.XiFloat;
import org.xiscript.xi.datatypes.numeric.XiInt;
import org.xiscript.xi.datatypes.numeric.XiLong;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;
import org.xiscript.xi.nodes.CollectionNode;
import org.xiscript.xi.nodes.DataNode;
import org.xiscript.xi.nodes.FunctionConverterNode;
import org.xiscript.xi.nodes.Node;
import org.xiscript.xi.nodes.OperationNode;
import org.xiscript.xi.nodes.VarNode;
import org.xiscript.xi.nodes.assignments.AssignmentNode;
import org.xiscript.xi.nodes.assignments.DivEqualsNode;
import org.xiscript.xi.nodes.assignments.MinusEqualsNode;
import org.xiscript.xi.nodes.assignments.MinusMinusNode;
import org.xiscript.xi.nodes.assignments.PlusEqualsNode;
import org.xiscript.xi.nodes.assignments.PlusPlusNode;
import org.xiscript.xi.nodes.assignments.TimesEqualsNode;
import org.xiscript.xi.operations.IntrinsicOperation;
import org.xiscript.xi.operations.ShortCircuitOperation;

public class Parser {

	private static final Pattern INT = Pattern.compile("-?\\d+");

	private static final Pattern LONG = Pattern.compile("-?\\d+[lL]");

	private static final Pattern FLOAT = Pattern
			.compile("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?");

	private static final Pattern IM = Pattern.compile("-?\\d+(\\.\\d+)*i");

	public static final Pattern IDENTIFIER = Pattern
			.compile("[\\.\\p{Alpha}_]\\w*");

	private static final Set<Character> W = new HashSet<Character>(63);

	private static final Set<Character> SPEC = new HashSet<Character>(63);

	private static final Pattern NUMBER = Pattern
			.compile("-?\\d*\\.?\\d*[LliI]?");

	private static final String ASSIGNMENT = ":=";
	private static final String PLUS_EQUALS = "+=";
	private static final String MINUS_EQUALS = "-=";
	private static final String TIMES_EQUALS = "*=";
	private static final String DIV_EQUALS = "/=";

	private static final String PLUS_PLUS = "++";
	private static final String MINUS_MINUS = "--";

	static {
		for (char c : "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_"
				.toCharArray())
			W.add(c);

		for (char c : "!+~-*/%=<>&|^$@,?:".toCharArray())
			SPEC.add(c);
	}

	public static Queue<Node> genNodeQueue(CharSequence source) {
		ArrayDeque<Node> nodes = new ArrayDeque<Node>();
		Queue<Character> chars = new ArrayDeque<Character>(source.length());
		for (int i = 0; i < source.length(); i++)
			chars.add(source.charAt(i));

		while (!chars.isEmpty()) {
			char c = chars.peek();

			CharSequence token = generateToken(c, chars);

			if (token == null)
				continue;

			nodes.add(parseNode(token.toString()));

			if (!nodes.isEmpty() && nodes.getLast() instanceof AssignmentNode) {
				Node last1 = nodes.pollLast();
				Node last2 = nodes.pollLast();
				nodes.addLast(last1);
				nodes.addLast(last2);
			}
		}

		return nodes;
	}

	private static CharSequence generateToken(char start, Queue<Character> chars) {
		if (Character.isDigit(start))
			return readNum(chars);

		else if (W.contains(start))
			return readWord(chars);

		else if (SPEC.contains(start))
			return readSpec(chars);

		else if (start == '.')
			return Character.toString(chars.poll());

		else if (start == '\'')
			return readAttribute(chars);

		else if (start == '{')
			return readBalanced(chars, '{', '}');

		else if (start == '[')
			return readBalanced(chars, '[', ']');

		else if (start == '(')
			return readBalanced(chars, '(', ')');

		else if (start == '\"')
			return readString(chars);

		else if (start == '`')
			return chars.poll().toString() + generateToken(chars.peek(), chars);

		else
			chars.poll();

		return null;
	}

	private static CharSequence readSpec(Queue<Character> chars) {
		StringBuilder sb = new StringBuilder(chars.poll().toString());

		if (sb.charAt(0) == '-' && Character.isDigit(chars.peek()))
			return '-' + readNum(chars).toString();

		while (SPEC.contains(chars.peek())) {
			sb.append(chars.poll());
		}

		return sb;
	}

	private static CharSequence readWord(Queue<Character> chars) {
		StringBuilder sb = new StringBuilder();

		while (W.contains(chars.peek())) {
			sb.append(chars.poll());

			if (!chars.isEmpty() && chars.peek() == '"') {
				sb.append(readString(chars));
				break;
			}
		}

		if (!chars.isEmpty() && chars.peek() == '`')
			sb.append(chars.poll());

		return sb;
	}

	private static CharSequence readNum(Queue<Character> chars) {
		StringBuilder sb = new StringBuilder(chars.poll().toString());

		while (NUMBER.matcher(sb.toString() + chars.peek()).matches()) {
			sb.append(chars.poll());
		}

		return sb;
	}

	private static CharSequence readAttribute(Queue<Character> chars) {
		StringBuilder sb = new StringBuilder(chars.poll().toString());

		while (true) {
			sb.append(chars.poll());

			if (chars.peek() == '\'') {
				sb.append(chars.poll());
				break;
			}
		}

		return sb;
	}

	private static CharSequence readString(Queue<Character> chars) {
		StringBuilder sb = new StringBuilder(chars.poll().toString());

		int escapeCount = 0;
		while (true) {
			char c = chars.poll();

			if (c == '\\') {
				escapeCount++;
			} else if (c == '\"' && escapeCount % 2 == 0) {
				sb.append('\"');
				break;
			} else {
				escapeCount = 0;
			}

			sb.append(c);
		}

		return sb;
	}

	private static CharSequence readBalanced(Queue<Character> chars,
			final char open, final char close) {
		StringBuilder sb = new StringBuilder(chars.poll().toString());

		int net = 1;
		boolean inQuote = false;
		int escapeCount = 0;

		while (net != 0) {
			char c = chars.poll();

			if (c == open && !inQuote)
				net++;
			else if (c == close && !inQuote)
				net--;
			else if (c == '\\')
				escapeCount++;
			else if (c == '\"') {
				if (escapeCount % 2 == 0)
					inQuote = !inQuote;
			} else
				escapeCount = 0;

			sb.append(c);
		}

		return sb;
	}

	public static Node parseNode(String exp) {
		if (INT.matcher(exp).matches())
			return new DataNode<XiInt>(XiInt.parse(exp));
		if (LONG.matcher(exp).matches())
			return new DataNode<XiLong>(XiLong.parse(exp));
		if (FLOAT.matcher(exp).matches())
			return new DataNode<XiFloat>(XiFloat.parse(exp));
		if (IM.matcher(exp).matches())
			return new DataNode<XiComplex>(XiComplex.parseIm(exp));
		if (exp.equals(ASSIGNMENT))
			return new AssignmentNode();
		if (exp.equals(PLUS_EQUALS))
			return new PlusEqualsNode();
		if (exp.equals(MINUS_EQUALS))
			return new MinusEqualsNode();
		if (exp.equals(TIMES_EQUALS))
			return new TimesEqualsNode();
		if (exp.equals(DIV_EQUALS))
			return new DivEqualsNode();
		if (exp.equals(PLUS_PLUS))
			return new PlusPlusNode();
		if (exp.equals(MINUS_MINUS))
			return new MinusMinusNode();
		if (exp.startsWith("[") || exp.startsWith("("))
			return new CollectionNode(exp);
		if (exp.startsWith("{"))
			return new DataNode<XiBlock>(new XiBlock(exp));
		if (exp.startsWith("\""))
			return new DataNode<XiString>(new XiString(exp.substring(1,
					exp.length() - 1)));
		if (exp.startsWith("'"))
			return new DataNode<XiAttribute>(new XiAttribute(exp.substring(1,
					exp.length() - 1), true));
		if (exp.startsWith("re\""))
			return new DataNode<XiRegex>(new XiRegex(exp.substring(3,
					exp.length() - 1)));
		if (exp.startsWith("r\""))
			return new DataNode<XiString>(new XiString(exp.substring(2,
					exp.length() - 1), true));
		if (IntrinsicOperation.idExists(exp))
			return new OperationNode(IntrinsicOperation.parse(exp));
		if (ShortCircuitOperation.idExists(exp))
			return ShortCircuitOperation.parse(exp).getNode();
		if (IDENTIFIER.matcher(exp).matches()) {
			return new VarNode(exp);
		}
		if (exp.endsWith("`")) {
			String id = exp.substring(0, exp.length() - 1);

			if (IntrinsicOperation.idExists(id))
				return new DataNode<XiLambda>(IntrinsicOperation.parse(id)
						.asLambda());

			return new FunctionConverterNode(id);
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