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
import org.xiscript.xi.core.Parser;
import org.xiscript.xi.core.SyntaxTree;
import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiAttribute;
import org.xiscript.xi.datatypes.XiDict;
import org.xiscript.xi.datatypes.XiNull;
import org.xiscript.xi.datatypes.XiSys;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.datatypes.collections.ArgumentList;
import org.xiscript.xi.datatypes.collections.CollectionWrapper;
import org.xiscript.xi.datatypes.collections.ListWrapper;
import org.xiscript.xi.datatypes.collections.XiList;
import org.xiscript.xi.datatypes.collections.XiSet;
import org.xiscript.xi.datatypes.collections.XiString;
import org.xiscript.xi.datatypes.collections.XiTuple;
import org.xiscript.xi.datatypes.functional.Function;
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

	NOT("!", 1), BITNOT("~", 1), ABS("abs", 1), ADD("+", 2), ADDALL("+:", -1), SUB(
			"-", 2), MUL("*", 2), MULTALL("*:", -1), DIVIDE("/", 2), INTDIV(
			"//", 2), MOD("%", 2), EQ("==", 2), NEQ("!=", 2), GT(">", 2), LT(
			"<", 2), GE(">=", 2), LE("<=", 2), AND("&", 2), OR("|", 2), XOR(
			"^", 2), RSHIFT(">>", 2), LSHIFT("<<", 2), POW("**", 2),

	DEF("def", 3),

	FIND("find", 2), IN("in", 2), MAP("@", 2), DEEPMAP("@@", 2), RANGE("\\", 3), RAND(
			"rnd", 1), SORT("$", 2), CUT("cut", 2), DEL("del", 1), REPLACE(
			"replace", 3), REMOVE("remove", 2), SPLIT("<>", 2), JOIN("><", 2),

	FOR("for", 3), IF("if", 3), DO("do", 2), WHILE("while", 2), DOWHILE(
			"dowhile", 2), LOOP("loop", 2),

	EVAL("eval", 1), EXEC("exec", 1),

	STR("str", 1), CHR("chr", 1), INT("int", 1), FLOAT("float", 1), LONG(
			"long", 1), LIST("list", 1), SET("set", 1), TUPLE("tuple", 1), DICT(
			"dict", 1), CMPLX("cmplx", 2), LAMBDA("=>", 2), FILE("file", 2),

	PRINT("print", 1), PRINTLN("println", 1), PRINTF("printf", 2),

	INPUT("input", 0),

	SLEEP("sleep", 1),

	HASH("hash", 1), LEN("len", 1),

	ASSERT("assert", 1),

	IMPORT("import", 1), LOAD("load", 1),

	GETATTR("->", 2), SETATTR("<-", 3),

	RETURN("return", 1), CONTINUE("continue", 1), BREAK("break", 1), EXIT(
			"exit", 1),

	TIC("tic", 1), TOC("toc", 1);

	private static final Map<String, BuiltInOperation> ids = new HashMap<String, BuiltInOperation>(
			values().length);

	private static final Random RND = new Random();

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
	public DataType evaluate(final VariableCache globals,
			final DataType... args) {
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
					return lambda.evaluate(globals);
			}
			if (args[0] instanceof XiBlock) {
				XiBlock block = (XiBlock) args[0];
				block.setOuterScope(globals);
				return block.evaluate();
			}
			return new XiInt(~((XiInt) args[0]).val());
		case ABS:
			if (args[0] instanceof ListWrapper)
				return ((ListWrapper) args[0]).abs();
			return ((XiNum) args[0]).abs();
		case ADD:
			if (args[0] instanceof Function)
				return ((Function) args[0]).compose((Function) args[1]);
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
		case SUB:
			if (args.length == 1)
				return ((XiNum) args[0]).neg();
			if (args[0] instanceof ListWrapper) {
				((ListWrapper) args[0]).remove(args[1]);
				return args[0];
			}
			if (args[0] instanceof XiSet && args[1] instanceof XiSet)
				return ((XiSet) args[0]).difference((XiSet) args[1], false);
			return ((XiNum) args[0]).sub((XiNum) args[1]);
		case MUL:
			if (args[0] instanceof ListWrapper)
				return ((ListWrapper) args[0]).mul((XiInt) args[1]);
			return ((XiNum) args[0]).mul((XiNum) args[1]);
		case MULTALL: {
			DataType d = args[0];

			if (args.length == 1) {
				Iterator<DataType> iter = ((XiIterable) d).iterator();

				DataType mul = iter.next();
				while (iter.hasNext())
					mul = MUL.evaluate(mul, iter.next());

				return mul;
			}

			for (int i = 0; i < args.length; i++)
				d = MUL.evaluate(d, args[i]);
			return d;
		}
		case DIVIDE:
			if (args[0] instanceof XiBlock) {
				XiBlock block = (XiBlock) args[0];
				block.setOuterScope(globals);
				return ((XiIterable) args[1]).filter(block);
			}
			if (args[0] instanceof Function)
				return ((XiIterable) args[1]).filter((Function) args[0],
						globals);
			return ((XiNum) args[0]).div((XiNum) args[1]);
		case INTDIV:
			if (args[0] instanceof XiBlock)
				return ((CollectionWrapper<?>) args[1]).filter(
						(XiBlock) args[0], true);

			return ((XiReal<?>) args[0]).intdiv((XiReal<?>) args[1]);
		case MOD:
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
		case GT:
			if (args[0] instanceof XiSet && args[1] instanceof XiSet)
				return new XiInt((!args[0].equals(args[1]))
						&& ((XiSet) args[0]).isSupersetOf((XiSet) args[1]));
			return new XiInt(args[0].compareTo(args[1]) > 0);
		case LT:
			if (args[0] instanceof XiSet && args[1] instanceof XiSet)
				return new XiInt((!args[0].equals(args[1]))
						&& ((XiSet) args[0]).isSubsetOf((XiSet) args[1]));
			return new XiInt(args[0].compareTo(args[1]) < 0);
		case GE:
			if (args[0] instanceof XiSet && args[1] instanceof XiSet)
				return new XiInt(
						((XiSet) args[0]).isSupersetOf((XiSet) args[1]));
			return new XiInt(args[0].compareTo(args[1]) >= 0);
		case LE:
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
		case DEF:
			globals.put((XiVar) args[0], new XiFunc((ArgumentList) args[1],
					(XiBlock) args[2]));
			return XiNull.instance();
		case FIND:
			return ((ListWrapper) args[0]).find(args[1]);
		case IN:
			if (args[1] instanceof XiDict)
				return new XiInt(((XiDict) args[1]).containsKey(args[0]));
			return new XiInt(((CollectionWrapper<?>) args[1]).contains(args[0]));
		case MAP: {
			if (args[0] instanceof Function)
				return ((XiIterable) args[1]).map((Function) args[0], globals);

			XiBlock body = (XiBlock) args[0];
			body.setOuterScope(globals);
			return ((XiIterable) args[1]).map(body);
		}
		case DEEPMAP: {
			if (args[0] instanceof Function)
				return ((CollectionWrapper<?>) args[1]).map((Function) args[0],
						true, globals);

			XiBlock body = (XiBlock) args[0];
			body.setOuterScope(globals);
			return ((CollectionWrapper<?>) args[1]).map(body, true);
		}
		case RANGE:
			switch (args.length) {
			case 1:
				return new RangeGenerator(((XiInt) args[0]).val());
			case 2:
				return new RangeGenerator(((XiInt) args[0]).val(),
						((XiInt) args[1]).val());
			case 3:
				return new RangeGenerator(((XiInt) args[0]).val(),
						((XiInt) args[1]).val(), ((XiInt) args[2]).val());
			default:
				ErrorHandler.invokeError(ErrorType.ARGUMENT, this);
			}
		case RAND:
			if (args[0] instanceof ListWrapper)
				return ((ListWrapper) args[0]).rnd();
			if (args[0] instanceof XiFloat)
				return new XiFloat(RND.nextDouble() * ((XiFloat) args[0]).num());
			return new XiInt(RND.nextInt(((XiInt) args[0]).val()));
		case SORT: {
			if (args.length == 1) {
				((ListWrapper) args[0]).sort();
				return args[0];
			}

			final Function key = (args[1] instanceof Function) ? (Function) args[1]
					: new XiLambda(new XiVar[] { XiVar.SPEC_VAR },
							(XiBlock) args[1]);

			final int len = key.length();

			Comparator<DataType> cmp = new Comparator<DataType>() {
				@Override
				public int compare(DataType d1, DataType d2) {
					switch (len) {
					case 1:
						return key.evaluate(globals, d1).compareTo(
								key.evaluate(globals, d2));
					case 2:
						return ((XiInt) key.evaluate(globals, d1, d2)).val();
					default:
						ErrorHandler.invokeError(ErrorType.ARGUMENT, SORT.id());
						return 0;
					}
				}
			};

			((ListWrapper) args[0]).sort(cmp);
			return args[0];
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
			String delim = args.length == 2 ? ((XiString) args[1]).toString()
					: "";

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
			XiVar[] vars = null;

			boolean packed = (args[0] instanceof ArgumentList);

			vars = packed ? ((ArgumentList) args[0]).getJavaAnalog()
					: new XiVar[] { (XiVar) args[0] };

			for (XiVar var : vars)
				var.setTemporary(true);

			XiIterable iter = (XiIterable) args[1];
			XiBlock body = (XiBlock) args[2];
			body.setOuterScope(globals);

			for (DataType data : iter) {
				if (packed) {
					ListWrapper lw = (ListWrapper) data;

					if (lw.length() != vars.length)
						ErrorHandler.invokeError(ErrorType.UNPACKING_ERROR,
								lw.toString());

					for (int i = 0; i < vars.length; i++)
						body.updateLocal(vars[i], lw.get(i));
				} else {
					body.updateLocal(vars[0], data);
				}

				try {
					body.evaluate();
				} catch (BreakException be) {
					be.decrement();
					if (be.n() > 0)
						throw be;
					break;
				} catch (ContinueException ce) {
					ce.decrement();
					if (ce.n() > 0)
						throw ce;
					continue;
				}
			}
			return XiNull.instance();
		}
		case IF: {
			boolean cond = !args[0].isEmpty();
			int nargs = args.length;

			XiBlock body = null;
			DataType last = XiNull.instance();

			if (cond) {
				body = (XiBlock) args[1];
			} else if (nargs == 3) {
				body = (XiBlock) args[2];
			}

			if (body != null) {
				body.setOuterScope(globals);
				last = body.evaluate();
			}

			return last;
		}
		case DO: {
			int n = ((XiInt) args[0]).val();
			XiBlock body = (XiBlock) args[1];
			body.setOuterScope(globals);
			for (int i = 0; i < n; i++) {
				try {
					body.evaluate();
				} catch (BreakException be) {
					be.decrement();
					if (be.n() > 0)
						throw be;
					break;
				} catch (ContinueException ce) {
					ce.decrement();
					if (ce.n() > 0)
						throw ce;
					continue;
				}
			}
			return XiNull.instance();
		}
		case WHILE: {
			XiBlock cond = (XiBlock) args[0];
			XiBlock body = (XiBlock) args[1];
			cond.setOuterScope(globals);
			body.setOuterScope(globals);
			while (!cond.evaluate().isEmpty()) {
				try {
					body.evaluate();
				} catch (BreakException be) {
					be.decrement();
					if (be.n() > 0)
						throw be;
					break;
				} catch (ContinueException ce) {
					ce.decrement();
					if (ce.n() > 0)
						throw ce;
					continue;
				}
			}
			return XiNull.instance();
		}
		case DOWHILE: {
			XiBlock cond = (XiBlock) args[1];
			XiBlock body = (XiBlock) args[0];
			cond.setOuterScope(globals);
			body.setOuterScope(globals);
			do {
				try {
					body.evaluate();
				} catch (BreakException be) {
					be.decrement();
					if (be.n() > 0)
						throw be;
					break;
				} catch (ContinueException ce) {
					ce.decrement();
					if (ce.n() > 0)
						throw ce;
					continue;
				}
			} while (!cond.evaluate().isEmpty());
			return XiNull.instance();
		}
		case LOOP: {
			XiIterable iter = (XiIterable) args[0];
			XiBlock body = (XiBlock) args[1];
			body.setOuterScope(globals);
			int index = 0;
			for (DataType data : iter) {
				body.updateLocal(XiVar.SPEC_VAR, data);
				body.updateLocal(XiVar.INDEX_VAR, new XiInt(index++));
				try {
					body.evaluate();
				} catch (BreakException be) {
					be.decrement();
					if (be.n() > 0)
						throw be;
					break;
				} catch (ContinueException ce) {
					ce.decrement();
					if (ce.n() > 0)
						throw ce;
					continue;
				}
			}
			if (iter instanceof XiGenerator)
				((XiGenerator) iter).reset();
			return XiNull.instance();
		}
		case EVAL: {
			return (new SyntaxTree(Parser.genNodeQueue(((XiString) args[0])
					.toString()))).evaluate(globals);
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
		case CHR:
			return new XiString(Character.toString((char) ((XiInt) args[0])
					.val()));
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
			if (args[0] instanceof XiDict)
				return ((XiDict) args[0]).itemSet();
			if (args[0] instanceof XiGenerator)
				return new XiSet((XiGenerator) args[0]);
			return ((CollectionWrapper<?>) args[0]).asSet();
		case TUPLE:
			if (args[0] instanceof XiGenerator)
				return new XiTuple((XiGenerator) args[0]);
			return ((CollectionWrapper<?>) args[0]).asTuple();
		case DICT:
			return new XiDict((CollectionWrapper<?>) args[0]);
		case CMPLX:
			double re = ((XiReal<?>) args[0]).num().doubleValue();
			double im = ((XiReal<?>) args[1]).num().doubleValue();
			return new XiComplex(re, im);
		case LAMBDA:
			return new XiLambda(
					(args[0] instanceof ArgumentList) ? (ArgumentList) args[0]
							: new ArgumentList((XiVar) args[0]),
					(XiBlock) args[1]);
		case FILE:
			return new XiFile(((XiString) args[0]).toString(),
					args.length == 1 ? XiFile.DEFAULT
							: ((XiAttribute) args[1]).toString());
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
		case HASH:
			return new XiInt(args[0].hashCode());
		case LEN:
			return new XiInt(args[0].length());
		case ASSERT:
			if (args[0].isEmpty())
				ErrorHandler.invokeError(ErrorType.ASSERT_ERROR);
			return XiNull.instance();
		case IMPORT:
			return ModuleLoader.load(args[0].toString());
		case LOAD:
			globals.putAll(ModuleLoader.load(args[0].toString()).contents());
			return XiNull.instance();
		case GETATTR: {
			if (!(args[1] instanceof XiAttribute)) {
				if (args[0] instanceof XiLambda) {
					return ((XiLambda) args[0]).evaluate((XiTuple) args[1],
							globals);
				}
				if (args[0] instanceof XiDict)
					return ((XiDict) args[0]).get(args[1]);
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
					((XiDict) args[0]).put(args[1], args[2]);
			} else
				args[0].setAttribute((XiAttribute) args[1], args[2]);
			return XiNull.instance();
		}
		case RETURN:
			throw new ReturnException(args.length == 1 ? args[0]
					: XiNull.instance());
		case CONTINUE:
			throw new ContinueException(
					args.length == 1 ? ((XiInt) args[0]).val() : 1);
		case BREAK:
			throw new BreakException(args.length == 1 ? ((XiInt) args[0]).val()
					: 1);
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

	public DataType evaluate(DataType... args) {
		return evaluate(VariableCache.EMPTY_CACHE, args);
	}

	private XiLambda genLambda() {
		return new HiddenLambda(numArgs) {
			private static final long serialVersionUID = 0L;

			@Override
			public DataType evaluate(DataType... args) {
				return BuiltInOperation.this.evaluate(args);
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