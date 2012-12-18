package com.poplicus.crawler.codegen.utilities.config;

import java.util.ArrayList;
import java.util.List;


public class SupportingFunctionDefConfig {

	private String name = null;
	
	private List<ParameterDefConfig> parameters = new ArrayList<ParameterDefConfig>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setParameter(ParameterDefConfig paramDef) {
		this.parameters.add(paramDef);
	}
	
	public void setParameters(List<ParameterDefConfig> params) {
		this.parameters = params;
	}
	
	public List<ParameterDefConfig> getParameters() {
		return this.parameters;
	}
	
}
