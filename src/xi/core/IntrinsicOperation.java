package xi.core;

import java.util.Random;

import xi.datatypes.DataType;
import xi.datatypes.XiBlock;
import xi.datatypes.XiDictionary;
import xi.datatypes.XiFunc;
import xi.datatypes.XiNull;
import xi.datatypes.XiVar;
import xi.datatypes.collections.CollectionWrapper;
import xi.datatypes.collections.ListWrapper;
import xi.datatypes.collections.XiList;
import xi.datatypes.collections.XiSet;
import xi.datatypes.collections.XiString;
import xi.datatypes.numeric.XiComplex;
import xi.datatypes.numeric.XiFloat;
import xi.datatypes.numeric.XiInt;
import xi.datatypes.numeric.XiNum;
import xi.datatypes.numeric.XiReal;

public enum IntrinsicOperation implements Operation {

	NOT("!", 1), BITNOT("~", 1), ABS("\\", 1), ADD("+", 2), SUBTRACT("-", 2), MULTIPLY(
			"*", 2), DIVIDE("/", 2), MODULUS("%", 2), EQ("=", 2), NEQ("!=", 2), GREATER(
			">", 2), LESS("<", 2), GREATER_EQ(">=", 2), LESS_EQ("<=", 2), AND(
			"&", 2), OR("|", 2), XOR("^", 2), RSHIFT(">>", 2), LSHIFT("<<", 2), POW(
			"**", 2), TERN("?", 3),

	IN("in", 2), AT("at", 2), MAP("@", 2), RANGE(",", 1), SUM("$", 1), RAND(
			"rnd", 1), SORT("sort", 1), ZIP("zip", 1), CUT("cut", 2),

	PUT("put", 3),

	FOR("for", 3), IF("if", 3), DO("do", 2), WHILE("while", 2), DOWHILE(
			"dowhile", 2),

	EVAL("eval", 1),

	STR("str", 1), INT("int", 1), FLOAT("float", 1), LIST("list", 1), SET(
			"set", 1), DICT("dict", 1), CMPLX("cmplx", 2), FUNC("func", 2),

	PRINT("print", 1), PRINTLN("println", 1),

	SLEEP("slp", 1),

	HASH("hash", 1), LEN("len", 1);

	private String id;
	private int numArgs;

	private IntrinsicOperation(String id, int numArgs) {
		this.id = id;
		this.numArgs = numArgs;
	}

	public String id() {
		return id;
	}

	@Override
	public int numArgs() {
		return numArgs;
	}

