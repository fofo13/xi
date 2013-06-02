package org.xiscript.xi.nodes;

import java.util.ArrayList;
import java.util.List;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.datatypes.functional.Function;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;

public class FunctionNode implements Node {

	private XiVar id;
	private int nargs;

	private List<Node> children;

	public FunctionNode(XiVar id, int nargs) {
		this.id = id;
		this.nargs = nargs;

		children = new ArrayList<Node>(nargs);
	}

	@Override
	public void addChild(Node node) {
		children.add(node);
	}

	@Override
	public int numChildren() {
		return nargs;
	}

	@Override
	public DataType evaluate(VariableCache cache) {
		Function f = (Function) cache.get(id.id());

		DataType[] arr = new DataType[children.size()];
		for (int i = 0; i < arr.length; i++) {
			try {
				arr[i] = children.get(i).evaluate(cache);
			} catch (ClassCastException cce) {
				ErrorHandler.invokeError(ErrorType.ARGUMENT, children.get(i));
			}
		}

		return f.evaluate(arr, cache);
	}

	@Override
	public void clear() {
		children.clear();
	}

}