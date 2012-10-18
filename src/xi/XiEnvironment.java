package xi;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class XiEnvironment implements Closeable {

	private VariableCache globals;
	private boolean closed;
	
	public XiEnvironment() {
		globals = new VariableCache();
	}
	
	public XiEnvironment(File file) throws FileNotFoundException {
		this();
		Scanner scan = new Scanner(file);
		while (scan.hasNext()) {
			String[] statements = scan.nextLine().split(";");
			for (String statement : statements)
				put(statement);
		}
		scan.close();
	}
	
	public XiEnvironment(String file) throws FileNotFoundException {
		this(new File(file));
	}

	public VariableCache globals() {
		return globals;
	}
	
	public void put(String statement) {
		if (closed)
			throw new RuntimeException("XiEnvironment is closed.");
		
		if (statement.contains(";"))
			throw new IllegalArgumentException("Argument is not a single statement.");
		
		if (statement.contains(":=")) { 
			String[] split = statement.split(":=");
			globals.put(new XiVar(split[0].trim(), new SyntaxTree(split[1].trim(), globals).evaluate()));
		}
		
		else
			(new SyntaxTree(statement, globals)).evaluate();
	}
	
	@Override
	public void close() {
		closed = true;
	}
	
}