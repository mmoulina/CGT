package com.poplicus.crawler.codegen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.poplicus.crawler.codegen.DataSet.TableName;
import com.poplicus.crawler.codegen.Datum.DatumIdentifier;
import com.poplicus.crawler.codegen.IfElseStatement.IfElseStatementType;
import com.poplicus.crawler.codegen.WebPage.BrowserType;
import com.poplicus.crawler.codegen.WebPage.PageType;

public class Method {

	private Indentation indentation = new Indentation(Indentation.INDENTATION_LEVEL_2);

	public enum MethodName {
		PrimaryBrowserControl, SecondaryBrowserControl, TertiaryBrowserControl, CollectResultLinks, ParseResults
	}

	public Class containingClass = null;

	private String accessModifier = "public";

	private String returnType = "void";

	private boolean isOverridden = false;

	private MethodName name = null;

	private String prefixName = null;

	private String suffixName = null;

	private List<Variable> parameters = new ArrayList<Variable>();

	private List<WebPage> pagesToParseInPrimaryBrowser = new ArrayList<WebPage>();

	private List<WebPage> pagesToParseInSecondaryBrowser = new ArrayList<WebPage>();

	private List<WebPage> pagesToParseInTertiaryBrowser = new ArrayList<WebPage>();

	//<b>otherPages</b> may contain WebPages that contains result links and result data
	private List<WebPage> otherPages = new ArrayList<WebPage>();

	private List<String> warningMessages = new ArrayList<String>();

	private boolean secureAccess = false;

	/** condition checks for PBC - start **/
	private boolean invokeBasePBC = false;
	/** condition checks for PBC - end **/

	/** condition checks for SBC - start **/
	private boolean invokeBaseSBC = false;
	/** condition checks for SBC - end **/

	/** condition checks for TBC - start **/
	private boolean invokeBaseTBC = false;
	/** condition checks for TBC - end **/

	/** condition checks for Collect Result Links Function - start **/
	private boolean invokeBaseCRL = false;
	/** condition checks for Collect Result Links Function - end **/

	/** condition checks for Parse Results Function - start **/
	private boolean invokeBaseParseResult = false;
	/** condition checks for Parse Results Function - end **/

	/** To contain all web pages that have result links **/
	private List<WebPage> webPageWithResultLinks = new ArrayList<WebPage>();

	/** To contain all web pages that have result **/
	private List<WebPage> webPageWithResults = new ArrayList<WebPage>();

	private List<StringBuffer> linesOfCodeAsStringBuffer = new ArrayList<StringBuffer>();

	private List<LineOfCode> initialLinesOfCode = new ArrayList<LineOfCode>();

	private StringBuffer helperMethodCode = new StringBuffer();

	private List<Variable> methodVariables = new ArrayList<Variable>();

	private StringBuffer methodVariablesCode = null;

	private IfElseStatement mainIfElseStatement = null;

	private IfElseStatement adhocIfElseStatement = null;

	/**
	 *
	 * @param accessModifier
	 * @param isOverridden
	 * @param returnType
	 * @param name
	 */
	public Method(String accessModifier, boolean isOverridden, String returnType, MethodName name) {
		if(accessModifier != null) {
			this.accessModifier = accessModifier;
		}
		this.isOverridden = isOverridden;
		this.returnType = returnType;
		this.name = name;
	}

	public List<String> getWarningMessages() {
		return warningMessages;
	}

	public boolean isInvokeBasePBC() {
		return invokeBasePBC;
	}

	public void setInvokeBasePBC(boolean invokeBasePBC) {
		this.invokeBasePBC = invokeBasePBC;
	}

	public boolean isSecureAccess() {
		return secureAccess;
	}

