package org.xiscript.xi.core;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

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
import org.xiscript.xi.nodes.FunctionNode;
import org.xiscript.xi.nodes.Node;
import org.xiscript.xi.nodes.OperationNode;
import org.xiscript.xi.nodes.VarNode;
import org.xiscript.xi.nodes.assignments.AssignmentNode;
import org.xiscript.xi.nodes.assignments.CompoundAssignmentNode;
import org.xiscript.xi.nodes.assignments.MinusMinusNode;
import org.xiscript.xi.nodes.assignments.PlusPlusNode;
import org.xiscript.xi.nodes.singletons.SepNode;
import org.xiscript.xi.nodes.singletons.StopNode;
import org.xiscript.xi.nodes.singletons.ToNode;
import org.xiscript.xi.operations.BuiltInOperation;
import org.xiscript.xi.operations.ShortCircuitOperation;
import org.xiscript.xi.util.CharacterQueue;

public class Parser {

	private static final Set<Character> W = new HashSet<Character>(63);

	private static final Set<Character> SPEC = new HashSet<Character>(63);

	private static final Set<String> ASSIGNMENTS = new HashSet<String>(12);

	public static final String WORD_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
			+ "abcdefghijklmnopqrstuvwxyz1234567890_";

	public static final String SPECIAL_CHARS = "\\!+~-*/%=<>&|^$@,?:";

	public static final char MINUS = '-';

	public static final char LIST_START = '[';
	public static final char LIST_END = ']';

	public static final char TUPLE_START = '(';
	public static final char TUPLE_END = ')';

	public static final char BLOCK_START = '{';
	public static final char BLOCK_END = '}';

	public static final char FUNCTION_CONVERTER = '`';

	public static final char DQUOTE = '"';
	public static final char SQUOTE = '\'';
	public static final char ESCAPE = '\\';

	public static final String RE_START = "re";
	public static final String RAW_START = "r";

	public static final String ASSIGNMENT = "=";
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

	public static final char CALL = ':';
	public static final char TO = ':';
	public static final char STOP = ';';
	public static final char SEP = ',';
	public static final char COMMENT = '#';
	public static final char NEWLINE = '\n';

	static {
		for (int i = 0; i < WORD_CHARS.length(); i++)
			W.add(WORD_CHARS.charAt(i));

		for (int i = 0; i < SPECIAL_CHARS.length(); i++)
			SPEC.add(SPECIAL_CHARS.charAt(i));

		ASSIGNMENTS.add(ASSIGNMENT);
		ASSIGNMENTS.add(PLUS_EQUALS);
		ASSIGNMENTS.add(MINUS_EQUALS);
		ASSIGNMENTS.add(TIMES_EQUALS);
		ASSIGNMENTS.add(DIV_EQUALS);
		ASSIGNMENTS.add(MOD_EQUALS);
		ASSIGNMENTS.add(POW_EQUALS);
		ASSIGNMENTS.add(RSHIFT_EQUALS);
		ASSIGNMENTS.add(LSHIFT_EQUALS);
		ASSIGNMENTS.add(AND_EQUALS);
		ASSIGNMENTS.add(OR_EQUALS);
		ASSIGNMENTS.add(XOR_EQUALS);
		ASSIGNMENTS.add(PLUS_PLUS);
		ASSIGNMENTS.add(MINUS_MINUS);
	}

	public static Queue<Node> genNodeQueue(Queue<Character> source) {
		ArrayDeque<Node> nodes = new ArrayDeque<Node>();

		while (!source.isEmpty()) {
			Node node = generateNode(source);

			if (node == null)
				continue;

			if (node instanceof AssignmentNode
					|| (node instanceof OperationNode && ((OperationNode) node)
							.op() == BuiltInOperation.LAMBDA)) {
				Node last = nodes.pollLast();
				nodes.add(node);
				nodes.add(last);
			} else {
				nodes.add(node);
			}
		}

		return nodes;
	}

	public static Queue<Node> genNodeQueue(CharSequence source) {
		return genNodeQueue(new CharacterQueue(source));
	}

