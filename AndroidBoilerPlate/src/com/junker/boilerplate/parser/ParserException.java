package com.junker.boilerplate.parser;

public class ParserException extends RuntimeException {

	private static final long serialVersionUID = -3355278474741556529L;

	public ParserException() {
		super();
	}

	public ParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParserException(String message) {
		super(message);
	}

	public ParserException(Throwable cause) {
		super(cause);
	}

}
