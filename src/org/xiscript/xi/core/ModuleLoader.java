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

	private static final String[] files = { "const.xi", "listutils.xi",
			"math.xi", "stat.xi", "stdlib.xi", "sys.xi", "vecmath.xi" };

	static {
		List<InputStream> dir = new ArrayList<InputStream>(files.length);

		for (String file : files)
			dir.add(XiEnvironment.class.getResourceAsStream(libpath + file));

		for (int i = 0; i < files.length; i++) {
			XiEnvironment sub = null;
			try {
				sub = new XiEnvironment(dir.get(i), false);
				sub.run();
			} catch (FileNotFoundException fnfe) {
				ErrorHandler.invokeError(ErrorType.INTERNAL);
			}
			stdlib.put(files[i].split("\\.")[0], new XiModule(sub.globals()));
			sub.close();
		}

		addSpecialVars();
	}

	public static XiModule load(String name) {
		if (stdlib.containsKey(name)) {
			return stdlib.get(name);
		}

		XiEnvironment env = null;
		try {
			name = name.replace(".", "/") + ".xi";
			File file = new File(name);
			env = new XiEnvironment(file);
			env.run();
			return new XiModule(env.globals());
		} catch (FileNotFoundException fnfe) {
			ErrorHandler.invokeError(ErrorType.FILE_NOT_FOUND, name);
			return null;
		} finally {
			env.close();
		}
	}

	private static void addSpecialVars() {
		stdlib.get("sys").addVar("sys", XiSys.instance());

		stdlib.get("math").addVar("sin", XiMath.sin);
		stdlib.get("math").addVar("cos", XiMath.cos);
		stdlib.get("math").addVar("tan", XiMath.tan);
		stdlib.get("math").addVar("cot", XiMath.cot);
		stdlib.get("math").addVar("sec", XiMath.sec);
		stdlib.get("math").addVar("csc", XiMath.csc);
		stdlib.get("math").addVar("asin", XiMath.asin);
		stdlib.get("math").addVar("acos", XiMath.acos);
		stdlib.get("math").addVar("atan", XiMath.atan);
		stdlib.get("math").addVar("acot", XiMath.acot);
		stdlib.get("math").addVar("asec", XiMath.asec);
		stdlib.get("math").addVar("acsc", XiMath.acsc);
		stdlib.get("math").addVar("sinh", XiMath.sinh);
		stdlib.get("math").addVar("cosh", XiMath.cosh);
		stdlib.get("math").addVar("tanh", XiMath.tanh);
		stdlib.get("math").addVar("coth", XiMath.coth);
		stdlib.get("math").addVar("sech", XiMath.sech);
		stdlib.get("math").addVar("csch", XiMath.csch);
		stdlib.get("math").addVar("log", XiMath.log);
		stdlib.get("math").addVar("log10", XiMath.log10);
		stdlib.get("math").addVar("floor", XiMath.floor);
		stdlib.get("math").addVar("ceil", XiMath.ceil);
	}

}