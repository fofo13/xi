package org.xiscript.xi.nodes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.xiscript.xi.core.Parser;
import org.xiscript.xi.core.SyntaxTree;
import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiDictionary;
import org.xiscript.xi.datatypes.collections.CollectionWrapper;
import org.xiscript.xi.datatypes.collections.XiList;
import org.xiscript.xi.datatypes.collections.XiSet;
import org.xiscript.xi.datatypes.collections.XiTuple;

public class CollectionNode extends DataNode<CollectionWrapper<?>> {

	private static final String LIST_TAG = ":list:";
	private static final String TUPLE_TAG = ":tuple:";
	private static final String SET_TAG = ":set:";
	private static final String DICT_TAG = ":dict:";

	private String expr;
	private char start;

	public CollectionNode(String expr) {
		super(null);
		this.expr = expr.substring(1, expr.length() - 1).trim();
		start = expr.charAt(0);
	}

	@Override
	public DataType evaluate(VariableCache cache) {
		if (start == Parser.TUPLE_START) {
			return createTuple(expr, cache);
		} else {
			if (expr.startsWith(LIST_TAG))
				return createList(expr.substring(LIST_TAG.length()), cache);
			if (expr.startsWith(TUPLE_TAG))
				return createTuple(expr.substring(TUPLE_TAG.length()), cache);
			if (expr.startsWith(SET_TAG))
				return createSet(expr.substring(SET_TAG.length()), cache);
			if (expr.startsWith(DICT_TAG))
				return createDict(expr.substring(DICT_TAG.length()), cache);
			else
				return createList(expr, cache);
		}
	}

	private static void parseAndAdd(String expr, Collection<DataType> col,
			VariableCache cache) {
		Queue<Node> nodes = Parser.genNodeQueue(expr);

		while (!nodes.isEmpty()) {
			SyntaxTree tree = new SyntaxTree(nodes, cache);
			col.add(tree.evaluate(cache));
			nodes = tree.nodes();
		}
	}

	private static XiList createList(String expr, VariableCache cache) {
		List<DataType> result = new ArrayList<DataType>();
		parseAndAdd(expr, result, cache);
		return new XiList(result);
	}

	private static XiTuple createTuple(String expr, VariableCache cache) {
		List<DataType> result = new ArrayList<DataType>();
		parseAndAdd(expr, result, cache);
		return new XiTuple(result);
	}

	private static XiSet createSet(String expr, VariableCache cache) {
		Set<DataType> result = new HashSet<DataType>();
		parseAndAdd(expr, result, cache);
		return new XiSet(result);
	}

	private static XiDictionary createDict(String expr, VariableCache cache) {
		Queue<Node> nodes = Parser.genNodeQueue(expr);
		Map<DataType, DataType> result = new HashMap<DataType, DataType>();

		while (!nodes.isEmpty()) {
			SyntaxTree tree = new SyntaxTree(nodes, cache);
			DataType key = tree.evaluate(cache);
			nodes = tree.nodes();

			tree = new SyntaxTree(nodes, cache);
			DataType value = tree.evaluate(cache);
			nodes = tree.nodes();

			result.put(key, value);
		}

		return new XiDictionary(result);
	}

}