package com.poplicus.crawler.codegen.factory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.poplicus.crawler.codegen.Class;
import com.poplicus.crawler.codegen.Constants;
import com.poplicus.crawler.codegen.FileGenerationException;
import com.poplicus.crawler.codegen.definitions.ActionDefinition;
import com.poplicus.crawler.codegen.definitions.BrowserDefinition;
import com.poplicus.crawler.codegen.definitions.ConditionDefinition;
import com.poplicus.crawler.codegen.definitions.Conditions;
import com.poplicus.crawler.codegen.definitions.CrawlerDefinition;
import com.poplicus.crawler.codegen.definitions.DataGroupDefinition;
import com.poplicus.crawler.codegen.definitions.DataSetDefinition;
import com.poplicus.crawler.codegen.definitions.DatumDefinition;
import com.poplicus.crawler.codegen.definitions.ElementDefinition;
import com.poplicus.crawler.codegen.definitions.ElseDefinition;
import com.poplicus.crawler.codegen.definitions.ElseIfDefinition;
import com.poplicus.crawler.codegen.definitions.ExpressionDefinition;
import com.poplicus.crawler.codegen.definitions.ExpressionElementsDef;
import com.poplicus.crawler.codegen.definitions.FunctionDefinition;
import com.poplicus.crawler.codegen.definitions.HTMLElementsDef;
import com.poplicus.crawler.codegen.definitions.IfDefinition;
import com.poplicus.crawler.codegen.definitions.IfElseElementsDef;
import com.poplicus.crawler.codegen.definitions.LHSDefinition;
import com.poplicus.crawler.codegen.definitions.LineOfCodeDefinition;
import com.poplicus.crawler.codegen.definitions.MainIfElementsDef;
import com.poplicus.crawler.codegen.definitions.NavigateDefinition;
import com.poplicus.crawler.codegen.definitions.OperandDefinition;
import com.poplicus.crawler.codegen.definitions.OperatorDefinition;
import com.poplicus.crawler.codegen.definitions.ParameterDefinition;
import com.poplicus.crawler.codegen.definitions.Parameters;
import com.poplicus.crawler.codegen.definitions.RightHSDefinition;
import com.poplicus.crawler.codegen.definitions.StringDefinition;
import com.poplicus.crawler.codegen.definitions.VariableDefinition;
import com.poplicus.crawler.codegen.definitions.WebPageDefinition;
import com.poplicus.crawler.codegen.definitions.WebPages;

public class CrawlerCodeGenFactory {

	private static CrawlerCodeGenFactory codeFactory = null;

	private static ObjectFactory objectFactory = ObjectFactory.getInstance();

	private String xmlDefinitionFileName = null;

    private TransformerFactory transFactory = TransformerFactory.newInstance();
    private Transformer transformer = null;
    private DOMSource source = null;
    private StreamResult result = null;


	private CrawlerCodeGenFactory() {

	}

	public static CrawlerCodeGenFactory getInstance() {
		if(codeFactory == null) {
			codeFactory = new CrawlerCodeGenFactory();
		}
		return codeFactory;
	}

	public String generateCrawlerCode(String fileName) throws Exception {
		CrawlerDefXMLReader xmlReader = new CrawlerDefXMLReader();
		CrawlerDefinition crawlerDef = xmlReader.getCrawlerDefinition(fileName);
		Class clazz = getBasicClass(crawlerDef);
		//adding PBC, SBC and TBC methods
		addMethods(crawlerDef, clazz);

		//generating code
		clazz.generateCode();

		return clazz.getGeneratedClassFileName();
	}

	public String generateCrawlerCode(CrawlerDefinition crawlerDef) throws Exception {
		Class clazz = getBasicClass(crawlerDef);
		//adding PBC, SBC and TBC methods
		addMethods(crawlerDef, clazz);

		//generating code
		clazz.generateCode();

		return clazz.getGeneratedClassFileName();
	}

	public String saveCrawlerDefinition(CrawlerDefinition crawlerDef) throws Exception {

		writeToDefinitionFile(crawlerDef.getClientName()
			+ Constants.UNDER_SCORE + crawlerDef.getSiteName(), getCrawlerDefinitionDocument(crawlerDef));

		return xmlDefinitionFileName;
	}

