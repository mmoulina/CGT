package com.poplicus.crawler.codegen.definitions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Parameters implements Iterable<ParameterDefinition> {

	private List<ParameterDefinition> parameters = new ArrayList<ParameterDefinition>();

	public Parameters() {
		//do nothing
	}

	public Parameters(List<ParameterDefinition> parameters) {
		super();
		this.parameters = parameters;
	}

	public void addParameter(ParameterDefinition parameter) {
		this.parameters.add(parameter);
	}

	public void setParameters(List<ParameterDefinition> parameters) {
		this.parameters = parameters;
	}

	public List<ParameterDefinition> getParameters() {
		return this.parameters;
	}

	public int getLength() {
		return parameters.size();
	}

	@Override
	public Iterator<ParameterDefinition> iterator() {
		return parameters.iterator();
	}

}
