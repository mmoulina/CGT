package com.poplicus.crawler.codegen.definitions;

public class ElementDefinition implements HTMLElementsDef{

	private String elementTagType = null;
	
	private String elementFieldType = null;
	
	private String value = null;
	
	private boolean exactMatch = false;

	public String getElementTagType() {
		return elementTagType;
	}

	public void setElementTagType(String tag) {
		this.elementTagType = tag;
	}

	public String getElementFieldType() {
		return elementFieldType;
	}

	public void setElementFieldType(String field) {
		this.elementFieldType = field;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isExactMatch() {
		return exactMatch;
	}

	public void setExactMatch(boolean exactMatch) {
		this.exactMatch = exactMatch;
	}
	
	public String toString() {
		StringBuffer sbf = new StringBuffer("\n[Element Definition, elementTagType : ");
		sbf.append(elementTagType).append(", elementFieldType : ").append(elementFieldType).append(", value : ").append(value);
		sbf.append(", exactMatch : ").append(exactMatch).append("]");
		return sbf.toString();
	}
	
}
