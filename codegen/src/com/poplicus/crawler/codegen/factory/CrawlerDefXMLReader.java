package com.poplicus.crawler.codegen.factory;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.poplicus.crawler.codegen.Constants;
import com.poplicus.crawler.codegen.definitions.ActionDefinition;
import com.poplicus.crawler.codegen.definitions.BrowserDefinition;
import com.poplicus.crawler.codegen.definitions.ConditionDefinition;
import com.poplicus.crawler.codegen.definitions.ConditionNameGenerator;
import com.poplicus.crawler.codegen.definitions.Conditions;
import com.poplicus.crawler.codegen.definitions.ConstructorDefinition;
import com.poplicus.crawler.codegen.definitions.CrawlerDefinition;
import com.poplicus.crawler.codegen.definitions.DataGroupDefinition;
import com.poplicus.crawler.codegen.definitions.DataSetDefinition;
import com.poplicus.crawler.codegen.definitions.DatumDefinition;
import com.poplicus.crawler.codegen.definitions.ElementDefinition;
import com.poplicus.crawler.codegen.definitions.ElseDefinition;
import com.poplicus.crawler.codegen.definitions.ElseIfDefinition;
import com.poplicus.crawler.codegen.definitions.ExpressionDefinition;
import com.poplicus.crawler.codegen.definitions.FunctionDefinition;
import com.poplicus.crawler.codegen.definitions.IfDefinition;
import com.poplicus.crawler.codegen.definitions.LHSDefinition;
import com.poplicus.crawler.codegen.definitions.LineOfCodeDefinition;
import com.poplicus.crawler.codegen.definitions.MainIfDefinition;
import com.poplicus.crawler.codegen.definitions.NavigateDefinition;
import com.poplicus.crawler.codegen.definitions.OperandDefinition;
import com.poplicus.crawler.codegen.definitions.OperatorDefinition;
import com.poplicus.crawler.codegen.definitions.RightHSDefinition;
import com.poplicus.crawler.codegen.definitions.SourceMapConfig;
import com.poplicus.crawler.codegen.definitions.StringDefinition;
import com.poplicus.crawler.codegen.definitions.VariableDefinition;
import com.poplicus.crawler.codegen.definitions.WebPageDefinition;
import com.poplicus.crawler.codegen.definitions.BrowserDefinition.WhichBrowser;
import com.poplicus.crawler.codegen.definitions.ConstructorDefinition.ConstructorType;
import com.poplicus.crawler.codegen.definitions.Parameters;
import com.poplicus.crawler.codegen.definitions.ParameterDefinition;
import com.poplicus.crawler.codegen.definitions.WebPages;

public class CrawlerDefXMLReader {

	private static ConditionNameGenerator conditionNameGen = ConditionNameGenerator.getInstance();

	private static ConditionCache conditionCache = ConditionCache.getInstance();

	public CrawlerDefinition getCrawlerDefinition(String fileName) throws Exception {
		CrawlerDefinition crawlerDef = null;
		Document crawlerDoc = null;
		try {
			crawlerDoc = getCrawlerDefinitionDocument(fileName);
			crawlerDef = new CrawlerDefinition();
			updateCrawlerDefinition(crawlerDoc, crawlerDef);
			crawlerDef.setCurrentConstructor(getConstructorDefinition(crawlerDoc, ConstructorType.CurrentConstructor));
			crawlerDef.setSuperConstructor(getConstructorDefinition(crawlerDoc, ConstructorType.SuperConstructor));
			crawlerDef.setPrimaryBrowserDefinition(getBrowserDefinition(crawlerDoc, WhichBrowser.Primary));
			crawlerDef.setSecondaryBrowserDefinition(getBrowserDefinition(crawlerDoc, WhichBrowser.Secondary));
			crawlerDef.setTertiaryBrowserDefinition(getBrowserDefinition(crawlerDoc, WhichBrowser.Tertiary));
			crawlerDef.setWebPages(getWebPages(crawlerDoc));
		} catch (Exception ex) {
			throw ex;
		}
		return crawlerDef;
	}

