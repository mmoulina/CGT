package com.poplicus.crawler.codegen;

public class FileGenerationException extends Exception {

	private static final long serialVersionUID = -5452885753437947640L;

	public FileGenerationException() {
		super();
	}

	public FileGenerationException(String message) {
		super(message);
	}
	
	public FileGenerationException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
