package org.xiscript.xi.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.xiscript.xi.core.Parser;
import org.xiscript.xi.core.SyntaxTree;
import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.collections.CollectionWrapper;
import org.xiscript.xi.datatypes.collections.XiList;
import org.xiscript.xi.datatypes.collections.XiTuple;

public class CollectionNode extends DataNode<CollectionWrapper<?>> {

	private String expr;

	public CollectionNode(String exp) {
		super(null);
		this.expr = exp;
	}

	@Override
	public DataType evaluate(VariableCache cache) {
		List<DataType> result = new ArrayList<DataType>();
		Queue<Node> nodes = Parser.genNodeQueue(expr.substring(1,
				expr.length() - 1));

		while (!nodes.isEmpty()) {
			SyntaxTree tree = new SyntaxTree(nodes, cache);
			result.add(tree.evaluate());
			nodes = tree.nodes();
		}

		if (expr.charAt(0) == '(')
			return new XiTuple(result);

		return new XiList(result);
	}

}