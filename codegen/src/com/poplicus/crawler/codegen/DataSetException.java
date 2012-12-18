package com.poplicus.crawler.codegen;

public class DataSetException extends Exception {

	private static final long serialVersionUID = 1227593730041398087L;

	public DataSetException() {
		super();
	}

	public DataSetException(String message) {
		super(message);
	}
	
	public DataSetException(String message, Throwable cause) {
		super(message, cause);
	}
	
}