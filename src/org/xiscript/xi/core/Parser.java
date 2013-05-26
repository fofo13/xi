package org.xiscript.xi.core;

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
import org.xiscript.xi.nodes.StopNode;
import org.xiscript.xi.nodes.VarNode;
import org.xiscript.xi.nodes.assignments.AssignmentNode;
import org.xiscript.xi.nodes.assignments.CompoundAssignmentNode;
import org.xiscript.xi.nodes.assignments.MinusMinusNode;
import org.xiscript.xi.nodes.assignments.PlusPlusNode;
import org.xiscript.xi.operations.BuiltInOperation;
import org.xiscript.xi.operations.ShortCircuitOperation;
import org.xiscript.xi.util.CharacterQueue;

public class Parser {

	private static final Pattern INT = Pattern.compile("-?\\d+");

	private static final Pattern LONG = Pattern.compile("-?\\d+[lL]");

	private static final Pattern FLOAT = Pattern
			.compile("-?\\d*\\.?\\d+([eE][-+]?\\d+)?");

	private static final Pattern IM = Pattern.compile("-?\\d+(\\.\\d+)*[iI]");

	public static final Pattern IDENTIFIER = Pattern
			.compile("[\\.\\p{Alpha}_]\\w*");

	private static final Set<Character> W = new HashSet<Character>(63);

	private static final Set<Character> SPEC = new HashSet<Character>(63);

	private static final Pattern NUMBER = Pattern
			.compile("-?\\d*\\.?\\d*[LliI]?");

	public static final String WORD_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
			+ "abcdefghijklmnopqrstuvwxyz1234567890_";

	public static final String SPECIAL_CHARS = "!+~-*/%=<>&|^$@,?:";

	public static final char MINUS = '-';

	public static final char LIST_START = '[';
	public static final char LIST_END = ']';

	public static final char TUPLE_START = '(';
	public static final char TUPLE_END = ')';

	public static final char BLOCK_START = '{';
	public static final char BLOCK_END = '}';

	public static final char DOTVAR = '.';
	public static final char FUNCTION_CONVERTER = '`';

	public static final char DQUOTE = '"';
	public static final char SQUOTE = '\'';
	public static final char ESCAPE = '\\';

	public static final String RE_START = "re\"";
	public static final String RAW_START = "r\"";

	public static final String ASSIGNMENT = ":=";
	public static final String PLUS_EQUALS = "+=";
	public static final String MINUS_EQUALS = "-=";
	public static final String TIMES_EQUALS = "*=";
	public static final String DIV_EQUALS = "/=";
	public static final String MOD_EQUALS = "%=";
	public static final String POW_EQUALS = "**=";
	public static final String RSHIFT_EQUALS = ">>=";
	public static final String LSHIFT_EQUALS = "<<=";
	public static final String AND_EQUALS = "&=";
	public static final String OR_EQUALS = "|=";
	public static final String XOR_EQUALS = "^=";

	public static final String PLUS_PLUS = "++";
	public static final String MINUS_MINUS = "--";

	public static final char STOP = ';';
	public static final char COMMENT = '#';
	public static final char NEWLINE = '\n';

	static {
		for (int i = 0; i < WORD_CHARS.length(); i++)
			W.add(WORD_CHARS.charAt(i));

		for (int i = 0; i < SPECIAL_CHARS.length(); i++)
			SPEC.add(SPECIAL_CHARS.charAt(i));
	}

