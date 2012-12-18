package com.poplicus.crawler.codegen;

public class ArgumentDefinitionException extends Exception {

	private static final long serialVersionUID = 3886942700431727777L;

	public ArgumentDefinitionException() {
		super();
	}

	public ArgumentDefinitionException(String message) {
		super(message);
	}
	
	public ArgumentDefinitionException(String message, Throwable cause) {
		super(message, cause);
	}
	
}