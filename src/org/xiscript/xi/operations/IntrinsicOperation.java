package org.xiscript.xi.operations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiAttribute;
import org.xiscript.xi.datatypes.XiDictionary;
import org.xiscript.xi.datatypes.XiNull;
import org.xiscript.xi.datatypes.XiSys;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.datatypes.collections.CollectionWrapper;
import org.xiscript.xi.datatypes.collections.ListWrapper;
import org.xiscript.xi.datatypes.collections.XiList;
import org.xiscript.xi.datatypes.collections.XiSet;
import org.xiscript.xi.datatypes.collections.XiString;
import org.xiscript.xi.datatypes.collections.XiTuple;
import org.xiscript.xi.datatypes.functional.XiBlock;
import org.xiscript.xi.datatypes.functional.XiFunc;
import org.xiscript.xi.datatypes.functional.XiLambda;
import org.xiscript.xi.datatypes.io.XiFile;
import org.xiscript.xi.datatypes.numeric.XiComplex;
import org.xiscript.xi.datatypes.numeric.XiFloat;
import org.xiscript.xi.datatypes.numeric.XiInt;
import org.xiscript.xi.datatypes.numeric.XiLong;
import org.xiscript.xi.datatypes.numeric.XiNum;
import org.xiscript.xi.datatypes.numeric.XiReal;
import org.xiscript.xi.exceptions.BreakException;
import org.xiscript.xi.exceptions.ContinueException;
import org.xiscript.xi.exceptions.ReturnException;

public enum IntrinsicOperation implements Operation {

	NULL("null", 0),

	NOT("!", 1), BITNOT("~", 1), ABS("abs", 1), ADD("+", 2), SUBTRACT("-", 2), MULTIPLY(
			"*", 2), DIVIDE("/", 2), INTDIV("//", 2), MODULUS("%", 2), EQ("=",
			2), NEQ("!=", 2), GREATER(">", 2), LESS("<", 2), GREATER_EQ(">=", 2), LESS_EQ(
			"<=", 2), AND("&", 2), OR("|", 2), XOR("^", 2), RSHIFT(">>", 2), LSHIFT(
			"<<", 2), POW("**", 2), TERN("?", 3),

	FIND("find", 2), IN("in", 2), AT("at", 2), MAP("@", 2), DEEPMAP("@@", 2), RANGE(
			",", 1), SUM("$", 1), RAND("rnd", 1), SORT("sort", 1), CUT("cut", 2), DEL(
			"del", 2), PUT("put", 3), REPLACE("replace", 3),

	FOR("for", 3), IF("if", 3), DO("do", 2), WHILE("while", 2), DOWHILE(
			"dowhile", 2), LOOP("loop", 2),

	EVAL("eval", 1), EXEC("exec", 1), APPLY("::", 2),

	STR("str", 1), INT("int", 1), FLOAT("float", 1), LONG("long", 1), LIST(
			"list", 1), SET("set", 1), TUPLE("tuple", 1), DICT("dict", 1), CMPLX(
			"cmplx", 2), FUNC("func", 2), LAMBDA("lambda", 2), FILE("file", 1),

	PRINT("print", 1), PRINTLN("println", 1), PRINTF("printf", 2),

	INPUT("input", 0),

	SLEEP("sleep", 1),

	HASH("hash", 1), LEN("len", 1),

	ASSERT("assert", 1),

	TYPE("type", 1),

	GETATTR("=>", 2), SETATTR("<=", 3);

	private static final Map<String, IntrinsicOperation> ids = new HashMap<String, IntrinsicOperation>(
			values().length);

	static {
		for (IntrinsicOperation op : values())
			ids.put(op.id, op);
	}

	private final String id;
	private final int numArgs;

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
		case NULL:
			return XiNull.instance();
		case NOT:
			return new XiInt(args[0].isEmpty());
		case BITNOT:
			if (args[0] instanceof ListWrapper)
				return ((ListWrapper) args[0]).zip();
			if (args[0] instanceof XiLambda) {
				XiLambda lambda = (XiLambda) args[0];
				if (lambda.length() == 0)
					return MAP.evaluate(
							new DataType[] { lambda, new XiTuple() }, globals);
			}
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
			if (args[0] instanceof XiBlock)
				return ((CollectionWrapper<?>) args[1]).filter(
						(XiBlock) args[0], false);
			return ((XiNum) args[0]).div((XiNum) args[1]);
		case INTDIV:
			if (args[0] instanceof XiBlock)
				return ((CollectionWrapper<?>) args[1]).filter(
						(XiBlock) args[0], true);

