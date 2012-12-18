package com.poplicus.crawler.codegen;

public class Variable {
	
	private Indentation indentation = new Indentation(Indentation.INDENTATION_LEVEL_2);
	
	private String accessModifier = "private";
	
	private Object type = null;
	
	private String name = null;
	
	private Object value = null;
	
	private boolean isStatic = false;
	
	private boolean isFinal = false;
	
	private boolean instanceVariable = false;
	
	private boolean methodVariable = false;
	
	private boolean assignToInstanceVariable = false;
	
	private Imports imports = null;
	
	private String warning = null;
	
	private String aliasName = null;
	
	private boolean noRHS = false;
	
	/**
	 * 
	 * @param type
	 * @param name
	 * @param value
	 */
	public Variable(Object type, String name, String value) {
		this.type = type;
		this.name = name;
		this.value = value;
	}
	
	/**
	 * 
	 * @param accessModifier
	 * @param isStatic
	 * @param isFinal
	 * @param type
	 * @param name
	 * @param value
	 * @param instanceVariable
	 * @param methodVariable
	 * @param assignToInstanceVariable
	 */
	public Variable(String accessModifier, boolean isStatic, boolean isFinal, Object type, String name, String value, 
					boolean instanceVariable, boolean methodVariable, boolean assignToInstanceVariable, String warning) {
		if(accessModifier != null) {
			this.accessModifier = accessModifier;
		}
		if(instanceVariable) {
			indentation.setLevel(Indentation.INDENTATION_LEVEL_2);
		}
		if(methodVariable) {
			indentation.setLevel(Indentation.INDENTATION_LEVEL_3);
		}
		this.isStatic = isStatic;
		this.isFinal = isFinal;
		this.type = type;
		this.name = name;
		this.value = value;
		this.instanceVariable = instanceVariable;
		this.methodVariable = methodVariable;
		this.assignToInstanceVariable = assignToInstanceVariable;
		this.warning = warning;
		addImports();
	}

	public void addImports() {
		if(imports == null) {
			imports = Imports.getInstance();
		}
		imports.addImport(type, null);
	}
	
	public boolean isInstanceVariable() {
		return instanceVariable;
	}

	public void setInstanceVariable(boolean instanceVariable) {
		this.instanceVariable = instanceVariable;
		indentation.setLevel(Indentation.INDENTATION_LEVEL_2);
	}
	
	public boolean isMethodVariable() {
		return methodVariable;
	}

	public void setMethodVariable(boolean methodVariable) {
		this.methodVariable = methodVariable;
		indentation.setLevel(Indentation.INDENTATION_LEVEL_3);
	}

	public boolean isAssignToInstanceVariable() {
		return assignToInstanceVariable;
	}

	public void setAssignToInstanceVariable(boolean assignToInstanceVariable) {
		this.assignToInstanceVariable = assignToInstanceVariable;
	}

	public String getAccessModifier() {
		return accessModifier;
	}

	public void setAccessModifier(String accessModifier) {
		this.accessModifier = accessModifier;
	}

	public Object getType() {
		return type;
	}

	public void setType(Object type) {
		this.type = type;
	}

	public boolean isNoRHS() {
		return noRHS;
	}

	public void setNoRHS(boolean noRHS) {
		this.noRHS = noRHS;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}
	
	public void setIndentationLevel(int level) {
		this.indentation.setLevel(level);
	}
	
	public StringBuffer toStringBuffer() {
		StringBuffer code = new StringBuffer();
		
		if(instanceVariable) {
			code.append("\n");
			code.append(indentation.getTabs());
			code.append(accessModifier).append(Constants.SPACE);
		} else if(methodVariable) {
			code.append("\n");
			code.append(indentation.getTabs());
		}
		
		if(isStatic) {
			code.append(Constants.STATIC).append(Constants.SPACE);
		}
		
		if(isFinal){
			code.append(Constants.FINAL).append(Constants.SPACE);
		}
		
		code.append(type).append(Constants.SPACE).append(name);
		if(instanceVariable || methodVariable) {
			if(value != null) {
				code.append(Constants.SPACE).append(Constants.EQUALS).append(Constants.SPACE);
				if(type.equals("string")) {
					code.append("\"").append(value).append("\"").append(Constants.SEMI_COLON);
				} else if(type.equals("int")) {
					if(value instanceof String) {
						code.append("0");
					} else {
						code.append(value);
					}
					code.append(Constants.SEMI_COLON);
				} else {
					code.append(value).append(Constants.SEMI_COLON);
				}
			} else {
				//if(noRHS) {
					code.append(Constants.SEMI_COLON);
				//}
			}
		}

		if(warning != null) {
			code.append(warning);
		}

		return code;
	}
	
	public String getWarning() {
		return this.warning;
	}

	public void setWarning(String warning) {
		this.warning = warning;
	}
	
	public String getAliasName() {
		return this.aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

}
