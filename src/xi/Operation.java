package xi;

import java.util.Random;

import xi.datatypes.DataType;
import xi.datatypes.XiBlock;
import xi.datatypes.XiList;
import xi.datatypes.XiNull;
import xi.datatypes.XiNum;
import xi.datatypes.XiString;
import xi.datatypes.XiVar;

public enum Operation {

	NOT("!", 1), BITNOT("~", 1), ABS("\\", 1), ADD("+", 2), SUBTRACT("-", 2), MULTIPLY(
			"*", 2), DIVIDE("/", 2), MODULUS("%", 2), EQ("=", 2), NEQ("!=", 2), GREATER(
			">", 2), LESS("<", 2), GREATER_EQ(">=", 2), LESS_EQ("<=", 2), AND(
			"&", 2), OR("|", 2), XOR("^", 2), RSHIFT(">>", 2), LSHIFT("<<", 2), POW(
			"**", 2), TERN("?", 3),

	AT("at", 2), MAP("@", 2), RANGE(",", 1), SUM("$", 1), RAND("rnd", 1), SORT(
			"sort", 1), ZIP("zip", 1),

	FOR("for", 3), IF("if", 3), DO("do", 2), WHILE("while", 2), DOWHILE(
			"dowhile", 2),

	EVAL("eval", 1),

	PRINT("print", 1), PRINTLN("println", 1),

	SLEEP("slp", 1),

	HASH("hash", 1), LEN("len", 1);

	private String id;
	private int numArgs;

	private static final XiNull xinull = new XiNull();

	private Operation(String id, int numArgs) {
		this.id = id;
		this.numArgs = numArgs;
	}

	public String id() {
		return id;
	}

	public int numArgs() {
		return numArgs;
	}