			return ((XiReal) args[0]).intdiv((XiReal) args[1]);
		case MODULUS:
			if (args[0] instanceof XiString) {
				XiTuple tup = ((XiTuple) args[1]);
				Object[] objs = new Object[tup.length()];

				for (int i = 0; i < objs.length; i++)
					objs[i] = tup.get(i).getJavaAnalog();

				return new XiString(String.format(args[0].toString(), objs));
			}
			return ((XiInt) args[0]).mod((XiInt) args[1]);
		case EQ:
			return new XiInt(args[0].equals(args[1]));
		case NEQ:
			return new XiInt(!args[0].equals(args[1]));
		case GREATER:
			if (args[0] instanceof XiSet && args[1] instanceof XiSet)
				return new XiInt((!args[0].equals(args[1]))
						&& ((XiSet) args[0]).superset((XiSet) args[1]));
			return new XiInt(args[0].compareTo(args[1]) > 0);
		case LESS:
			if (args[0] instanceof XiSet && args[1] instanceof XiSet)
				return new XiInt((!args[0].equals(args[1]))
						&& ((XiSet) args[0]).subset((XiSet) args[1]));
			return new XiInt(args[0].compareTo(args[1]) < 0);
		case GREATER_EQ:
			if (args[0] instanceof XiSet && args[1] instanceof XiSet)
				return new XiInt(((XiSet) args[0]).superset((XiSet) args[1]));
			return new XiInt(args[0].compareTo(args[1]) >= 0);
		case LESS_EQ:
			if (args[0] instanceof XiSet && args[1] instanceof XiSet)
				return new XiInt(((XiSet) args[0]).subset((XiSet) args[1]));
			return new XiInt(args[0].compareTo(args[1]) <= 0);
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
		case FIND:
			return ((ListWrapper) args[0]).find(args[1]);
		case IN:
			if (args[1] instanceof XiDictionary)
				return new XiInt(((XiDictionary) args[1]).containsKey(args[0]));
			return new XiInt(((CollectionWrapper<?>) args[1]).contains(args[0]));
		case AT:
			if (args[0] instanceof XiDictionary)
				return ((XiDictionary) args[0]).get(args[1]);
			return ((ListWrapper) args[0]).get((XiInt) args[1]);
		case MAP: {
			if (args[0] instanceof XiLambda)
				return ((CollectionWrapper<?>) args[1]).map((XiLambda) args[0],
						false, globals);

			XiBlock body = (XiBlock) args[0];
			body.addVars(globals);
			return ((CollectionWrapper<?>) args[1]).map(body, false);
		}
		case DEEPMAP: {
			if (args[0] instanceof XiLambda)
				return ((CollectionWrapper<?>) args[1]).map((XiLambda) args[0],
						true, globals);

			XiBlock body = (XiBlock) args[0];
			body.addVars(globals);
			return ((CollectionWrapper<?>) args[1]).map(body, true);
		}
		case RANGE:
			if (args[0] instanceof XiList)
				return ListWrapper.range((XiTuple) args[0]);
			return new XiList(((XiInt) args[0]).val());
		case SUM:
			return ((CollectionWrapper<?>) args[0]).sum();
		case RAND:
			if (args[0] instanceof ListWrapper)
				return ((ListWrapper) args[0]).rnd();
			return new XiInt((new Random()).nextInt(((XiInt) args[0]).val()));
		case SORT:
			return ((ListWrapper) args[0]).sort();
		case CUT:
			if (args[1] instanceof XiInt)
				return ((ListWrapper) args[0]).cut((XiInt) args[1]);
			if (args[1] instanceof XiString)
				return ((XiString) args[0]).cut((XiString) args[1]);
			return ((ListWrapper) args[0]).cut((XiTuple) args[1]);
		case DEL:
			((ListWrapper) args[0]).del(args[1]);
			return XiNull.instance();
		case PUT:
			if (args[0] instanceof ListWrapper)
				((ListWrapper) args[0]).put((XiInt) args[1], args[2]);
			else
				((XiDictionary) args[0]).put(args[1], args[2]);
			return XiNull.instance();
		case REPLACE:
			return ((XiString) args[0]).replace((XiString) args[1],
					(XiString) args[2]);
		case FOR: {
			String id = ((XiAttribute) args[0]).toString();
			CollectionWrapper<?> col = (CollectionWrapper<?>) args[1];
			XiBlock body = (XiBlock) args[2];
			body.addVars(globals);
			for (DataType data : col) {
				body.updateLocal(new XiVar(id, data));
				try {
					body.evaluate();
				} catch (BreakException be) {
					break;
				} catch (ContinueException ce) {
					continue;
				}
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
			for (int i = 0; i < n; i++) {
				try {
					body.evaluate();
				} catch (BreakException be) {
					break;
				} catch (ContinueException ce) {
					continue;
				}
			}
			globals.addAll(body.locals());
			return XiNull.instance();
		}
		case WHILE: {
			XiBlock cond = (XiBlock) args[0];
			XiBlock body = (XiBlock) args[1];
			cond.addVars(globals);
			body.addVars(globals);
			while (!cond.evaluate().isEmpty()) {
				try {
					body.evaluate();
				} catch (BreakException be) {
					break;
				} catch (ContinueException ce) {
					continue;
				}
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
				try {
					body.evaluate();
				} catch (BreakException be) {
					break;
				} catch (ContinueException ce) {
					continue;
				}
				cond.addVars(body.locals());
			} while (!cond.evaluate().isEmpty());
			globals.addAll(body.locals());
			return XiNull.instance();
		}
		case LOOP: {
			CollectionWrapper<?> col = (CollectionWrapper<?>) args[0];
			XiBlock body = (XiBlock) args[1];
			body.addVars(globals);
			int index = 0;
			for (DataType data : col) {
				body.updateLocal(new XiVar(".", data));
				body.updateLocal(new XiVar("_", new XiInt(index)));
				try {
					body.evaluate();
				} catch (BreakException be) {
					break;
				} catch (ContinueException ce) {
					continue;
				}
				index++;
			}
			globals.addAll(body.locals());
			return XiNull.instance();
		}
		case EVAL: {
			if (args[0] instanceof XiString)
				return evaluate(new DataType[] { new XiBlock("{"
						+ ((XiString) args[0]).toString() + "}") }, globals);

			XiBlock block = (XiBlock) args[0];
			block.addVars(globals);
			try {
				return block.evaluate();
			} finally {
				globals.addAll(block.locals());
			}
		}
		case EXEC: {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						Runtime.getRuntime().exec(args[0].toString())
								.getInputStream()));

