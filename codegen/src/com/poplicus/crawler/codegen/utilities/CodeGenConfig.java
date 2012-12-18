package com.poplicus.crawler.codegen.utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import com.poplicus.crawler.codegen.Constants;
import com.poplicus.crawler.codegen.utilities.config.CodeGenDefConfig;
import com.poplicus.crawler.codegen.utilities.config.ColumnDefConfig;
import com.poplicus.crawler.codegen.utilities.config.ElementDefConfig;
import com.poplicus.crawler.codegen.utilities.config.FunctionDefConfig;
import com.poplicus.crawler.codegen.utilities.config.ParameterDefConfig;
import com.poplicus.crawler.codegen.utilities.config.SupportingFunctionDefConfig;
import com.poplicus.crawler.codegen.utilities.config.TableDefConfig;

public class CodeGenConfig {

	public CodeGenDefConfig getCodeGenConfiguration(String fileName) throws Exception {
		CodeGenDefConfig codeGenDef = null;
		Document codeGenDoc = null;
		try {
			codeGenDoc = getCodeGenDefinitionDocument(fileName);
			codeGenDef = new CodeGenDefConfig();
			codeGenDef.setConditionOperators(getConditionOperators(codeGenDoc));
			codeGenDef.setExpressionOperators(getExpressionOperator(codeGenDoc));
			codeGenDef.setWebflowTypes(getWebFlowTypes(codeGenDoc));
			codeGenDef.setBrowserToExecute(getBrowsersToExecute(codeGenDoc));
			codeGenDef.setVariableTypes(getVariableTypes(codeGenDoc));
			codeGenDef.setFunctionDefinitions(getFunctionDefinitions(codeGenDoc));
			codeGenDef.setElementDefinitions(getElementDefinitions(codeGenDoc));
			codeGenDef.setTableDefinitions(getTableDefinitions(codeGenDoc));
		} catch (Exception ex) {
			throw ex;
		}
		return codeGenDef;
	}
	
    private Document getCodeGenDefinitionDocument(String filename) throws Exception {
    	DocumentBuilderFactory factory = null;
    	Document doc = null;
        try {
        	factory = DocumentBuilderFactory.newInstance();
            doc = factory.newDocumentBuilder().parse(new File(filename));
        } catch (Exception ex) {
        	throw ex;
        }
        return doc;
    }
    
    private List<String> getConditionOperators(Document codeGenDoc) throws Exception {
    	List<String> conditionOperators = new ArrayList<String>();
    	XPathFactory factory = XPathFactory.newInstance();
    	XPath path = factory.newXPath();
    	String root = "//condition-operators/operator";
    	XPathExpression expression = path.compile(root);
    	NodeList childs = (NodeList) expression.evaluate(codeGenDoc, XPathConstants.NODESET);
    	for(int index = 0; index < childs.getLength(); index ++) {
    		if(childs.item(index).getNodeName().equalsIgnoreCase(Constants.OPERATOR_TAG)) {
    			conditionOperators.add(childs.item(index).getTextContent());
    		}
    	}
    	return conditionOperators;
    }

    private List<String> getExpressionOperator(Document codeGenDoc) throws Exception {
    	List<String> expressionOperators = new ArrayList<String>();
    	XPathFactory factory = XPathFactory.newInstance();
    	XPath path = factory.newXPath();
    	String root = "//expression-operators/operator";
    	XPathExpression expression = path.compile(root);
    	NodeList childs = (NodeList) expression.evaluate(codeGenDoc, XPathConstants.NODESET);
    	for(int index = 0; index < childs.getLength(); index ++) {
    		if(childs.item(index).getNodeName().equalsIgnoreCase(Constants.OPERATOR_TAG)) {
    			expressionOperators.add(childs.item(index).getTextContent());
    		}
    	}
    	return expressionOperators;
    }

    private List<String> getWebFlowTypes(Document codeGenDoc) throws Exception {
    	List<String> webflowTypes = new ArrayList<String>();
    	XPathFactory factory = XPathFactory.newInstance();
    	XPath path = factory.newXPath();
    	String root = "//webflow-types/type";
    	XPathExpression expression = path.compile(root);
    	NodeList childs = (NodeList) expression.evaluate(codeGenDoc, XPathConstants.NODESET);
    	for(int index = 0; index < childs.getLength(); index ++) {
    		if(childs.item(index).getNodeName().equalsIgnoreCase(Constants.TYPE_TAG)) {
    			webflowTypes.add(childs.item(index).getTextContent());
    		}
    	}
    	return webflowTypes;
    }

