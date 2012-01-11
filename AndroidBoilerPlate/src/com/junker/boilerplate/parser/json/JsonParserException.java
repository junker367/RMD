package com.junker.boilerplate.parser.json;

public class JsonParserException extends RuntimeException {

	private static final long serialVersionUID = -3355278474741556529L;

	public JsonParserException() {
		super();
	}

	public JsonParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public JsonParserException(String message) {
		super(message);
	}

	public JsonParserException(Throwable cause) {
		super(cause);
	}

}
