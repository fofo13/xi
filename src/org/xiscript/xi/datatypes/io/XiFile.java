package org.xiscript.xi.datatypes.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiAttribute;
import org.xiscript.xi.datatypes.XiNull;
import org.xiscript.xi.datatypes.collections.XiString;
import org.xiscript.xi.datatypes.functional.HiddenLambda;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;

public class XiFile extends DataType {

	private final File file;
	private final XiWriter writer;
	private final XiReader reader;

	public XiFile(final String file) {
		this.file = new File(file);
		writer = getWriter();
		reader = getReader();

		HiddenLambda wrtr = new HiddenLambda(1) {
			@Override
			public DataType evaluate(DataType... args) {
				writer.print(args[0]);
				return XiNull.instance();
			}
		};

		HiddenLambda lnwrtr = new HiddenLambda(1) {
			@Override
			public DataType evaluate(DataType... args) {
				writer.println(args[0]);
				return XiNull.instance();
			}
		};

		HiddenLambda rdr = new HiddenLambda(0) {
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

		HiddenLambda lnrdr = new HiddenLambda(0) {
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

		attributes.put(new XiAttribute("w"), wrtr);
		attributes.put(new XiAttribute("lnw"), lnwrtr);
		attributes.put(new XiAttribute("r"), rdr);
		attributes.put(new XiAttribute("lnr"), lnrdr);

		attributes.put(new XiAttribute("raw_w"), writer);
		attributes.put(new XiAttribute("raw_r"), reader);
	}

	public XiFile(XiString file) {
		this(file.toString());
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

}