package com.poplicus.crawler.codegen.definitions;

public class SourceMapConfig {

	private String type = null;

	public SourceMapConfig(String type) {
		super();
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String toString() {
		return "Type : " + type;
	}
	
}
