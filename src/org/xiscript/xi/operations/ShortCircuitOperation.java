package org.xiscript.xi.operations;

import java.util.HashMap;
import java.util.Map;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;
import org.xiscript.xi.nodes.Node;
import org.xiscript.xi.nodes.shortcircuit.AndNode;
import org.xiscript.xi.nodes.shortcircuit.OrNode;
import org.xiscript.xi.nodes.shortcircuit.TernNode;

public enum ShortCircuitOperation {

	OR("||"), AND("&&"), TERN("?");

	private static final Map<String, ShortCircuitOperation> ids = new HashMap<String, ShortCircuitOperation>(
			values().length);

	static {
		for (ShortCircuitOperation op : values())
			ids.put(op.id, op);
	}

	private final String id;

	private ShortCircuitOperation(String id) {
		this.id = id;
	}

	public Node getNode(VariableCache globals) {
		switch (this) {
		case OR:
			return new OrNode(globals);
		case AND:
			return new AndNode(globals);
		case TERN:
			return new TernNode(globals);
		default:
			ErrorHandler.invokeError(ErrorType.INTERNAL);
			return null;
		}
	}

	public static boolean idExists(String id) {
		return ids.containsKey(id);
	}

	public static ShortCircuitOperation parse(String id) {
		ShortCircuitOperation op = ids.get(id);
		if (op == null)
			ErrorHandler.invokeError(ErrorType.IDNETIFIER_NOT_FOUND, id);
		return op;
	}

}