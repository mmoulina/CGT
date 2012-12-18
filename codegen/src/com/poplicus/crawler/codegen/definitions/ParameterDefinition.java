package com.poplicus.crawler.codegen.definitions;

public class ParameterDefinition {

	private String name = null;

	private String type = null;

	private String value = null;

	private boolean passByValue = false;

	private boolean passByReference = false;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ParameterDefinition() {
		//do nothing
	}

	public ParameterDefinition(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

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

	public String toString() {
		StringBuffer sbf = new StringBuffer("Parameter Definition : [Name : ");
		sbf.append(name).append(", Type : ").append(type).append(", Value : ").append(value).append("]");
		return  sbf.toString();
	}

}
