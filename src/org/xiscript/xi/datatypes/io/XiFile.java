package org.xiscript.xi.datatypes.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiAttribute;
import org.xiscript.xi.datatypes.XiNull;
import org.xiscript.xi.datatypes.collections.XiString;
import org.xiscript.xi.datatypes.functional.HiddenLambda;

public class XiFile extends DataType {

	private final File file;
	private final XiWriter writer;

	public XiFile(String file) {
		this.file = new File(file);
		writer = getWriter();
	}

	public XiFile(XiString file) {
		this(file.toString());
	}

	private XiWriter getWriter() {
		try {
			return new XiWriter(new PrintStream(file));
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException("File not found: " + file);
		}
	}

	@Override
	protected void refreshAttributes() {
		HiddenLambda printWriter = new HiddenLambda() {
			@Override
			public DataType evaluate(DataType... args) {
				writer.print(args[0]);
				return XiNull.instance();
			}
		};

		HiddenLambda lineWriter = new HiddenLambda() {
			@Override
			public DataType evaluate(DataType... args) {
				writer.println(args[0]);
				return XiNull.instance();
			}
		};

		attributes.put(new XiAttribute("writer"), printWriter);
		attributes.put(new XiAttribute("lnwriter"), lineWriter);

		attributes.put(new XiAttribute("plain_writer"), writer);
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