package org.xiscript.xi.operations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import org.xiscript.xi.core.ModuleLoader;
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
import org.xiscript.xi.datatypes.functional.HiddenLambda;
import org.xiscript.xi.datatypes.functional.XiBlock;
import org.xiscript.xi.datatypes.functional.XiFunc;
import org.xiscript.xi.datatypes.functional.XiLambda;
import org.xiscript.xi.datatypes.io.XiFile;
import org.xiscript.xi.datatypes.iterable.RangeGenerator;
import org.xiscript.xi.datatypes.iterable.XiGenerator;
import org.xiscript.xi.datatypes.iterable.XiIterable;
import org.xiscript.xi.datatypes.numeric.XiComplex;
import org.xiscript.xi.datatypes.numeric.XiFloat;
import org.xiscript.xi.datatypes.numeric.XiInt;
import org.xiscript.xi.datatypes.numeric.XiLong;
import org.xiscript.xi.datatypes.numeric.XiNum;
import org.xiscript.xi.datatypes.numeric.XiReal;
import org.xiscript.xi.exceptions.BreakException;
import org.xiscript.xi.exceptions.ContinueException;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;
import org.xiscript.xi.exceptions.ReturnException;
import org.xiscript.xi.util.TimerManager;

public enum BuiltInOperation implements Operation {

	NULL("null", 0),

	NOT("!", 1), BITNOT("~", 1), ABS("abs", 1), ADD("+", 2), ADDALL("+:", -1), SUBTRACT(
			"-", 2), MULTIPLY("*", 2), MULTALL("*:", -1), DIVIDE("/", 2), INTDIV(
			"//", 2), MODULUS("%", 2), EQ("==", 2), NEQ("!=", 2), GREATER(">",
			2), LESS("<", 2), GREATER_EQ(">=", 2), LESS_EQ("<=", 2), AND("&", 2), OR(
			"|", 2), XOR("^", 2), RSHIFT(">>", 2), LSHIFT("<<", 2), POW("**", 2),

	FIND("find", 2), IN("in", 2), MAP("@", 2), DEEPMAP("@@", 2), RANGE(",", 1), SUM(
			"$", 1), RAND("rnd", 1), SORT("sort", 1), CSORT("csort", 2), CUT(
			"cut", 2), DEL("del", 1), REPLACE("replace", 3), REMOVE("remove", 2), SPLIT(
			"<>", 2), JOIN("><", 2),

	FOR("for", 3), IF("if", 3), DO("do", 2), WHILE("while", 2), DOWHILE(
			"dowhile", 2), LOOP("loop", 2),

	EVAL("eval", 1), EXEC("exec", 1),

	STR("str", 1), INT("int", 1), FLOAT("float", 1), LONG("long", 1), LIST(
			"list", 1), SET("set", 1), TUPLE("tuple", 1), DICT("dict", 1), CMPLX(
			"cmplx", 2), FUNC("func", 2), LAMBDA("lambda", 2), FILE("file", 1),

	PRINT("print", 1), PRINTLN("println", 1), PRINTF("printf", 2),

	INPUT("input", 0),

	SLEEP("sleep", 1),

	HASH("hash", 1), LEN("len", 1),

	ASSERT("assert", 1),

	TYPE("type", 1),

	IMPORT("import", 1), LOAD("load", 1),

	GETATTR("=>", 2), SETATTR("<=", 3),

	RETURN("return", 1), CONTINUE("continue", 0), BREAK("break", 0), EXIT(
			"exit", 1),

	TIC("tic", 1), TOC("toc", 1);

	private static final Map<String, BuiltInOperation> ids = new HashMap<String, BuiltInOperation>(
			values().length);

	private static final Random RND = new Random();

	private static final VariableCache EMPTY_CACHE = new VariableCache();

	private static final Pattern SPLUS = Pattern.compile("\\s+");

	static {
		for (BuiltInOperation op : values())
			ids.put(op.id, op);
	}

	private final String id;
	private final int numArgs;
	private final XiLambda asLambda;