	@Override
	public DataType evaluate(DataType[] args, VariableCache globals) {
		switch (this) {
		case NOT:
			return new XiInt(args[0].isEmpty());
		case BITNOT:
			if (args[0] instanceof XiList)
				return ((XiList) args[0]).zip();
			if (args[0] instanceof XiBlock) {
				XiBlock block = (XiBlock) args[0];
				block.addVars(globals);
				try {
					return block.evaluate();
				} finally {
					globals.addAll(block.locals());
				}
			}
			return new XiInt(~((XiInt) args[0]).val());
		case ABS:
			if (args[0] instanceof XiList)
				return ((XiList) args[0]).abs();
			return ((XiNum) args[0]).abs();
		case ADD:
			if (args[0] instanceof CollectionWrapper)
				return ((CollectionWrapper<?>) args[0]).add(args[1]);
			if (args[0] instanceof XiString || args[1] instanceof XiString)
				return new XiString(args[0].toString() + args[1].toString());
			return ((XiNum) args[0]).add((XiNum) args[1]);
		case SUBTRACT:
			if (args[0] instanceof ListWrapper)
				return ((ListWrapper) args[0]).remove((XiInt) args[1]);
			if (args[0] instanceof XiSet && args[1] instanceof XiSet)
				return ((XiSet) args[0]).difference((XiSet) args[1], false);
			return ((XiNum) args[0]).sub((XiNum) args[1]);
		case MULTIPLY:
			if (args[0] instanceof ListWrapper)
				return ((ListWrapper) args[0]).mul((XiInt) args[1]);
			return ((XiNum) args[0]).mul((XiNum) args[1]);
		case DIVIDE:
			if (args[0] instanceof CollectionWrapper)
				return ((CollectionWrapper<?>) args[0])
						.filter((XiBlock) args[1]);
			return ((XiNum) args[0]).div((XiNum) args[1]);
		case MODULUS:
			return ((XiInt) args[0]).mod((XiInt) args[1]);
		case EQ:
			return new XiInt(args[0].equals(args[1]));
		case NEQ:
			return new XiInt(!args[0].equals(args[1]));
		case GREATER:
			if (args[0] instanceof XiSet && args[1] instanceof XiSet)
				return new XiInt((!args[0].equals(args[1]))
						&& ((XiSet) args[0]).superset((XiSet) args[1]));
			return new XiInt(((XiInt) args[0]).val() > ((XiInt) args[1]).val());
		case LESS:
			if (args[0] instanceof XiSet && args[1] instanceof XiSet)
				return new XiInt((!args[0].equals(args[1]))
						&& ((XiSet) args[0]).subset((XiSet) args[1]));
			return new XiInt(((XiInt) args[0]).val() < ((XiInt) args[1]).val());
		case GREATER_EQ:
			if (args[0] instanceof XiSet && args[1] instanceof XiSet)
				return new XiInt(((XiSet) args[0]).superset((XiSet) args[1]));
			return new XiInt(((XiInt) args[0]).val() >= ((XiInt) args[1]).val());
		case LESS_EQ:
			if (args[0] instanceof XiSet && args[1] instanceof XiSet)
				return new XiInt(((XiSet) args[0]).subset((XiSet) args[1]));
			return new XiInt(((XiInt) args[0]).val() <= ((XiInt) args[1]).val());
		case AND:
			if (args[0] instanceof XiInt && args[1] instanceof XiInt)
				return new XiInt(((XiInt) args[0]).val()
						& ((XiInt) args[1]).val());
			if (args[0] instanceof XiSet && args[1] instanceof XiSet)
				return ((XiSet) args[0]).intersection((XiSet) args[1]);
			return new XiInt((!args[0].isEmpty()) && (!args[1].isEmpty()));
		case OR:
			if (args[0] instanceof XiInt && args[1] instanceof XiInt)
				return new XiInt(((XiInt) args[0]).val()
						| ((XiInt) args[1]).val());
			if (args[0] instanceof XiSet && args[1] instanceof XiSet)
				return ((XiSet) args[0]).union((XiSet) args[1]);
			return new XiInt((!args[0].isEmpty()) || (!args[1].isEmpty()));
		case XOR:
			if (args[0] instanceof XiInt && args[1] instanceof XiInt)
				return new XiInt(((XiInt) args[0]).val()
						^ ((XiInt) args[1]).val());
			if (args[0] instanceof XiSet && args[1] instanceof XiSet)
				return ((XiSet) args[0]).difference((XiSet) args[1], true);
			return new XiInt((!args[0].isEmpty()) ^ (!args[1].isEmpty()));
		case RSHIFT:
			if (args[0] instanceof ListWrapper)
				return ((ListWrapper) args[0]).rshift((XiInt) args[1]);
			return new XiInt(((XiInt) args[0]).val() >> ((XiInt) args[1]).val());
		case LSHIFT:
			if (args[0] instanceof ListWrapper)
				return ((ListWrapper) args[0]).lshift((XiInt) args[1]);
			return new XiInt(((XiInt) args[0]).val() << ((XiInt) args[1]).val());
		case POW:
			return ((XiNum) args[0]).pow((XiNum) args[1]);
		case TERN:
			return args[0].isEmpty() ? args[2] : args[1];
		case IN:
			return new XiInt(((CollectionWrapper<?>) args[0]).contains(args[1]));
		case AT:
			if (args[0] instanceof XiDictionary)
				return ((XiDictionary) args[0]).get(args[1]);
			return ((ListWrapper) args[0]).get((XiInt) args[1]);
		case MAP: {
			XiBlock body = (XiBlock) args[1];
			body.addVars(globals);
			return ((CollectionWrapper<?>) args[0]).map(body);
		}
		case RANGE:
			return new XiList(((XiInt) args[0]).val());
		case SUM:
			return ((CollectionWrapper<?>) args[0]).sum();
		case RAND:
			if (args[0] instanceof ListWrapper)
				return ((ListWrapper) args[0]).shuffle();
			return new XiInt((new Random()).nextInt(((XiInt) args[0]).val()));
		case SORT:
			return ((ListWrapper) args[0]).sort();
		case ZIP:
			return ((XiList) args[0]).zip();
		case CUT:
			if (args[1] instanceof XiInt)
				return ((ListWrapper) args[0]).cut((XiInt) args[1]);
			return ((ListWrapper) args[0]).cut((XiList) args[1]);
		case PUT:
			((XiDictionary) args[0]).put(args[1], args[2]);
			return XiNull.instance();
		case FOR: {
			String id = ((XiString) args[0]).toString();
			CollectionWrapper<?> col = (CollectionWrapper<?>) args[1];
			XiBlock body = (XiBlock) args[2];
			body.addVars(globals);
			for (DataType data : col) {
				body.updateLocal(new XiVar(id, data));
				body.evaluate();
			}
			globals.addAll(body.locals());
			return XiNull.instance();
		}
		case IF: {
			XiBlock body = (XiBlock) (args[0].isEmpty() ? args[2] : args[1]);
			body.addVars(globals);
			body.evaluate();
			globals.addAll(body.locals());
			return XiNull.instance();
		}
		case DO: {
			int n = ((XiInt) args[0]).val();
			XiBlock body = (XiBlock) args[1];
			body.addVars(globals);
			for (int i = 0; i < n; i++)
				body.evaluate();
			globals.addAll(body.locals());
			return XiNull.instance();
		}
		case WHILE: {
			XiBlock cond = (XiBlock) args[0];
			XiBlock body = (XiBlock) args[1];
			cond.addVars(globals);
			body.addVars(globals);
			while (!cond.evaluate().isEmpty()) {
				body.evaluate();
				cond.addVars(body.locals());
			}
			globals.addAll(body.locals());
			return XiNull.instance();
		}
		case DOWHILE: {
			XiBlock cond = (XiBlock) args[1];
			XiBlock body = (XiBlock) args[0];
			cond.addVars(globals);
			body.addVars(globals);
			do {
				body.evaluate();
				cond.addVars(body.locals());
			} while (!cond.evaluate().isEmpty());
			globals.addAll(body.locals());
			return XiNull.instance();
		}
		case EVAL: {
			XiBlock block = (XiBlock) args[0];
			block.addVars(globals);
			try {
				return block.evaluate();
			} finally {
				globals.addAll(block.locals());
			}
		}
		case STR:
			return new XiString(args[0].toString());
		case INT:
			return XiInt.parse(args[0].toString());
		case FLOAT:
			return XiFloat.parse(args[0].toString());
		case LIST:
			return ((CollectionWrapper<?>) args[0]).asList();
		case SET:
			if (args[0] instanceof XiDictionary)
				return ((XiDictionary) args[0]).keySet();
			return ((CollectionWrapper<?>) args[0]).asSet();
		case DICT:
			return new XiDictionary();
		case CMPLX:
			double re = ((XiReal) args[0]).num().doubleValue();
			double im = ((XiReal) args[1]).num().doubleValue();
			return new XiComplex(re, im);
		case FUNC:
			return new XiFunc((XiBlock) args[0], ((XiInt) args[1]).val());
		case PRINT:
			System.out.print(args[0]);
			return XiNull.instance();
		case PRINTLN:
			System.out.println(args[0]);
			return XiNull.instance();
		case SLEEP:
			try {
				Thread.sleep(((XiInt) args[0]).val());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return XiNull.instance();
		case HASH:
			return new XiInt(args[0].hashCode());
		case LEN:
			return new XiInt(args[0].length());
		default:
			throw new RuntimeException("Internal error");
		}
	}

	public static boolean idExists(String id) {
		for (IntrinsicOperation op : values())
			if (id.equals(op.id()))
				return true;
		return false;
	}

	public static IntrinsicOperation parse(String id) {
		for (IntrinsicOperation op : values())
			if (id.equals(op.id()))
				return op;
		throw new IllegalArgumentException("Invalid identifier: " + id);
	}

}