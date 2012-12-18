package com.poplicus.crawler.codegen;

public class LineOfCodeException extends Exception {

	private static final long serialVersionUID = -2591728571085153308L;

	public LineOfCodeException() {
		super();
	}

	public LineOfCodeException(String message) {
		super(message);
	}
	
	public LineOfCodeException(String message, Throwable cause) {
		super(message, cause);
	}
	
}