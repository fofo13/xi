package org.xiscript.xi.datatypes.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.xiscript.xi.core.XiGenerator;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.numeric.XiInt;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;
import org.xiscript.xi.operations.IntrinsicOperation;

public class XiTuple extends XiList {

	public XiTuple(List<DataType> list) {
		super(Collections.unmodifiableList(list));
	}

	public XiTuple(DataType... elements) {
		super(Arrays.asList(elements));
	}

	public XiTuple() {
		super();
	}

	public XiTuple(XiGenerator iter) {
		this();
		for (DataType d : iter)
			add(d);
	}

	@Override
	public DataType rnd() {
		return collection.get((int) (Math.random() * collection.size()));
	}

	@Override
	public CollectionWrapper<List<DataType>> delete(DataType params) {
		ErrorHandler.invokeError(ErrorType.ARGUMENT,
				IntrinsicOperation.SUBTRACT);
		return null;
	}

	@Override
	public void put(XiInt index, DataType data) {
		ErrorHandler
				.invokeError(ErrorType.ARGUMENT, IntrinsicOperation.SETATTR);
	}

	@Override
	public CollectionWrapper<List<DataType>> instantiate(
			Collection<DataType> col) {
		return new XiTuple(new ArrayList<DataType>(col));
	}

	@Override
	public String toString() {
		String s = super.toString();
		return "(" + s.substring(1, s.length() - 1) + ")";
	}

}