	public DataType evaluate(DataType[] args, VariableCache globals) {
		switch (this) {
		case NOT:
			return new XiNum(args[0].isEmpty());
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
			return new XiNum(~((XiNum) args[0]).val());
		case ABS:
			if (args[0] instanceof XiList)
				return ((XiList) args[0]).abs();
			return new XiNum(Math.abs(((XiNum) args[0]).val()));
		case ADD:
			if (args[0] instanceof XiString || args[1] instanceof XiString)
				return new XiString(args[0].toString() + args[1].toString());
			if (args[0] instanceof XiList)
				return ((XiList) args[0]).add(args[1]);
			return ((XiNum) args[0]).add((XiNum) args[1]);
		case SUBTRACT:
			if (args[0] instanceof XiList)
				return ((XiList) args[0]).remove((XiNum) args[1]);
			return ((XiNum) args[0]).sub((XiNum) args[1]);
		case MULTIPLY:
			if (args[0] instanceof XiList)
				return ((XiList) args[0]).mul((XiNum) args[1]);
			if (args[0] instanceof XiString)
				return ((XiString) args[0]).mul((XiNum) args[1]);
			return ((XiNum) args[0]).mul((XiNum) args[1]);
		case DIVIDE:
			if (args[0] instanceof XiList)
				return ((XiList) args[0]).filter((XiBlock) args[1]);
			return ((XiNum) args[0]).div((XiNum) args[1]);
		case MODULUS:
			return ((XiNum) args[0]).mod((XiNum) args[1]);
		case EQ:
			return new XiNum(args[0].equals(args[1]));
		case NEQ:
			return new XiNum(!args[0].equals(args[1]));
		case GREATER:
			return new XiNum(((XiNum) args[0]).val() > ((XiNum) args[1]).val());
		case LESS:
			return new XiNum(((XiNum) args[0]).val() < ((XiNum) args[1]).val());
		case GREATER_EQ:
			return new XiNum(((XiNum) args[0]).val() >= ((XiNum) args[1]).val());
		case LESS_EQ:
			return new XiNum(((XiNum) args[0]).val() <= ((XiNum) args[1]).val());
		case AND:
			if (args[0] instanceof XiNum && args[1] instanceof XiNum)
				return new XiNum(((XiNum) args[0]).val()
						& ((XiNum) args[1]).val());
			return new XiNum((!args[0].isEmpty()) && (!args[1].isEmpty()));
		case OR:
			if (args[0] instanceof XiNum && args[1] instanceof XiNum)
				return new XiNum(((XiNum) args[0]).val()
						| ((XiNum) args[1]).val());
			return new XiNum((!args[0].isEmpty()) || (!args[1].isEmpty()));
		case XOR:
			if (args[0] instanceof XiNum && args[1] instanceof XiNum)
				return new XiNum(((XiNum) args[0]).val()
						^ ((XiNum) args[1]).val());
			return new XiNum((!args[0].isEmpty()) ^ (!args[1].isEmpty()));
		case RSHIFT:
			if (args[0] instanceof XiList)
				return ((XiList) args[0]).rshift((XiNum) args[1]);
			if (args[0] instanceof XiString)
				return ((XiString) args[0]).rshift((XiNum) args[1]);
			return new XiNum(((XiNum) args[0]).val() >> ((XiNum) args[1]).val());
		case LSHIFT:
			if (args[0] instanceof XiList)
				return ((XiList) args[0]).lshift((XiNum) args[1]);
			if (args[0] instanceof XiString)
				return ((XiString) args[0]).lshift((XiNum) args[1]);
			return new XiNum(((XiNum) args[0]).val() << ((XiNum) args[1]).val());
		case POW:
			return ((XiNum) args[0]).pow((XiNum) args[1]);
		case TERN:
			return args[0].isEmpty() ? args[2] : args[1];
		case AT:
			return ((XiList) args[0]).get((XiNum) args[1]);
		case MAP: {
			XiBlock body = (XiBlock) args[1];
			body.addVars(globals);
			return ((XiList) args[0]).map(body);
		}
		case RANGE:
			return new XiList(((XiNum) args[0]).val());
		case SUM:
			return ((XiList) args[0]).sum();
		case RAND:
			if (args[0] instanceof XiList)
				return ((XiList) args[0]).shuffle();
			return new XiNum((new Random()).nextInt(((XiNum) args[0]).val()));
		case SORT:
			return ((XiList)args[0]).sort();
		case ZIP:
			return ((XiList) args[0]).zip();
		case FOR: {
			String id = ((XiString) args[0]).val();
			XiList list = (XiList) args[1];
			XiBlock body = (XiBlock) args[2];
			body.addVars(globals);
			for (int i = 0; i < list.size(); i++) {
				body.updateLocal(new XiVar(id, list.get(i)));
				body.evaluate();
			}
			globals.addAll(body.locals());
			return xinull;
		}
		case IF: {
			XiBlock body = (XiBlock) (args[0].isEmpty() ? args[2] : args[1]);
			body.addVars(globals);
			body.evaluate();
			globals.addAll(body.locals());
			return xinull;
		}
		case DO: {
			int n = ((XiNum) args[0]).val();
			XiBlock body = (XiBlock) args[1];
			body.addVars(globals);
			for (int i = 0; i < n; i++)
				body.evaluate();
			globals.addAll(body.locals());
			return xinull;
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
			return xinull;
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
			return xinull;
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
		case PRINT:
			System.out.print(args[0]);
			return xinull;
		case PRINTLN:
			System.out.println(args[0]);
			return xinull;
		case SLEEP:
			try {
				Thread.sleep(((XiNum) args[0]).val());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return xinull;
		case HASH:
			return new XiNum(args[0].hashCode());
		case LEN:
			return new XiNum(args[0].length());
		default:
			throw new RuntimeException("Internal error");
		}
	}

	public static boolean idExists(String id) {
		for (Operation op : values())
			if (id.equals(op.id()))
				return true;
		return false;
	}

	public static Operation parse(String id) {
		for (Operation op : values())
			if (id.equals(op.id()))
				return op;
		throw new IllegalArgumentException("Invalid identifier: " + id);
	}

}