package com.poplicus.crawler.codegen;

public class WebPageException extends Exception {
	
	private static final long serialVersionUID = -7929373452749479466L;

	public WebPageException() {
		super();
	}

	public WebPageException(String message) {
		super(message);
	}
	
	public WebPageException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
