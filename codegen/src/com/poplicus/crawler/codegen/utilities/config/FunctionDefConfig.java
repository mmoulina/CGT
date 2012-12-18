package com.poplicus.crawler.codegen.utilities.config;

import java.util.ArrayList;
import java.util.List;

import com.poplicus.crawler.codegen.ClassDefinition.ClassAccessType;

public class FunctionDefConfig {

	private String packageName = null;
	
	private String className = null;
	
	private String name = null;
	
	private String variableReferenceName = null;
	
	private boolean staticAccess = false;
	
	private boolean instanceAccess = false;
	
	private List<ParameterDefConfig> parameters = new ArrayList<ParameterDefConfig>();
	
	private List<SupportingFunctionDefConfig> supportingFunctions = new ArrayList<SupportingFunctionDefConfig>();
	
	public ClassAccessType getClassAccessType() {
		ClassAccessType accessType = ClassAccessType.instance_access;
		if(instanceAccess && staticAccess) {
			accessType = ClassAccessType.static_instance_access;
		} else {
			if(instanceAccess) {
				accessType = ClassAccessType.instance_access;
			} else if(staticAccess) {
				accessType = ClassAccessType.static_access;
			}
		}
		return accessType;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVariableReferenceName() {
		return variableReferenceName;
	}

	public void setVariableReferenceName(String variableReferenceName) {
		this.variableReferenceName = variableReferenceName;
	}

	public boolean isStaticAccess() {
		return staticAccess;
	}

	public void setStaticAccess(boolean staticAccess) {
		this.staticAccess = staticAccess;
	}

	public boolean isInstanceAccess() {
		return instanceAccess;
	}

	public void setInstanceAccess(boolean instanceAccess) {
		this.instanceAccess = instanceAccess;
	}
	
	public void addParameter(ParameterDefConfig paramDef) {
		this.parameters.add(paramDef);
	}
	
	public void setParameters(List<ParameterDefConfig> params) {
		this.parameters = params;
	}
	
	public List<ParameterDefConfig> getParameters() {
		return this.parameters;
	}

	public void addSupportingFunction(SupportingFunctionDefConfig supportingFunction) {
		this.supportingFunctions.add(supportingFunction);
	}
	
	public void setSupportingFunctions(List<SupportingFunctionDefConfig> functions) {
		this.supportingFunctions = functions;
	}
	
	public List<SupportingFunctionDefConfig> getSupportingFunctions() {
		return this.supportingFunctions;
	}

}
