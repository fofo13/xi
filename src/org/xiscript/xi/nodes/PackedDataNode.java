package org.xiscript.xi.nodes;

import java.util.ArrayList;
import java.util.List;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.collections.CollectionWrapper;

public class PackedDataNode extends DataNode<CollectionWrapper<?>> {

	private List<DataNode<DataType>> contents;

	public PackedDataNode(CollectionWrapper<?> data) {
		super(data);

		contents = new ArrayList<DataNode<DataType>>(data.length());

		for (DataType d : data)
			contents.add(new DataNode<DataType>(d));
	}

	public List<DataNode<DataType>> contents() {
		return contents;
	}

}