    private List<String> getBrowsersToExecute(Document codeGenDoc) throws Exception {
    	List<String> browsers = new ArrayList<String>();
    	XPathFactory factory = XPathFactory.newInstance();
    	XPath path = factory.newXPath();
    	String root = "//browsers-to-execute/browser";
    	XPathExpression expression = path.compile(root);
    	NodeList childs = (NodeList) expression.evaluate(codeGenDoc, XPathConstants.NODESET);
    	for(int index = 0; index < childs.getLength(); index ++) {
    		if(childs.item(index).getNodeName().equalsIgnoreCase(Constants.BROWSER_TAG)) {
    			browsers.add(childs.item(index).getTextContent());
    		}
    	}
    	return browsers;
    }

    private List<String> getVariableTypes(Document codeGenDoc) throws Exception {
    	List<String> browsers = new ArrayList<String>();
    	XPathFactory factory = XPathFactory.newInstance();
    	XPath path = factory.newXPath();
    	String root = "//variable-types/type";
    	XPathExpression expression = path.compile(root);
    	NodeList childs = (NodeList) expression.evaluate(codeGenDoc, XPathConstants.NODESET);
    	for(int index = 0; index < childs.getLength(); index ++) {
    		if(childs.item(index).getNodeName().equalsIgnoreCase(Constants.TYPE_TAG)) {
    			browsers.add(childs.item(index).getTextContent());
    		}
    	}
    	return browsers;
    }
    
    private List<FunctionDefConfig> getFunctionDefinitions(Document codeGenDoc) throws Exception {
    	List<FunctionDefConfig> functionDefs = new ArrayList<FunctionDefConfig>();
    	FunctionDefConfig funcDef = null;
    	List<ParameterDefConfig> paramDefs = null;
    	ParameterDefConfig paramDef = null;
    	List<SupportingFunctionDefConfig> supportingFunDefs = null;
    	SupportingFunctionDefConfig supFunDef = null;
    	List<ParameterDefConfig> supportParamDefs = null;
    	NamedNodeMap attrs = null;
    	NamedNodeMap paramAttrs = null;
    	NamedNodeMap sFuncAttrs = null;
    	NamedNodeMap sfParamAttrs = null;
    	XPathFactory factory = XPathFactory.newInstance();
    	XPath path = factory.newXPath();
    	String root = "//function-definitions/function-definition";
    	XPathExpression expression = path.compile(root);
    	NodeList childs = (NodeList) expression.evaluate(codeGenDoc, XPathConstants.NODESET);
    	NodeList functionChilds = null;
    	NodeList params = null;
    	NodeList supportingFuncs = null;
    	NodeList sfChilds = null;
    	NodeList sfParams = null;
    	for(int index = 0; index < childs.getLength(); index ++) {
    		funcDef = new FunctionDefConfig();
    		supportingFunDefs = new ArrayList<SupportingFunctionDefConfig>();
    		attrs = childs.item(index).getAttributes();
    		funcDef.setPackageName(attrs.getNamedItem(Constants.C_ATTR_PACKAGE_NAME).getNodeValue());
    		funcDef.setClassName(attrs.getNamedItem(Constants.C_ATTR_CLASS_NAME).getNodeValue());
    		funcDef.setName(attrs.getNamedItem(Constants.C_ATTR_NAME).getNodeValue());
    		funcDef.setStaticAccess(new Boolean(attrs.getNamedItem(Constants.C_ATTR_STATIC_ACCESS).getNodeValue()).booleanValue());
    		funcDef.setInstanceAccess(new Boolean(attrs.getNamedItem(Constants.C_ATTR_INSTANCE_ACCESS).getNodeValue()).booleanValue());
    		funcDef.setVariableReferenceName(attrs.getNamedItem(Constants.C_ATTR_VARIABLE_REFERENCE_NAME).getNodeValue());
    		functionChilds = childs.item(index).getChildNodes();
    		for(int fIndex = 0; fIndex < functionChilds.getLength(); fIndex ++) {
    			if(functionChilds.item(fIndex).getNodeName().equals(Constants.PARAMETERS_TAG)) {
    				paramDefs = new ArrayList<ParameterDefConfig>();
    				params = functionChilds.item(fIndex).getChildNodes();
    				for(int pIndex = 0; pIndex < params.getLength(); pIndex ++) {
    					if(params.item(pIndex).getNodeName().equals(Constants.PARAMETER_TAG)) {
        					paramDef = new ParameterDefConfig();
        					paramAttrs = params.item(pIndex).getAttributes();
        					paramDef.setName(paramAttrs.getNamedItem(Constants.C_ATTR_NAME).getNodeValue());
        					paramDef.setType(paramAttrs.getNamedItem(Constants.C_ATTR_TYPE).getNodeValue());
        					paramDef.setValue(paramAttrs.getNamedItem(Constants.C_ATTR_VALUE).getNodeValue());
        					paramDef.setPassByValue(new Boolean(paramAttrs.getNamedItem(Constants.C_ATTR_PASS_BY_VALUE).getNodeValue()).booleanValue());
        					paramDef.setPassByReference(new Boolean(paramAttrs.getNamedItem(Constants.C_ATTR_PASS_BY_REFERENCE).getNodeValue()).booleanValue());
        					paramDefs.add(paramDef);
    					}
    				}
    				funcDef.setParameters(paramDefs);
    			} else if(functionChilds.item(fIndex).getNodeName().equals(Constants.CONFIG_SUPPORTING_FUNCTIONS_TAG)) {
    				supportingFuncs = functionChilds.item(fIndex).getChildNodes();
    				for(int sIndex = 0; sIndex < supportingFuncs.getLength(); sIndex ++) {
    					if(supportingFuncs.item(sIndex).getNodeName().equals(Constants.CONFIG_SUPPORTING_FUNCTION_TAG)) {
        					supFunDef = new SupportingFunctionDefConfig();
        					sFuncAttrs = supportingFuncs.item(sIndex).getAttributes();
        					supFunDef.setName(sFuncAttrs.getNamedItem(Constants.C_ATTR_NAME).getNodeValue());
        					sfChilds = supportingFuncs.item(sIndex).getChildNodes();
        					for(int spIndex = 0; spIndex < sfChilds.getLength(); spIndex ++) {
        						if(sfChilds.item(spIndex).getNodeName().equals(Constants.PARAMETERS_TAG)) {
        							supportParamDefs = new ArrayList<ParameterDefConfig>();
        							sfParams = functionChilds.item(fIndex).getChildNodes();
        		    				for(int pIndex = 0; pIndex < sfParams.getLength(); pIndex ++) {
        		    					if(sfParams.item(pIndex).getNodeName().equals(Constants.PARAMETER_TAG)) {
            		    					paramDef = new ParameterDefConfig();
            		    					sfParamAttrs = sfParams.item(pIndex).getAttributes();
            		    					paramDef.setName(sfParamAttrs.getNamedItem(Constants.C_ATTR_NAME).getNodeValue());
            		    					paramDef.setType(sfParamAttrs.getNamedItem(Constants.C_ATTR_TYPE).getNodeValue());
            		    					paramDef.setValue(sfParamAttrs.getNamedItem(Constants.C_ATTR_VALUE).getNodeValue());
            		    					paramDef.setPassByValue(new Boolean(sfParamAttrs.getNamedItem(Constants.C_ATTR_PASS_BY_VALUE).getNodeValue()).booleanValue());
            		    					paramDef.setPassByReference(new Boolean(sfParamAttrs.getNamedItem(Constants.C_ATTR_PASS_BY_REFERENCE).getNodeValue()).booleanValue());
            		    					supportParamDefs.add(paramDef);
        		    					}
        		    				}
        		    				supFunDef.setParameters(supportParamDefs);
        						}
        					}
        					supportingFunDefs.add(supFunDef);
    					}
    				}
    				funcDef.setSupportingFunctions(supportingFunDefs);
    			}
    		}
    		functionDefs.add(funcDef);
    	}
    	return functionDefs;
    }

