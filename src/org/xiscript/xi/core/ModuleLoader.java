package org.xiscript.xi.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xiscript.xi.datatypes.XiModule;
import org.xiscript.xi.datatypes.XiSys;
import org.xiscript.xi.datatypes.numeric.XiMath;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;

public class ModuleLoader {

	protected static final Map<String, XiModule> stdlib = new HashMap<String, XiModule>();

	private static final String libpath = "/org/xiscript/xi/modules/";

	private static final String[] files = { "const", "listutils", "math",
			"stat", "stdlib", "sys", "vecmath" };

	private static final String EXT = ".xi";

	static {
		List<InputStream> dir = new ArrayList<InputStream>(files.length);

		for (String file : files)
			dir.add(XiEnvironment.class.getResourceAsStream(libpath + file
					+ EXT));

		for (int i = 0; i < files.length; i++) {
			XiEnvironment sub = null;
			try {
				sub = new XiEnvironment(dir.get(i), false);
				sub.run();
			} catch (FileNotFoundException fnfe) {
				ErrorHandler.invokeError(ErrorType.INTERNAL);
			}
			stdlib.put(files[i], new XiModule(sub.globals()));
		}

		addSpecialVars();
	}

	public static XiModule load(String name) {
		if (stdlib.containsKey(name)) {
			return stdlib.get(name);
		}

		XiEnvironment env = null;
		try {
			name = name.replace(".", "/") + EXT;
			File file = new File(name);
			env = new XiEnvironment(file);
			env.run();
			return new XiModule(env.globals());
		} catch (FileNotFoundException fnfe) {
			ErrorHandler.invokeError(ErrorType.FILE_NOT_FOUND, name);
			return null;
		}
	}

	private static void addSpecialVars() {
		stdlib.get("sys").addVar("sys", XiSys.instance());

		XiModule math = stdlib.get("math");
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
	}

}