	private BuiltInOperation(String id, int numArgs) {
		this.id = id;
		this.numArgs = numArgs;

		asLambda = genLambda();
	}

	public String id() {
		return id;
	}

	@Override
	public int numArgs() {
		return numArgs;
	}

	@Override
	public DataType evaluate(final DataType[] args, final VariableCache globals) {
		switch (this) {
		case NULL:
			return XiNull.instance();
		case NOT:
			return new XiInt(args[0].isEmpty());
		case BITNOT:
			if (args[0] instanceof ListWrapper)
				return ((ListWrapper) args[0]).zip();
			if (args[0] instanceof XiGenerator) {
				DataType d = ((XiGenerator) args[0]).next();
				return (d == null) ? XiNull.instance() : d;
			}
			if (args[0] instanceof XiLambda) {
				XiLambda lambda = (XiLambda) args[0];
				if (lambda.length() == 0)
					return lambda.evaluate(new DataType[0], globals);
			}
			if (args[0] instanceof XiBlock) {
				XiBlock block = (XiBlock) args[0];
				block.addVars(globals);
				try {
					return block.evaluate();
				} finally {
					globals.putAll(block.locals());
				}
			}
			return new XiInt(~((XiInt) args[0]).val());
		case ABS:
			if (args[0] instanceof ListWrapper)
				return ((ListWrapper) args[0]).abs();
			return ((XiNum) args[0]).abs();
		case ADD:
			if (args[0] instanceof XiString)
				return new XiString(args[0].toString() + args[1].toString());
			if (args[0] instanceof CollectionWrapper) {
				((CollectionWrapper<?>) args[0]).add(args[1]);
				return args[0];
			}
			if (args[1] instanceof XiString)
				return new XiString(args[0].toString() + args[1].toString());
			return ((XiNum) args[0]).add((XiNum) args[1]);
		case ADDALL: {
			DataType d = args[0];

			if (args.length == 1)
				return ((XiIterable) d).sum();

			for (int i = 1; i < args.length; i++)
				d = ADD.evaluate(d, args[i]);
			return d;
		}
		case SUBTRACT:
			if (args[0] instanceof ListWrapper) {
				((ListWrapper) args[0]).remove(args[1]);
				return args[0];
			}
			if (args[0] instanceof XiSet && args[1] instanceof XiSet)
				return ((XiSet) args[0]).difference((XiSet) args[1], false);
			return ((XiNum) args[0]).sub((XiNum) args[1]);
		case MULTIPLY:
			if (args[0] instanceof ListWrapper)
				return ((ListWrapper) args[0]).mul((XiInt) args[1]);
			return ((XiNum) args[0]).mul((XiNum) args[1]);
		case MULTALL: {
			DataType d = args[0];

			if (args.length == 1) {
				Iterator<DataType> iter = ((XiIterable) d).iterator();

				DataType mul = iter.next();
				while (iter.hasNext())
					mul = MULTIPLY.evaluate(mul, iter.next());

				return mul;
			}

			for (int i = 0; i < args.length; i++)
				d = MULTIPLY.evaluate(d, args[i]);
			return d;
		}
		case DIVIDE:
			if (args[0] instanceof XiBlock)
				return ((XiIterable) args[1]).filter((XiBlock) args[0]);
			if (args[0] instanceof XiLambda)
				return ((XiIterable) args[1]).filter((XiLambda) args[0],
						globals);
			return ((XiNum) args[0]).div((XiNum) args[1]);
		case INTDIV:
			if (args[0] instanceof XiBlock)
				return ((CollectionWrapper<?>) args[1]).filter(
						(XiBlock) args[0], true);

			return ((XiReal<?>) args[0]).intdiv((XiReal<?>) args[1]);
		case MODULUS:
			if (args[0] instanceof XiString) {
				XiTuple tup = ((XiTuple) args[1]);
				Object[] objs = new Object[tup.length()];

				for (int i = 0; i < objs.length; i++)
					objs[i] = tup.get(i).getJavaAnalog();

				try {
					return new XiString(String.format(args[0].toString(), objs));
				} catch (IllegalFormatException ife) {
					ErrorHandler.invokeError(ErrorType.STRING_FORMAT, args[0],
							tup);
				}
			}
			if (args[0] instanceof ListWrapper)
				return ((ListWrapper) args[0]).mod(((XiInt) args[1]).val());
			return ((XiInt) args[0]).mod((XiInt) args[1]);
		case EQ:
			return new XiInt(args[0].equals(args[1]));
		case NEQ:
			return new XiInt(!args[0].equals(args[1]));
		case GREATER:
			if (args[0] instanceof XiSet && args[1] instanceof XiSet)
				return new XiInt((!args[0].equals(args[1]))
						&& ((XiSet) args[0]).isSupersetOf((XiSet) args[1]));
			return new XiInt(args[0].compareTo(args[1]) > 0);
		case LESS:
			if (args[0] instanceof XiSet && args[1] instanceof XiSet)
				return new XiInt((!args[0].equals(args[1]))
						&& ((XiSet) args[0]).isSubsetOf((XiSet) args[1]));
			return new XiInt(args[0].compareTo(args[1]) < 0);
		case GREATER_EQ:
			if (args[0] instanceof XiSet && args[1] instanceof XiSet)
				return new XiInt(
						((XiSet) args[0]).isSupersetOf((XiSet) args[1]));
			return new XiInt(args[0].compareTo(args[1]) >= 0);
		case LESS_EQ:
			if (args[0] instanceof XiSet && args[1] instanceof XiSet)
				return new XiInt(((XiSet) args[0]).isSubsetOf((XiSet) args[1]));
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
		case FIND:
			return ((ListWrapper) args[0]).find(args[1]);
		case IN:
			if (args[1] instanceof XiDictionary)
				return new XiInt(((XiDictionary) args[1]).containsKey(args[0]));
			return new XiInt(((CollectionWrapper<?>) args[1]).contains(args[0]));
		case MAP: {
			if (args[0] instanceof XiLambda)
				return ((XiIterable) args[1]).map((XiLambda) args[0], globals);

			XiBlock body = (XiBlock) args[0];
			body.addVars(globals);
			return ((XiIterable) args[1]).map(body);
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
			if (args[0] instanceof XiTuple) {
				XiTuple t = (XiTuple) args[0];
				switch (t.length()) {
				case 1:
					return new RangeGenerator(((XiInt) t.get(0)).val());
				case 2:
					return new RangeGenerator(((XiInt) t.get(0)).val(),
							((XiInt) t.get(1)).val());
				case 3:
					return new RangeGenerator(((XiInt) t.get(0)).val(),
							((XiInt) t.get(1)).val(), ((XiInt) t.get(2)).val());
				default:
					ErrorHandler.invokeError(ErrorType.ARGUMENT, this);
				}
			}
			return new RangeGenerator(((XiInt) args[0]).val());
		case SUM:
			return ((XiIterable) args[0]).sum();
		case RAND:
			if (args[0] instanceof ListWrapper)
				return ((ListWrapper) args[0]).rnd();
			if (args[0] instanceof XiFloat)
				return new XiFloat(RND.nextDouble() * ((XiFloat) args[0]).num());
			return new XiInt(RND.nextInt(((XiInt) args[0]).val()));
		case SORT:
			((ListWrapper) args[0]).sort();
			return args[0];
		case CSORT: {
			final XiLambda key = (args[0] instanceof XiLambda) ? (XiLambda) args[0]
					: new XiLambda(new String[] { XiVar.SPEC_VAR.id() },
							(XiBlock) args[0]);

			Comparator<DataType> cmp = new Comparator<DataType>() {
				@Override
				public int compare(DataType d1, DataType d2) {
					switch (key.length()) {
					case 1:
						return key.evaluate(new DataType[] { d1 }, globals)
								.compareTo(
										key.evaluate(new DataType[] { d2 },
												globals));
					case 2:
						return ((XiInt) key.evaluate(new DataType[] { d1, d2 },
								globals)).val();
					default:
						ErrorHandler
								.invokeError(ErrorType.ARGUMENT, CSORT.id());
					}
					return 0;
				}
			};

			((ListWrapper) args[1]).sort(cmp);
			return args[1];
		}
		case CUT:
			if (args[1] instanceof XiInt)
				return ((ListWrapper) args[0]).cut((XiInt) args[1]);
			if (args[1] instanceof XiString)
				return ((XiString) args[0]).cut((XiString) args[1]);
			return ((ListWrapper) args[0]).cut((XiTuple) args[1]);
		case DEL: {
			String id = ((XiAttribute) args[0]).toString();
			if (!globals.containsId(id))
				ErrorHandler.invokeError(ErrorType.IDNETIFIER_NOT_FOUND, id);

			globals.get(id).delete();
			globals.remove(id);

			return XiNull.instance();
		}
		case REPLACE:
			return ((XiString) args[0]).replace((XiString) args[1],
					(XiString) args[2]);
		case REMOVE:
			((ListWrapper) args[0]).remove(((XiInt) args[1]).val());
			return args[0];
		case SPLIT:
			if (args.length == 2) {
				return ((XiString) args[1]).useToSplit((XiString) args[0]);
			} else {
				String[] split = SPLUS.split(((XiString) args[0]).toString());
				List<DataType> list = new ArrayList<DataType>(split.length);
				for (String s : split)
					list.add(new XiString(s));

				return new XiList(list);
			}
		case JOIN: {
			StringBuilder sb = new StringBuilder();
			XiIterable iter = (XiIterable) args[0];
			String delim = ((XiString) args[1]).toString();

			boolean first = true;
			for (DataType d : iter) {
				if (!first)
					sb.append(delim);
				sb.append(d.toString());
				first = false;
			}

			return new XiString(sb.toString());
		}
		case FOR: {
			String id = null;
			XiTuple t = null;

			boolean packed = args[0] instanceof XiTuple;
			if (packed)
				t = (XiTuple) args[0];
			else
				id = ((XiAttribute) args[0]).toString();

			XiIterable col = (XiIterable) args[1];
			XiBlock body = (XiBlock) args[2];
			body.addVars(globals);
			for (DataType data : col) {
				if (packed) {
					ListWrapper lw = (ListWrapper) data;

					if (lw.length() != t.length())
						ErrorHandler.invokeError(ErrorType.UNPACKING_ERROR,
								lw.toString());

					for (int i = 0; i < t.length(); i++) {
						String subid = ((XiAttribute) t.get(i)).toString();
						body.updateLocal(new XiVar(subid, true), lw.get(i));
					}
				} else {
					body.updateLocal(new XiVar(id, true), data);
				}

				try {
					body.evaluate();
				} catch (BreakException be) {
					break;
				} catch (ContinueException ce) {
					continue;
				}
			}
			globals.putAll(body.locals());
			return XiNull.instance();
		}
		case IF: {
			boolean cond = !args[0].isEmpty();
			int nargs = args.length;

			XiBlock body = null;

			if (cond) {
				body = (XiBlock) args[1];
			} else if (nargs == 3) {
				body = (XiBlock) args[2];
			}

			if (body != null) {
				body.addVars(globals);
				body.evaluate();
				globals.putAll(body.locals());
			}

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
			globals.putAll(body.locals());
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
			globals.putAll(body.locals());
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
			globals.putAll(body.locals());
			return XiNull.instance();
		}
		case LOOP: {
			XiIterable col = (XiIterable) args[0];
			XiBlock body = (XiBlock) args[1];
			body.addVars(globals);
			int index = 0;
			for (DataType data : col) {
				body.updateLocal(XiVar.SPEC_VAR, data);
				body.updateLocal(XiVar.INDEX_VAR, new XiInt(index));
				try {
					body.evaluate();
				} catch (BreakException be) {
					break;
				} catch (ContinueException ce) {
					continue;
				}
				index++;
			}
			globals.putAll(body.locals());
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
				globals.putAll(block.locals());
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
				ErrorHandler.invokeError(ErrorType.EXEC_ERROR,
						args[0].toString());
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
			if (args[0] instanceof XiGenerator)
				return new XiList((XiGenerator) args[0]);
			return ((CollectionWrapper<?>) args[0]).asList();
		case SET:
			if (args[0] instanceof XiDictionary)
				return ((XiDictionary) args[0]).itemSet();
			if (args[0] instanceof XiGenerator)
				return new XiSet((XiGenerator) args[0]);
			return ((CollectionWrapper<?>) args[0]).asSet();
		case TUPLE:
			if (args[0] instanceof XiGenerator)
				return new XiTuple((XiGenerator) args[0]);
			return ((CollectionWrapper<?>) args[0]).asTuple();
		case DICT:
			return new XiDictionary((CollectionWrapper<?>) args[0]);
		case CMPLX:
			double re = ((XiReal<?>) args[0]).num().doubleValue();
			double im = ((XiReal<?>) args[1]).num().doubleValue();
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
			XiSys.instance().stdout().println(args.length == 1 ? args[0] : "");
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
				ErrorHandler.invokeError(ErrorType.INTERRUPTION);
			}
			return XiNull.instance();
		case LEN:
			return new XiInt(args[0].length());
		case ASSERT:
			if (args[0].isEmpty())
				ErrorHandler.invokeError(ErrorType.ASSERT_ERROR);
			return XiNull.instance();
		case TYPE:
			return args[0].type();
		case IMPORT:
			return ModuleLoader.load(args[0].toString());
		case LOAD:
			globals.putAll(ModuleLoader.load(args[0].toString()).contents());
			return XiNull.instance();
		case GETATTR: {
			if (!(args[1] instanceof XiAttribute)) {
				if (args[0] instanceof XiLambda)
					return ((XiLambda) args[0]).evaluate((XiTuple) args[1],
							globals);
				if (args[0] instanceof XiDictionary)
					return ((XiDictionary) args[0]).get(args[1]);
				if (args[1] instanceof XiInt)
					return ((ListWrapper) args[0]).get((XiInt) args[1]);
				if (args[1] instanceof XiTuple)
					return ((ListWrapper) args[0]).get((XiTuple) args[1]);
				if (args[1] instanceof XiList)
					return ((ListWrapper) args[0]).get((XiList) args[1]);
			}
			return args[0].getAttribute((XiAttribute) args[1]);
		}
		case SETATTR: {
			if (!(args[1] instanceof XiAttribute)) {
				if (args[0] instanceof ListWrapper)
					((ListWrapper) args[0]).put((XiInt) args[1], args[2]);
				else
					((XiDictionary) args[0]).put(args[1], args[2]);
			} else
				args[0].setAttribute((XiAttribute) args[1], args[2]);
			return XiNull.instance();
		}
		case RETURN:
			throw new ReturnException(args[0]);
		case CONTINUE:
			throw new ContinueException();
		case BREAK:
			throw new BreakException();
		case EXIT:
			System.exit(((XiInt) args[0]).val());
		case TIC:
			TimerManager.addTimer(((XiInt) args[0]).val());
			return XiNull.instance();
		case TOC:
			return new XiLong(TimerManager.readTimer(((XiInt) args[0]).val()));
		default:
			ErrorHandler.invokeError(ErrorType.INTERNAL);
			return null;
		}
	}

	public DataType evaluate(DataType... dataTypes) {
		return evaluate(dataTypes, EMPTY_CACHE);
	}

	private XiLambda genLambda() {
		final BuiltInOperation op = this;
		return new HiddenLambda(numArgs) {
			@Override
			public DataType evaluate(DataType... args) {
				return op.evaluate(args, new VariableCache());
			}
		};
	}

	@Override
	public XiLambda asLambda() {
		return asLambda;
	}

	public static boolean idExists(String id) {
		return ids.containsKey(id);
	}

	public static BuiltInOperation parse(String id) {
		if (!ids.containsKey(id))
			ErrorHandler.invokeError(ErrorType.IDNETIFIER_NOT_FOUND, id);
		return ids.get(id);
	}

	@Override
	public String toString() {
		return id;
	}

}