    private List<ElementDefConfig> getElementDefinitions(Document codeGenDoc) throws Exception {
    	List<ElementDefConfig> elementsDef = new ArrayList<ElementDefConfig>();
    	List<String> supportedFields = null;
    	ElementDefConfig elemDef = null;
    	XPathFactory factory = XPathFactory.newInstance();
    	XPath path = factory.newXPath();
    	String root = "//element-definitions/element-definition";
    	XPathExpression expression = path.compile(root);
    	NodeList childs = (NodeList) expression.evaluate(codeGenDoc, XPathConstants.NODESET);
    	NodeList elemDefChilds = null;
    	NodeList fields = null;
    	NamedNodeMap eAttrs = null;
    	for(int eIndex = 0; eIndex < childs.getLength(); eIndex ++) {
    		elemDef = new ElementDefConfig();
    		supportedFields = new ArrayList<String>();
    		eAttrs = childs.item(eIndex).getAttributes();
    		elemDef.setTagType(eAttrs.getNamedItem(Constants.C_ATTR_TAG_TYPE).getNodeValue());
    		elemDefChilds = childs.item(eIndex).getChildNodes();
    		for(int ecIndex = 0; ecIndex < elemDefChilds.getLength(); ecIndex ++) {
    			if(elemDefChilds.item(ecIndex).getNodeName().equals(Constants.SUPPORTED_FIELDS_TAG)) {
    				fields = elemDefChilds.item(ecIndex).getChildNodes();
    				for(int fIndex = 0; fIndex < fields.getLength(); fIndex ++) {
    					if(fields.item(fIndex).getNodeName().equals(Constants.FIELD_TAG)) {
    						supportedFields.add(fields.item(fIndex).getTextContent());
    					}
    				}
    			}
    			elemDef.setSupportedFields(supportedFields);
    		}
    		elementsDef.add(elemDef);
    	}
    	return elementsDef;
    }