	public void setSecureAccess(boolean secureAccess) {
		this.secureAccess = secureAccess;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public boolean isOverridden() {
		return isOverridden;
	}

	public void setOverridden(boolean isOverridden) {
		this.isOverridden = isOverridden;
	}

	public void addParameter(Variable variable) {
		parameters.add(variable);
	}

	public void setParameters(List<Variable> parameters) {
		this.parameters = parameters;
	}

	public List<Variable> getParameters() {
		return this.parameters;
	}

	public String getAccessModifier() {
		return accessModifier;
	}

	public void setAccessModifier(String accessModifier) {
		this.accessModifier = accessModifier;
	}

	public MethodName getName() {
		return name;
	}

	public void setName(MethodName name) {
		this.name = name;
	}

	public boolean isInvokeBaseParseResult() {
		return invokeBaseParseResult;
	}

	public void setInvokeBaseParseResult(boolean invokeBaseParseResult) {
		this.invokeBaseParseResult = invokeBaseParseResult;
	}

	public StringBuffer toStringBuffer()
			throws ClassDefinitionException, ArgumentValueDefinitionException, LineOfCodeException, ActionDefinitionException, RHSTaskException {
		int noOfParams = 1;
		StringBuffer code = new StringBuffer();
		code.append(Constants.NEW_LINE).append(indentation.getTabs());
		code.append(this.accessModifier).append(Constants.SPACE);
		if(isOverridden) {
			code.append(Constants.OVERRIDE).append(Constants.SPACE);
		}
		code.append(this.returnType).append(Constants.SPACE);
		if(prefixName != null || suffixName != null) {
			if(prefixName != null) {
				code.append(prefixName).append(this.name);
			}
			if(suffixName != null) {
				if(prefixName == null) {
					code.append(this.name);
				}
				code.append(suffixName);
			}
		} else {
			code.append(this.name);
		}
		code.append(Constants.OPEN_PARENTHESIS);
		for(Variable var : parameters) {
			if(noOfParams == 1) {
				code.append(var.toStringBuffer());
			} else {
				code.append(Constants.COMMA).append(Constants.SPACE).append(var.toStringBuffer());
			}
			noOfParams++;
		}
		code.append(Constants.CLOSE_PARENTHESIS);

		//opening the method
		code.append(Constants.NEW_LINE).append(indentation.getTabs()).append(Constants.OPEN_BRACES);

		//generating method variable code
		generateMethodVariableCode();
		code.append(methodVariablesCode);

		//generating method implementation code
		code.append(getMethodImplementation());

		if(adhocIfElseStatement != null) {
			if(adhocIfElseStatement.getCode() != null && (adhocIfElseStatement.getCode().size() > 0)) {
				code.append(Constants.NEW_LINE).append(adhocIfElseStatement.getIndentation().getTabs()).append(adhocIfElseStatement.toStringBuffer());
			}
		}

		//closing the method
		code.append(Constants.NEW_LINE).append(indentation.getTabs()).append(Constants.CLOSE_BRACES);

		if(webPageWithResultLinks.size() > 0) {
			code.append(getSupportingMethods(webPageWithResultLinks));
		}

		if(webPageWithResults.size() > 0) {
			code.append(getSupportingMethods(webPageWithResults));
		}

		return code;
	}

	private StringBuffer generateInitialLinesOfCode(StringBuffer tabs) throws RHSTaskException, LineOfCodeException,
																			  ArgumentValueDefinitionException, ClassDefinitionException,
																			  ActionDefinitionException {
		StringBuffer code = new StringBuffer();
		LineOfCode loc = null;
		int noOfInitialLOC = initialLinesOfCode.size();
		for(int index = 0; index < noOfInitialLOC; index ++) {
			loc = initialLinesOfCode.get(index);
			loc.setIndentationLevel(indentation.getNumberOfTabs(tabs));
			code.append(loc.toStringBuffer());
		}
		code.append(Constants.NEW_LINE);
		return code;
	}

	private StringBuffer getSupportingMethods(List<WebPage> webPages)
			throws ClassDefinitionException, ArgumentValueDefinitionException, LineOfCodeException, ActionDefinitionException, RHSTaskException {
		StringBuffer code = new StringBuffer();
		//generating collectResultLinks & parseResult methods
		for(WebPage webPage : webPages) {
			code.append(Constants.NEW_LINE).append(webPage.getIndentation().getTabs());
			Method method = null;
			if(webPage.getPageType().equals(PageType.ResultLinks)) {
				method = new Method("public", false, "void", MethodName.CollectResultLinks);
				method.setContainingClass(this.getContainingClass()); // this is mandatory
				method.setInvokeBaseCRL(true);
				method.setSecureAccess(this.secureAccess);
				method.addParameter(new Variable(null, false, false, "string", "html", null, false, false, false, null));
			} else if (webPage.getPageType().equals(PageType.Result)) {
				method = new Method("public", false, "void", MethodName.ParseResults);
				method.setContainingClass(this.getContainingClass()); // this is mandatory
				method.addParameter(new Variable(null, false, false, "string", "html", null, false, false, false, null));
				method.addParameter(new Variable(null, false, false, "string", "unParsedHTML", null, false, false, false, null));

				//adding initial lines of code
//				if(initialLinesOfCode.size() > 0) {
//					code.append(generateInitialLinesOfCode(Constants.THREE_TABS));
//				} ... this piece of commented code is not required

				//adding restore HTML code
				if(webPage.isRestoreHTMLSymbols()) {
					method.addLineOfCode(new StringBuffer("html = Text.RestoreHTMLSymbols(html);"));
				}

				//pre-parsing instructions
				if(webPage.getPreParsingInstructions().size() > 0) {
					for(LineOfCode loc : webPage.getPreParsingInstructions()) {
						method.addLineOfCode(loc.toStringBuffer());
					}
				}

				//this piece of code extracts data from result page and store the same to DB
				for(DataGroup dataGroup : webPage.getDataGroups()) {
					if(dataGroup.getStartTag() != null && dataGroup.getStartTag().length() > 0) {
						method.addMethodVariable(new Variable(null, false, false, "string", "tempHTML", "", false, true, false, null));
						method.addLineOfCode(new StringBuffer("while(html != \"\"){"));
						method.addLineOfCode(new StringBuffer(Constants.TAB).append("html = Text.TextAfterTag(html, ")
							.append(Constants.DOUBLE_QUOTE).append(dataGroup.getStartTag()).append(Constants.DOUBLE_QUOTE).append(");"));
						if(dataGroup.getEndTag() != null && dataGroup.getEndTag().length() > 0) {
							method.addLineOfCode(new StringBuffer(Constants.TAB).append("tempHTML = Text.TextBeforeTag(html, ")
								.append(Constants.DOUBLE_QUOTE).append(dataGroup.getEndTag()).append(Constants.DOUBLE_QUOTE).append(");"));
						} else {
							method.addLineOfCode(new StringBuffer(Constants.TAB).append("tempHTML = Text.TextBeforeTag(html, ")
								.append(Constants.DOUBLE_QUOTE).append(dataGroup.getStartTag()).append(Constants.DOUBLE_QUOTE).append(");"));
						}
						method.setAliasNameToParameter("html", "tempHTML");
						addDataExtractionAndPersistenceCode(webPage.getBrowserToNavigate(), webPage.getPageName(), dataGroup.getDataSets(), method, true);
						method.addLineOfCode(new StringBuffer("}"));
					} else {
						addDataExtractionAndPersistenceCode(webPage.getBrowserToNavigate(), webPage.getPageName(), dataGroup.getDataSets(), method, false);
					}
				}
			}
			method.setSuffixName(Constants.FROM + webPage.getPageName());
			code.append(method.toStringBuffer());
			if(helperMethodCode != null){
				code.append(helperMethodCode);
			}
		}
		return code;
	}

	private void addDataExtractionAndPersistenceCode(BrowserType browserType, String pageName, List<DataSet> dataSets,
													 Method method, boolean additionalTab)
													 throws ClassDefinitionException, ArgumentValueDefinitionException,
													 LineOfCodeException, ActionDefinitionException, RHSTaskException {
		Method helperMethod = null;
		for(DataSet ds : dataSets) {
			helperMethod = new Method("private", false, "void", MethodName.ParseResults);
			//In some cases we can name the method like ParseResults_PageName_PopBidIndustryCode_Naics. In this Naics is added using additional information.
			if(ds.getAdditionalInformation() != null) {
				helperMethod.setSuffixName(Constants.UNDER_SCORE + pageName + Constants.UNDER_SCORE + ds.getTableName()
					+ Constants.UNDER_SCORE + ds.getAdditionalInformation());
			} else {
				helperMethod.setSuffixName(Constants.UNDER_SCORE + pageName + Constants.UNDER_SCORE + ds.getTableName());
			}
			helperMethod.setContainingClass(method.getContainingClass());
			helperMethod.setParameters(method.getParameters());

			addDataExtractionCode(browserType, helperMethod, ds);
			addDataPersistenceCode(helperMethod, ds);

			helperMethodCode.append(Constants.NEW_LINE).append(helperMethod.toStringBuffer());

			if(ds.getTableName().equals(TableName.PopBidDocument)) {
				Method docExtractionMethod = new Method("private", false, "List<DatabaseRecord>", MethodName.ParseResults);
				docExtractionMethod.setSuffixName(Constants.UNDER_SCORE + "GetDocuments");
				docExtractionMethod.setParameters(method.getParameters());
				docExtractionMethod.addLineOfCode(
					new StringBuffer("//** This method should implement the logic to extract document URL/Title/PostedDate. **//"));
				docExtractionMethod.addLineOfCode(new StringBuffer(""));
				docExtractionMethod.addLineOfCode(new StringBuffer("return null; //** should return valid List<DatabaseRecord> object. **//"));
				helperMethodCode.append(Constants.NEW_LINE).append(docExtractionMethod.toStringBuffer());
			}

			if(additionalTab) {
//				if(ds.getParseResultConstruct() != null) {
//					ParseResultConstruct psConstruct = ds.getParseResultConstruct();
//					psConstruct.setIndentationLevel(this.indentation.getLevel());
//					psConstruct.setInvokeMethodCode(new StringBuffer("\t").append(helperMethod.getMethodNameAsStringBuffer()).append(Constants.SEMI_COLON));
//					method.addLineOfCode(psConstruct.toStringBuffer());
//				} else {
//					method.addLineOfCode(new StringBuffer("\t").append(helperMethod.getMethodNameAsStringBuffer()).append(Constants.SEMI_COLON));
//				}
				method.addLineOfCode(new StringBuffer("\t").append(helperMethod.getMethodNameAsStringBuffer()).append(Constants.SEMI_COLON));
			} else {
//				if(ds.getParseResultConstruct() != null) {
//					ParseResultConstruct psConstruct = ds.getParseResultConstruct();
//					psConstruct.setIndentationLevel(this.indentation.getLevel());
//					psConstruct.setInvokeMethodCode(helperMethod.getMethodNameAsStringBuffer().append(Constants.SEMI_COLON));
//					method.addLineOfCode(psConstruct.toStringBuffer());
//				} else {
//					method.addLineOfCode(helperMethod.getMethodNameAsStringBuffer().append(Constants.SEMI_COLON));
//				}
				method.addLineOfCode(helperMethod.getMethodNameAsStringBuffer().append(Constants.SEMI_COLON));
			}
		}
	}

	private void addDataPersistenceCode(Method helperMethod, DataSet ds) {
		StringBuffer lineOfCode = new StringBuffer();
		StringBuffer tableHandler = getTableDatabaseRecordHandler(ds);
		helperMethod.addLineOfCode(new StringBuffer(""));
		helperMethod.addLineOfCode(new StringBuffer("//Going to insert data in to ").append(ds.getTableName()).append(" table."));
		//adding prefix code to persistence code - start
		if(ds.getTableName().equals(TableName.PopBidDocument)) {
			addPrefixCodeForDocumentPersistence(lineOfCode, helperMethod);
		} else {
			lineOfCode.append(tableHandler).append(Constants.DOT).append(Constants.CLEAR).append(Constants.OPEN_PARENTHESIS)
				.append(Constants.CLOSE_PARENTHESIS).append(Constants.SEMI_COLON);
			helperMethod.addLineOfCode(lineOfCode);
		}
		//adding prefix code to persistence code - end
		if(ds.getAdditionalDataByReference().size() > 0) {
			if(ds.getTableName().equals(TableName.PopBidDocument)) {
				addAdditionalData(ds.getAdditionalDataByReference(), tableHandler, helperMethod, false, true);
			} else {
				addAdditionalData(ds.getAdditionalDataByReference(), tableHandler, helperMethod, false, false);
			}
		}
		if(ds.getAdditionalDataByValue().size() > 0) {
			if(ds.getTableName().equals(TableName.PopBidDocument)) {
				addAdditionalData(ds.getAdditionalDataByValue(), tableHandler, helperMethod, true, true);
			} else {
				addAdditionalData(ds.getAdditionalDataByValue(), tableHandler, helperMethod, true, false);
			}
		}
		List<Datum> resultData = null;
//		if(ds.getTableName().equals(TableName.PopBid)) {
//			resultData = ds.getBidData();
//		} else if(ds.getTableName().equals(TableName.PopBidIndustryCode)) {
//			resultData = ds.getBidIndustryData();
//		} else if(ds.getTableName().equals(TableName.PopBidLocation)) {
//			resultData = ds.getBidLocationData();
//		} else if(ds.getTableName().equals(TableName.PopBidContact)) {
//			resultData = ds.getBidContactData();
//		} else if(ds.getTableName().equals(TableName.PopBidSetAside)) {
//			resultData = ds.getBidSetAsideData();
//		} else if(ds.getTableName().equals(TableName.PopBidDocument)) {
//			resultData = ds.getBidDocumentData();
//		} else if(ds.getTableName().equals(TableName.PopBidVendor)) {
//			resultData = ds.getBidVendorData();
//		}

		resultData = ds.getData();
		if(resultData != null) {
			for(Datum datum : resultData) {
				if(datum.getColumnName() != null && (!datum.getColumnName().equals(Constants.NO_COLUMN_MAPPING))) {
					lineOfCode = new StringBuffer();
					if(ds.getTableName().equals(TableName.PopBidDocument)) {
						lineOfCode.append(Constants.TAB_TAB);
					}
					lineOfCode.append(tableHandler).append(Constants.DOT).append(Constants.SET_VALUE).append(Constants.OPEN_PARENTHESIS);
					lineOfCode.append(Constants.DOUBLE_QUOTE).append(datum.getColumnName()).append(Constants.DOUBLE_QUOTE).append(Constants.COMMA).append(Constants.SPACE);
					lineOfCode.append(datum.getName()).append(Constants.CLOSE_PARENTHESIS).append(Constants.SEMI_COLON);
					helperMethod.addLineOfCode(lineOfCode);
				}
			}
			lineOfCode = new StringBuffer();
			//adding post-fix code to persistence code - start
			if(ds.getTableName().equals(TableName.PopBidDocument)) {
				addPostfixCodeForDocumentPersistence(lineOfCode, helperMethod, tableHandler);
			} else {
				lineOfCode.append(tableHandler).append(Constants.DOT).append(Constants.APPLY).append(Constants.OPEN_PARENTHESIS)
					.append(Constants.CLOSE_PARENTHESIS).append(Constants.SEMI_COLON);
			}
			//adding post-fix code to persistence code - end
			helperMethod.addLineOfCode(lineOfCode);
			additionalCodeForDBEntity(helperMethod, ds);
		}
	}

	//This method hardcodes two lines of code for document persistence
	private void addPrefixCodeForDocumentPersistence(StringBuffer lineOfCode, Method helperMethod) {
		lineOfCode.append("if((documents != null) && (documents.Count > 0)) {").append(Constants.NEW_LINE).append(Constants.FOUR_TABS);
		lineOfCode.append("foreach(DatabaseRecord dbRecord in documents) {");
		helperMethod.addLineOfCode(lineOfCode);
	}

	private void addPostfixCodeForDocumentPersistence(StringBuffer lineOfCode, Method helperMethod, StringBuffer tableHandler) {
		lineOfCode.append(Constants.TAB_TAB).append(tableHandler).append(Constants.DOT).append(Constants.APPLY).append(Constants.OPEN_PARENTHESIS)
			.append(Constants.CLOSE_PARENTHESIS).append(Constants.SEMI_COLON);
		lineOfCode.append(Constants.NEW_LINE).append(Constants.FOUR_TABS).append("}").append(Constants.NEW_LINE).append(Constants.THREE_TABS).append("}");
	}

	private void addAdditionalData(Map<String, String> additionalData, StringBuffer tableHandler,
								   Method helperMethod, boolean addByValue, boolean additionalTabs) {
		StringBuffer lineOfCode = new StringBuffer();
		Iterator<String> keys = additionalData.keySet().iterator();
		String key = null;
		String value = null;
		while(keys.hasNext()) {
			key = keys.next();
			value = additionalData.get(key);
			lineOfCode = new StringBuffer();
			if(key.length() > 0) {
				if(additionalTabs) {
					lineOfCode.append(Constants.TAB_TAB);
				}
				lineOfCode.append(tableHandler).append(Constants.DOT).append(Constants.SET_VALUE).append(Constants.OPEN_PARENTHESIS);
				lineOfCode.append(Constants.DOUBLE_QUOTE).append(key).append(Constants.DOUBLE_QUOTE).append(Constants.COMMA).append(Constants.SPACE);
				if(addByValue) {
					lineOfCode.append(Constants.DOUBLE_QUOTE);
				}
				lineOfCode.append(value);
				if(addByValue) {
					lineOfCode.append(Constants.DOUBLE_QUOTE);
				}
				lineOfCode.append(Constants.CLOSE_PARENTHESIS).append(Constants.SEMI_COLON);
				helperMethod.addLineOfCode(lineOfCode);
			}
		}
	}

	private void additionalCodeForDBEntity(Method helperMethod, DataSet ds) {
		switch (ds.getTableName()) {
			case PopBid:
				helperMethod.addLineOfCode(new StringBuffer(""));
				helperMethod.containingClass.addInstanceVariable(
					new Variable("protected", false, false, "int", "bidID", "", true, false, false, null));
				helperMethod.addLineOfCode(new StringBuffer("bidID = _popBidDR.GetID();"));
			break;

			default:

			break;
		}
	}

	private StringBuffer getTableDatabaseRecordHandler(DataSet ds) {
		StringBuffer code = new StringBuffer();
		switch (ds.getTableName()) {
			case PopBid:
				code.append("_popBidDR");
			break;

			case PopBidIndustryCode:
				code.append("_popBidIndustryCodeDR");
			break;

			case PopBidLocation:
				code.append("_popBidLocationDR");
			break;

			case PopBidContact:
				code.append("_popBidContactDR");
			break;

			case PopBidSetAside:
				code.append("_popBidSetAsideDR");
			break;

			case PopBidDocument:
				code.append("dbRecord");
			break;

			case PopBidVendor:
				code.append("_popBidVendorDR");
			break;

			case PopBidVendorAgencyId:
				code.append("_popBidVendorAgencyIdDR");
			break;

			case PopBidVendorAgencyOrg:
				code.append("_popBidVendorAgencyOrgDR");
			break;

			case PopBidGrant:
				code.append("_popBidGrantDR");
			break;

			default:

			break;
		}
		return code;
	}

	/**
	 * This is the method that add code to extract data from result in parseResult* functions.
	 * like soliciationNumber = Text.GetTextStringBetweenTags(innerHTML, startTag, endTag);
	 *
	 * @param webPage
	 * @param helperMethod
	 * @param ds
	 */
	private void addDataExtractionCode(BrowserType browserType, Method helperMethod, DataSet ds) {
		List<Datum> resultData = null;
//		if(ds.getTableName().equals(TableName.PopBid)) {
//			resultData = ds.getBidData();
//		} else if(ds.getTableName().equals(TableName.PopBidIndustryCode)) {
//			resultData = ds.getBidIndustryData();
//		} else if(ds.getTableName().equals(TableName.PopBidLocation)) {
//			resultData = ds.getBidLocationData();
//		} else if(ds.getTableName().equals(TableName.PopBidContact)) {
//			resultData = ds.getBidContactData();
//		} else if(ds.getTableName().equals(TableName.PopBidSetAside)) {
//			resultData = ds.getBidSetAsideData();
//		} else if(ds.getTableName().equals(TableName.PopBidDocument)) {
//			resultData = ds.getBidDocumentData();
//		}else if(ds.getTableName().equals(TableName.PopBidVendor)) {
//			resultData = ds.getBidVendorData();
//		}else if(ds.getTableName().equals(TableName.PopBidVendorAgencyId)) {
//			resultData = ds.getBidVendorAgencyIdData();
//		}else if(ds.getTableName().equals(TableName.PopBidVendorAgencyOrg)) {
//			resultData = ds.getBidVendorAgencyOrgData();
//		}else if(ds.getTableName().equals(TableName.PopBidGrant)) {
//			resultData = ds.getBidGrantData();
//		}

		resultData = ds.getData();

		if(resultData != null) {
			for(Datum datum : resultData) {
				if(datum.getDatumIdentifier().equals(DatumIdentifier.Normal)) {
					if(ds.getTableName().equals(TableName.PopBidDocument)) {
						helperMethod.addMethodVariable(
							new Variable("", false, false, datum.getType(), datum.getName(), "",
								false, true, false, Constants.LINE_OF_CODE_IMPLEMENTATION_WARNING));
					} else {
						helperMethod.containingClass.addInstanceVariable(
							new Variable("protected", false, false, datum.getType(), datum.getName(), "",
								true, false, false, null));
					}
				}
				if(datum.getDatumIdentifier().equals(DatumIdentifier.Normal)){
					if(!ds.getTableName().equals(TableName.PopBidDocument)) {
						helperMethod.addLineOfCode(datum.toStringBuffer());
					}
				} else {
					switch (datum.getDatumIdentifier()) {
						case ProposalType:
							helperMethod.containingClass.addInstanceVariable(new Variable("protected", false, false,
								datum.getType(), datum.getName(), "", true, false, false, null));
							helperMethod.addLineOfCode(new StringBuffer("proposalType = getProposalType(sourceProposalType);"));
						break;

						case SourceURL:
							helperMethod.containingClass.addInstanceVariable(new Variable("protected", false, false,
									datum.getType(), datum.getName(), "", true, false, false, null));
							if(browserType.equals(BrowserType.Primary)) {
								helperMethod.addLineOfCode(new StringBuffer("sourceURL = Text.ChangeAmps(_primaryBrowser.LocationUrl);"));
							} else if(browserType.equals(BrowserType.Secondary)) {
								helperMethod.addLineOfCode(new StringBuffer("sourceURL = Text.ChangeAmps(_secondaryBrowser.LocationUrl);"));
							} else if(browserType.equals(BrowserType.Tertiary)) {
								helperMethod.addLineOfCode(new StringBuffer("sourceURL = Text.ChangeAmps(_tertiaryBrowser.LocationUrl);"));
							}
						break;

						default:
						break;
					}
				}
			}
		}
		//We are going to process Bid Documents in a different way than other methods.
		if(ds.getTableName().equals(TableName.PopBidDocument)) {
			addCodeForProcessingBidDocuments(helperMethod);
		}
	}

	private void addCodeForProcessingBidDocuments(Method helperMethod) {
		helperMethod.addMethodVariable(new Variable("", false, false, "List<DatabaseRecord>", "documents",
			"ParseResults_GetDocuments(html, unParsedHTML)", false, true, false, null));
	}

	private String getMethodImplementation()
			throws ClassDefinitionException, ArgumentValueDefinitionException,
				   LineOfCodeException, ActionDefinitionException, RHSTaskException {
		return getFicstarMethodImplementation();
	}

	private String getFicstarMethodImplementation()
			throws ClassDefinitionException, ArgumentValueDefinitionException,
				   LineOfCodeException, ActionDefinitionException, RHSTaskException {
		StringBuffer code = new StringBuffer();
		if(name.equals(MethodName.PrimaryBrowserControl)) {
			appendFicstarPBCImplementation(code);
		} else if(name.equals(MethodName.SecondaryBrowserControl)) {
			appendFicstarSBCImplementation(code);
		} else if(name.equals(MethodName.TertiaryBrowserControl)) {
			appendFicstarTBCImplementation(code);
		} else if(name.equals(MethodName.CollectResultLinks)) {
			appendFicstarCRLImplementation(code);
		} else if(name.equals(MethodName.ParseResults)) {
			appendFicstarParseResultsImplementation(code);
		}
		return code.toString();
	}

	private void appendFicstarParseResultsImplementation(StringBuffer code) throws ClassDefinitionException, ArgumentValueDefinitionException,
													   							   LineOfCodeException, RHSTaskException, ActionDefinitionException {
		//adding initial lines of code
		if(initialLinesOfCode.size() > 0) {
			code.append(generateInitialLinesOfCode(indentation.getTabsForNextLevel()));
		}

		for(StringBuffer loc : linesOfCodeAsStringBuffer) {
			code.append(Constants.NEW_LINE).append(indentation.getTabsForNextLevel()).append(loc);
		}
	}

	private void appendFicstarCRLImplementation(StringBuffer code) throws ClassDefinitionException, ArgumentValueDefinitionException,
																		  LineOfCodeException, RHSTaskException, ActionDefinitionException {
		//adding initial lines of code
		if(initialLinesOfCode.size() > 0) {
			code.append(generateInitialLinesOfCode(indentation.getTabsForNextLevel()));
		}

		//this is the code that adds content to collectResultLinks function
		StringBuffer standardChecks = new StringBuffer();
		StringBuffer baseCRLCheck = new StringBuffer();
		if(invokeBaseCRL) {
			if(standardChecks.length() > 0) {
				if(standardChecks.charAt(standardChecks.length()-1) == '}') {
					baseCRLCheck.append(Constants.NEW_LINE).append(Constants.THREE_TABS).append("else");
					baseCRLCheck.append(Constants.NEW_LINE).append(Constants.THREE_TABS).append(Constants.OPEN_BRACES);
					baseCRLCheck.append(Constants.NEW_LINE).append(Constants.FOUR_TABS);
					baseCRLCheck.append("CollectResultLinks(html);");
					baseCRLCheck.append(Constants.NEW_LINE).append(Constants.THREE_TABS).append(Constants.CLOSE_BRACES);
				} else {
					warningMessages.add("Could not add base.CollectResultLinks() code. Check the other criterias properly.");
				}
			} else if(standardChecks.length() == 0) {
				baseCRLCheck.append(Constants.NEW_LINE).append(Constants.THREE_TABS).append("CollectResultLinks(html);");
			}
		}
		code.append(baseCRLCheck);
		if(secureAccess) {
			appendCodeSnippetToAppendHTTPSToLinks(code);
		}
	}

	private void appendCodeSnippetToAppendHTTPSToLinks(StringBuffer code) {
		code.append(Constants.NEW_LINE).append(Constants.NEW_LINE).append(Constants.THREE_TABS);
		code.append("//appending https to links..");
        code.append(Constants.NEW_LINE).append(Constants.THREE_TABS).append("string link = null;").append(Constants.NEW_LINE);
        code.append(Constants.THREE_TABS).append("for(int i = 0; i < _resultLinks.Count; i++)").append(Constants.NEW_LINE);
        code.append(Constants.THREE_TABS).append("{").append(Constants.NEW_LINE);
        code.append(Constants.FOUR_TABS).append("link = _resultLinks[i];").append(Constants.NEW_LINE);
        code.append(Constants.FOUR_TABS).append("if(!link.Contains(\"https\"))").append(Constants.NEW_LINE);
        code.append(Constants.FOUR_TABS).append("{").append(Constants.NEW_LINE);
        code.append(Constants.FIVE_TABS).append("link = \"https://\" + link;").append(Constants.NEW_LINE);
        code.append(Constants.FOUR_TABS).append("}").append(Constants.NEW_LINE);
        code.append(Constants.FOUR_TABS).append("_resultLinks[i] = link;").append(Constants.NEW_LINE);
        code.append(Constants.THREE_TABS).append("}");
	}

	private void appendFicstarPBCImplementation(StringBuffer code) throws ClassDefinitionException, ArgumentValueDefinitionException,
				  														  LineOfCodeException, ActionDefinitionException, RHSTaskException {
		StringBuffer noOfTabs = indentation.getTabsForNextLevel();
		if(mainIfElseStatement != null) {
			code.append(Constants.NEW_LINE).append(noOfTabs).append(mainIfElseStatement.toStringBuffer2());
			noOfTabs = indentation.getAdditionalTabs(2);
		}

		this.containingClass.addInstanceVariable(new Variable("protected", false, false, "string", "primaryBrowserHTML", "", true, false, false, null));

		code.append(Constants.NEW_LINE).append(noOfTabs).append("primaryBrowserHTML = _primaryBrowser.GetInnerHTML();").append(Constants.NEW_LINE);

		//adding initial lines of code
		if(initialLinesOfCode.size() > 0) {
			code.append(generateInitialLinesOfCode(noOfTabs));
		}

		StringBuffer standardChecks = new StringBuffer();
		addStandardBrowserControlPageChecks(standardChecks, "primaryBrowserHTML", "_primaryBrowser", noOfTabs);

		StringBuffer basePBCCheck = new StringBuffer();

		//code written for a specific web page should be here. start
		for(WebPage wPg : pagesToParseInPrimaryBrowser) {
			appendWebPageSpecificCode(standardChecks, wPg, noOfTabs);
		}

		//adding base PBC invoke code
		if(invokeBasePBC) {
			if(standardChecks.length() > 0) {
				if(standardChecks.charAt(standardChecks.length()-1) == '}') {
					basePBCCheck.append(Constants.NEW_LINE).append(noOfTabs).append("else");
					basePBCCheck.append(Constants.NEW_LINE).append(noOfTabs).append(Constants.OPEN_BRACES);
					if(mainIfElseStatement != null) {
						basePBCCheck.append(Constants.NEW_LINE).append(indentation.getAdditionalTabs(mainIfElseStatement.getIndentation().getLevel()));
					} else {
						basePBCCheck.append(Constants.NEW_LINE).append(indentation.getAdditionalTabs(indentation.getLevel()));
					}
					basePBCCheck.append("base.PrimaryBrowserControl();");
					basePBCCheck.append(Constants.NEW_LINE).append(noOfTabs).append(Constants.CLOSE_BRACES);
				} else {
					warningMessages.add("Could not add base.PrimaryBrowserControl() code. Check the other criterias properly.");
					basePBCCheck.append(Constants.NEW_LINE).append(noOfTabs).append("base.PrimaryBrowserControl();");
				}
			} else if(standardChecks.length() == 0) {
				basePBCCheck.append(Constants.NEW_LINE).append(noOfTabs).append("base.PrimaryBrowserControl();");
			}
		}

		//code written for a specific web page should be here. end
		code.append(standardChecks);
		code.append(basePBCCheck);

		if(mainIfElseStatement != null) {
			code.append(Constants.NEW_LINE).append(mainIfElseStatement.getIndentation().getTabs());
			code.append("}");
		}

	}

	private void appendFicstarSBCImplementation(StringBuffer code)
			throws ClassDefinitionException, ArgumentValueDefinitionException,
				   LineOfCodeException, ActionDefinitionException, RHSTaskException {

		//String noOfTabs = Constants.THREE_TABS;
		StringBuffer noOfTabs = indentation.getTabsForNextLevel();
		if(mainIfElseStatement != null) {
			code.append(Constants.NEW_LINE).append(noOfTabs).append(mainIfElseStatement.toStringBuffer2());
			noOfTabs = indentation.getAdditionalTabs(2);
		}

		this.containingClass.addInstanceVariable(new Variable("protected", false, false, "string", "secondaryBrowserHTML", "",
			true, false, false, null));

		code.append(Constants.NEW_LINE).append(noOfTabs).append("secondaryBrowserHTML = _secondaryBrowser.GetInnerHTML();").append(Constants.NEW_LINE);

		//adding initial lines of code
		if(initialLinesOfCode.size() > 0) {
			code.append(generateInitialLinesOfCode(noOfTabs));
		}

		StringBuffer standardChecks = new StringBuffer();
		addStandardBrowserControlPageChecks(standardChecks, "secondaryBrowserHTML", "_secondaryBrowser", noOfTabs);

		StringBuffer baseSBCCheck = new StringBuffer();

		//code written for a specific web page should be here. start
		for(WebPage wPg : pagesToParseInSecondaryBrowser) {
			appendWebPageSpecificCode(standardChecks, wPg, noOfTabs);
		}

		//invoking base SBC invoke code
		if(invokeBaseSBC) {
			if(standardChecks.length() > 0) {
				if(standardChecks.charAt(standardChecks.length()-1) == '}') {
					baseSBCCheck.append(Constants.NEW_LINE).append(noOfTabs).append("else");
					baseSBCCheck.append(Constants.NEW_LINE).append(noOfTabs).append(Constants.OPEN_BRACES);
					if(mainIfElseStatement != null) {
						baseSBCCheck.append(Constants.NEW_LINE).append(indentation.getAdditionalTabs(mainIfElseStatement.getIndentation().getLevel()));
					} else {
						baseSBCCheck.append(Constants.NEW_LINE).append(indentation.getAdditionalTabs(indentation.getLevel()));
					}
					baseSBCCheck.append("base.SecondaryBrowserControl();");
					baseSBCCheck.append(Constants.NEW_LINE).append(noOfTabs).append(Constants.CLOSE_BRACES);
				} else {
					warningMessages.add("Could not add base.SecondaryBrowserControl() code. Check the other criterias properly.");
				}
			} else if(standardChecks.length() == 0) {
				baseSBCCheck.append(Constants.NEW_LINE).append(noOfTabs).append("base.SecondaryBrowserControl();");
			}
		}

		//code written for a specific web page should be here. end
		code.append(standardChecks);
		code.append(baseSBCCheck);

		if(mainIfElseStatement != null) {
			code.append(Constants.NEW_LINE).append(mainIfElseStatement.getIndentation().getTabs());
			code.append("}");
		}
	}

	private void appendFicstarTBCImplementation(StringBuffer code)
			throws ClassDefinitionException, ArgumentValueDefinitionException,
				   LineOfCodeException, ActionDefinitionException, RHSTaskException {
		StringBuffer noOfTabs = indentation.getTabsForNextLevel();
		if(mainIfElseStatement != null) {
			code.append(Constants.NEW_LINE).append(noOfTabs).append(mainIfElseStatement.toStringBuffer2());
			noOfTabs = indentation.getAdditionalTabs(2);
		}

		this.containingClass.addInstanceVariable(new Variable("protected", false, false, "string", "tertiaryBrowserHTML", "",
			true, false, false, null));

		code.append(Constants.NEW_LINE).append(noOfTabs).append("tertiaryBrowserHTML = _tertiaryBrowser.GetInnerHTML();").append(Constants.NEW_LINE);

		//adding initial lines of code
		if(initialLinesOfCode.size() > 0) {
			code.append(generateInitialLinesOfCode(noOfTabs));
		}

		StringBuffer standardChecks = new StringBuffer();
		addStandardBrowserControlPageChecks(standardChecks, "tertiaryBrowserHTML", "_tertiaryBrowser", noOfTabs);

		StringBuffer baseTBCCheck = new StringBuffer();
		if(invokeBaseTBC) {
			if(standardChecks.length() > 0) {
				if(standardChecks.charAt(standardChecks.length()-1) == '}') {
					baseTBCCheck.append(Constants.NEW_LINE).append(noOfTabs).append("else");
					baseTBCCheck.append(Constants.NEW_LINE).append(noOfTabs).append(Constants.OPEN_BRACES);
					if(mainIfElseStatement != null) {
						baseTBCCheck.append(Constants.NEW_LINE).append(indentation.getAdditionalTabs(mainIfElseStatement.getIndentation().getLevel()));
					} else {
						baseTBCCheck.append(Constants.NEW_LINE).append(indentation.getAdditionalTabs(indentation.getLevel()));
					}
					baseTBCCheck.append("base.TertiaryBrowserControl();");
					baseTBCCheck.append(Constants.NEW_LINE).append(noOfTabs).append(Constants.CLOSE_BRACES);
				} else {
					warningMessages.add("Could not add base.TertiaryBrowserControl() code. Check the other criterias properly.");
				}
			} else if(standardChecks.length() == 0) {
				baseTBCCheck.append(Constants.NEW_LINE).append(noOfTabs).append("base.TertiaryBrowserControl();");
			}
		}
		//code written for a specific web page should be here. start
		for(WebPage wPg : pagesToParseInTertiaryBrowser) {
			appendWebPageSpecificCode(standardChecks, wPg, noOfTabs);
		}

		//code written for a specific web page should be here. end
		code.append(standardChecks);
		code.append(baseTBCCheck);

		if(mainIfElseStatement != null) {
			code.append(Constants.NEW_LINE).append(mainIfElseStatement.getIndentation().getTabs());
			code.append("}");
		}
	}

	private void appendWebPageSpecificCode(StringBuffer code, WebPage webPage, StringBuffer tabs)
															throws ClassDefinitionException, ArgumentValueDefinitionException,
																   LineOfCodeException, ActionDefinitionException, RHSTaskException {
		boolean elseBlock = false;
		if(code.length() > 0) {
			if(code.charAt(code.length()-1) == '}') {
				if(webPage.getCondition() != null) {
					code.append(Constants.NEW_LINE).append(tabs).append("else if(");
				} else {
					elseBlock = true;
					code.append(Constants.NEW_LINE).append(tabs).append("else");
				}
			} else {
				if(webPage.getCondition() != null) {
					code.append(Constants.NEW_LINE).append(tabs).append("if(");
				}
			}
		} else {
			if(webPage.getCondition() != null) {
				code.append(Constants.NEW_LINE).append(tabs).append("if(");
			}
		}
		//condition code
		if(webPage.getCondition() != null) {
			code.append(((Condition)webPage.getCondition()).toStringBuffer());
		}

		//one time execution code -- variable declaration & adding condition
		if(webPage.isOneTimeExecution()) {
			this.containingClass.addInstanceVariable(new Variable("protected", false, false, "bool",
				Constants.ACCESSED + webPage.getPageName(), "false", true, false, false, null));
			code.append(Constants.SPACE).append(Constants.LOGICAL_AND).append(Constants.SPACE).append(Constants.OPEN_PARENTHESIS);
			code.append(Constants.LOGICAL_COMPLEMENT).append(Constants.ACCESSED + webPage.getPageName()).append(Constants.CLOSE_PARENTHESIS);
		}

		//closing if loop and opening brace
		if(webPage.getCondition() != null) {
			code.append(")");
		}

		if(webPage.getCondition() != null | elseBlock) {
			code.append(Constants.NEW_LINE).append(tabs).append(Constants.OPEN_BRACES);
		}

		//one time execution code -- variable declaration & adding condition
		if(webPage.isOneTimeExecution()) {
			code.append(Constants.NEW_LINE).append(indentation.getTabsForNextLevel(tabs));
			code.append(Constants.ACCESSED + webPage.getPageName()).append(Constants.SPACE).append(Constants.EQUALS);
			code.append(Constants.SPACE).append(Constants.TRUE).append(Constants.SEMI_COLON);
		}


		if(webPage.getCondition() != null | elseBlock) {
			//replace code for below
			code.append(webPage.getEvaluationCode(tabs, false));
			code.append(Constants.NEW_LINE).append(tabs).append(Constants.CLOSE_BRACES);
		} else {
			code.append(webPage.getEvaluationCode(tabs, true));
		}
	}

	/**
	 * This method should implement all the default condition checks that we do in primary browser control.
	 *
	 * @param code
	 */
	private void addStandardBrowserControlPageChecks(StringBuffer code, String innerHTML, String browser, StringBuffer tabs) {
		if(secureAccess) {
			code.append(Constants.NEW_LINE).append(tabs);
			code.append("if(").append(innerHTML).append(".Contains(\"Continue to this website (not recommended).</\"))");
			code.append(Constants.NEW_LINE).append(tabs);
			code.append(Constants.OPEN_BRACES);
			code.append(Constants.NEW_LINE).append(indentation.getTabsForNextLevel(tabs));
			code.append(browser).append(".ClickElement(BrowserControlLibrary.ElementTagType.a, BrowserControlLibrary.ElementFieldType.innerHTML, \"Continue to this website (not recommended).\", false);");
			code.append(Constants.NEW_LINE).append(tabs);
			code.append(Constants.CLOSE_BRACES);
		}
	}

	public void addCodeToEvaluateWebPages(IfElseStatement ifElseStatement) throws WebPageException {
		if(ifElseStatement != null) {
			ifElseStatement.setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
		}
		if(!ifElseStatement.getStatementType().equals(IfElseStatementType.None)) {
			mainIfElseStatement = ifElseStatement;
		}
		if(ifElseStatement.getWebPages() != null) {
			addWebPages(ifElseStatement.getWebPages());
		}
	}

	private void addWebPages(List<WebPage> webPages) throws WebPageException {
		List<Integer> orderOfWebPage = new ArrayList<Integer>();
		if(webPages != null) {
			WebPage page = null;
			int noOfWebPages = webPages.size();
			for(int i = 0; i < noOfWebPages; i++) {
				page = webPages.get(i);
				//checking for navigation order duplicates
				if(orderOfWebPage.contains(page.getNavigationOrder())) {
					throw new WebPageException("One or more Web Flow has same Navigation Order.");
				} else {
					if(page.getNavigationOrder() < 1) {
						throw new WebPageException("One or more Web Flow does not have Navigation Order value.");
					}
					orderOfWebPage.add(page.getNavigationOrder());
					if(page.getBrowserToNavigate() == BrowserType.Primary) {
						this.pagesToParseInPrimaryBrowser.add(page);
					} else if (page.getBrowserToNavigate() == BrowserType.Secondary) {
						this.pagesToParseInSecondaryBrowser.add(page);
					} else if (page.getBrowserToNavigate() == BrowserType.Tertiary) {
						this.pagesToParseInTertiaryBrowser.add(page);
					} else if (page.getBrowserToNavigate() == BrowserType.Others) {
						this.otherPages.add(page);
					} else {
						throw new WebPageException("One or more Web Flow has invalid BrowserToNavigate value, valid values are " +
							"BrowserType.Primary, BrowserType.Secondary, BrowserType.Tertiary and BrowserType.Others");
					}
					page.setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
					//checking whether a web page contains either result links or results. If so add them to the corresponding containers.
					//toStringBuffer method generates code for these methods
					if(page.getPageType().equals(PageType.ResultLinks)) {
						webPageWithResultLinks.add(page);
					} else if(page.getPageType().equals(PageType.Result)) {
						webPageWithResults.add(page);
					}
				}
			}
		}
	}

	public Class getContainingClass() {
		return containingClass;
	}

	public void setContainingClass(Class containingClass) {
		this.containingClass = containingClass;
	}

	public boolean isInvokeBaseSBC() {
		return invokeBaseSBC;
	}

	public void setInvokeBaseSBC(boolean invokeBaseSBC) {
		this.invokeBaseSBC = invokeBaseSBC;
	}

	public boolean isInvokeBaseTBC() {
		return invokeBaseTBC;
	}

	public void setInvokeBaseTBC(boolean invokeBaseTBC) {
		this.invokeBaseTBC = invokeBaseTBC;
	}

	public boolean isInvokeBaseCRL() {
		return invokeBaseCRL;
	}

	public void setInvokeBaseCRL(boolean invokeBaseCRL) {
		this.invokeBaseCRL = invokeBaseCRL;
	}

	public String getPrefixName() {
		return prefixName;
	}

	public void setPrefixName(String prefixName) {
		this.prefixName = prefixName;
	}

	public String getSuffixName() {
		return suffixName;
	}

	public void setSuffixName(String suffixName) {
		this.suffixName = suffixName;
	}

	public void addLineOfCode(StringBuffer lineOfCode) {
		linesOfCodeAsStringBuffer.add(lineOfCode);
	}

	public StringBuffer getMethodNameAsStringBuffer() {
		StringBuffer methodName = new StringBuffer();
		if(this.prefixName != null || this.suffixName != null) {
			if(this.prefixName != null) {
				methodName.append(this.prefixName).append(this.name);
			}
			if(this.suffixName != null) {
				if(this.prefixName == null) {
					methodName.append(this.name);
				}
				methodName.append(this.suffixName);
			}
		} else {
			methodName.append(this.name);
		}
		methodName.append(Constants.OPEN_PARENTHESIS);
		if(parameters.size() > 0){
			Iterator<Variable> itrParameters = parameters.iterator();
			Variable tempVar = null;
			StringBuffer paramString = new StringBuffer();
			while(itrParameters.hasNext()) {
				tempVar = itrParameters.next();
				if(paramString.length() > 0) {
					paramString.append(Constants.COMMA).append(Constants.SPACE);
				}
				if(tempVar.getAliasName() != null && tempVar.getAliasName().length() > 0) {
					paramString.append(tempVar.getAliasName());
				} else {
					paramString.append(tempVar.getName());
				}
			}
			methodName.append(paramString);
		}
		methodName.append(Constants.CLOSE_PARENTHESIS);
		return methodName;
	}

	public void addMethodVariable(Variable methodVariable) {
		if(methodVariable != null) {
			methodVariable.setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
		}
		this.methodVariables.add(methodVariable);
	}

	public void generateMethodVariableCode() {
		StringBuffer methodVariables = new StringBuffer();
		methodVariablesCode = new StringBuffer();
		for(Variable var : this.methodVariables) {
			methodVariables.append(var.toStringBuffer());
		}
		methodVariablesCode.append(methodVariables);
	}

	public void setAliasNameToParameter(String variableName, String aliasName) {
		int noOfParameters = 0;
		Variable tempParam = null;
		if(parameters.size() > 0) {
			noOfParameters = parameters.size();
			for(int index = 0; index < noOfParameters; index ++) {
				tempParam = parameters.get(index);
				if(tempParam.getName().equals(variableName)) {
					tempParam.setAliasName(aliasName);
				}
			}
		}
	}

	public void addInitialLinesOfCode(LineOfCode loc) {
		if(loc != null) {
			loc.setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
		}
		this.initialLinesOfCode.add(loc);
	}

	public List<LineOfCode> getInitialLinesOfCode() {
		return this.initialLinesOfCode;
	}

	public void setIndentationLevel(int level) {
		this.indentation.setLevel(level);
	}

	public IfElseStatement getAdhocIfElseStatement() {
		return adhocIfElseStatement;
	}

	public void setAdhocIfElseStatement(IfElseStatement adhocIfElseStatement) {
		if(adhocIfElseStatement != null) {
			if(adhocIfElseStatement.getCode() != null && (adhocIfElseStatement.getCode().size() > 0)) {
				adhocIfElseStatement.setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabs()));
				this.adhocIfElseStatement = adhocIfElseStatement;
			}
		}
	}

}