	public static Queue<Node> genNodeQueue(Queue<Character> source) {
		ArrayDeque<Node> nodes = new ArrayDeque<Node>();

		while (!source.isEmpty()) {
			CharSequence token = generateToken(source);

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

	public static Queue<Node> genNodeQueue(CharSequence source) {
		return genNodeQueue(new CharacterQueue(source));
	}

	private static CharSequence generateToken(Queue<Character> chars) {
		char start = chars.peek();

		if (Character.isDigit(start))
			return readNum(chars);

		else if (W.contains(start))
			return readWord(chars);

		else if (SPEC.contains(start))
			return readSpec(chars);

		else if (start == DOTVAR)
			return Character.toString(chars.poll());

		else if (start == SQUOTE)
			return readAttribute(chars);

		else if (start == BLOCK_START)
			return readBalanced(chars, BLOCK_START, BLOCK_END);

		else if (start == LIST_START)
			return readBalanced(chars, LIST_START, LIST_END);

		else if (start == TUPLE_START)
			return readBalanced(chars, TUPLE_START, TUPLE_END);

		else if (start == DQUOTE)
			return readString(chars);

		else if (start == COMMENT)
			readComment(chars);

		else if (start == STOP)
			return Character.toString(chars.poll());

		else
			chars.poll();

		return null;
	}

	private static void readComment(Queue<Character> chars) {
		while (!chars.isEmpty() && chars.poll() != NEWLINE)
			;
	}

	private static CharSequence readSpec(Queue<Character> chars) {
		StringBuilder sb = new StringBuilder(chars.poll().toString());

		if (sb.charAt(0) == MINUS && Character.isDigit(chars.peek()))
			return MINUS + readNum(chars).toString();

		while (SPEC.contains(chars.peek())) {
			sb.append(chars.poll());
		}

		if (!chars.isEmpty() && chars.peek() == FUNCTION_CONVERTER)
			sb.append(chars.poll());

		return sb;
	}

	private static CharSequence readWord(Queue<Character> chars) {
		StringBuilder sb = new StringBuilder();

		while (W.contains(chars.peek())) {
			sb.append(chars.poll());

			if (!chars.isEmpty() && chars.peek() == DQUOTE) {
				sb.append(readString(chars));
				break;
			}
		}

		if (!chars.isEmpty() && chars.peek() == FUNCTION_CONVERTER)
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

			if (chars.peek() == SQUOTE) {
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

			if (c == ESCAPE) {
				escapeCount++;
			} else if (c == DQUOTE && escapeCount % 2 == 0) {
				sb.append(DQUOTE);
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
			else if (c == ESCAPE)
				escapeCount++;
			else if (c == DQUOTE && escapeCount % 2 == 0)
				inQuote = !inQuote;
			else
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
			return new CompoundAssignmentNode(BuiltInOperation.ADD);
		if (exp.equals(MINUS_EQUALS))
			return new CompoundAssignmentNode(BuiltInOperation.SUBTRACT);
		if (exp.equals(TIMES_EQUALS))
			return new CompoundAssignmentNode(BuiltInOperation.MULTIPLY);
		if (exp.equals(DIV_EQUALS))
			return new CompoundAssignmentNode(BuiltInOperation.DIVIDE);
		if (exp.equals(MOD_EQUALS))
			return new CompoundAssignmentNode(BuiltInOperation.MODULUS);
		if (exp.equals(POW_EQUALS))
			return new CompoundAssignmentNode(BuiltInOperation.POW);
		if (exp.equals(RSHIFT_EQUALS))
			return new CompoundAssignmentNode(BuiltInOperation.RSHIFT);
		if (exp.equals(LSHIFT_EQUALS))
			return new CompoundAssignmentNode(BuiltInOperation.LSHIFT);
		if (exp.equals(AND_EQUALS))
			return new CompoundAssignmentNode(BuiltInOperation.AND);
		if (exp.equals(OR_EQUALS))
			return new CompoundAssignmentNode(BuiltInOperation.OR);
		if (exp.equals(XOR_EQUALS))
			return new CompoundAssignmentNode(BuiltInOperation.XOR);

		if (exp.equals(PLUS_PLUS))
			return new PlusPlusNode();
		if (exp.equals(MINUS_MINUS))
			return new MinusMinusNode();

		if (exp.charAt(0) == LIST_START || exp.charAt(0) == TUPLE_START)
			return new CollectionNode(exp);
		if (exp.charAt(0) == BLOCK_START)
			return new DataNode<XiBlock>(new XiBlock(exp));
		if (exp.charAt(0) == DQUOTE)
			return new DataNode<XiString>(new XiString(exp.substring(1,
					exp.length() - 1)));
		if (exp.charAt(0) == SQUOTE)
			return new DataNode<XiAttribute>(XiAttribute.valueOf(exp.substring(
					1, exp.length() - 1)));
		if (exp.startsWith(RE_START))
			return new DataNode<XiRegex>(new XiRegex(exp.substring(3,
					exp.length() - 1)));
		if (exp.startsWith(RAW_START))
			return new DataNode<XiString>(new XiString(exp.substring(2,
					exp.length() - 1), true));
		if (BuiltInOperation.idExists(exp))
			return new OperationNode(BuiltInOperation.parse(exp));
		if (ShortCircuitOperation.idExists(exp))
			return ShortCircuitOperation.parse(exp).getNode();
		if (IDENTIFIER.matcher(exp).matches())
			return new VarNode(exp);
		if (exp.charAt(exp.length() - 1) == FUNCTION_CONVERTER) {
			String id = exp.substring(0, exp.length() - 1);

			if (BuiltInOperation.idExists(id))
				return new DataNode<XiLambda>(BuiltInOperation.parse(id)
						.asLambda());

			return new FunctionConverterNode(id);
		}

		if (exp.charAt(0) == STOP)
			return StopNode.instance;

		ErrorHandler.invokeError(ErrorType.PARSE_ERROR, exp);
		return null;
	}

	public static String unescapeJava(String str) {
		int len = str.length();
		StringBuilder sb = new StringBuilder(len);

		StringBuilder unicode = new StringBuilder(4);
		boolean hadSlash = false;
		boolean inUnicode = false;
		for (int i = 0; i < len; i++) {
			char c = str.charAt(i);
			if (inUnicode) {
				unicode.append(c);
				if (unicode.length() == 4) {
					try {
						int value = Integer.parseInt(unicode.toString(), 16);
						sb.append((char) value);
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
				switch (c) {
				case '\\':
					sb.append('\\');
					break;
				case '\'':
					sb.append('\'');
					break;
				case '\"':
					sb.append('"');
					break;
				case 'r':
					sb.append('\r');
					break;
				case 'f':
					sb.append('\f');
					break;
				case 't':
					sb.append('\t');
					break;
				case 'n':
					sb.append('\n');
					break;
				case 'b':
					sb.append('\b');
					break;
				case 'u': {
					inUnicode = true;
					break;
				}
				default:
					sb.append(c);
					break;
				}
				continue;
			} else if (c == '\\') {
				hadSlash = true;
				continue;
			}
			sb.append(c);
		}
		if (hadSlash) {
			sb.append('\\');
		}

		return sb.toString();
	}

}