	private static Node generateNode(Queue<Character> chars) {
		if (chars.isEmpty())
			return null;

		char start = chars.peek();

		if (Character.isDigit(start))
			return readNum(chars);

		else if (W.contains(start))
			return readWord(chars);

		else if (SPEC.contains(start))
			return readSpec(chars);

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

		else if (start == STOP) {
			chars.poll();
			return StopNode.instance();
		}

		else
			chars.poll();

		return generateNode(chars);
	}

	private static void readComment(Queue<Character> chars) {
		while (!chars.isEmpty() && chars.poll() != NEWLINE)
			;
	}

	private static boolean isValidSpec(CharSequence s) {
		return BuiltInOperation.idExists(s.toString())
				|| ShortCircuitOperation.idExists(s.toString())
				|| ASSIGNMENTS.contains(s);
	}

	private static Node readSpec(Queue<Character> chars) {
		StringBuilder sb = new StringBuilder(chars.poll().toString());

		if (sb.charAt(0) == MINUS && Character.isDigit(chars.peek()))
			return readNum(chars, false);

		if (sb.charAt(0) == SEP)
			return SepNode.instance();

		if (sb.charAt(0) == TO)
			return ToNode.instance();

		while (SPEC.contains(chars.peek())) {
			if (isValidSpec(sb) && !isValidSpec(sb.toString() + chars.peek()))
				break;

			sb.append(chars.poll());
		}

		if (!chars.isEmpty() && chars.peek() == FUNCTION_CONVERTER)
			sb.append(chars.poll());

		if (ASSIGNMENT.contentEquals(sb))
			return new AssignmentNode();
		if (PLUS_EQUALS.contentEquals(sb))
			return new CompoundAssignmentNode(BuiltInOperation.ADD);
		if (MINUS_EQUALS.contentEquals(sb))
			return new CompoundAssignmentNode(BuiltInOperation.SUB);
		if (TIMES_EQUALS.contentEquals(sb))
			return new CompoundAssignmentNode(BuiltInOperation.MUL);
		if (DIV_EQUALS.contentEquals(sb))
			return new CompoundAssignmentNode(BuiltInOperation.DIVIDE);
		if (MOD_EQUALS.contentEquals(sb))
			return new CompoundAssignmentNode(BuiltInOperation.MOD);
		if (POW_EQUALS.contentEquals(sb))
			return new CompoundAssignmentNode(BuiltInOperation.POW);
		if (RSHIFT_EQUALS.contentEquals(sb))
			return new CompoundAssignmentNode(BuiltInOperation.RSHIFT);
		if (LSHIFT_EQUALS.contentEquals(sb))
			return new CompoundAssignmentNode(BuiltInOperation.LSHIFT);
		if (AND_EQUALS.contentEquals(sb))
			return new CompoundAssignmentNode(BuiltInOperation.AND);
		if (OR_EQUALS.contentEquals(sb))
			return new CompoundAssignmentNode(BuiltInOperation.OR);
		if (OR_EQUALS.contentEquals(sb))
			return new CompoundAssignmentNode(BuiltInOperation.XOR);

		if (PLUS_PLUS.contentEquals(sb))
			return new PlusPlusNode();
		if (MINUS_MINUS.contentEquals(sb))
			return new MinusMinusNode();

		if (BuiltInOperation.idExists(sb.toString()))
			return new OperationNode(BuiltInOperation.parse(sb.toString()));
		if (ShortCircuitOperation.idExists(sb.toString()))
			return ShortCircuitOperation.parse(sb.toString()).getNode();

		ErrorHandler.invokeError(ErrorType.PARSE_ERROR, sb);
		return null;
	}

	private static Node readWord(Queue<Character> chars) {
		StringBuilder sb = new StringBuilder();

		while ((!chars.isEmpty())
				&& (W.contains(chars.peek()) || chars.peek() == '.')) {
			sb.append(chars.poll());

			if (!chars.isEmpty() && chars.peek() == DQUOTE) {
				return readString(chars, sb);
			}
		}

		if (!chars.isEmpty() && chars.peek() == FUNCTION_CONVERTER) {
			chars.poll();
			String id = sb.toString();

			if (BuiltInOperation.idExists(id))
				return new DataNode<XiLambda>(BuiltInOperation.parse(id)
						.asLambda());

			return new FunctionConverterNode(id);
		}

		if (!chars.isEmpty() && chars.peek() == CALL) {
			chars.poll();
			String id = sb.toString();

			return new FunctionNode(id);
		}

		if (BuiltInOperation.idExists(sb.toString()))
			return new OperationNode(BuiltInOperation.parse(sb.toString()));
		if (ShortCircuitOperation.idExists(sb.toString()))
			return ShortCircuitOperation.parse(sb.toString()).getNode();

		return new VarNode(sb.toString());
	}