				List<DataType> out = new ArrayList<DataType>();

				String line = null;
				while ((line = br.readLine()) != null)
					out.add(new XiString(line));

				return new XiList(out);
			} catch (IOException ioe) {
				throw new RuntimeException("exec failed");
			}
		}
		case APPLY: {
			try {
				return ((XiLambda) args[0])
						.evaluate((XiTuple) args[1], globals);
			} catch (ReturnException re) {
				return re.data();
			}
		}
		case STR:
			return new XiString(args[0].toString());
		case INT:
			return XiInt.parse(args[0].toString());
		case FLOAT:
			return XiFloat.parse(args[0].toString());
		case LONG:
			return XiLong.parse(args[0].toString());
		case LIST:
			return ((CollectionWrapper<?>) args[0]).asList();
		case SET:
			if (args[0] instanceof XiDictionary)
				return ((XiDictionary) args[0]).keySet();
			return ((CollectionWrapper<?>) args[0]).asSet();
		case TUPLE:
			return ((CollectionWrapper<?>) args[0]).asTuple();
		case DICT:
			return new XiDictionary((CollectionWrapper<?>) args[0]);
		case CMPLX:
			double re = ((XiReal) args[0]).num().doubleValue();
			double im = ((XiReal) args[1]).num().doubleValue();
			return new XiComplex(re, im);
		case FUNC:
			return new XiFunc((XiTuple) args[0], (XiBlock) args[1]);
		case LAMBDA:
			return new XiLambda((XiTuple) args[0], (XiBlock) args[1]);
		case FILE:
			return new XiFile((XiString) args[0]);
		case PRINT:
			XiSys.instance().stdout().print(args[0]);
			return XiNull.instance();
		case PRINTLN:
			XiSys.instance().stdout().println(args[0]);
			return XiNull.instance();
		case PRINTF: {
			XiTuple tup = ((XiTuple) args[1]);
			Object[] objs = new Object[tup.length()];

			for (int i = 0; i < objs.length; i++)
				objs[i] = tup.get(i).getJavaAnalog();

			XiSys.instance().stdout().printf(args[0].toString(), objs);
			return XiNull.instance();
		}
		case INPUT:
			return XiSys.instance().stdin().readln();
		case SLEEP:
			try {
				Thread.sleep(((XiInt) args[0]).val());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return XiNull.instance();
		case LEN:
			return new XiInt(args[0].length());
		case ASSERT:
			if (args[0].isEmpty())
				throw new RuntimeException("assertion failed");
			return XiNull.instance();
		case GETATTR:
			return args[0].getAttribute((XiAttribute) args[1]);
		case SETATTR:
			args[0].setAttribute((XiAttribute) args[1], args[2]);
			return XiNull.instance();
		default:
			throw new RuntimeException("Internal error");
		}
	}

	public static boolean idExists(String id) {
		return ids.containsKey(id);
	}

	public static IntrinsicOperation parse(String id) {
		IntrinsicOperation op = ids.get(id);
		if (op == null)
			throw new IllegalArgumentException("Invalid identifier: " + id);
		return op;
	}

	@Override
	public String toString() {
		return id;
	}

}