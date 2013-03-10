package org.xiscript.xi.nodes;

import java.util.ArrayList;
import java.util.List;

import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.collections.CollectionWrapper;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;

public class PackedDataNode extends DataNode<CollectionWrapper<?>> {

	private List<Node> contents;
	private VarNode pointer;

	private PackedDataNode() {
		super(null);
	}

	public PackedDataNode(CollectionWrapper<?> data) {
		super(data);

		contents = new ArrayList<Node>(data.length());

		for (DataType d : data)
			contents.add(new DataNode<DataType>(d));
	}

	public PackedDataNode(VarNode pointer) {
		this();
		this.pointer = pointer;
	}

	public List<Node> contents(VariableCache cache) {
		if (contents != null)
			return contents;

		DataType data = cache.get(pointer.id());

		if (!(data instanceof CollectionWrapper<?>))
			ErrorHandler.invokeError(ErrorType.UNPACKING_ERROR, data);

		return (new PackedDataNode((CollectionWrapper<?>) data))
				.contents(cache);
	}
}