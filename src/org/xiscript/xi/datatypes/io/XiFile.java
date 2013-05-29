package org.xiscript.xi.datatypes.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Scanner;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiAttribute;
import org.xiscript.xi.datatypes.XiNull;
import org.xiscript.xi.datatypes.XiType;
import org.xiscript.xi.datatypes.collections.XiString;
import org.xiscript.xi.datatypes.functional.HiddenFunc;
import org.xiscript.xi.datatypes.functional.XiFunc;
import org.xiscript.xi.datatypes.iterable.XiIterable;
import org.xiscript.xi.datatypes.numeric.XiInt;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;

public class XiFile extends XiIterable {

	public static final String R = "r";
	public static final String W = "w";
	public static final String DEFAULT = "rw";

	private static final XiAttribute WRITER = XiAttribute.valueOf("writer");
	private static final XiAttribute READER = XiAttribute.valueOf("reader");

	private static final XiAttribute NAME = XiAttribute.valueOf("name");
	private static final XiAttribute PATH = XiAttribute.valueOf("path");
	private static final XiAttribute ABS = XiAttribute.valueOf("abs");
	private static final XiAttribute RENAME = XiAttribute.valueOf("rename");

	private static final XiAttribute READ = XiAttribute.valueOf("read");
	private static final XiAttribute READLN = XiAttribute.valueOf("readln");
	private static final XiAttribute READC = XiAttribute.valueOf("readc");
	private static final XiAttribute NEXT = XiAttribute.valueOf("next");

	private static final XiAttribute WRITE = XiAttribute.valueOf("write");
	private static final XiAttribute WRITELN = XiAttribute.valueOf("writeln");

	private static final XiAttribute EXISTS = XiAttribute.valueOf("exists");
	private static final XiAttribute CREATE = XiAttribute.valueOf("create");
	private static final XiAttribute CLEAR = XiAttribute.valueOf("clear");
	private static final XiAttribute CLOSE = XiAttribute.valueOf("close");
	private static final XiAttribute DELETE = XiAttribute.valueOf("delete");

	private final File file;
	private XiWriter writer;
	private XiReader reader;

	private XiString name;
	private XiString path;
	private XiString abs;
	private XiFunc rename;

	private XiFunc read;
	private XiFunc readln;
	private XiFunc readc;
	private XiFunc next;
	private XiFunc write;
	private XiFunc writeln;

	private XiFunc exists;
	private XiFunc create;
	private XiFunc clear;
	private XiFunc close;
	private XiFunc delete;

	private Scanner out;
	private BufferedReader outc;

	private BufferedWriter in;

	public XiFile(String name, String options) {
		this.file = new File(name);

		if (options.contains(W)) {
			create();

			try {
				in = new BufferedWriter(new FileWriter(file, true));
			} catch (IOException ioe) {
				ErrorHandler.invokeError(ErrorType.FILE_NOT_FOUND, file);
			}

			write = new HiddenFunc(1) {
				@Override
				public DataType evaluate(DataType... args) {
					try {
						in.write(args[0].toString());
						return new XiInt(1);
					} catch (IOException ioe) {
						return new XiInt(0);
					}
				}
			};

			writeln = new HiddenFunc(1) {
				@Override
				public DataType evaluate(DataType... args) {
					try {
						in.write(args[0].toString());
						in.newLine();
						return new XiInt(1);
					} catch (IOException ioe) {
						return new XiInt(0);
					}
				}
			};
		}

		if (options.contains(R)) {
			try {
				outc = new BufferedReader(new FileReader(file));
				out = new Scanner(file);
			} catch (FileNotFoundException fnfe) {
				ErrorHandler.invokeError(ErrorType.FILE_NOT_FOUND, file);
			}

			read = new HiddenFunc(0) {
				@Override
				public DataType evaluate(DataType... args) {
					return out.hasNext() ? new XiString(out.next()) : XiNull
							.instance();
				}
			};

			readln = new HiddenFunc(0) {
				@Override
				public DataType evaluate(DataType... args) {
					return out.hasNextLine() ? new XiString(out.nextLine())
							: XiNull.instance();
				}
			};

			readc = new HiddenFunc(0) {
				@Override
				public DataType evaluate(DataType... args) {
					int c = 0;
					try {
						c = outc.read();
					} catch (IOException ioe) {
						return XiNull.instance();
					}
					return c == 0 ? XiNull.instance() : new XiString(
							Character.toString((char) c));
				}
			};

			next = new HiddenFunc(1) {
				@Override
				public DataType evaluate(DataType... args) {
					String s = args[0].toString();
					return out.hasNext(s) ? new XiString(out.next(s)) : XiNull
							.instance();
				}
			};
		}

		clear = new HiddenFunc(0) {
			@Override
			public DataType evaluate(DataType... args) {
				try {
					PrintWriter writer = new PrintWriter(file);
					writer.print("");
					writer.close();
					return new XiInt(1);
				} catch (FileNotFoundException ioe) {
					return new XiInt(0);
				}
			}
		};

		exists = new HiddenFunc(0) {
			@Override
			public DataType evaluate(DataType... args) {
				return new XiInt(file.exists());
			}
		};

		create = new HiddenFunc(0) {
			@Override
			public DataType evaluate(DataType... args) {
				return new XiInt(create());
			}
		};

		rename = new HiddenFunc(1) {
			@Override
			public DataType evaluate(DataType... args) {
				return new XiInt(file.renameTo(new File(((XiString) args[0])
						.toString())));
			}
		};

		close = new HiddenFunc(0) {
			@Override
			public DataType evaluate(DataType... args) {
				delete();
				return XiNull.instance();
			}
		};

		delete = new HiddenFunc(0) {
			@Override
			public DataType evaluate(DataType... args) {
				return new XiInt(file.delete());
			}
		};
	}

	private boolean create() {
		File parent = file.getParentFile();
		if (parent != null)
			file.getParentFile().mkdirs();
		try {
			return file.createNewFile();
		} catch (IOException ioe) {
			return false;
		}
	}

	@Override
	public DataType getAttribute(XiAttribute a) {
		if (a.equals(WRITER))
			return writer == null ? writer = getWriter() : writer;
		if (a.equals(READER))
			return reader == null ? reader = getReader() : reader;
		if (a.equals(NAME))
			return name == null ? name = new XiString(file.getName()) : name;
		if (a.equals(PATH))
			return path == null ? path = new XiString(file.getPath()) : path;
		if (a.equals(ABS))
			return abs == null ? abs = new XiString(file.getAbsolutePath())
					: abs;
		if (a.equals(RENAME))
			return rename;
		if (a.equals(READ))
			return read;
		if (a.equals(READLN))
			return readln;
		if (a.equals(READC))
			return readc;
		if (a.equals(NEXT))
			return next;
		if (a.equals(WRITE))
			return write;
		if (a.equals(WRITELN))
			return writeln;
		if (a.equals(EXISTS))
			return exists;
		if (a.equals(CREATE))
			return create;
		if (a.equals(CLEAR))
			return clear;
		if (a.equals(CLOSE))
			return close;
		if (a.equals(DELETE))
			return delete;

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
		if (writer != null)
			writer.close();
		if (writer != null)
			reader.close();
		if (out != null)
			out.close();
		try {
			if (outc != null)
				outc.close();
			if (in != null)
				in.close();
		} catch (IOException ioe) {
			ErrorHandler.invokeError(ErrorType.INTERNAL);
		}
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