	private void writeToDefinitionFile(String fileName, Document crawlerDef) throws FileGenerationException {
		File xmlFile = null;
		String oldFileDateandTime = null;
		try {
			if(validateDefinitionFolderStructure()) {
				xmlFile = new File("C:\\Poplicus\\CodeGen\\Definition\\" + fileName + ".xml");
				if(xmlFile.exists()){
					oldFileDateandTime = (new SimpleDateFormat("MM_dd_yyyy_hh_mm_ss")).format((new Date(xmlFile.lastModified())));
					xmlFile.renameTo(new File("C:\\Poplicus\\CodeGen\\Definition\\" + fileName + "_"
						+ oldFileDateandTime + ".xml"));
				}
				if(xmlFile.createNewFile()) {
					transformer = transFactory.newTransformer();
					source = new DOMSource(crawlerDef);
					result = new StreamResult(xmlFile);
					transformer.transform(source, result);
				}
				xmlDefinitionFileName = xmlFile.getAbsolutePath();
			} else {
				throw new FileGenerationException("Exception occcurred while writing definition file to C:\\Poplicus\\CodeGen\\Definition.");
			}
		} catch(FileGenerationException exFileGen) {
			throw exFileGen;
		} catch(Exception ex) {
			throw new FileGenerationException(ex.getMessage(), ex);
		}
	}

	private boolean validateDefinitionFolderStructure() throws FileGenerationException {
		boolean valid = true;
		File file = null;
		try {
			file = new File("C:\\Poplicus\\CodeGen\\Definition");
			if(!file.exists()) {
				file.mkdirs();
			}
		} catch(Exception ex) {
			valid = false;
			throw new FileGenerationException("Could not create folder structure C:\\Poplicus\\CodeGen\\Definition.");
		}
		return valid;
	}

	private Document getCrawlerDefinitionDocument(CrawlerDefinition crawlerDef) throws Exception {
		DocumentBuilderFactory xmlFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = xmlFactory.newDocumentBuilder();
		Document document = docBuilder.newDocument();
		document.appendChild(getCrawlerElement(document, crawlerDef));

		return document;
	}

	private Element getCrawlerElement(Document document, CrawlerDefinition crawlerDef) {
		Element crawlerElement = document.createElement(Constants.CRAWLER_TAG);
		crawlerElement.setAttribute(Constants.C_ATTR_NAME, crawlerDef.getName());
		crawlerElement.setAttribute(Constants.C_ATTR_HANDLE_POPUP, new Boolean(crawlerDef.isHandlePopup()).toString());
		crawlerElement.setAttribute(Constants.C_ATTR_CLIENT_NAME, crawlerDef.getClientName());
		crawlerElement.setAttribute(Constants.C_ATTR_SITE_NAME, crawlerDef.getSiteName());
		crawlerElement.setAttribute(Constants.C_ATTR_SOURCE_ID, crawlerDef.getSourceID());
		crawlerElement.setAttribute(Constants.C_ATTR_SOURCE_NAME, crawlerDef.getSourceName());
		crawlerElement.setAttribute(Constants.C_ATTR_INVOKE_BASE_PBC, new Boolean(crawlerDef.isInvokeBasePBC()).toString());
		crawlerElement.setAttribute(Constants.C_ATTR_INVOKE_BASE_SBC, new Boolean(crawlerDef.isInvokeBaseSBC()).toString());
		crawlerElement.setAttribute(Constants.C_ATTR_INVOKE_BASE_TBC, new Boolean(crawlerDef.isInvokeBaseTBC()).toString());

		crawlerElement.appendChild(getConstructElement(document, crawlerDef));
		crawlerElement.appendChild(getSuperConstructElement(document, crawlerDef));
		crawlerElement.appendChild(getBrowserElement(document, crawlerDef.getPrimaryBrowserDefinition()));
		crawlerElement.appendChild(getBrowserElement(document, crawlerDef.getSecondaryBrowserDefinition()));
		crawlerElement.appendChild(getBrowserElement(document, crawlerDef.getTertiaryBrowserDefinition()));
		crawlerElement.appendChild(getWebPagesElement(document, crawlerDef.getWebPages()));
		return crawlerElement;
	}

	private Element getConstructElement(Document document, CrawlerDefinition crawlerDef) {
		Element constructorElement = document.createElement(Constants.CONSTRUCTOR_TAG);
		constructorElement.setAttribute(Constants.C_ATTR_INVOKE_SOURCE_MAP_CONFIG,
			new Boolean(crawlerDef.getCurrentConstructor().isInvokeSourceMapConfig()).toString());
		constructorElement.appendChild(getParametersElement(document, crawlerDef.getCurrentConstructor().getParameters()));

		Element sourceMapConfig = document.createElement(Constants.SOURCE_MAP_CONFIG_TAG);
		sourceMapConfig.setAttribute(Constants.C_ATTR_TYPE, crawlerDef.getCurrentConstructor().getSourceMapConfig().getType());
		constructorElement.appendChild(sourceMapConfig);

		return constructorElement;
	}

	private Element getSuperConstructElement(Document document, CrawlerDefinition crawlerDef) {
		Element superConsElement = document.createElement(Constants.SUPER_CONSTRUCTOR_TAG);
		superConsElement.appendChild(getParametersElement(document, crawlerDef.getSuperConstructor().getParameters()));
		return superConsElement;
	}

