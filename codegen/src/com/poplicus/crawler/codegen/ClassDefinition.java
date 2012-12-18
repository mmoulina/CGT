package com.poplicus.crawler.codegen;

import java.util.ArrayList;
import java.util.List;

public class ClassDefinition {
	
	public enum ClassAccessType {
		/**
		 * instance_access - when a class is instance_access, its methods can be accessed only after creating an instance to the class.
		 * static_access - when a class is static_access, its methods are static. So we can access its methods like ClassName.MethodName.
		 * static_instance_access - when a class is static_instance_access, it has both static and non-static methods for e.g. System.DateTime.
		 */
		instance_access, 
		static_access, 
		static_instance_access 
	}
	
	private String packageName = null;
	
	private String className = null;
	
	private String fullyQualifiedClassName = null;
	
	private List<MethodDefinition> methodDefinitions = new ArrayList<MethodDefinition>();
	
	private ClassAccessType classAccessType = null;
	
	private String variableRefName = null;
	
	public ClassDefinition(String packageName, String className, ClassAccessType classAccessType, String variableRefName) throws ClassDefinitionException {
		if(packageName == null) {
			throw new ClassDefinitionException("Package name cannot be null.");
		}
		if(className == null) {
			throw new ClassDefinitionException("Class name cannot be null.");
		}
		this.packageName = packageName;
		this.className = className;
		this.fullyQualifiedClassName = this.packageName + Constants.DOT + this.className;
		this.classAccessType = classAccessType;
		if(this.classAccessType.equals(ClassAccessType.instance_access) 
			|| this.classAccessType.equals(ClassAccessType.static_instance_access)) {
			if(variableRefName == null || variableRefName.length() <= 0) {
				throw new ClassDefinitionException("Variable reference name cannot be null when the class needs to be accessed using an instance.");
			}
		}
		this.variableRefName = variableRefName;
	}
	
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getFullyQualifiedClassName() {
		if(fullyQualifiedClassName == null) {
			fullyQualifiedClassName = packageName + Constants.DOT + className;
		}
		return fullyQualifiedClassName;
	}

	public void setFullyQualifiedClassName(String fullyQualifiedClassName) {
		this.fullyQualifiedClassName = fullyQualifiedClassName;
	}

	public void addMethodDefinition(MethodDefinition methodDef) {
		this.methodDefinitions.add(methodDef);
	}
	
	public List<MethodDefinition> getMethodDefinitions() {
		return this.methodDefinitions;
	}
	
	public MethodDefinition getMethodDefinition(String methodNameKey) {
		for(MethodDefinition mDef : methodDefinitions) {
			if(mDef.getMethodNameKey().equals(methodNameKey)) {
				return mDef;
			}
		}
		return null;
	}
	
	public String getVariableRefName() {
		return variableRefName;
	}

	public void setVariableRefName(String variableRefName) {
		this.variableRefName = variableRefName;
	}
	
	public ClassAccessType getClassAccessType() {
		return classAccessType;
	}

	public void setClassAccessType(ClassAccessType classAccessType) {
		this.classAccessType = classAccessType;
	}

}
