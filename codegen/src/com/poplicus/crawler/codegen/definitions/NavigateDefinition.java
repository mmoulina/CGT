package com.poplicus.crawler.codegen.definitions;

public class NavigateDefinition implements HTMLElementsDef {
	
	public String url = null;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String toString() {
		StringBuffer sbf = new StringBuffer("\n[Navigation Definition, url : ");
		sbf.append(url);
		sbf.append("]");
		return sbf.toString();
	}
	
}