	private Element getParametersElement(Document document, Parameters parameters) {
		Element parametersElement = document.createElement(Constants.PARAMETERS_TAG);
		Element parameterElement = null;
		Element valueElement = null;
		for(ParameterDefinition param : parameters) {
			parameterElement = document.createElement(Constants.PARAMETER_TAG);
			parameterElement.setAttribute(Constants.C_ATTR_NAME, param.getName());
			parameterElement.setAttribute(Constants.C_ATTR_TYPE, param.getType());
			parameterElement.setAttribute(Constants.C_ATTR_PASS_BY_VALUE, new Boolean(param.isPassByValue()).toString());
			parameterElement.setAttribute(Constants.C_ATTR_PASS_BY_REFERENCE, new Boolean(param.isPassByReference()).toString());
			valueElement = document.createElement(Constants.VALUE_TAG);
			valueElement.appendChild(document.createCDATASection(param.getValue()));
			parameterElement.appendChild(valueElement);
			parametersElement.appendChild(parameterElement);
		}
		return parametersElement;
	}

	private Element getBrowserElement(Document document, BrowserDefinition browserDef) {
		Element browserElement = null;
		if(browserDef.getWhichBrowser().equals(BrowserDefinition.WhichBrowser.Primary)) {
			browserElement = document.createElement(Constants.PRIMARY_BROWSER_TAG);
		} else if(browserDef.getWhichBrowser().equals(BrowserDefinition.WhichBrowser.Secondary)) {
			browserElement = document.createElement(Constants.SECONDARY_BROWSER_TAG);
		} else if(browserDef.getWhichBrowser().equals(BrowserDefinition.WhichBrowser.Tertiary)) {
			browserElement = document.createElement(Constants.TERTIARY_BROWSER_TAG);
		}
		browserElement.setAttribute(Constants.C_ATTR_SECURED_WEB_PAGE, new Boolean(browserDef.isSecuredWebPage()).toString());
		browserElement.setAttribute(Constants.C_ATTR_DOWNLOAD_IMAGES, new Boolean(browserDef.isDownloadImages()).toString());
		browserElement.setAttribute(Constants.C_ATTR_DOWNLOAD_SCRIPTS, new Boolean(browserDef.isDownloadJavaScripts()).toString());
		browserElement.appendChild(getConditionsElement(document, browserDef.getConditions()));

		return browserElement;
	}

	private Element getConditionsElement(Document document, Conditions conditions) {
		Element conditionsElement = document.createElement(Constants.CONDITIONS_TAG);
		for(ConditionDefinition condition : conditions) {
			conditionsElement.appendChild(getConditionElement(document, condition));
		}

		return conditionsElement;
	}

	private Element getConditionElement(Document document, ConditionDefinition conditionDef) {
		Element conditionElement = document.createElement(Constants.CONDITION_TAG);
		conditionElement.setAttribute(Constants.C_ATTR_NAME, conditionDef.getName());
		conditionElement.setAttribute(Constants.C_ATTR_SUPER_CONDITION, new Boolean(conditionDef.isSuperCondition()).toString());

		Element lhsOperandElement = document.createElement(Constants.LHS_OPERAND_TAG);
		lhsOperandElement.appendChild(document.createCDATASection(conditionDef.getLhsOperand()));
		conditionElement.appendChild(lhsOperandElement);

		Element operatorElement = document.createElement(Constants.OPERATOR_TAG);
		operatorElement.appendChild(document.createCDATASection(conditionDef.getOperator()));
		conditionElement.appendChild(operatorElement);

		Element rhsOperandElement = document.createElement(Constants.RHS_OPERAND_TAG);
		rhsOperandElement.appendChild(document.createCDATASection(conditionDef.getRhsOperand()));
		conditionElement.appendChild(rhsOperandElement);

		return conditionElement;
	}

	private Element getWebPagesElement(Document document, WebPages webPages) {
		Element webPagesElement = document.createElement(Constants.WEB_PAGES_TAG);
		for(WebPageDefinition webPage : webPages) {
			webPagesElement.appendChild(getWebPageElement(document, webPage));
		}
		return webPagesElement;
	}

