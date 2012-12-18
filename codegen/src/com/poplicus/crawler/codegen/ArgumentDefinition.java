package com.poplicus.crawler.codegen;

public class ArgumentDefinition {
	
	public enum ArgumentType {
		string {
			public String toString() {
				return "string";
			}
		},
		double_type {
			public String toString() {
				return "double";
			}
		}
		
	}
	
	//private ArgumentType argumentType = null;
	
	private String argumentType = null;
	
	private String argumentName = null;

	public ArgumentDefinition(String argumentType, String argumentName) throws ArgumentDefinitionException {
		if(argumentType == null) {
			throw new ArgumentDefinitionException("Argument Type cannot be null.");
		}
		if(argumentName == null) {
			throw new ArgumentDefinitionException("Argument Name cannot be null.");
		}
		this.argumentType = argumentType;
		this.argumentName = argumentName;
	}

	public String getArgumentType() {
		return argumentType;
	}

	public void setArgumentType(String argumentType) {
		this.argumentType = argumentType;
	}

	public String getArgumentName() {
		return argumentName;
	}

	public void setArgumentName(String argumentName) {
		this.argumentName = argumentName;
	}

}
