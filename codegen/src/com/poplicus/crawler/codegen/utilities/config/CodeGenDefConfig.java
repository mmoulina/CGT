package com.poplicus.crawler.codegen.utilities.config;

import java.util.ArrayList;
import java.util.List;

public class CodeGenDefConfig {

	private List<String> conditionOperators = new ArrayList<String>(); 
	
	private List<String> expressionOperators = new ArrayList<String>();
	
	private List<String> webflowTypes = new ArrayList<String>();
	
	private List<String> browserToExecute = new ArrayList<String>();
	
	private List<String> variableTypes = new ArrayList<String>();
	
	private List<FunctionDefConfig> functionDefs = new ArrayList<FunctionDefConfig>();
	
	private List<ElementDefConfig> elementDefs = new ArrayList<ElementDefConfig>();
	
	private List<TableDefConfig> tableDefs = new ArrayList<TableDefConfig>();
	
	public TableDefConfig getTableDefByName(String tableName) {
		for(TableDefConfig tableDef : tableDefs) {
			if(tableDef.getName().equals(tableName)) {
				return tableDef;
			}
		}
		return null;
	}
	
	public List<TableDefConfig> getTableDefsForBidCrawlers() {
		List<TableDefConfig> tables = new ArrayList<TableDefConfig>();
		for(TableDefConfig tableConfig : tableDefs) {
			if(tableConfig.isUsedInBidCrawler()) {
				tables.add(tableConfig);
			}
		}
		return tables;
	}
	
	public List<TableDefConfig> getTableDefsForOrgCrawlers() {
		List<TableDefConfig> tables = new ArrayList<TableDefConfig>();
		for(TableDefConfig tableConfig : tableDefs) {
			if(tableConfig.isUsedInOrgCrawler()) {
				tables.add(tableConfig);
			}
		}
		return tables;
	}
	
	public List<TableDefConfig> getTableDefsForPlanHolderCrawlers() {
		List<TableDefConfig> tables = new ArrayList<TableDefConfig>();
		for(TableDefConfig tableConfig : tableDefs) {
			if(tableConfig.isUsedInPlanHolderCrawler()) {
				tables.add(tableConfig);
			}
		}
		return tables;
	}
	
	public List<TableDefConfig> getTableDefsForBidResultCrawlers() {
		List<TableDefConfig> tables = new ArrayList<TableDefConfig>();
		for(TableDefConfig tableConfig : tableDefs) {
			if(tableConfig.isUsedInBidResultsCrawler()) {
				tables.add(tableConfig);
			}
		}
		return tables;
	}
	
	public List<String> getSupportedHTMLElements() {
		List<String> elements = null;
		if(elementDefs.size() > 0) {
			elements = new ArrayList<String>();
			for(ElementDefConfig elementDef : elementDefs) {
				elements.add(elementDef.getTagType());
			}
		}
		return elements;
	}
	
	public List<String> getSupportedFieldsOfElement(String element) {
		List<String> fields = null;
		if(elementDefs.size() > 0) {
			for(ElementDefConfig elementDef : elementDefs) {
				if(elementDef.getTagType().equals(element)) {
					fields = elementDef.getSupportedFields();
				}
			}
		}
		return fields;
	}
	
	public void addTableDefinition(TableDefConfig tableDef) {
		this.tableDefs.add(tableDef);
	}
	
	public void setTableDefinitions(List<TableDefConfig> tableDefs) {
		this.tableDefs = tableDefs;
	}
	
	public List<TableDefConfig> getTableDefinitions() {
		return this.tableDefs;
	}
	
	public void addElementDefinition(ElementDefConfig elementDef) {
		this.elementDefs.add(elementDef);
	}
	
	public void setElementDefinitions(List<ElementDefConfig> elementDefs) {
		this.elementDefs = elementDefs;
	}
	
	public List<ElementDefConfig> getElementDefinitions() {
		return this.elementDefs;
	}
	
	public void addFunctionDefinition(FunctionDefConfig functionDef) {
		this.functionDefs.add(functionDef);
	}
	
	public List<FunctionDefConfig> getFunctionDefinitions() {
		return this.functionDefs;
	}
	
	public void setFunctionDefinitions(List<FunctionDefConfig> functionDefs) {
		this.functionDefs = functionDefs;
	}
	
	public List<String> getConditionOperators() {
		return conditionOperators;
	}

	public void setConditionOperators(List<String> conditionOperators) {
		this.conditionOperators = conditionOperators;
	}
	
	public void addConditionOperator(String operator) {
		this.conditionOperators.add(operator);
	}

	public List<String> getExpressionOperators() {
		return expressionOperators;
	}

	public void setExpressionOperators(List<String> expressionOperators) {
		this.expressionOperators = expressionOperators;
	}
	
	public void addExpressionOperator(String operator) {
		this.expressionOperators.add(operator);
	}

	public List<String> getWebflowTypes() {
		return webflowTypes;
	}

	public void setWebflowTypes(List<String> webflowTypes) {
		this.webflowTypes = webflowTypes;
	}
	
	public void addWebflowType(String webflowType) {
		this.webflowTypes.add(webflowType);
	}

	public List<String> getBrowserToExecute() {
		return browserToExecute;
	}

	public void setBrowserToExecute(List<String> browserToExecute) {
		this.browserToExecute = browserToExecute;
	}
	
	public void addBrowserToExecute(String browser) {
		this.browserToExecute.add(browser);
	}

	public List<String> getVariableTypes() {
		return variableTypes;
	}

	public void setVariableTypes(List<String> variableTypes) {
		this.variableTypes = variableTypes;
	}
	
	public void addVariableType(String variableType) {
		this.variableTypes.add(variableType);
	}
	
}