	private Element getWebPageElement(Document document, WebPageDefinition webPage) {
		Element webPageElement = document.createElement(Constants.WEB_PAGE_TAG);
		webPageElement.setAttribute(Constants.C_ATTR_NAME, webPage.getName());
		webPageElement.setAttribute(Constants.C_ATTR_TYPE, webPage.getType());
		webPageElement.setAttribute(Constants.C_ATTR_NAVIGATION_ORDER, webPage.getNavigationOrder());
		webPageElement.setAttribute(Constants.C_ATTR_EXECUTE_ONCE, new Boolean(webPage.isExecuteOnce()).toString());
		webPageElement.setAttribute(Constants.C_ATTR_BROWSER, webPage.getBrowser());

		Element urlElement = document.createElement(Constants.URL_TAG);
		urlElement.appendChild(document.createCDATASection(webPage.getUrl()));
		webPageElement.appendChild(urlElement);

		webPageElement.appendChild(getConditionsElement(document, webPage.getConditions()));

		Element mainIfElement = document.createElement(Constants.MAIN_IF_TAG);
		mainIfElement.appendChild(getConditionsElement(document, webPage.getMainIF().getConditions()));
		appendMainIfElements(document, webPage.getMainIF().getMainIfElementsDef(), mainIfElement);

		webPageElement.appendChild(mainIfElement);

		if(webPage.getType().equals(Constants.WEB_PAGE_TYPE_RESULT)) {
			List<DataGroupDefinition> dataGroups = webPage.getDataGroups();
			List<DataSetDefinition> dataSets = null;
			List<DatumDefinition> data = null;
			Element dataGroupElement = null;
			Element startTag = null;
			Element endTag = null;
			Element datumStartTag = null;
			Element datumEndTag = null;
			Element dataSetElement = null;
			Element datumElement = null;
			for(DataGroupDefinition dataGroup : dataGroups) {
				dataGroupElement = document.createElement(Constants.RESULTSET_GROUP_TAG);
				startTag = document.createElement(Constants.START_TAG);
				if(dataGroup.getStartTag() != null && dataGroup.getStartTag().length() > 0) {
					startTag.appendChild(document.createCDATASection(dataGroup.getStartTag()));
				}
				dataGroupElement.appendChild(startTag);
				endTag = document.createElement(Constants.END_TAG);
				if(dataGroup.getEndTag() != null && dataGroup.getEndTag().length() > 0) {
					endTag.appendChild(document.createCDATASection(dataGroup.getEndTag()));
				}
				dataGroupElement.appendChild(endTag);
//				dataGroupElement.setAttribute(Constants.C_ATTR_START_TAG, dataGroup.getStartTag());
//				dataGroupElement.setAttribute(Constants.C_ATTR_END_TAG, dataGroup.getEndTag());
				dataSets = dataGroup.getDataSets();
				for(DataSetDefinition dsDef : dataSets) {
					dataSetElement = document.createElement(Constants.RESULTSET_TAG);
					dataSetElement.setAttribute(Constants.C_ATTR_TABLE_NAME, dsDef.getTableName());
					data = dsDef.getData();
					for(DatumDefinition datum : data) {
						datumElement = document.createElement(Constants.DATUM_TAG);
						datumElement.setAttribute(Constants.C_ATTR_UNIQUE_ID, datum.getUniqueName());
						datumElement.setAttribute(Constants.C_ATTR_COLUMN_NAME, datum.getFieldName());
						datumElement.setAttribute(Constants.C_ATTR_DATA_TYPE, datum.getDataType());
						datumElement.setAttribute(Constants.C_ATTR_VARIABLE_NAME, datum.getVariableName());
						datumElement.setAttribute(Constants.C_ATTR_HTML_SOURCE, datum.getHtmlSource());
						datumElement.setAttribute(Constants.C_ATTR_ENTIRE_STRING, new Boolean(datum.isEntireString()).toString());
						datumElement.setAttribute(Constants.C_ATTR_AFTER_TAG, new Boolean(datum.isAfterTag()).toString());
						datumElement.setAttribute(Constants.C_ATTR_IS_CONFIGURABLE, new Boolean(datum.isConfigurable()).toString());
						datumStartTag = document.createElement(Constants.START_TAG);
						if(datum.getStartTag() != null && datum.getStartTag().length() > 0) {
							datumStartTag.appendChild(document.createCDATASection(datum.getStartTag()));
						}
						datumElement.appendChild(datumStartTag);
						datumEndTag = document.createElement(Constants.END_TAG);
						if(datum.getEndTag() != null && datum.getEndTag().length() > 0) {
							datumEndTag.appendChild(document.createCDATASection(datum.getEndTag()));
						}
						datumElement.appendChild(datumEndTag);
						dataSetElement.appendChild(datumElement);
					}
					dataGroupElement.appendChild(dataSetElement);
				}
				webPageElement.appendChild(dataGroupElement);
			}
		}

		return webPageElement;
	}

