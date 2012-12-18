package com.poplicus.crawler.codegen;

import java.util.ArrayList;
import java.util.List;

public class RHSDefinition {
	
	private String fullyQualifiedClassName = null;
	private String packageName = null;
	private String className = null;
	private String methodNameKey = null;
	private String supportingMethodName = null;

	private List<ArgumentValueDefinition> argValueDefs = new ArrayList<ArgumentValueDefinition>();
	private List<ArgumentValueDefinition> supportingMethodArgValDefs = new ArrayList<ArgumentValueDefinition>();
	
	private RHSTask rhsTask = null;
	
	public RHSDefinition(String packageName, String className, String fullyQualifiedClassName) throws LineOfCodeException {
		if(packageName != null) {
			this.packageName = packageName;
		} else {
			throw new LineOfCodeException("Package name cannot be null in RHSDefinition.");
		}
		if(className != null) {
			this.className = className;
		} else {
			throw new LineOfCodeException("Class name cannot be null in RHSDefinition.");
		}
		if(fullyQualifiedClassName == null && (packageName != null && className != null)) {
			this.fullyQualifiedClassName = this.packageName + Constants.DOT + this.className;
		} else {
			this.fullyQualifiedClassName = fullyQualifiedClassName;
		}
	}
	
	public RHSDefinition(RHSTask rhsTask) {
		this.rhsTask = rhsTask;
	}
	
	public void setMethodNameKey(String methodNameKey) {
		this.methodNameKey = methodNameKey;
	}
	
	public void addArgumentValueDefinition(ArgumentValueDefinition argValueDef) {
		this.argValueDefs.add(argValueDef);
	}
	
	public void addSupportingMethodArgumentValueDefinition(ArgumentValueDefinition argValueDef) {
		this.supportingMethodArgValDefs.add(argValueDef);
	}

	public String getFullyQualifiedClassName() {
		return fullyQualifiedClassName;
	}
	
	public String getPackageName() {
		return this.packageName;
	}
	
	public String getClassName() {
		return this.className;
	}
	
	public String getMethodNameKey() {
		return this.methodNameKey;
	}
	
	public List<ArgumentValueDefinition> getArgumentValueDefinitions() {
		return this.argValueDefs;
	}
	
	public List<ArgumentValueDefinition> getSupportingMethodArgumentValueDefinitions() {
		return this.supportingMethodArgValDefs;
	}
	
	public ArgumentValueDefinition getArgumentValueDefinition(String argumentName) {
		for(ArgumentValueDefinition argValDef : argValueDefs) {
			if(argValDef.getArgumentName().equals(argumentName)) {
				return argValDef;
			}
		}
		return null;
	}

	public ArgumentValueDefinition getSupportingMethodArgumentValueDefinition(String argumentName) {
		for(ArgumentValueDefinition argValDef : supportingMethodArgValDefs) {
			if(argValDef.getArgumentName().equals(argumentName)) {
				return argValDef;
			}
		}
		return null;
	}

	public String getSupportingMethodName() {
		return supportingMethodName;
	}

	public void setSupportingMethodName(String supportingMethodName) {
		this.supportingMethodName = supportingMethodName;
	}
	
	public RHSTask getRhsTask() {
		return rhsTask;
	}

	public void setRhsTask(RHSTask rhsTask) {
		this.rhsTask = rhsTask;
	}
	
}
