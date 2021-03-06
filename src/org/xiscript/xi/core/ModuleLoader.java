package org.xiscript.xi.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.xiscript.xi.datatypes.XiModule;
import org.xiscript.xi.datatypes.XiSys;
import org.xiscript.xi.datatypes.XiType;
import org.xiscript.xi.datatypes.XiType.Type;
import org.xiscript.xi.datatypes.numeric.XiFloat;
import org.xiscript.xi.datatypes.numeric.XiMath;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;

public class ModuleLoader {

	private static final Map<String, XiModule> stdlib = new HashMap<String, XiModule>();

	private static final String PATH = "/org/xiscript/xi/modules/";

	private static final String MATH = "math";
	private static final String SYS = "sys";
	private static final String TYPES = "types";

	private static final String[] files = { "listutils", "stat", "vecmath",
			MATH, SYS, TYPES };

	private static final String EXT = ".xi";

	static {
		for (String file : files) {
			stdlib.put(file, null);
		}
	}

	public static XiModule getStandard(String name) {
		if (name.equals(MATH))
			return getStandardMath();
		if (name.equals(SYS))
			return getStandardSys();
		if (name.equals(TYPES))
			return getStandardTypes();

		if (stdlib.get(name) == null) {
			XiProgram env = new XiProgram(
					XiProgram.class.getResourceAsStream(PATH + name + EXT));
			env.run();
			stdlib.put(name, new XiModule(env.scope()));
		}

		return stdlib.get(name);
	}

	public static XiModule load(String name) {
		if (stdlib.containsKey(name)) {
			return getStandard(name);
		}

		try {
			name = name.replace(".", "/") + EXT;
			File file = new File(name);
			XiProgram env = new XiProgram(file);
			env.run();
			return new XiModule(env.scope());
		} catch (FileNotFoundException fnfe) {
			ErrorHandler.invokeError(ErrorType.FILE_NOT_FOUND, name);
			return null;
		}
	}

	private static XiModule getStandardMath() {
		XiModule math = new XiModule(24);
		math.addVar("sin", XiMath.sin);
		math.addVar("cos", XiMath.cos);
		math.addVar("tan", XiMath.tan);
		math.addVar("cot", XiMath.cot);
		math.addVar("sec", XiMath.sec);
		math.addVar("csc", XiMath.csc);
		math.addVar("asin", XiMath.asin);
		math.addVar("acos", XiMath.acos);
		math.addVar("atan", XiMath.atan);
		math.addVar("acot", XiMath.acot);
		math.addVar("asec", XiMath.asec);
		math.addVar("acsc", XiMath.acsc);
		math.addVar("sinh", XiMath.sinh);
		math.addVar("cosh", XiMath.cosh);
		math.addVar("tanh", XiMath.tanh);
		math.addVar("coth", XiMath.coth);
		math.addVar("sech", XiMath.sech);
		math.addVar("csch", XiMath.csch);
		math.addVar("log", XiMath.log);
		math.addVar("log10", XiMath.log10);
		math.addVar("floor", XiMath.floor);
		math.addVar("ceil", XiMath.ceil);
		math.addVar("PI", new XiFloat(Math.PI));
		math.addVar("E", new XiFloat(Math.E));
		return math;
	}

	private static XiModule getStandardSys() {
		XiModule sys = new XiModule(1);
		sys.addVar("sys", XiSys.instance());
		return sys;
	}

	private static XiModule getStandardTypes() {
		Type[] allTypes = XiType.Type.values();
		XiModule types = new XiModule(allTypes.length);

		for (Type type : allTypes) {
			types.addVar(type.toString().toLowerCase(), XiType.valueOf(type));
		}

		return types;
	}

}