	private void appendMainIfElements(Document document, List<MainIfElementsDef> mainIfElems, Element mainIfElement) {
		for(MainIfElementsDef element : mainIfElems) {
			if(element instanceof IfDefinition) {
				Element ifElement = document.createElement(Constants.IF_TAG);
				ifElement.appendChild(getConditionsElement(document, ((IfDefinition) element).getConditions()));
				List<IfElseElementsDef> childElements = ((IfDefinition) element).getIfElseElements();
				for(IfElseElementsDef childElement : childElements) {
					if(childElement instanceof IfDefinition) {
						ifElement.appendChild(getIfElement(document, (IfDefinition) childElement));
					} else if(childElement instanceof ElseIfDefinition) {
						ifElement.appendChild(getElseIfElement(document, (ElseIfDefinition) childElement));
					} else if(childElement instanceof ElseDefinition) {
						ifElement.appendChild(getElseElement(document, (ElseDefinition) childElement));
					} else if(childElement instanceof ActionDefinition) {
						ifElement.appendChild(getActionElement(document, (ActionDefinition) childElement));
					} else if(childElement instanceof LineOfCodeDefinition) {
						ifElement.appendChild(getLineOfCodeElement(document, (LineOfCodeDefinition) childElement));
					}
				}
				mainIfElement.appendChild(ifElement);
			} else if(element instanceof ElseIfDefinition) {
				Element ifElseElement = document.createElement(Constants.ELSE_IF_TAG);
				ifElseElement.appendChild(getConditionsElement(document, ((ElseIfDefinition) element).getConditions()));
				List<IfElseElementsDef> childElements = ((ElseIfDefinition) element).getIfElseElements();
				for(IfElseElementsDef childElement : childElements) {
					if(childElement instanceof IfDefinition) {
						ifElseElement.appendChild(getIfElement(document, (IfDefinition) childElement));
					} else if(childElement instanceof ElseIfDefinition) {
						ifElseElement.appendChild(getElseIfElement(document, (ElseIfDefinition) childElement));
					} else if(childElement instanceof ElseDefinition) {
						ifElseElement.appendChild(getElseElement(document, (ElseDefinition) childElement));
					} else if(childElement instanceof ActionDefinition) {
						ifElseElement.appendChild(getActionElement(document, (ActionDefinition) childElement));
					} else if(childElement instanceof LineOfCodeDefinition) {
						ifElseElement.appendChild(getLineOfCodeElement(document, (LineOfCodeDefinition) childElement));
					}
				}
				mainIfElement.appendChild(ifElseElement);
			} else if(element instanceof ElseDefinition) {
				Element elseElement = document.createElement(Constants.ELSE_TAG);
				elseElement.appendChild(getConditionsElement(document, ((ElseDefinition) element).getConditions()));
				List<IfElseElementsDef> childElements = ((ElseDefinition) element).getIfElseElements();
				for(IfElseElementsDef childElement : childElements) {
					if(childElement instanceof IfDefinition) {
						elseElement.appendChild(getIfElement(document, (IfDefinition) childElement));
					} else if(childElement instanceof ElseIfDefinition) {
						elseElement.appendChild(getElseIfElement(document, (ElseIfDefinition) childElement));
					} else if(childElement instanceof ElseDefinition) {
						elseElement.appendChild(getElseElement(document, (ElseDefinition) childElement));
					} else if(childElement instanceof ActionDefinition) {
						elseElement.appendChild(getActionElement(document, (ActionDefinition) childElement));
					} else if(childElement instanceof LineOfCodeDefinition) {
						elseElement.appendChild(getLineOfCodeElement(document, (LineOfCodeDefinition) childElement));
					}
				}
				mainIfElement.appendChild(elseElement);
			} else if(element instanceof LineOfCodeDefinition) {
				mainIfElement.appendChild(getLineOfCodeElement(document, (LineOfCodeDefinition) element));
			} else if(element instanceof ActionDefinition) {
				mainIfElement.appendChild(getActionElement(document, (ActionDefinition) element));
			}
		}
	}

	private Element getIfElement(Document document, IfDefinition ifDef) {
		Element ifElement = document.createElement(Constants.IF_TAG);
		ifElement.appendChild(getConditionsElement(document, ifDef.getConditions()));
		List<IfElseElementsDef> ifElseElements = ifDef.getIfElseElements();
		for(IfElseElementsDef ifElseElement : ifElseElements) {
			if(ifElseElement instanceof IfDefinition) {
				ifElement.appendChild(getIfElement(document, (IfDefinition) ifElseElement));
			} else if(ifElseElement instanceof ElseIfDefinition) {
				ifElement.appendChild(getElseIfElement(document, (ElseIfDefinition) ifElseElement));
			} else if(ifElseElement instanceof ElseDefinition) {
				ifElement.appendChild(getElseElement(document, (ElseDefinition) ifElseElement));
			} else if(ifElseElement instanceof LineOfCodeDefinition) {
				ifElement.appendChild(getLineOfCodeElement(document, (LineOfCodeDefinition) ifElseElement));
			} else if(ifElseElement instanceof ActionDefinition) {
				ifElement.appendChild(getActionElement(document, (ActionDefinition) ifElseElement));
			}
		}

		return ifElement;
	}