    private List<TableDefConfig> getTableDefinitions(Document codeGenDoc) throws Exception {
    	List<TableDefConfig> tablesDef = new ArrayList<TableDefConfig>();
    	TableDefConfig tableDef = null;
    	ColumnDefConfig colDef = null;
    	XPathFactory factory = XPathFactory.newInstance();
    	XPath path = factory.newXPath();
    	String root = "//supported-tables/table-definition";
    	XPathExpression expression = path.compile(root);
    	NodeList childs = (NodeList) expression.evaluate(codeGenDoc, XPathConstants.NODESET);
    	NodeList tableDefChilds = null;
    	NodeList defaultColValues = null;
    	NamedNodeMap tAttrs = null;
    	NamedNodeMap cAttrs = null;
    	NamedNodeMap defaultValueAttrs = null;
    	for(int eIndex = 0; eIndex < childs.getLength(); eIndex ++) {
    		tableDef = new TableDefConfig();
    		tAttrs = childs.item(eIndex).getAttributes();
    		tableDef.setName(tAttrs.getNamedItem(Constants.C_ATTR_NAME).getNodeValue());	
    		tableDef.setDisplayName(tAttrs.getNamedItem(Constants.C_ATTR_DISPLAY_NAME).getNodeValue());	
    		tableDef.setDisplayInUI(new Boolean(tAttrs.getNamedItem(Constants.C_ATTR_SHOW_IN_UI).getNodeValue()).booleanValue());
    		tableDef.setUsedInBidCrawler(
    			new Boolean(tAttrs.getNamedItem(Constants.C_ATTR_USED_IN_BID_CRAWLER).getNodeValue()).booleanValue());
    		tableDef.setUsedInOrgCrawler(
    			new Boolean(tAttrs.getNamedItem(Constants.C_ATTR_USED_IN_ORG_CRAWLER).getNodeValue()).booleanValue());
    		tableDef.setUsedInPlanHolderCrawler(
    			new Boolean(tAttrs.getNamedItem(Constants.C_ATTR_USED_IN_PLAN_HOLDERS_CRAWLER).getNodeValue()).booleanValue());
    		tableDef.setUsedInBidResultsCrawler(
    			new Boolean(tAttrs.getNamedItem(Constants.C_ATTR_USED_IN_BID_RESULTS_CRAWLER).getNodeValue()).booleanValue());
    		tableDefChilds = childs.item(eIndex).getChildNodes();
    		for(int ecIndex = 0; ecIndex < tableDefChilds.getLength(); ecIndex ++) {
    			if(tableDefChilds.item(ecIndex).getNodeName().equals(Constants.COLUMN_DEFINITION_TAG)) {
    				colDef = new ColumnDefConfig();
    				cAttrs = tableDefChilds.item(ecIndex).getAttributes();
    				colDef.setName(cAttrs.getNamedItem(Constants.C_ATTR_NAME).getNodeValue());
    				colDef.setDataType(cAttrs.getNamedItem(Constants.C_ATTR_DATA_TYPE).getNodeValue());
    				colDef.setVariableName(cAttrs.getNamedItem(Constants.C_ATTR_VARIABLE_NAME).getNodeValue());
    				tableDef.addColumnDefinition(colDef);
    			} else if(tableDefChilds.item(ecIndex).getNodeName().equals(Constants.DEFAULT_COLUMN_VALUE_MAPPING_TAG)) {
    				defaultColValues = tableDefChilds.item(ecIndex).getChildNodes();
    				for(int dcIndex = 0; dcIndex < defaultColValues.getLength(); dcIndex ++) {
    					if(defaultColValues.item(dcIndex).getNodeName().equals(Constants.DEFAULT_VALUE_TAG)) {
    						colDef = new ColumnDefConfig();
    						defaultValueAttrs = defaultColValues.item(dcIndex).getAttributes();
    						colDef.setName(defaultValueAttrs.getNamedItem(Constants.C_ATTR_COLUMN_NAME).getNodeValue());
    						colDef.setDefaultValue(defaultValueAttrs.getNamedItem(Constants.C_ATTR_DEFAULT_VALUE).getNodeValue());
    						colDef.setPassByValue(new Boolean(defaultValueAttrs.getNamedItem(Constants.C_ATTR_PASS_BY_VALUE).getNodeValue()).booleanValue());
    						tableDef.addDefaultValueColumn(colDef);
    					}
    				}
    			}
    		}
    		tablesDef.add(tableDef);
    	}
    	return tablesDef;
    }

}
