package org.xiscript.xi.datatypes.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiAttribute;
import org.xiscript.xi.datatypes.XiNull;
import org.xiscript.xi.datatypes.XiType;
import org.xiscript.xi.datatypes.collections.XiString;
import org.xiscript.xi.datatypes.functional.HiddenFunc;
import org.xiscript.xi.datatypes.functional.HiddenLambda;
import org.xiscript.xi.datatypes.functional.XiFunc;
import org.xiscript.xi.datatypes.functional.XiLambda;
import org.xiscript.xi.datatypes.iterable.XiIterable;
import org.xiscript.xi.datatypes.numeric.XiInt;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;

public class XiFile extends XiIterable {

	private static final XiAttribute W = XiAttribute.valueOf("w");
	private static final XiAttribute LNW = XiAttribute.valueOf("lnw");
	private static final XiAttribute R = XiAttribute.valueOf("r");
	private static final XiAttribute LNR = XiAttribute.valueOf("lnr");
	private static final XiAttribute RAW_W = XiAttribute.valueOf("raw_w");
	private static final XiAttribute RAW_R = XiAttribute.valueOf("raw_r");

	private static final XiAttribute NAME = XiAttribute.valueOf("name");
	private static final XiAttribute RENAME = XiAttribute.valueOf("rename");

	private final File file;
	private final XiWriter writer;
	private final XiReader reader;

	private XiLambda w;
	private XiLambda lnw;
	private XiLambda r;
	private XiLambda lnr;

	private XiFunc rename;

	public XiFile(final String str) {
		this.file = new File(str);
		writer = getWriter();
		reader = getReader();

		w = new HiddenLambda(1) {
			@Override
			public DataType evaluate(DataType... args) {
				writer.print(args[0]);
				return XiNull.instance();
			}
		};

		lnw = new HiddenLambda(1) {
			@Override
			public DataType evaluate(DataType... args) {
				writer.println(args[0]);
				return XiNull.instance();
			}
		};

		r = new HiddenLambda(0) {
			@Override
			public DataType evaluate(DataType... args) {
				try {
					return reader.read();
				} catch (NoSuchElementException nsee) {
					return XiNull.instance();
				}
			}

			@Override
			public boolean isEmpty() {
				return !reader.hasNext();
			}
		};

		lnr = new HiddenLambda(0) {
			@Override
			public DataType evaluate(DataType... args) {
				try {
					return reader.readln();
				} catch (NoSuchElementException nsee) {
					return XiNull.instance();
				}
			}

			@Override
			public boolean isEmpty() {
				return !reader.hasNextLine();
			}
		};

		rename = new HiddenFunc(1) {
			@Override
			public DataType evaluate(DataType... args) {
				return new XiInt(file.renameTo(new File(((XiString) args[0])
						.toString())));
			}
		};
	}

	public XiFile(XiString file) {
		this(file.toString());
	}

	@Override
	public DataType getAttribute(XiAttribute a) {
		if (a.equals(W))
			return w;
		if (a.equals(LNW))
			return lnw;
		if (a.equals(R))
			return r;
		if (a.equals(LNR))
			return lnr;
		if (a.equals(RAW_W))
			return writer;
		if (a.equals(RAW_R))
			return reader;
		if (a.equals(NAME))
			return new XiString(file.getAbsolutePath());
		if (a.equals(RENAME))
			return rename;

		return super.getAttribute(a);
	}

	private XiWriter getWriter() {
		try {
			return new XiWriter(new PrintStream(
					new FileOutputStream(file, true)));
		} catch (FileNotFoundException fnfe) {
			ErrorHandler.invokeError(ErrorType.FILE_NOT_FOUND, file);
			return null;
		}
	}

	private XiReader getReader() {
		try {
			return new XiReader(new Scanner(file));
		} catch (FileNotFoundException fnfe) {
			ErrorHandler.invokeError(ErrorType.FILE_NOT_FOUND, file);
			return null;
		}
	}

	@Override
	public Iterator<DataType> iterator() {
		try {
			final Scanner scan = new Scanner(file);
			return new Iterator<DataType>() {
				@Override
				public boolean hasNext() {
					boolean b = scan.hasNextLine();
					if (!b)
						scan.close();
					return b;
				}

				@Override
				public DataType next() {
					return new XiString(scan.nextLine());
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}
			};
		} catch (FileNotFoundException fnfe) {
			ErrorHandler.invokeError(ErrorType.FILE_NOT_FOUND, file);
			return null;
		}
	}

	@Override
	public boolean isEmpty() {
		return file.isHidden();
	}

	@Override
	public File getJavaAnalog() {
		return file;
	}

	@Override
	public int compareTo(DataType other) {
		if (other instanceof XiFile)
			return file.compareTo(((XiFile) other).file);
		return 0;
	}

	@Override
	public void delete() {
		writer.close();
		reader.close();
	}

	@Override
	public String toString() {
		return file.toString();
	}

	@Override
	public boolean equals(Object other) {
		return (other instanceof XiFile) && file.equals(((XiFile) other).file);
	}

	@Override
	public int hashCode() {
		return file.hashCode();
	}

	@Override
	public XiType type() {
		return XiType.valueOf(XiType.Type.FILE);
	}

}