	private Element getElseIfElement(Document document, ElseIfDefinition elseIfDef) {
		Element elseIfElement = document.createElement(Constants.ELSE_IF_TAG);
		elseIfElement.appendChild(getConditionsElement(document, elseIfDef.getConditions()));
		List<IfElseElementsDef> ifElseElements = elseIfDef.getIfElseElements();
		for(IfElseElementsDef ifElseElement : ifElseElements) {
			if(ifElseElement instanceof IfDefinition) {
				elseIfElement.appendChild(getIfElement(document, (IfDefinition) ifElseElement));
			} else if(ifElseElement instanceof ElseIfDefinition) {
				elseIfElement.appendChild(getElseIfElement(document, (ElseIfDefinition) ifElseElement));
			} else if(ifElseElement instanceof ElseDefinition) {
				elseIfElement.appendChild(getElseElement(document, (ElseDefinition) ifElseElement));
			} else if(ifElseElement instanceof LineOfCodeDefinition) {
				elseIfElement.appendChild(getLineOfCodeElement(document, (LineOfCodeDefinition) ifElseElement));
			} else if(ifElseElement instanceof ActionDefinition) {
				elseIfElement.appendChild(getActionElement(document, (ActionDefinition) ifElseElement));
			}
		}

		return elseIfElement;
	}

	private Element getElseElement(Document document, ElseDefinition elseDef) {
		Element elseElement = document.createElement(Constants.ELSE_TAG);
		List<IfElseElementsDef> ifElseElements = elseDef.getIfElseElements();
		for(IfElseElementsDef ifElseElement : ifElseElements) {
			if(ifElseElement instanceof IfDefinition) {
				elseElement.appendChild(getIfElement(document, (IfDefinition) ifElseElement));
			} else if(ifElseElement instanceof ElseIfDefinition) {
				elseElement.appendChild(getElseIfElement(document, (ElseIfDefinition) ifElseElement));
			} else if(ifElseElement instanceof ElseDefinition) {
				elseElement.appendChild(getElseElement(document, (ElseDefinition) ifElseElement));
			} else if(ifElseElement instanceof LineOfCodeDefinition) {
				elseElement.appendChild(getLineOfCodeElement(document, (LineOfCodeDefinition) ifElseElement));
			} else if(ifElseElement instanceof ActionDefinition) {
				elseElement.appendChild(getActionElement(document, (ActionDefinition) ifElseElement));
			}
		}

		return elseElement;
	}

	private Element getLineOfCodeElement(Document document, LineOfCodeDefinition locDef) {
		Element locElement = document.createElement(Constants.LINE_OF_CODE_TAG);

		Element lhsDefElement = document.createElement(Constants.LHS_DEFINITION_TAG);
		LHSDefinition lhsDefinition = locDef.getLhsDefiniton();
		lhsDefElement.setAttribute(Constants.C_ATTR_TYPE, lhsDefinition.getType());
		if(lhsDefinition.getVariableDefinition().getName() != null) {
			lhsDefElement.appendChild(getVariableElement(document, lhsDefinition.getVariableDefinition()));
		}
		locElement.appendChild(lhsDefElement);

		Element rhsDefElement = document.createElement(Constants.RHS_DEFINITION_TAG);
		RightHSDefinition rhsDefinition = locDef.getRhsDefinition();
		rhsDefElement.setAttribute(Constants.C_ATTR_TYPE, rhsDefinition.getType());
		if(rhsDefinition.getType().equalsIgnoreCase(Constants.RHS_TYPE_ASSIGN_VALUE_TO_AN_EXISTING_VARIABLE)) {
			rhsDefElement.appendChild(getFunctionElement(document, rhsDefinition.getFunctionDef()));
		} else if(rhsDefinition.getType().equalsIgnoreCase(Constants.RHS_TYPE_RETURN_VALUE_FROM_ACTION)) {
			rhsDefElement.appendChild(getActionElement(document, rhsDefinition.getActionDef()));
		} else if(rhsDefinition.getType().equalsIgnoreCase(Constants.RHS_TYPE_EXPRESSION)) {
			rhsDefElement.appendChild(getExpressionElement(document, rhsDefinition.getExpressionDef()));
		}
		locElement.appendChild(rhsDefElement);

		return locElement;
	}

