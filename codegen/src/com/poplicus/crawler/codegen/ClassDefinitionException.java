package com.poplicus.crawler.codegen;

public class ClassDefinitionException extends Exception {

	private static final long serialVersionUID = -4199764352074587861L;

	public ClassDefinitionException() {
		super();
	}

	public ClassDefinitionException(String message) {
		super(message);
	}
	
	public ClassDefinitionException(String message, Throwable cause) {
		super(message, cause);
	}
	
}