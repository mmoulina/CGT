package com.poplicus.crawler.codegen.definitions;

import java.util.List;


public class ConstructorDefinition {

	public enum ConstructorType {
		SuperConstructor {
			public String toString() {
				return "Super Constructor";
			}
		}, 
		CurrentConstructor {
			public String toString() {
				return "Current Constructor";
			}
		}
	}
	
	private ConstructorType consType = ConstructorType.CurrentConstructor;
	
	private boolean invokeSourceMapConfig = false;
	
	private Parameters parameters = null;
	
	private SourceMapConfig sourceMapConfig = null;
	
	public ConstructorDefinition() {
		
	}

	public ConstructorDefinition(ConstructorType consType) {
		super();
		this.consType = consType;
	}

	public SourceMapConfig getSourceMapConfig() {
		return sourceMapConfig;
	}

	public void setSourceMapConfig(SourceMapConfig sourceMapConfig) {
		this.sourceMapConfig = sourceMapConfig;
	}

	public Parameters getParameters() {
		return parameters;
	}

	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

	public boolean isInvokeSourceMapConfig() {
		return invokeSourceMapConfig;
	}

	public void setInvokeSourceMapConfig(boolean invokeSourceMapConfig) {
		this.invokeSourceMapConfig = invokeSourceMapConfig;
	}

	public ConstructorType getConsType() {
		return consType;
	}

	public void setConsType(ConstructorType consType) {
		this.consType = consType;
	}
	
	public String toString() {
		StringBuffer sbf = new StringBuffer("[Constructor Type : ");
		sbf.append(consType.toString());
		if(sourceMapConfig != null) {
			sbf.append(", Source Map Config : [").append(sourceMapConfig.toString());
			sbf.append("]");
		}
		sbf.append(", \nParameters : [\n");
		if(parameters == null) {
			sbf.append(parameters);
		} else {
			List<ParameterDefinition> actualParameters = parameters.getParameters();
			for(int index = 0; index < actualParameters.size(); index ++) {
				if(index > 0) {
					sbf.append(", ");
				}
				if(index != 0) {
					sbf.append("\n");
				}
				sbf.append(actualParameters.get(index).toString());
			}
		}
		sbf.append("]]");
		return sbf.toString();
	}
	
}