	private Element getExpressionElement(Document document, ExpressionDefinition expressionDef) {
		Element expressionElement = document.createElement(Constants.EXPRESSION_TAG);
		Element operandElement = null;
		Element operatorElement = null;
		List<ExpressionElementsDef> expressionElements = expressionDef.getExpressionElements();
		for(ExpressionElementsDef element : expressionElements) {
			if(element instanceof OperandDefinition) {
				operandElement = document.createElement(Constants.OPERAND_TAG);
				if(((OperandDefinition) element).getActionDef() != null) {
					operandElement.appendChild(getActionElement(document, ((OperandDefinition) element).getActionDef()));
				} else if(((OperandDefinition) element).getExpressionDef() != null) {
					operandElement.appendChild(getExpressionElement(document, ((OperandDefinition) element).getExpressionDef()));
				} else if(((OperandDefinition) element).getFunctionDef() != null) {
					operandElement.appendChild(getFunctionElement(document, ((OperandDefinition) element).getFunctionDef()));
				} else if(((OperandDefinition) element).getStringDef() != null) {
					operandElement.appendChild(getStringElement(document, ((OperandDefinition) element).getStringDef()));
				} else if(((OperandDefinition) element).getVariableDef() != null) {
					if(((OperandDefinition) element).getVariableDef().getName() != null) {
						operandElement.appendChild(getVariableElement(document, ((OperandDefinition) element).getVariableDef()));
					}
				}
				expressionElement.appendChild(operandElement);
			} else if(element instanceof OperatorDefinition) {
				operatorElement = document.createElement(Constants.OPERATOR_TAG);
				operatorElement.appendChild(document.createCDATASection(((OperatorDefinition) element).getOperator()));
				expressionElement.appendChild(operatorElement);
			}
		}

		return expressionElement;
	}

	private Element getVariableElement(Document document, VariableDefinition variableDef) {
		Element variableElement = document.createElement(Constants.VARIABLE_TAG);
		variableElement.setAttribute(Constants.C_ATTR_NAME, variableDef.getName());
		variableElement.setAttribute(Constants.C_ATTR_TYPE, variableDef.getType());
		Element variableValueElement = document.createElement(Constants.VALUE_TAG);
		if(variableDef.getValue() != null) {
			variableValueElement.appendChild(document.createCDATASection(variableDef.getValue()));
			variableElement.appendChild(variableValueElement);
		}

		return variableElement;
	}

	private Element getStringElement(Document document, StringDefinition stringDef) {
		Element stringElement = document.createElement(Constants.STRING_TAG);
		stringElement.setAttribute(Constants.C_ATTR_PASS_BY_VALUE, Constants.TRUE);
		stringElement.setAttribute(Constants.C_ATTR_PASS_BY_REFERENCE, new Boolean(stringDef.isPassByReference()).toString());
		Element valueElement = document.createElement(Constants.VALUE_TAG);
		//valueElement.appendChild(document.createCDATASection(stringDef.getValue()));
		if(stringDef.getName() != null) {
			valueElement.appendChild(document.createCDATASection(stringDef.getName()));
		} else if(stringDef.getValue() != null) {
			valueElement.appendChild(document.createCDATASection(stringDef.getValue()));
		}
		stringElement.appendChild(valueElement);

		return stringElement;
	}

	private Element getActionElement(Document document, ActionDefinition actionDef) {
		Element actionElement = document.createElement(Constants.ACTION_TAG);
		actionElement.setAttribute(Constants.C_ATTR_TYPE, actionDef.getType());
		actionElement.setAttribute(Constants.C_ATTR_BROWSER_TYPE, actionDef.getBrowserType());

		HTMLElementsDef htmlElement = actionDef.getHtmlElement();
		if(htmlElement instanceof ElementDefinition) {
			actionElement.appendChild(getXMLElementOfHTMLElement(document, (ElementDefinition) htmlElement));
		} else if(htmlElement instanceof FunctionDefinition) {
			actionElement.appendChild(getFunctionElement(document, (FunctionDefinition) htmlElement));
		} else if(htmlElement instanceof NavigateDefinition) {
			actionElement.appendChild(getNavigateElement(document, (NavigateDefinition) htmlElement));
		}

		return actionElement;
	}

	private Element getXMLElementOfHTMLElement(Document document, ElementDefinition htmlElementDef) {
		Element xmlElementDef = document.createElement(Constants.ELEMENT_TAG);
		xmlElementDef.setAttribute(Constants.C_ATTR_ELEMENT_TAG_TYPE, htmlElementDef.getElementTagType());
		xmlElementDef.setAttribute(Constants.C_ATTR_ELEMENT_FIELD_TYPE, htmlElementDef.getElementFieldType());
		xmlElementDef.setAttribute(Constants.C_ATTR_EXACT_MATCH, new Boolean(htmlElementDef.isExactMatch()).toString());
		Element valueElement = document.createElement(Constants.VALUE_TAG);
		valueElement.appendChild(document.createCDATASection(htmlElementDef.getValue()));
		xmlElementDef.appendChild(valueElement);

		return xmlElementDef;
	}

