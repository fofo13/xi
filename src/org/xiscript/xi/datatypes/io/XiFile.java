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

public class XiFile extends DataType {

	private final File file;
	private final XiWriter writer;
	private final XiReader reader;

	public XiFile(final String file) {
		this.file = new File(file);
		writer = getWriter();
		reader = getReader();

		HiddenLambda wrtr = new HiddenLambda() {
			@Override
			public DataType evaluate(DataType... args) {
				writer.print(args[0]);
				return XiNull.instance();
			}
		};

		HiddenLambda lnwrtr = new HiddenLambda() {
			@Override
			public DataType evaluate(DataType... args) {
				writer.println(args[0]);
				return XiNull.instance();
			}
		};

		HiddenLambda rdr = new HiddenLambda() {
			@Override
			public DataType evaluate(DataType... args) {
				try {
					return reader.read();
				} catch (NoSuchElementException nsee) {
					return XiNull.instance();
				}
			}
		};

		HiddenLambda lnrdr = new HiddenLambda() {
			@Override
			public DataType evaluate(DataType... args) {
				try {
					return reader.readln();
				} catch (NoSuchElementException nsee) {
					return XiNull.instance();
				}
			}
		};

		attributes.put(new XiAttribute("writer"), wrtr);
		attributes.put(new XiAttribute("lnwriter"), lnwrtr);
		attributes.put(new XiAttribute("reader"), rdr);
		attributes.put(new XiAttribute("lnreader"), lnrdr);

		attributes.put(new XiAttribute("plain_writer"), writer);
		attributes.put(new XiAttribute("plain_reader"), reader);
	}

	public XiFile(XiString file) {
		this(file.toString());
	}

	private XiWriter getWriter() {
		try {
			return new XiWriter(new PrintStream(
					new FileOutputStream(file, true)));
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException("File not found: " + file);
		}
	}

	private XiReader getReader() {
		try {
			return new XiReader(new Scanner(file));
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException("File not found: " + file);
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

}