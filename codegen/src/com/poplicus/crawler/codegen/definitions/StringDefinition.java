package com.poplicus.crawler.codegen.definitions;

public class StringDefinition {

	private String type = null;

	private String name = null;

	private String value = null;

	private boolean passByValue = false;

	private boolean passByReference = false;

	public boolean isPassByValue() {
		return passByValue;
	}

	public void setPassByValue(boolean passByValue) {
		this.passByValue = passByValue;
	}

	public boolean isPassByReference() {
		return passByReference;
	}

	public void setPassByReference(boolean passByReference) {
		this.passByReference = passByReference;
	}

	public StringDefinition() {

	}

	public StringDefinition(String type, String name, String value) {
		this.type = type;
		this.name = name;
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