    private Document getCrawlerDefinitionDocument(String filename) throws Exception {
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

    private WebPages getWebPages(Document crawlerDoc) throws Exception {
    	WebPages webPages = new WebPages();
    	XPathFactory factory = XPathFactory.newInstance();
    	XPath path = factory.newXPath();
    	XPathExpression expression = path.compile("//web-flows/web-flow");
    	NodeList nlOfWebPages = (NodeList) expression.evaluate(crawlerDoc, XPathConstants.NODESET);
    	int noOfWebPages = nlOfWebPages.getLength();
    	if(noOfWebPages > 0) {
    		for(int wpIndex = 0; wpIndex < noOfWebPages; wpIndex ++) {
    			webPages.addWebPage(getWebPageDefinition(nlOfWebPages.item(wpIndex)));
    		}
    	}
    	return webPages;
    }

    private WebPageDefinition getWebPageDefinition(Node node) throws Exception {
    	WebPageDefinition webPage = new WebPageDefinition();
    	populateAttributesOfWebPage(node, webPage);
    	NodeList childNodes = node.getChildNodes();
    	int noOfChildNodes = childNodes.getLength();
    	for(int childIndex = 0; childIndex < noOfChildNodes; childIndex ++) {
    		if(childNodes.item(childIndex).getNodeName().equalsIgnoreCase(Constants.URL_TAG)) {
    			webPage.setUrl(childNodes.item(childIndex).getTextContent());
    	    } else if(childNodes.item(childIndex).getNodeName().equalsIgnoreCase(Constants.CONDITIONS_TAG)) {
    			populateConditions(childNodes.item(childIndex), webPage);
    		} else if(childNodes.item(childIndex).getNodeName().equalsIgnoreCase(Constants.MAIN_IF_TAG)) {
    			populateMainIf(childNodes.item(childIndex), webPage);
    		} else if(childNodes.item(childIndex).getNodeName().equalsIgnoreCase(Constants.RESULTSET_GROUP_TAG)) {
    			populateResultSetGroup(childNodes.item(childIndex), webPage);
    		}
    	}
    	return webPage;
    }

    private void populateResultSetGroup(Node node, WebPageDefinition webPage) throws Exception {
    	DataGroupDefinition dGroupDef = new DataGroupDefinition();
    	DataSetDefinition dSetDef = null;
    	DatumDefinition datumDef = null;
    	Node resultSetNode = null;
    	NodeList datumNodes = null;
    	NodeList childNodes = node.getChildNodes();
    	NodeList datumChilds = null;
    	NamedNodeMap datumAttrs = null;
    	int noOfChildNodes = childNodes.getLength();
    	int noOfDatums = -1;
    	int noOfDatumChilds = -1;
    	for(int childIndex = 0; childIndex < noOfChildNodes; childIndex ++) {
    		if(childNodes.item(childIndex).getNodeName().equalsIgnoreCase(Constants.START_TAG)) {
    			dGroupDef.setStartTag(childNodes.item(childIndex).getTextContent());
    	    } else if(childNodes.item(childIndex).getNodeName().equalsIgnoreCase(Constants.END_TAG)) {
    	    	dGroupDef.setEndTag(childNodes.item(childIndex).getTextContent());
    		} else if(childNodes.item(childIndex).getNodeName().equalsIgnoreCase(Constants.RESULTSET_TAG)) {
    			resultSetNode = childNodes.item(childIndex);
    			dSetDef = new DataSetDefinition(
    				resultSetNode.getAttributes().getNamedItem(Constants.C_ATTR_TABLE_NAME).getNodeValue(), null);
    			datumNodes = resultSetNode.getChildNodes();
    			noOfDatums = datumNodes.getLength();
    			for(int datumIndex = 0; datumIndex < noOfDatums; datumIndex ++) {
    				if(datumNodes.item(datumIndex).getNodeName().equals(Constants.DATUM_TAG)) {
        				datumAttrs = datumNodes.item(datumIndex).getAttributes();
        				datumDef = new DatumDefinition(datumAttrs.getNamedItem(Constants.C_ATTR_COLUMN_NAME).getNodeValue(),
    						datumAttrs.getNamedItem(Constants.C_ATTR_DATA_TYPE).getNodeValue(),
    						datumAttrs.getNamedItem(Constants.C_ATTR_VARIABLE_NAME).getNodeValue(),
    						datumAttrs.getNamedItem(Constants.C_ATTR_HTML_SOURCE).getNodeValue());
        				datumDef.setAfterTag(
        					new Boolean(datumAttrs.getNamedItem(Constants.C_ATTR_AFTER_TAG).getNodeValue()).booleanValue());
        				datumDef.setEntireString(
        					new Boolean(datumAttrs.getNamedItem(Constants.C_ATTR_ENTIRE_STRING).getNodeValue()).booleanValue());
        				datumDef.setUniqueName(datumAttrs.getNamedItem(Constants.C_ATTR_UNIQUE_ID).getNodeValue());
        				datumDef.setConfigurable(
        					new Boolean(datumAttrs.getNamedItem(Constants.C_ATTR_IS_CONFIGURABLE).getNodeValue()).booleanValue());
        				datumChilds = datumNodes.item(datumIndex).getChildNodes();
        				noOfDatumChilds = datumChilds.getLength();
        				for(int dChildIndex = 0; dChildIndex < noOfDatumChilds; dChildIndex ++) {
        					if(datumChilds.item(dChildIndex).getNodeName().equalsIgnoreCase(Constants.START_TAG)) {
        						datumDef.setStartTag(datumChilds.item(dChildIndex).getTextContent());
        		    	    } else if(datumChilds.item(dChildIndex).getNodeName().equalsIgnoreCase(Constants.END_TAG)) {
        		    	    	datumDef.setEndTag(datumChilds.item(dChildIndex).getTextContent());
        		    		}
        				}
        				dSetDef.addDatum(datumDef);
    				}
    			}
    			dGroupDef.addDataSet(dSetDef);
    		}
    	}
    	webPage.addDataGroup(dGroupDef);
    }

    private void populateMainIf(Node node, WebPageDefinition webPage) throws Exception {
    	MainIfDefinition mainIf = new MainIfDefinition();
    	NodeList mainIfChilds = node.getChildNodes();
    	int noOfMainIfChilds = mainIfChilds.getLength();
    	Node tempNode = null;
    	for(int mainChildIndex = 0; mainChildIndex < noOfMainIfChilds; mainChildIndex ++) {
    		tempNode = mainIfChilds.item(mainChildIndex);
    		if(tempNode.getNodeName().equalsIgnoreCase(Constants.CONDITIONS_TAG)) {
    			mainIf.setConditions(getConditions(tempNode));
    		} else if(tempNode.getNodeName().equalsIgnoreCase(Constants.IF_TAG)) {
    			mainIf.setMainIfElementsDef(getIfDefinition(tempNode));
    		} else if(tempNode.getNodeName().equalsIgnoreCase(Constants.ELSE_IF_TAG)) {
    			mainIf.setMainIfElementsDef(getElseIfDefinition(tempNode));
    		} else if(tempNode.getNodeName().equalsIgnoreCase(Constants.ELSE_TAG)) {
    			mainIf.setMainIfElementsDef(getElseDefinition(tempNode));
    		} else if(tempNode.getNodeName().equalsIgnoreCase(Constants.LINE_OF_CODE_TAG)) {
    			mainIf.setMainIfElementsDef(getLOCDefinition(tempNode));
    		} else if(tempNode.getNodeName().equalsIgnoreCase(Constants.ACTION_TAG)) {
    			mainIf.setMainIfElementsDef(getActionDefinition(tempNode));
    		}
    	}
    	webPage.setMainIF(mainIf);
    }

//    private CodeBlockDefinition getCodeBlockElements(Node node) throws Exception {
//    	CodeBlockDefinition cbDef = new CodeBlockDefinition();
//    	NodeList cbChilds = node.getChildNodes();
//    	Node child = null;
//    	for(int index = 0; index < cbChilds.getLength(); index ++) {
//    		child = cbChilds.item(index);
//    		// this list needs to grow...
//    		if(child.getNodeName().equalsIgnoreCase(Constants.LINE_OF_CODE_TAG)) {
//    			//cbDef.setCodeBlockElement(getLOCDefinition(child)); ========= ********** Need to fix this *********** ==================
//    		} else if(child.getNodeName().equalsIgnoreCase(Constants.ACTION_TAG)) {
//    			//cbDef.setCodeBlockElement(getActionDefinition(child)); ========= ********** Need to fix this *********** ==================
//    		}
//    	}
//    	return cbDef;
//    }

    private IfDefinition getIfDefinition(Node node) throws Exception {
    	IfDefinition ifDefinition = new IfDefinition();
    	NodeList childs = node.getChildNodes();
    	int noOfChilds = childs.getLength();
    	Node tempNode = null;
    	for(int childIndex = 0; childIndex < noOfChilds; childIndex ++) {
    		tempNode = childs.item(childIndex);
    		if(tempNode.getNodeName().equalsIgnoreCase(Constants.CONDITIONS_TAG)) {
    			ifDefinition.setConditions(getConditions(tempNode));
    		} else if(tempNode.getNodeName().equalsIgnoreCase(Constants.IF_TAG)) {
    			ifDefinition.setIfElseElement(getIfDefinition(tempNode));
    		} else if(tempNode.getNodeName().equalsIgnoreCase(Constants.ELSE_IF_TAG)) {
    			ifDefinition.setIfElseElement(getElseIfDefinition(tempNode));
    		} else if(tempNode.getNodeName().equalsIgnoreCase(Constants.ELSE_TAG)) {
    			ifDefinition.setIfElseElement(getElseDefinition(tempNode));
    		} else if(tempNode.getNodeName().equalsIgnoreCase(Constants.LINE_OF_CODE_TAG)) {
    			ifDefinition.setIfElseElement(getLOCDefinition(tempNode));
    		} else if(tempNode.getNodeName().equalsIgnoreCase(Constants.ACTION_TAG)) {
    			ifDefinition.setIfElseElement(getActionDefinition(tempNode));
    		}
    	}

    	return ifDefinition;
    }

    private ElseIfDefinition getElseIfDefinition(Node node) throws Exception {
    	ElseIfDefinition elseIfDefinition = new ElseIfDefinition();
    	NodeList childs = node.getChildNodes();
    	int noOfChilds = childs.getLength();
    	Node tempNode = null;
    	for(int childIndex = 0; childIndex < noOfChilds; childIndex ++) {
    		tempNode = childs.item(childIndex);
    		if(tempNode.getNodeName().equalsIgnoreCase(Constants.CONDITIONS_TAG)) {
    			elseIfDefinition.setConditions(getConditions(tempNode));
    		} else if(tempNode.getNodeName().equalsIgnoreCase(Constants.IF_TAG)) {
    			elseIfDefinition.setIfElseElement(getIfDefinition(tempNode));
    		} else if(tempNode.getNodeName().equalsIgnoreCase(Constants.ELSE_IF_TAG)) {
    			elseIfDefinition.setIfElseElement(getElseIfDefinition(tempNode));
    		} else if(tempNode.getNodeName().equalsIgnoreCase(Constants.ELSE_TAG)) {
    			elseIfDefinition.setIfElseElement(getElseDefinition(tempNode));
    		} else if(tempNode.getNodeName().equalsIgnoreCase(Constants.LINE_OF_CODE_TAG)) {
    			elseIfDefinition.setIfElseElement(getLOCDefinition(tempNode));
    		} else if(tempNode.getNodeName().equalsIgnoreCase(Constants.ACTION_TAG)) {
    			elseIfDefinition.setIfElseElement(getActionDefinition(tempNode));
    		}
    	}

    	return elseIfDefinition;
    }

    private ElseDefinition getElseDefinition(Node node) throws Exception {
    	ElseDefinition elseDefinition = new ElseDefinition();
    	NodeList childs = node.getChildNodes();
    	int noOfChilds = childs.getLength();
    	Node tempNode = null;
    	for(int childIndex = 0; childIndex < noOfChilds; childIndex ++) {
    		tempNode = childs.item(childIndex);
    		if(tempNode.getNodeName().equalsIgnoreCase(Constants.CONDITIONS_TAG)) {
    			elseDefinition.setConditions(getConditions(tempNode));
    		} else if(tempNode.getNodeName().equalsIgnoreCase(Constants.IF_TAG)) {
    			elseDefinition.setIfElseElement(getIfDefinition(tempNode));
    		} else if(tempNode.getNodeName().equalsIgnoreCase(Constants.ELSE_IF_TAG)) {
    			elseDefinition.setIfElseElement(getElseIfDefinition(tempNode));
    		} else if(tempNode.getNodeName().equalsIgnoreCase(Constants.ELSE_TAG)) {
    			elseDefinition.setIfElseElement(getElseDefinition(tempNode));
    		} else if(tempNode.getNodeName().equalsIgnoreCase(Constants.LINE_OF_CODE_TAG)) {
    			elseDefinition.setIfElseElement(getLOCDefinition(tempNode));
    		} else if(tempNode.getNodeName().equalsIgnoreCase(Constants.ACTION_TAG)) {
    			elseDefinition.setIfElseElement(getActionDefinition(tempNode));
    		}
    	}

    	return elseDefinition;
     }

    private LineOfCodeDefinition getLOCDefinition(Node node) throws Exception {
    	LineOfCodeDefinition locDef = new LineOfCodeDefinition();
    	NodeList childOfLOC = node.getChildNodes();
    	Node tempNode = null;
    	for(int index = 0; index < childOfLOC.getLength(); index ++) {
    		tempNode = childOfLOC.item(index);
    		if(tempNode.getNodeName().equalsIgnoreCase(Constants.LHS_DEFINITION_TAG)) {
    			locDef.setLhsDefiniton(getLHSDefinition(tempNode));
    		} else if(tempNode.getNodeName().equalsIgnoreCase(Constants.RHS_DEFINITION_TAG)) {
    			locDef.setRhsDefinition(getRHSDefinition(tempNode));
    		}
    	}
    	return locDef;
    }

    private LHSDefinition getLHSDefinition(Node node) throws Exception {
    	LHSDefinition lhsDef = new LHSDefinition();
    	NamedNodeMap attrs = node.getAttributes();
    	NamedNodeMap varAttrs = null;
    	lhsDef.setType(attrs.getNamedItem(Constants.C_ATTR_TYPE).getNodeValue());
    	NodeList childs = node.getChildNodes();
    	VariableDefinition var = null;
    	Node tempNode = null;
    	NodeList varChilds = null;
    	for(int index = 0; index < childs.getLength(); index ++) {
    		tempNode = childs.item(index);
    		if(tempNode.getNodeName().equalsIgnoreCase(Constants.VARIABLE_TAG)) {
    			varAttrs = tempNode.getAttributes();
    			var = new VariableDefinition(varAttrs.getNamedItem(Constants.C_ATTR_TYPE).getNodeValue(),
    				varAttrs.getNamedItem(Constants.C_ATTR_NAME).getNodeValue(), null);
    				//tempNode.getFirstChild().getTextContent());
    			varChilds = tempNode.getChildNodes();
    			for(int childIndex = 0; childIndex < varChilds.getLength(); childIndex ++) {
    				if(varChilds.item(childIndex).getNodeName().equalsIgnoreCase(Constants.VALUE_TAG)) {
    					var.setValue(varChilds.item(childIndex).getTextContent());
    				}
    			}
        		lhsDef.setVariableDefinition(var);
    		}
    	}
    	return lhsDef;
    }

    private RightHSDefinition getRHSDefinition(Node node) throws Exception {
    	RightHSDefinition rhsDef = new RightHSDefinition();
//    	ExpressionDefinition expression = new ExpressionDefinition();
    	NamedNodeMap attrs = node.getAttributes();
    	rhsDef.setType(attrs.getNamedItem(Constants.C_ATTR_TYPE).getNodeValue());
    	NodeList rhsChilds = node.getChildNodes();
    	for(int rhsIndex = 0; rhsIndex < rhsChilds.getLength(); rhsIndex ++) {
    		if(rhsChilds.item(rhsIndex).getNodeName().equalsIgnoreCase(Constants.EXPRESSION_TAG)) {
//    	    	NodeList expressionChilds = rhsChilds.item(rhsIndex).getChildNodes();
//    	    	int noOfExpressionChilds = expressionChilds.getLength();
//    	    	Node tempNode = null;
//    	    	for(int index = 0; index < noOfExpressionChilds; index ++) {
//    	    		tempNode = expressionChilds.item(index);
//    	    		if(tempNode.getNodeName().equalsIgnoreCase(Constants.OPERAND_TAG)) {
//    	    			expression.addExpressionElement(getOperandDefinition(tempNode));
//    	    		} else if(tempNode.getNodeName().equalsIgnoreCase(Constants.OPERATOR_TAG)) {
//    	    			expression.addExpressionElement(getOperatorDefinition(tempNode));
//    	    		}
//    	    	}
//    	    	rhsDef.setExpressionDef(expression);
    	    	rhsDef.setExpressionDef(getExpressionDefinition(rhsChilds.item(rhsIndex)));
    		} else if(rhsChilds.item(rhsIndex).getNodeName().equalsIgnoreCase(Constants.ACTION_TAG)) {
    			rhsDef.setActionDef(getActionDefinition(rhsChilds.item(rhsIndex)));
    		} else if(rhsChilds.item(rhsIndex).getNodeName().equalsIgnoreCase(Constants.FUNCTION_DEFINITION_TAG)) {
    			rhsDef.setFunctionDef(getFunctionDefinition(rhsChilds.item(rhsIndex)));
    		}
    	}

    	return rhsDef;
    }

    private ExpressionDefinition getExpressionDefinition(Node node) throws Exception {
    	ExpressionDefinition expression = new ExpressionDefinition();
    	NodeList expressionChilds = node.getChildNodes();
    	int noOfExpressionChilds = expressionChilds.getLength();
    	Node tempNode = null;
    	for(int index = 0; index < noOfExpressionChilds; index ++) {
    		tempNode = expressionChilds.item(index);
    		if(tempNode.getNodeName().equalsIgnoreCase(Constants.OPERAND_TAG)) {
    			expression.addExpressionElement(getOperandDefinition(tempNode));
    		} else if(tempNode.getNodeName().equalsIgnoreCase(Constants.OPERATOR_TAG)) {
    			expression.addExpressionElement(getOperatorDefinition(tempNode));
    		}
    	}
    	return expression;
    }

    private OperatorDefinition getOperatorDefinition(Node node) throws Exception {
    	OperatorDefinition operatorDef = new OperatorDefinition();
    	operatorDef.setOperator(node.getTextContent());
    	return operatorDef;
    }

    private OperandDefinition getOperandDefinition(Node node) throws Exception {
    	OperandDefinition operandDef = new OperandDefinition();
    	NodeList childNodeList = node.getChildNodes();
    	int noOfChilds = childNodeList.getLength();
    	Node tempNode = null;
    	System.out.println("Node Name : " + node.getNodeName());
    	for(int index = 0; index < noOfChilds; index ++) {
    		tempNode = childNodeList.item(index);
    		if(tempNode.getNodeName().equalsIgnoreCase(Constants.STRING_TAG)) {
//    	    	NamedNodeMap attrs = tempNode.getAttributes();
//    	    	String whatObject = attrs.getNamedItem(Constants.C_ATTR_WHAT_OBJECT).getNodeValue();
//    	    	if(whatObject.equalsIgnoreCase(Constants.VARIABLE_TAG)) {
//    	    		operandDef.setVariableDef(getVariableDefinitionFromString(tempNode));
//    	    	} else if(whatObject.equalsIgnoreCase(Constants.STRING_TAG)) {
//    	    		operandDef.setStringDef(getStringDefinition(tempNode));
//    	    	}
//    			operandDef.setOperandName(tempNode.getAttributes().getNamedItem(Constants.C_ATTR_NAME).getNodeValue());

    			//working copy
    			/*NodeList childNodes = tempNode.getChildNodes();
    			for(int childIndex = 0; childIndex < childNodes.getLength(); childIndex ++) {
    				if(childNodes.item(childIndex).getNodeName().equalsIgnoreCase(Constants.VALUE_TAG)) {
    					operandDef.setOperandName(childNodes.item(childIndex).getTextContent());
    				}
    			}*/

    			operandDef.setStringDef(getStringDefinition(tempNode));

    		} else if(tempNode.getNodeName().equalsIgnoreCase(Constants.FUNCTION_DEFINITION_TAG)) {
    			operandDef.setFunctionDef(getFunctionDefinition(tempNode));
    		} else if(tempNode.getNodeName().equalsIgnoreCase(Constants.ACTION_TAG)) {
    			operandDef.setActionDef(getActionDefinition(tempNode));
    		} else if(tempNode.getNodeName().equalsIgnoreCase(Constants.EXPRESSION_TAG)) {
    			operandDef.setExpressionDef(getExpressionDefinition(tempNode));
    		}
    	}
    	return operandDef;
    }

    private StringDefinition getStringDefinition(Node node) throws Exception {
    	NamedNodeMap attrs = node.getAttributes();
//    	StringDefinition stringDef = new StringDefinition(attrs.getNamedItem(Constants.C_ATTR_TYPE).getNodeValue(),
//    			attrs.getNamedItem(Constants.C_ATTR_NAME).getNodeValue(), null);
    	StringDefinition stringDef = new StringDefinition();
    	stringDef.setPassByValue((new Boolean(attrs.getNamedItem(Constants.C_ATTR_PASS_BY_VALUE).getNodeValue())).booleanValue());
    	stringDef.setPassByReference((new Boolean(attrs.getNamedItem(Constants.C_ATTR_PASS_BY_VALUE).getNodeValue())).booleanValue());
		NodeList childNodes = node.getChildNodes();
		for(int index = 0; index < childNodes.getLength(); index ++) {
			if(childNodes.item(index).getNodeName().equalsIgnoreCase(Constants.VALUE_TAG)) {
				stringDef.setValue(childNodes.item(index).getTextContent());
			}
		}
		return stringDef;
    }
//
//    private VariableDefinition getVariableDefinitionFromString(Node node) throws Exception {
//    	NamedNodeMap attrs = node.getAttributes();
//		VariableDefinition varDef = new VariableDefinition(attrs.getNamedItem(Constants.C_ATTR_TYPE).getNodeValue(),
//			attrs.getNamedItem(Constants.C_ATTR_NAME).getNodeValue(), null);
//		NodeList childNodes = node.getChildNodes();
//		for(int index = 0; index < childNodes.getLength(); index ++) {
//			if(childNodes.item(index).getNodeName().equalsIgnoreCase(Constants.VALUE_TAG)) {
//				varDef.setValue(childNodes.item(index).getTextContent());
//			}
//		}
//		return varDef;
//    }

    private ActionDefinition getActionDefinition(Node node) throws Exception {
    	ActionDefinition actionDef = new ActionDefinition();
    	NamedNodeMap attrs = node.getAttributes();
    	actionDef.setType(attrs.getNamedItem(Constants.C_ATTR_TYPE).getNodeValue());
    	actionDef.setBrowserType(attrs.getNamedItem(Constants.C_ATTR_BROWSER_TYPE).getNodeValue());
    	if(node.hasChildNodes()) {
        	NodeList actionChilds = node.getChildNodes();
        	Node childNode = null;
        	for(int index = 0; index < actionChilds.getLength(); index ++) {
        		childNode = actionChilds.item(index);
        		if(childNode.getNodeName().equalsIgnoreCase(Constants.ELEMENT_TAG)) {
        			actionDef.setHtmlElement(getElementDefinition(childNode));
        		} else if(childNode.getNodeName().equalsIgnoreCase(Constants.NAVIGATE_TAG)) {
        			actionDef.setHtmlElement(getNavigateDefinition(childNode));
        		} else if(childNode.getNodeName().equalsIgnoreCase(Constants.FUNCTION_DEFINITION_TAG)) {
        			actionDef.setHtmlElement(getFunctionDefinition(childNode));
        		}
        	}
    	}
    	return actionDef;
    }

    private NavigateDefinition getNavigateDefinition(Node node) throws Exception {
    	NavigateDefinition navDef = new NavigateDefinition();
    	navDef.setUrl(node.getFirstChild().getTextContent());
    	return navDef;
    }

    private ElementDefinition getElementDefinition(Node node) throws Exception {
    	ElementDefinition elemDef = new ElementDefinition();
    	NamedNodeMap attrs = node.getAttributes();
    	elemDef.setElementTagType(attrs.getNamedItem(Constants.C_ATTR_ELEMENT_TAG_TYPE).getNodeValue());
    	elemDef.setElementFieldType(attrs.getNamedItem(Constants.C_ATTR_ELEMENT_FIELD_TYPE).getNodeValue());
    	elemDef.setExactMatch((new Boolean(attrs.getNamedItem(Constants.C_ATTR_EXACT_MATCH).getNodeValue())).booleanValue());
    	NodeList childs = node.getChildNodes();
    	for(int index = 0; index < childs.getLength(); index ++) {
    		if(childs.item(index).getNodeName().equalsIgnoreCase(Constants.VALUE_TAG)) {
    	    	elemDef.setValue(childs.item(index).getTextContent());
    		}
    	}
    	return elemDef;
    }

    private FunctionDefinition getFunctionDefinition(Node node) throws Exception {
		FunctionDefinition funDef = new FunctionDefinition();
		NamedNodeMap attrs = node.getAttributes();
		funDef.setName(attrs.getNamedItem(Constants.C_ATTR_NAME).getNodeValue());
		funDef.setPackageName(attrs.getNamedItem(Constants.C_ATTR_PACKAGE_NAME).getNodeValue());
		funDef.setClassName(attrs.getNamedItem(Constants.C_ATTR_CLASS_NAME).getNodeValue());
		NodeList paramList = node.getChildNodes();
		Node paramNode = null;

		for(int paramIndex = 0; paramIndex < paramList.getLength(); paramIndex ++) {
			paramNode = paramList.item(paramIndex);
			if(paramNode.getNodeName().equalsIgnoreCase(Constants.PARAMETER_TAG)) {
				funDef.addParameter(getParameter(paramNode));
			} else if(paramNode.getNodeName().equals(Constants.SUPPORTING_FUNCTION_TAG)) {
				NamedNodeMap supportingFnAttrs = paramNode.getAttributes();
				funDef.setSupportingFunctionName(supportingFnAttrs.getNamedItem(Constants.C_ATTR_NAME).getNodeValue());
				NodeList supportFnChilds = paramNode.getChildNodes();
				for(int fnIndex = 0; fnIndex < supportFnChilds.getLength(); fnIndex ++) {
					if(supportFnChilds.item(fnIndex).getNodeName().equalsIgnoreCase(Constants.PARAMETER_TAG)) {
						funDef.addSupportingFunctionParameter(getParameter(supportFnChilds.item(fnIndex)));
					}
				}
			}
		}
		return funDef;
    }

    private ParameterDefinition getParameter(Node paramNode) throws Exception {
    	ParameterDefinition paramDef = new ParameterDefinition();
    	NamedNodeMap paramAttrs = paramNode.getAttributes();
		paramDef.setName(paramAttrs.getNamedItem(Constants.C_ATTR_NAME).getNodeValue());
		paramDef.setType(paramAttrs.getNamedItem(Constants.C_ATTR_TYPE).getNodeValue());
		paramDef.setPassByValue((new Boolean(paramAttrs.getNamedItem(Constants.C_ATTR_PASS_BY_VALUE).getNodeValue())).booleanValue());
		paramDef.setPassByReference((new Boolean(paramAttrs.getNamedItem(Constants.C_ATTR_PASS_BY_REFERENCE).getNodeValue())).booleanValue());
		NodeList valueList = paramNode.getChildNodes();
		for(int valIndex = 0; valIndex < valueList.getLength(); valIndex ++) {
			if(valueList.item(valIndex).getNodeName().equalsIgnoreCase(Constants.VALUE_TAG)) {
				paramDef.setValue(valueList.item(valIndex).getTextContent());
			}
		}
		return paramDef;
    }

    private void populateConditions(Node node, WebPageDefinition webPage) throws Exception {
    	webPage.setConditions(getConditions(node));
    }

    private Conditions getConditions(Node node) throws Exception {
    	Conditions conditions = new Conditions();
    	ConditionDefinition condition = null;
    	NamedNodeMap attrs = null;
    	NodeList childList = node.getChildNodes();
    	NodeList childsOfCondition = null;
    	Node tempNode = null;
    	int noOfChilds = childList.getLength();
    	int noOfConditionChilds = 0;
    	for(int childIndex = 0; childIndex < noOfChilds; childIndex ++) {
    		if(childList.item(childIndex).getNodeName().equalsIgnoreCase(Constants.CONDITION_TAG)) {
    			condition = new ConditionDefinition();
    			attrs = childList.item(childIndex).getAttributes();
    			condition.setName(attrs.getNamedItem(Constants.C_ATTR_NAME).getNodeValue());
    			condition.setSuperCondition((new Boolean(attrs.getNamedItem(Constants.C_ATTR_SUPER_CONDITION).getNodeValue())).booleanValue());
    			childsOfCondition = childList.item(childIndex).getChildNodes();
    			noOfConditionChilds = childsOfCondition.getLength();
    			for(int conChildIndex = 0; conChildIndex < noOfConditionChilds; conChildIndex ++) {
    				tempNode = childsOfCondition.item(conChildIndex);
    				if(tempNode.getNodeName().equalsIgnoreCase(Constants.LHS_OPERAND_TAG)) {
    					condition.setLshOperand(tempNode.getTextContent());
    				} else if(tempNode.getNodeName().equalsIgnoreCase(Constants.OPERATOR_TAG)) {
    					condition.setOperator(tempNode.getTextContent());
    				} else if(tempNode.getNodeName().equalsIgnoreCase(Constants.RHS_OPERAND_TAG)) {
    					condition.setRhsOperand(tempNode.getTextContent());
    				}
    			}
    			//condition name handling
    			if(condition.getName() == null) {
    				condition.setName(conditionNameGen.getUniqueName());
    			}
    			conditionCache.put(condition.getName(), condition.getCondition());

    			conditions.addCondition(condition);
    		}
    	}
    	return conditions;
    }

    private void populateAttributesOfWebPage(Node node, WebPageDefinition webPage) throws Exception {
    	NamedNodeMap attrs = node.getAttributes();
    	webPage.setName(attrs.getNamedItem(Constants.C_ATTR_NAME).getNodeValue());
    	webPage.setType(attrs.getNamedItem(Constants.C_ATTR_TYPE).getNodeValue());
    	webPage.setNavigationOrder(attrs.getNamedItem(Constants.C_ATTR_NAVIGATION_ORDER).getNodeValue());
    	webPage.setExecuteOnce((new Boolean(attrs.getNamedItem(Constants.C_ATTR_EXECUTE_ONCE).getNodeValue())).booleanValue());
    	webPage.setBrowser(attrs.getNamedItem(Constants.C_ATTR_BROWSER).getNodeValue());
    }

    private BrowserDefinition getBrowserDefinition(Document crawlerDoc, WhichBrowser whichBrowser) throws Exception {
    	BrowserDefinition browserDef = new BrowserDefinition();
    	String root = null;
    	XPathFactory factory = XPathFactory.newInstance();
    	XPath path = factory.newXPath();
    	if(whichBrowser.equals(WhichBrowser.Primary)) {
    		root = "//primary-browser";
    	} else if(whichBrowser.equals(WhichBrowser.Secondary)) {
    		root = "//secondary-browser";
    	} else if(whichBrowser.equals(WhichBrowser.Tertiary)) {
    		root = "//tertiary-browser";
    	}
    	browserDef.setSecuredWebPage((new Boolean((String)path.evaluate(root + "/@securedWebpage", crawlerDoc, XPathConstants.STRING))).booleanValue());
    	browserDef.setDownloadImages((new Boolean((String)path.evaluate(root + "/@downloadImages", crawlerDoc, XPathConstants.STRING))).booleanValue());
    	browserDef.setDownloadJavaScripts((new Boolean((String)path.evaluate(root + "/@downloadScripts", crawlerDoc, XPathConstants.STRING))).booleanValue());
    	//fetching conditions
    	XPathExpression expression = path.compile(root + "/conditions");
    	NodeList childs = (NodeList) expression.evaluate(crawlerDoc, XPathConstants.NODESET);
    	for(int index = 0; index < childs.getLength(); index ++) {
    		if(childs.item(index).getNodeName().equalsIgnoreCase(Constants.CONDITIONS_TAG)) {
    			browserDef.setConditions(getConditions(childs.item(index)));
    		}
    	}
    	return browserDef;
    }

    private void updateCrawlerDefinition(Document crawlerDoc, CrawlerDefinition cDef) throws Exception {
    	XPathFactory factory = XPathFactory.newInstance();
    	XPath path = factory.newXPath();
    	cDef.setName((String) path.evaluate("//@name", crawlerDoc, XPathConstants.STRING));
    	cDef.setHandlePopup((new Boolean((String)path.evaluate("//@handlePopup", crawlerDoc, XPathConstants.STRING))).booleanValue());
    	cDef.setClientName((String) path.evaluate("//@clientName", crawlerDoc, XPathConstants.STRING));
    	cDef.setSiteName((String) path.evaluate("//@siteName", crawlerDoc, XPathConstants.STRING));
    	cDef.setSourceID((String) path.evaluate("//@sourceID", crawlerDoc, XPathConstants.STRING));
    	cDef.setSourceName((String) path.evaluate("//@sourceName", crawlerDoc, XPathConstants.STRING));
    	cDef.setInvokeBasePBC((new Boolean((String)path.evaluate("//@invokeBasePBC", crawlerDoc, XPathConstants.STRING))).booleanValue());
    	cDef.setInvokeBaseSBC((new Boolean((String)path.evaluate("//@invokeBaseSBC", crawlerDoc, XPathConstants.STRING))).booleanValue());
    	cDef.setInvokeBaseTBC((new Boolean((String)path.evaluate("//@invokeBaseTBC", crawlerDoc, XPathConstants.STRING))).booleanValue());
    }

    private ConstructorDefinition getConstructorDefinition(Document crawlerDoc, ConstructorType conType) throws Exception {
    	ConstructorDefinition conDef = new ConstructorDefinition(conType);
    	XPathFactory xFactory = XPathFactory.newInstance();
    	XPath xPath = xFactory.newXPath();
    	XPathExpression xExpression = null;
    	XPathExpression xTypeExpression = null;
    	if(conType.equals(ConstructorType.CurrentConstructor)) {
    		xExpression = xPath.compile("//constructor/parameters/parameter");
    		xTypeExpression = xPath.compile("//constructor/source-map-config/@type");
    	} else if(conType.equals(ConstructorType.SuperConstructor)) {
    		xExpression = xPath.compile("//super-constructor/parameters/parameter");
    	}
    	NodeList nList = (NodeList) xExpression.evaluate(crawlerDoc, XPathConstants.NODESET);
    	int noOfParameters = nList.getLength();
    	if(noOfParameters > 0) {
    		Parameters conParameters = new Parameters();
    		ParameterDefinition parameter = null;
        	Node node = null;
        	NamedNodeMap nodeAttrs = null;
        	NodeList nListValue = null;
        	for(int paramIndex = 0; paramIndex < noOfParameters; paramIndex ++) {
        		node = nList.item(paramIndex);
        		nodeAttrs = node.getAttributes();
        		nListValue = node.getChildNodes();
        		parameter = new ParameterDefinition(nodeAttrs.getNamedItem(Constants.C_ATTR_NAME).getNodeValue(),
    				nodeAttrs.getNamedItem(Constants.C_ATTR_TYPE).getNodeValue());
        		parameter.setPassByValue((new Boolean(nodeAttrs.getNamedItem(Constants.C_ATTR_PASS_BY_VALUE).getNodeValue())).booleanValue());
        		parameter.setPassByReference((new Boolean(nodeAttrs.getNamedItem(Constants.C_ATTR_PASS_BY_REFERENCE).getNodeValue())).booleanValue());

    			for(int valIndex = 0; valIndex < nListValue.getLength(); valIndex ++) {
    				if(nListValue.item(valIndex).getNodeName().equalsIgnoreCase(Constants.VALUE_TAG)) {
    					parameter.setValue(nListValue.item(valIndex).getTextContent());
    					conParameters.addParameter(parameter);
    				}
    			}
    			conDef.setParameters(conParameters);
        	}
        	if(conType.equals(ConstructorType.CurrentConstructor)) {
    	    	SourceMapConfig sMapConfig = new SourceMapConfig((String) xTypeExpression.evaluate(crawlerDoc, XPathConstants.STRING));
    	    	conDef.setSourceMapConfig(sMapConfig);
        	}
    	}
    	return conDef;
    }

    public static void main(String[] args) throws Exception {
    	String fileName = "C:\\Poplicus\\CodeGen\\Definition\\a_a.xml";
    	CrawlerDefXMLReader reader = new CrawlerDefXMLReader();
    	reader.getCrawlerDefinition(fileName);
    	//System.out.println(reader.getCrawlerDefinition(fileName));
    }

}
