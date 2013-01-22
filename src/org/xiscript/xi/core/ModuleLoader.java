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
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;

public class ModuleLoader {

	protected static final Map<String, XiModule> stdlib = new HashMap<String, XiModule>();

	private static final String libpath = "/org/xiscript/xi/modules/";

	private static final String[] files = { "const.xi", "listutils.xi",
			"math.xi", "operator.xi", "stat.xi", "stdlib.xi", "sys.xi",
			"vecmath.xi" };

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

		stdlib.get("sys").addVar("sys", XiSys.instance());
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

}