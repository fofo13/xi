package org.xiscript.xi.datatypes.io;

import java.io.Closeable;
import java.io.IOException;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;

public abstract class XiStream extends DataType implements Closeable {

	private static final long serialVersionUID = 0L;

	@Override
	public void delete() {
		try {
			close();
		} catch (IOException ioe) {
			ErrorHandler.invokeError(ErrorType.INTERNAL);
		}
	}

	@Override
	public int compareTo(DataType other) {
		ErrorHandler.invokeError(ErrorType.UNCOMPARABLE, type());
		return 0;
	}

}
