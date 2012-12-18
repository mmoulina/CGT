package com.poplicus.crawler.codegen;

public class ArgumentValueDefinition {
	
	public enum ArgumentValueType {
		PassByReference, PassByValue
	}
	
	private ArgumentValueType argValueType = null;
	
	private String argumentName = null;

	private Object value = null;
	
	public ArgumentValueDefinition(ArgumentValueType argValueType, String argumentName, Object value) throws ArgumentValueDefinitionException {
		if(argValueType == null) {
			throw new ArgumentValueDefinitionException("Argument value type cannot be null.");
		}
		if(argumentName == null) {
			throw new ArgumentValueDefinitionException("Argument name cannot be null.");
		}
		this.argumentName = argumentName;
		this.argValueType = argValueType;
		this.value = value;
	}

	public ArgumentValueType getArgValueType() {
		return argValueType;
	}

	public void setArgValueType(ArgumentValueType argValueType) {
		this.argValueType = argValueType;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	public String getArgumentName() {
		return argumentName;
	}

	public void setArgumentName(String argumentName) {
		this.argumentName = argumentName;
	}

}
