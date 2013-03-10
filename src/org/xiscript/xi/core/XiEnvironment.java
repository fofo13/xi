package org.xiscript.xi.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Queue;
import java.util.Scanner;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiNull;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.exceptions.ControlFlowException;
import org.xiscript.xi.nodes.Node;

public class XiEnvironment {

	private CharSequence source;
	private VariableCache globals;

	protected XiEnvironment(InputStream file, boolean primary)
			throws FileNotFoundException {
		globals = new VariableCache();

		if (primary) {
			globals.addAll(ModuleLoader.stdlib.get("stdlib").contents());
			globals.addAll(ModuleLoader.stdlib.get("const").contents());
		}

		source = compile(file);
	}

	public XiEnvironment(File file) throws FileNotFoundException {
		this(new FileInputStream(file), true);
	}

	public VariableCache globals() {
		return globals;
	}

	public DataType run() {
		DataType last = XiNull.instance();
		Queue<Node> nodes = Parser.genNodeQueue(source);
		try {
			while (!nodes.isEmpty()) {
				SyntaxTree tree = new SyntaxTree(nodes, globals);
				last = tree.evaluate();
				nodes = tree.nodes();
			}
		} catch (ControlFlowException cfe) {
			throw cfe;
		}
		return last;
	}

	private static CharSequence compile(InputStream file)
			throws FileNotFoundException {

		Scanner scan = new Scanner(file);
		StringBuilder source = new StringBuilder();
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			if (!line.isEmpty()) {
				source.append(line);
				source.append('\n');
			}
		}
		scan.close();

		return source;
	}

	public void delete() {
		for (XiVar v : globals)
			v.delete();
	}

}