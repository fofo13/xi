package org.xiscript.xi.exceptions;

public class ErrorHandler {

	private static final String template = "[Error] %s%n";
	private static final int errCode = -1;

	public enum ErrorType {

		INCOMPLETE_EXPRESSION(
				"Incomplete expression detected when loading file."),

		ARGUMENT("Invalid arguments for operator: %s"),

		IO("An I/O exception occured."),

		INVALID_IDENTIFIER("Invalid variable identifier: %s"),

		IDNETIFIER_NOT_FOUND("Variable identifier not found: %s"),

		INVALID_OVERRIDE("Cannot redefine built-in function: %s"),

		MISPLACED_STATEMENT("Statement misplaced: %s"),

		UNPACKING_ERROR("Could not unpack expression: %s"),

		EXEC_ERROR("Could not execute: %s"),

		NUMBER_FORMAT("Number format error for input string: %s"),

		STRING_FORMAT("String format error: \"%s\", %s"),

		INTERRUPTION("Unexpected thread interruption occured."),

		ASSERT_ERROR("Assertion failed."),

		FILE_NOT_FOUND("File not found: %s"),

		UNCOMPARABLE("Cannot compare objects of type: %s"),

		ZERO_DIV("Division by zero: %s"),

		INVALID_ATTRIBUTE("%s has no attribute: %s"),

		UNASSIGNABLE_ATTRIBUTE("%s has no assignable attribute: %s"),

		INVALID_ATTRIBUTE_TUPLE(
				"Argument-tuple must consist of only attribute identifiers: %s"),

		NOT_CALLABLE("%s of type %s is not callable"),

		PARSE_ERROR("Could not parse expression: %s"),

		MALFORMED_DICT("Malformed dictionary literal detected."),

		MALFORMED_ARGUMENT_LIST("Malformed argument list."),

		UNICODE_ERROR("Unable to parse unicode value: %s"),

		INTERNAL("Internal error occured."),

		CUSTOM("%s");

		private String message;

		private ErrorType(String message) {
			this.message = message;
		}

	}

	public static final void invokeError(ErrorType err, Object... args) {
		System.err.printf(template, String.format(err.message, args));
		System.exit(errCode);
	}

}
