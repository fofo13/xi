package org.xiscript.xi.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;

public class XiProgram {
   /*a  XiProgram as got 2 private  field:Syntaxtree is the collection of instructions(statements is three of 
    * syntax with a  three structure) to execute and a VariablCache*/
	
	private SyntaxTree program;
	private VariableCache scope;

	private XiProgram(SyntaxTree program) {
		this.program = program;
		scope = new VariableCache();
	}

	protected XiProgram(InputStream stream) {
		this(compile(stream));
	}

	public XiProgram(File file) throws FileNotFoundException {
		this(new FileInputStream(file));
	}

	public VariableCache scope() {
		return scope;
	}

	public DataType run() {
		return program.evaluate(scope);
	}

	private static SyntaxTree compile(InputStream file) {
		Queue<Character> source = new LinkedList<Character>();

		int next = -1;
		/* read input file's lines */
		try {
			while ((next = file.read()) != -1) {
				source.add((char) next);
			}
			file.close();
		} catch (IOException ioe) {
			ErrorHandler.invokeError(ErrorType.INTERNAL);
		}

		return new SyntaxTree(Parser.genNodeQueue(source));
	}

	public void writeTo(File f) {
		program.writeTo(f);
	}

	public static XiProgram readFrom(File f) {
		return new XiProgram(SyntaxTree.readFrom(f));
	}

}