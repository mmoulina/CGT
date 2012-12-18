package com.poplicus.crawler.codegen;

import java.util.ArrayList;
import java.util.List;

public class MethodDefinition {
	
	private String methodNameKey = null;
	
	private String methodName = null;
	
	private List<ArgumentDefinition> argumentList = new ArrayList<ArgumentDefinition>();
	
	private List<MethodDefinition> supportingMethods = new ArrayList<MethodDefinition>();
	
	private boolean staticAccess = false;
	
	/**
	 * For e.g. System.DateTime has both static and non-static methods. To decide on how to invoke a method of a class
	 * we will be using <code>staticAccess</code> value.
	 * 
	 * @param methodName
	 * @param staticAccess
	 * @throws MethodDefinitionException
	 */
	public MethodDefinition(String methodNameKey, String methodName, boolean staticAccess) throws MethodDefinitionException {
		if(methodNameKey == null) {
			throw new MethodDefinitionException("Method name key cannot be null.");
		}
		if(methodName == null) {
			throw new MethodDefinitionException("Method name cannot be null.");
		}
		this.methodNameKey = methodNameKey;
		this.methodName = methodName;
		this.staticAccess = staticAccess;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void addArgument(ArgumentDefinition argDef) throws MethodDefinitionException {
		if(argDef == null) {
			throw new MethodDefinitionException("Cannot add a null Argument Definition to Method Definition.");
		}
		argumentList.add(argDef);
	}
	
	public List<ArgumentDefinition> getArgumentDefinitions() {
		return this.argumentList;
	}
	
	public void addSupportingMethod(MethodDefinition methodDef) {
		this.supportingMethods.add(methodDef);
	}
	
	public List<MethodDefinition> getSupportingMethods() {
		return this.supportingMethods;
	}
	
	public boolean isStaticAccess() {
		return staticAccess;
	}

	public void setStaticAccess(boolean staticAccess) {
		this.staticAccess = staticAccess;
	}
	
	public String getMethodNameKey() {
		return methodNameKey;
	}

	public void setMethodNameKey(String methodNameKey) {
		this.methodNameKey = methodNameKey;
	}
	
	public MethodDefinition getSupportingMethodDefinition(String methodNameKey) {
		for(MethodDefinition mDef : supportingMethods) {
			if(mDef.getMethodNameKey().equals(methodNameKey)) {
				return mDef;
			}
		}
		return null;
	}

}