	private Element getNavigateElement(Document document, NavigateDefinition navigateDef) {
		Element navigateElement = document.createElement(Constants.NAVIGATE_TAG);
		Element urlElement = document.createElement(Constants.URL_TAG);
		Element valueElement = document.createElement(Constants.VALUE_TAG);
		valueElement.appendChild(document.createCDATASection(navigateDef.getUrl()));
		urlElement.appendChild(valueElement);
		navigateElement.appendChild(urlElement);

		return navigateElement;
	}

	private Element getFunctionElement(Document document, FunctionDefinition funcDef) {
		Element functionElement = document.createElement(Constants.FUNCTION_DEFINITION_TAG);
		functionElement.setAttribute(Constants.C_ATTR_NAME, funcDef.getName());
		functionElement.setAttribute(Constants.C_ATTR_PACKAGE_NAME, funcDef.getPackageName());
		functionElement.setAttribute(Constants.C_ATTR_CLASS_NAME, funcDef.getClassName());

		List<ParameterDefinition> parameters = funcDef.getParameters();
		Element parameterElement = null;
		Element valueElement = null;
		for(ParameterDefinition param : parameters) {
			parameterElement = document.createElement(Constants.PARAMETER_TAG);
			parameterElement.setAttribute(Constants.C_ATTR_NAME, param.getName());
			parameterElement.setAttribute(Constants.C_ATTR_TYPE, param.getType());
			parameterElement.setAttribute(Constants.C_ATTR_PASS_BY_VALUE, new Boolean(param.isPassByValue()).toString());
			parameterElement.setAttribute(Constants.C_ATTR_PASS_BY_REFERENCE, new Boolean(param.isPassByReference()).toString());
			valueElement = document.createElement(Constants.VALUE_TAG);
			valueElement.appendChild(document.createCDATASection(param.getValue()));
			parameterElement.appendChild(valueElement);
			functionElement.appendChild(parameterElement);
		}

		if(funcDef.getSupportingFunctionName() != null && funcDef.getSupportingFunctionName().length() > 0) {
			functionElement.appendChild(getSupportingFunctionElement(document, funcDef));
		}

		return functionElement;
	}

	private Element getSupportingFunctionElement(Document document, FunctionDefinition funcDef) {
		Element supportingFunctionElement = document.createElement(Constants.SUPPORTING_FUNCTION_TAG);
		supportingFunctionElement.setAttribute(Constants.C_ATTR_NAME, funcDef.getSupportingFunctionName());

		List<ParameterDefinition> parameters = funcDef.getSupportingFunctionParameters();
		Element parameterElement = null;
		Element valueElement = null;
		for(ParameterDefinition param : parameters) {
			parameterElement = document.createElement(Constants.PARAMETER_TAG);
			parameterElement.setAttribute(Constants.C_ATTR_NAME, param.getName());
			parameterElement.setAttribute(Constants.C_ATTR_TYPE, param.getType());
			parameterElement.setAttribute(Constants.C_ATTR_PASS_BY_VALUE, new Boolean(param.isPassByValue()).toString());
			parameterElement.setAttribute(Constants.C_ATTR_PASS_BY_REFERENCE, new Boolean(param.isPassByReference()).toString());
			valueElement = document.createElement(Constants.VALUE_TAG);
			valueElement.appendChild(document.createCDATASection(param.getValue()));
			parameterElement.appendChild(valueElement);
			supportingFunctionElement.appendChild(parameterElement);
		}

		return supportingFunctionElement;
	}

	private Class getBasicClass(CrawlerDefinition crawlerDef) {
		Class clazz = objectFactory.getClass(crawlerDef);
		clazz.addConstructor(objectFactory.getConstructor(crawlerDef));
		clazz.setSecurityAlertPopup(crawlerDef.isHandlePopup());

		return clazz;
	}

	private void addMethods(CrawlerDefinition crawlerDef, Class clazz) throws Exception {
		//adding PBC
		if(crawlerDef.getWebPages().isImplementPBC()) {
			clazz.addMethod(objectFactory.getPBCMethod(crawlerDef, clazz));
		}
		//adding SBC
		if(crawlerDef.getWebPages().isImplementSBC()) {
			clazz.addMethod(objectFactory.getSBCMethod(crawlerDef, clazz));
		}

		//adding TBC
		if(crawlerDef.getWebPages().isImplementTBC()) {
			clazz.addMethod(objectFactory.getTBCMethod(crawlerDef, clazz));
		}
	}

	public String getDefinitionFileName() {
		return xmlDefinitionFileName;
	}

	public static void main(String[] args) throws Exception {
		String fileName = "C:\\Important\\test_classes\\crawlerDefs\\PopCodeGen_FedArmy1.xml";
		CrawlerCodeGenFactory codeFactory = CrawlerCodeGenFactory.getInstance();
		codeFactory.generateCrawlerCode(fileName);
	}

}
