package com.poplicus.crawler.codegen.definitions;

import java.util.ArrayList;
import java.util.List;

public class FunctionDefinition implements HTMLElementsDef {

	private String name = null;
	
	private String packageName = null;
	
	private String className = null;
	
	private List<ParameterDefinition> parameters = new ArrayList<ParameterDefinition>();
	
	private String supportingFunctionName = null;
	
	private List<ParameterDefinition> supportingFunctionParameters = new ArrayList<ParameterDefinition>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	
	public void addParameter(ParameterDefinition param) {
		this.parameters.add(param);
	}

	public List<ParameterDefinition> getParameters() {
		return this.parameters;
	}

	public String getSupportingFunctionName() {
		return supportingFunctionName;
	}

	public void setSupportingFunctionName(String supportingFunctionName) {
		this.supportingFunctionName = supportingFunctionName;
	}

	public void addSupportingFunctionParameter(ParameterDefinition param) {
		this.supportingFunctionParameters.add(param);
	}

	public List<ParameterDefinition> getSupportingFunctionParameters() {
		return this.supportingFunctionParameters;
	}

	public String toString() {
		StringBuffer sbf = new StringBuffer("\nFunction name : ");
		sbf.append(name).append(", packageName : ").append(packageName);
		sbf.append(", className : ").append(className).append("\n");
		for(int index = 0; index < parameters.size(); index ++) {
			if(index > 0) {
				sbf.append(", ");
			}
			sbf.append(parameters.get(index));	
		}
		if(supportingFunctionName != null) {
			sbf.append(", \nSupporting function name : ").append(supportingFunctionName);
			for(int supportIndex = 0; supportIndex < supportingFunctionParameters.size(); supportIndex ++) {
				if(supportIndex > 0) {
					sbf.append(", ");
				}
				sbf.append(supportingFunctionParameters.get(supportIndex));
			}
		}
		return sbf.toString();
	}
	
}