	private static Node readNum(Queue<Character> chars, boolean positive) {
		StringBuilder sb = new StringBuilder((positive ? ""
				: Character.toString(MINUS))
				+ chars.poll());

		boolean dotFound = false;

		while (!chars.isEmpty()) {
			char peek = chars.peek();

			if (Character.isDigit(peek)) {
				sb.append(chars.poll());
			}

			else if (peek == '.') {
				if (dotFound)
					break;

				dotFound = true;
				sb.append(chars.poll());
			}

			else
				break;
		}

		char modifier = chars.isEmpty() ? 0 : chars.peek();

		if (modifier == 'i' || modifier == 'I') {
			chars.poll();
			return new DataNode<XiComplex>(XiComplex.parseIm(sb.toString()));
		}
		if (modifier == 'l' || modifier == 'L') {
			chars.poll();
			return new DataNode<XiLong>(XiLong.parse(sb.toString()));
		}
		if (dotFound)
			return new DataNode<XiFloat>(XiFloat.parse(sb.toString()));
		return new DataNode<XiInt>(XiInt.parse(sb.toString()));
	}

	private static Node readNum(Queue<Character> chars) {
		return readNum(chars, true);
	}

	private static Node readAttribute(Queue<Character> chars) {
		chars.poll();
		StringBuilder sb = new StringBuilder();

		while (chars.peek() != SQUOTE) {
			sb.append(chars.poll());
		}
		chars.poll();

		return new DataNode<XiAttribute>(XiAttribute.valueOf(sb.toString()));
	}

	private static Node readString(Queue<Character> chars, CharSequence type) {
		chars.poll();
		StringBuilder sb = new StringBuilder();

		int escapeCount = 0;
		while (true) {
			char c = chars.poll();

			if (c == ESCAPE) {
				escapeCount++;
			} else if (c == DQUOTE && escapeCount % 2 == 0) {
				break;
			} else {
				escapeCount = 0;
			}

			sb.append(c);
		}

		if (type != null) {
			if (RE_START.contentEquals(type))
				return new DataNode<XiRegex>(new XiRegex(sb.toString()));
			if (RAW_START.contentEquals(type))
				return new DataNode<XiString>(new XiString(sb.toString(), true));
		}

		return new DataNode<XiString>(new XiString(sb.toString()));
	}

	private static Node readString(Queue<Character> chars) {
		return readString(chars, null);
	}

	private static Node readBalanced(Queue<Character> chars, final char open,
			final char close) {
		chars.poll();
		StringBuilder sb = new StringBuilder();

		int net = 1;
		boolean inQuote = false;
		int escapeCount = 0;

		if (!chars.isEmpty()) {
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

				if (net != 0)
					sb.append(c);
			}
		}

		Node[] nodes = new SyntaxTree(Parser.genNodeQueue(sb)).statements();

		if (open == LIST_START)
			return new CollectionNode(nodes, CollectionNode.TYPE_LIST);

		if (open == TUPLE_START)
			return new CollectionNode(nodes, CollectionNode.TYPE_TUPLE);

		if (open == BLOCK_START) {
			boolean sepFound = false;

			for (Node node : nodes) {
				if (node == SepNode.instance())
					sepFound = true;
				else if (node == ToNode.instance())
					return new CollectionNode(nodes, CollectionNode.TYPE_DICT);
			}

			if (sepFound)
				return new CollectionNode(nodes, CollectionNode.TYPE_SET);
			return new DataNode<XiBlock>(new XiBlock(nodes));
		}

		ErrorHandler.invokeError(ErrorType.INTERNAL);
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