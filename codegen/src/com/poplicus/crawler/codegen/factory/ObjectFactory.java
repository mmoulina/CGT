package com.poplicus.crawler.codegen.factory;

import java.util.ArrayList;
import java.util.List;

import com.poplicus.crawler.codegen.Action;
import com.poplicus.crawler.codegen.ArgumentValueDefinition;
import com.poplicus.crawler.codegen.Class;
import com.poplicus.crawler.codegen.Constructor;
import com.poplicus.crawler.codegen.DataGroup;
import com.poplicus.crawler.codegen.DataSet;
import com.poplicus.crawler.codegen.Datum;
import com.poplicus.crawler.codegen.Expression;
import com.poplicus.crawler.codegen.IfElseStatement;
import com.poplicus.crawler.codegen.LineOfCode;
import com.poplicus.crawler.codegen.Method;
import com.poplicus.crawler.codegen.RHSDefinition;
import com.poplicus.crawler.codegen.Variable;
import com.poplicus.crawler.codegen.ArgumentValueDefinition.ArgumentValueType;
import com.poplicus.crawler.codegen.IfElseStatement.IfElseStatementType;
import com.poplicus.crawler.codegen.Method.MethodName;
import com.poplicus.crawler.codegen.WebPage;
import com.poplicus.crawler.codegen.definitions.ActionDefinition;
import com.poplicus.crawler.codegen.definitions.BrowserDefinition;
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
import com.poplicus.crawler.codegen.definitions.MainIfDefinition;
import com.poplicus.crawler.codegen.definitions.MainIfElementsDef;
import com.poplicus.crawler.codegen.definitions.NavigateDefinition;
import com.poplicus.crawler.codegen.definitions.OperandDefinition;
import com.poplicus.crawler.codegen.definitions.OperatorDefinition;
import com.poplicus.crawler.codegen.definitions.ParameterDefinition;
import com.poplicus.crawler.codegen.definitions.RightHSDefinition;
import com.poplicus.crawler.codegen.definitions.StringDefinition;
import com.poplicus.crawler.codegen.definitions.VariableDefinition;
import com.poplicus.crawler.codegen.definitions.WebPageDefinition;
import com.poplicus.crawler.codegen.utilities.CodeGenUtil;

public class ObjectFactory {

	private static ObjectFactory objectFactory = null;

	private static CodeGenUtil codeGenUtil = CodeGenUtil.getInstance();

	private ObjectFactory() {

	}

	public static ObjectFactory getInstance() {
		if(objectFactory == null) {
			objectFactory = new ObjectFactory();
		}
		return objectFactory;
	}

	public Class getClass(CrawlerDefinition crawlerDef) {
		Class clazz = new Class(null, crawlerDef.getClientName() + "_" + crawlerDef.getSiteName(), crawlerDef.getSuperClass());
		//Class clazz = new Class(null, crawlerDef.getName(), crawlerDef.getSuperClass());
		//adding instance variables
		clazz.addInstanceVariable(getInstanceVariable("CLIENT_NAME", crawlerDef.getClientName()));
		clazz.addInstanceVariable(getInstanceVariable("SITE_NAME", crawlerDef.getSiteName()));
		clazz.addInstanceVariable(getInstanceVariable("SOURCE_ID", crawlerDef.getSourceID()));
		clazz.addInstanceVariable(getInstanceVariable("SOURCE_NAME", crawlerDef.getSourceName()));

		return clazz;
	}

	public Variable getInstanceVariable(String name, String value) {
		Variable var = new Variable("protected", false, true, "string", name, value, true, false, false, null);
		return var;
	}

	public Method getPBCMethod(CrawlerDefinition crawlerDef, Class clazz) throws Exception {
		Method method = new Method("public", true, "void", MethodName.PrimaryBrowserControl);
		method.setContainingClass(clazz);
		method.setSecureAccess(crawlerDef.getPrimaryBrowserDefinition().isSecuredWebPage());
		method.setInvokeBasePBC(crawlerDef.isInvokeBasePBC());
		addWebPages(crawlerDef, method, "primary", clazz);


		return method;
	}

	public Method getSBCMethod(CrawlerDefinition crawlerDef, Class clazz) throws Exception {
		Method method = new Method("public", true, "void", MethodName.SecondaryBrowserControl);
		method.setContainingClass(clazz);
		method.setSecureAccess(crawlerDef.getSecondaryBrowserDefinition().isSecuredWebPage());
		method.setInvokeBaseSBC(crawlerDef.isInvokeBaseSBC());
		addWebPages(crawlerDef, method, "secondary", clazz);

		return method;
	}

	public Method getTBCMethod(CrawlerDefinition crawlerDef, Class clazz) throws Exception {
		Method method = new Method("public", true, "void", MethodName.TertiaryBrowserControl);
		method.setContainingClass(clazz);
		method.setSecureAccess(crawlerDef.getTertiaryBrowserDefinition().isSecuredWebPage());
		method.setInvokeBaseTBC(crawlerDef.isInvokeBaseTBC());
		addWebPages(crawlerDef, method, "tertiary", clazz);

		return method;
	}

	private void addWebPages(CrawlerDefinition crawlerDef, Method method, String browser, Class clazz) throws Exception {
		List<WebPageDefinition> webPages = crawlerDef.getWebPages().getWebPages(browser);
		int noOfWebPages = webPages.size();
		List<WebPage> webPageList = new ArrayList<WebPage>(noOfWebPages);
		for(int index = 0; index < noOfWebPages; index ++) {
			webPageList.add(getWebPage(webPages.get(index), clazz));
		}

		IfElseStatement ifElseOfMethod = null;
		if(browser.equalsIgnoreCase("primary")) {
			if(crawlerDef.getPrimaryBrowserDefinition().getConditions() != null
				&& ( crawlerDef.getPrimaryBrowserDefinition().getConditions().getConditions().size() > 0 ) ) {
				ifElseOfMethod = new IfElseStatement(IfElseStatementType.If);
				ifElseOfMethod.setCondition(codeGenUtil.getCondition(crawlerDef.getPrimaryBrowserDefinition().getConditions()));
			} else {
				ifElseOfMethod = new IfElseStatement(IfElseStatementType.None);
			}
		} else if(browser.equalsIgnoreCase("secondary")) {
			if(crawlerDef.getSecondaryBrowserDefinition().getConditions() != null
				&& ( crawlerDef.getSecondaryBrowserDefinition().getConditions().getConditions().size() > 0 ) ) {
				ifElseOfMethod = new IfElseStatement(IfElseStatementType.If);
				ifElseOfMethod.setCondition(codeGenUtil.getCondition(crawlerDef.getSecondaryBrowserDefinition().getConditions()));
			} else {
				ifElseOfMethod = new IfElseStatement(IfElseStatementType.None);
			}
		} else if(browser.equalsIgnoreCase("tertiary")) {
			if(crawlerDef.getTertiaryBrowserDefinition().getConditions() != null
				&& ( crawlerDef.getTertiaryBrowserDefinition().getConditions().getConditions().size() > 0 ) ) {
				ifElseOfMethod = new IfElseStatement(IfElseStatementType.If);
				ifElseOfMethod.setCondition(codeGenUtil.getCondition(crawlerDef.getTertiaryBrowserDefinition().getConditions()));
			} else {
				ifElseOfMethod = new IfElseStatement(IfElseStatementType.None);
			}
		}
		ifElseOfMethod.setWebPages(webPageList);
		method.addCodeToEvaluateWebPages(ifElseOfMethod);
	}

	private WebPage getWebPage(WebPageDefinition webPageDef, Class clazz) throws Exception {
		WebPage webPage = new WebPage();
		webPage.setPageName(webPageDef.getName());
		webPage.setUrl(webPageDef.getUrl());
		webPage.setBrowserToNavigate(codeGenUtil.getBrowserType(webPageDef.getBrowser()));
		webPage.setNavigationOrder((new Integer(webPageDef.getNavigationOrder())).intValue());
		webPage.setOneTimeExecution(webPageDef.isExecuteOnce());
		webPage.setPageType(codeGenUtil.getPageType(webPageDef.getType()));
		webPage.setCondition(codeGenUtil.getCondition(webPageDef.getConditions()));
		webPage.addCodeToEvaluate(getMainIfOfWebPage(webPageDef.getMainIF(), clazz, webPageDef.getName()));

		if(webPageDef.getType().equals("Result")) {
			List<DataGroupDefinition> dGroupDef = webPageDef.getDataGroups();
			for(int dGroupIndex = 0; dGroupIndex < dGroupDef.size(); dGroupIndex ++) {
				webPage.addDataGroup(getDataGroup((DataGroupDefinition) dGroupDef.get(dGroupIndex)));
			}
		}

		return webPage;
	}

	private DataGroup getDataGroup(DataGroupDefinition dGroupDef) throws Exception {
		DataGroup dGroup = new DataGroup();
		dGroup.setStartTag(dGroupDef.getStartTag());
		dGroup.setEndTag(dGroupDef.getEndTag());

		List<DataSetDefinition> dSetDefs = dGroupDef.getDataSets();
		List<DatumDefinition> datumDefs = null;
		DataSetDefinition dSetDef = null;
		DatumDefinition datumDef = null;
		DataSet dSet = null;
		Datum datum = null;

		int noOfDataSets = dSetDefs.size();
		int noOfDatum = 0;
		for(int dSetIndex = 0; dSetIndex < noOfDataSets; dSetIndex ++) {

			dSetDef = dSetDefs.get(dSetIndex);
			dSet = new DataSet(codeGenUtil.getValidTableName(dSetDef.getTableName()), dSetDef.getAdditionalInformation());
			datumDefs = dSetDef.getData();
			noOfDatum = datumDefs.size();
			for(int datumIndex = 0; datumIndex < noOfDatum; datumIndex ++) {
				datumDef = datumDefs.get(datumIndex);
				if(datumDef.getDatumIdentifier() != null) {
					datum = new Datum(datumDef.getDatumIdentifier());
				} else {
					datum = new Datum(datumDef.getHtmlSource(), datumDef.getStartTag(), datumDef.getEndTag(),
						datumDef.getDataType(), datumDef.getVariableName(), datumDef.getFieldName(), datumDef.isEntireString(), datumDef.isAfterTag());
				}
				dSet.addDatum(datum);

			}

			dSet.addAdditionalDataByReference(dSetDef.getAdditionalDataByReference());
			dSet.addAdditionalDataByValue(dSetDef.getAdditionalDataByValue());

			dGroup.addDataSet(dSet);

		}
		return dGroup;
	}

	private IfElseStatement getMainIfOfWebPage(MainIfDefinition mainIf, Class clazz, String webPageName) throws Exception {
		IfElseStatement ifElse = null;
		if(mainIf.getConditions() != null && (mainIf.getConditions().getConditions().size() > 0)) {
			ifElse = new IfElseStatement(IfElseStatementType.If);
			ifElse.setCondition(codeGenUtil.getCondition(mainIf.getConditions()));
		} else {
			ifElse = new IfElseStatement(IfElseStatementType.None);
		}
		List<MainIfElementsDef> mainIfElements = mainIf.getMainIfElementsDef();
		int noOfMainIFElements = mainIfElements.size();
		IfElseStatement peerIf = null;
		for(int index = 0; index < noOfMainIFElements; index ++) {
			peerIf = udpateMainIfElseCodeBlocklements(mainIfElements.get(index), ifElse, clazz, webPageName, peerIf);
		}
		return ifElse;
	}

//	private void udpateMainIfElseCodeBlocklements(MainIfElementsDef element, IfElseStatement mainIf, Class clazz, String webPageName) throws Exception {
//		if(element instanceof CodeBlockDefinition) {
//			updateFromCodeBlock((CodeBlockDefinition) element, mainIf, clazz, webPageName);
//		} else if(element instanceof IfDefinition) {
//
//		} else if(element instanceof ElseIfDefinition) {
//
//		} else if(element instanceof ElseDefinition) {
//
//		}
//	}

	private IfElseStatement udpateMainIfElseCodeBlocklements(Object element, IfElseStatement mainIf, Class clazz,
												  String webPageName, IfElseStatement peerIf) throws Exception {
		if(element instanceof IfDefinition) {
			IfElseStatement ifStatement = new IfElseStatement(IfElseStatementType.If);
			ifStatement.setCondition(codeGenUtil.getCondition(((IfDefinition) element).getConditions()));
			if(((IfDefinition) element).getIfElseElements().size() > 0) {
				List<IfElseElementsDef> childs = ((IfDefinition) element).getIfElseElements();
				int noOfChilds = childs.size();
				IfElseStatement childPeerIf = null;
				for(int childIndex = 0; childIndex < noOfChilds; childIndex ++) {
					childPeerIf = udpateMainIfElseCodeBlocklements(childs.get(childIndex), ifStatement, clazz, webPageName, childPeerIf);
				}
			}
			peerIf = ifStatement;
			mainIf.addCode(ifStatement);
		} else if(element instanceof ElseIfDefinition) {
			IfElseStatement elseIfStatement = new IfElseStatement(IfElseStatementType.ElseIf);
			elseIfStatement.setCondition(codeGenUtil.getCondition(((ElseIfDefinition) element).getConditions()));
			if(((ElseIfDefinition) element).getIfElseElements().size() > 0) {
				List<IfElseElementsDef> childs = ((ElseIfDefinition) element).getIfElseElements();
				int noOfChilds = childs.size();
				IfElseStatement childPeerIf = null;
				for(int childIndex = 0; childIndex < noOfChilds; childIndex ++) {
					childPeerIf = udpateMainIfElseCodeBlocklements(childs.get(childIndex), elseIfStatement, clazz, webPageName, childPeerIf);
				}
			}
			peerIf.setPeerIfElseStatement(elseIfStatement);
		} else if(element instanceof ElseDefinition) {
			IfElseStatement elseStatement = new IfElseStatement(IfElseStatementType.Else);
			elseStatement.setCondition(codeGenUtil.getCondition(((ElseDefinition) element).getConditions()));
			if(((ElseDefinition) element).getIfElseElements().size() > 0) {
				List<IfElseElementsDef> childs = ((ElseDefinition) element).getIfElseElements();
				int noOfChilds = childs.size();
				IfElseStatement childPeerIf = null;
				for(int childIndex = 0; childIndex < noOfChilds; childIndex ++) {
					childPeerIf = udpateMainIfElseCodeBlocklements(childs.get(childIndex), elseStatement, clazz, webPageName, childPeerIf);
				}
			}
			peerIf.setPeerIfElseStatement(elseStatement);
		} else if(element instanceof LineOfCodeDefinition) {
			mainIf.addCode(getLineOfCode((LineOfCodeDefinition) element, clazz, webPageName));
		} else if(element instanceof ActionDefinition) {
			mainIf.addCode(getActionFromActionDef((ActionDefinition) element, webPageName));
		}
		return peerIf;
	}

//	private void updateFromCodeBlock(CodeBlockDefinition codeBlockDef, IfElseStatement mainIf, Class clazz, String webPageName) throws Exception {
//		List<CodeBlockElementsDef> cbElements = codeBlockDef.getCodeBlockElements();
//		int noOfElements = cbElements.size();
//		for(int index = 0; index < noOfElements; index ++) {
//			if(cbElements.get(index) instanceof LineOfCodeDefinition) {
//				mainIf.addCode(getLineOfCode((LineOfCodeDefinition) cbElements.get(index), clazz, webPageName));
//			} else if(cbElements.get(index) instanceof ActionDefinition) {
//				mainIf.addCode(getActionFromActionDef((ActionDefinition) cbElements.get(index), webPageName));
//			}
//		}
//	}

	private LineOfCode getLineOfCode(LineOfCodeDefinition locDef, Class clazz, String webPageName) throws Exception {
		LineOfCode loc = new LineOfCode(codeGenUtil.getLHSType(locDef.getLhsDefiniton().getType()),
			codeGenUtil.getRHSType(locDef.getRhsDefinition().getType()), clazz);
		LHSDefinition lhsDef = locDef.getLhsDefiniton();
		RightHSDefinition rhsDef = locDef.getRhsDefinition();
		String lhsType = codeGenUtil.getLHSType(lhsDef.getType()).toString();
		String rhsType = codeGenUtil.getRHSType(rhsDef.getType()).toString();
		if(! lhsType.equalsIgnoreCase("NoLHS")) {
			if(lhsType.equalsIgnoreCase("DeclareInstanceVariableAndInitialize")
				|| lhsType.equalsIgnoreCase("DeclareInstanceVariable")) {
				loc.setLHSVariable(new Variable("protected", false, false, lhsDef.getVariableDefinition().getType(),
					lhsDef.getVariableDefinition().getName(), lhsDef.getVariableDefinition().getValue(), true, false, false, null));
			} else if(lhsType.equalsIgnoreCase("DeclareMethodVariableAndInitialize")
				|| lhsType.equalsIgnoreCase("DeclareMethodVariable") || lhsType.equalsIgnoreCase("AssignValueToAnExistingVariable")) {
				loc.setLHSVariable(new Variable(null, false, false, lhsDef.getVariableDefinition().getType(),
					lhsDef.getVariableDefinition().getName(), lhsDef.getVariableDefinition().getValue(), false, true, false, null));
			}
		}

		if(! rhsType.equalsIgnoreCase("NoRHS")) {
			if(rhsType.equalsIgnoreCase("ReturnValueFromAction")) {
				 loc.setRHSDefinition(getActionFromActionDef(locDef.getRhsDefinition().getActionDef(), webPageName));
			} else if(rhsType.equalsIgnoreCase("Expression")) {
				loc.setRHSDefinition(getRHSDefinitionAsExpression(locDef.getRhsDefinition(), webPageName));
			} else {
				if(locDef.getRhsDefinition() != null) {
					loc.setRHSDefinition(getRHSDefinition(locDef.getRhsDefinition()));
				}
			}
		}
		return loc;
	}

	public LineOfCode getLineOfCode(LineOfCodeDefinition locDef, String webPageName) throws Exception {
		LineOfCode loc = new LineOfCode(codeGenUtil.getLHSType(locDef.getLhsDefiniton().getType()),
			codeGenUtil.getRHSType(locDef.getRhsDefinition().getType()));
		LHSDefinition lhsDef = locDef.getLhsDefiniton();
		RightHSDefinition rhsDef = locDef.getRhsDefinition();
		String lhsType = codeGenUtil.getLHSType(lhsDef.getType()).toString();
		String rhsType = codeGenUtil.getRHSType(rhsDef.getType()).toString();
		if(! lhsType.equalsIgnoreCase("NoLHS")) {
			if(lhsType.equalsIgnoreCase("DeclareInstanceVariableAndInitialize")
				|| lhsType.equalsIgnoreCase("DeclareInstanceVariable")) {
				loc.setLHSVariable(new Variable("protected", false, false, lhsDef.getVariableDefinition().getType(),
					lhsDef.getVariableDefinition().getName(), lhsDef.getVariableDefinition().getValue(), true, false, false, null));
			} else if(lhsType.equalsIgnoreCase("DeclareMethodVariableAndInitialize")
				|| lhsType.equalsIgnoreCase("DeclareMethodVariable") || lhsType.equalsIgnoreCase("AssignValueToAnExistingVariable")) {
				loc.setLHSVariable(new Variable(null, false, false, lhsDef.getVariableDefinition().getType(),
					lhsDef.getVariableDefinition().getName(), lhsDef.getVariableDefinition().getValue(), false, true, false, null));
			}
		}
		if(rhsType.equalsIgnoreCase("AssignValueToAnExistingVariable")) {
			if(locDef.getRhsDefinition() != null && getRHSDefinition(locDef.getRhsDefinition()) != null) {
				loc.setRHSDefinition(getRHSDefinition(locDef.getRhsDefinition()));
			}
		} else if(rhsType.equalsIgnoreCase("ReturnValueFromAction") || rhsType.equalsIgnoreCase("PerformATask")) {
			if(locDef.getRhsDefinition() != null && locDef.getRhsDefinition().getActionDef() != null) {
				loc.setRHSDefinition(getActionFromActionDef(locDef.getRhsDefinition().getActionDef(), webPageName));
			}
		} else if(rhsType.equalsIgnoreCase("Expression")) {
			if(locDef.getRhsDefinition() != null) {
				loc.setRHSDefinition(getRHSDefinitionAsExpression(locDef.getRhsDefinition(), webPageName));
			}
		} else if(rhsType.equalsIgnoreCase("NoRHS")) {
			//do nothing
			//loc.setRHSDefinition(null);
		} else {
			if(locDef.getRhsDefinition() != null) {
				loc.setRHSDefinition(getRHSDefinition(locDef.getRhsDefinition()));
			}
		}


//		if(rhsType.equalsIgnoreCase("ReturnValueFromAction") || rhsType.equalsIgnoreCase("AssignValueToAnExistingVariable")
//			|| rhsType.equalsIgnoreCase("PerformATask")) {
//			 loc.setRHSDefinition(getActionFromActionDef(locDef.getRhsDefinition().getActionDef(), webPageName));
//		} else if(rhsType.equalsIgnoreCase("Expression")) {
//			loc.setRHSDefinition(getRHSDefinitionAsExpression(locDef.getRhsDefinition(), webPageName));
//		} else if(rhsType.equalsIgnoreCase("NoRHS")) {
//			//do nothing
//			//loc.setRHSDefinition(null);
//		} else {
//			loc.setRHSDefinition(getRHSDefinition(locDef.getRhsDefinition()));
//		}

		return loc;
	}

	private RHSDefinition getRHSDefinition(RightHSDefinition rightHSDef) throws Exception {
		if(rightHSDef.getFunctionDef() != null) {
			return getRHSDefinitionOfFunction(rightHSDef.getFunctionDef());
		}
		return null;
	}

	public Action getActionFromActionDef(ActionDefinition actionDef, String webPageName) throws Exception {
		Action action = null;
		HTMLElementsDef htmlElem = actionDef.getHtmlElement();
		if(htmlElem != null) {
			if(htmlElem instanceof ElementDefinition) {
				action = getActionFromElementDefinition(actionDef, webPageName);
			} else if(htmlElem instanceof NavigateDefinition) {
				action = getActionFromNavigateDefinition(actionDef, webPageName);
			} else if(htmlElem instanceof FunctionDefinition) {
				action = getActionFromFunctionDefinition(actionDef, webPageName);
			}
		} else {
			action =  new Action(codeGenUtil.getActionType(actionDef.getType()),
				codeGenUtil.getBrowserType(actionDef.getBrowserType()));
			action.setWebPageName(webPageName);
			action.setMatchingValue("");
		}

		return action;
	}

	private Action getActionFromElementDefinition(ActionDefinition actionDef, String webPageName) {
		Action action = new Action(codeGenUtil.getActionType(actionDef.getType()),
			codeGenUtil.getBrowserType(actionDef.getBrowserType()));
		ElementDefinition elemDef = (ElementDefinition) actionDef.getHtmlElement();
		action.setElemTagType(codeGenUtil.getElementTagType(elemDef.getElementTagType()));
		action.setElemFieldType(codeGenUtil.getElementFieldType(elemDef.getElementFieldType()));
		action.setExactMatch(elemDef.isExactMatch());
		action.setMatchingValue(elemDef.getValue());
		action.setWebPageName(webPageName);

		return action;
	}

	private Action getActionFromNavigateDefinition(ActionDefinition actionDef, String webPageName) {
		Action action = new Action(codeGenUtil.getActionType(actionDef.getType()),
			codeGenUtil.getBrowserType(actionDef.getBrowserType()));
		NavigateDefinition navigateDef = (NavigateDefinition) actionDef.getHtmlElement();
		action.setNavigationURL(navigateDef.getUrl());
		action.setWebPageName(webPageName);
		action.setMatchingValue("");

		return action;
	}

	public Action getActionFromFunctionDefinition(ActionDefinition actionDef, String webPageName) throws Exception {
		Action action = new Action(codeGenUtil.getActionType(actionDef.getType()),
			codeGenUtil.getBrowserType(actionDef.getBrowserType()));
		FunctionDefinition funcDef = (FunctionDefinition) actionDef.getHtmlElement();
		action.setPackageName(funcDef.getPackageName());
		action.setClassName(funcDef.getClassName());
		action.setMethodName(funcDef.getName());
		action.setWebPageName(webPageName);
		action.setMatchingValue("");
		action.setActionType(codeGenUtil.getActionType(actionDef.getType()));

		int noOfParams = funcDef.getParameters().size();
		if(noOfParams > 0) {
			List<ParameterDefinition> params = funcDef.getParameters();
			ParameterDefinition tempParam = null;
			ArgumentValueDefinition argValDef = null;
			for(int pIndex = 0; pIndex < noOfParams; pIndex ++) {
				tempParam = params.get(pIndex);
				if(tempParam.isPassByValue()) {
					argValDef = new ArgumentValueDefinition(ArgumentValueType.PassByValue, tempParam.getName(), tempParam.getValue());
				} else if(tempParam.isPassByReference()) {
					argValDef = new ArgumentValueDefinition(ArgumentValueType.PassByReference, tempParam.getName(), tempParam.getValue());
				}
				action.addArgumentValueDefinition(argValDef);
			}
		}

		action.setSupportingMethodName(funcDef.getSupportingFunctionName());
		noOfParams = funcDef.getSupportingFunctionParameters().size();
		if(noOfParams > 0) {
			List<ParameterDefinition> params = funcDef.getSupportingFunctionParameters();
			ParameterDefinition tempParam = null;
			ArgumentValueDefinition argValDef = null;
			for(int pIndex = 0; pIndex < noOfParams; pIndex ++) {
				tempParam = params.get(pIndex);
				if(tempParam.isPassByValue()) {
					argValDef = new ArgumentValueDefinition(ArgumentValueType.PassByValue, tempParam.getName(), tempParam.getValue());
				} else if(tempParam.isPassByReference()) {
					argValDef = new ArgumentValueDefinition(ArgumentValueType.PassByReference, tempParam.getName(), tempParam.getValue());
				}
				action.addSupportingMethodArgumentValueDefinition(argValDef);
			}
		}

		return action;
	}

	private Expression getRHSDefinitionAsExpression(RightHSDefinition rightHSDef, String webPageName) throws Exception {
		Expression expression = new Expression();
		OperandDefinition operandDef = null;
		OperatorDefinition operatorDef = null;
		List<ExpressionElementsDef> expressionElems = rightHSDef.getExpressionDef().getExpressionElements();
		int noOfElements = expressionElems.size();
		for(int index = 0; index < noOfElements; index ++) {
			if(expressionElems.get(index) instanceof OperandDefinition) {
				operandDef = (OperandDefinition) expressionElems.get(index);
				if(operandDef.getFunctionDef() != null) {
					expression.setOperand(getRHSDefinitionOfFunction(operandDef.getFunctionDef()));
				} else if(operandDef.getActionDef() != null) {
					expression.setOperand(getActionFromActionDef(operandDef.getActionDef(), webPageName));
				} else if(operandDef.getVariableDef() != null) {
					expression.setOperand(getVariableDefinition(operandDef.getVariableDef()));
				} else if(operandDef.getStringDef() != null) {
					expression.setOperand(getStringDefinition(operandDef.getStringDef()));
				} else if(operandDef.getExpressionDef() != null) {
					expression.setOperand(getExpressionFromExpressionDefinition(operandDef.getExpressionDef(), webPageName));
				} else if(operandDef.getOperandName() != null) {
					expression.setOperand(operandDef.getOperandName());
				}
			} else if(expressionElems.get(index) instanceof OperatorDefinition) {
				operatorDef = (OperatorDefinition) expressionElems.get(index);
				expression.setOperator(codeGenUtil.getExpressionOperator(operatorDef.getOperator()));
			}
		}
		return expression;
	}

	public Expression getExpressionFromExpressionDefinition(ExpressionDefinition expDef, String webPageName) throws Exception {
		Expression expression = new Expression();
		OperandDefinition operandDef = null;
		OperatorDefinition operatorDef = null;
		List<ExpressionElementsDef> expressionElems = expDef.getExpressionElements();
		int noOfElements = expressionElems.size();
		for(int index = 0; index < noOfElements; index ++) {
			if(expressionElems.get(index) instanceof OperandDefinition) {
				operandDef = (OperandDefinition) expressionElems.get(index);
				if(operandDef.getFunctionDef() != null) {
					expression.setOperand(getRHSDefinitionOfFunction(operandDef.getFunctionDef()));
				} else if(operandDef.getActionDef() != null) {
					expression.setOperand(getActionFromActionDef(operandDef.getActionDef(), webPageName));
				} else if(operandDef.getVariableDef() != null) {
					expression.setOperand(getVariableDefinition(operandDef.getVariableDef()));
				} else if(operandDef.getStringDef() != null) {
					expression.setOperand(getStringDefinition(operandDef.getStringDef()));
				} else if(operandDef.getExpressionDef() != null) {
					expression.setOperand(getExpressionFromExpressionDefinition(operandDef.getExpressionDef(), webPageName));
				}
			} else if(expressionElems.get(index) instanceof OperatorDefinition) {
				operatorDef = (OperatorDefinition) expressionElems.get(index);
				expression.setOperator(codeGenUtil.getExpressionOperator(operatorDef.getOperator()));
			}
		}
		return expression;
	}

	private Variable getVariableDefinition(VariableDefinition varDef) {
		Variable var = new Variable(null, false, false, varDef.getType(), varDef.getName(),
				varDef.getValue(), false, false, false, null);
		return var;
	}

	private String getStringDefinition(StringDefinition stringDef) {
		if(stringDef.getName() != null) {
			return stringDef.getName();
		} else if(stringDef.getValue() != null) {
			return stringDef.getValue();
		}
		return null;
		//return stringDef.getName();
	}

	private RHSDefinition getRHSDefinitionOfFunction(FunctionDefinition functionDef) throws Exception {
		RHSDefinition rhsDef = new RHSDefinition(functionDef.getPackageName(), functionDef.getClassName(),
			(functionDef.getPackageName() + "." + functionDef.getClassName()));
		rhsDef.setMethodNameKey(functionDef.getName());
		int noOfParams = functionDef.getParameters().size();
		if(noOfParams > 0) {
			List<ParameterDefinition> params = functionDef.getParameters();
			ParameterDefinition tempParam = null;
			ArgumentValueDefinition argValDef = null;
			for(int pIndex = 0; pIndex < noOfParams; pIndex ++) {
				tempParam = params.get(pIndex);
				if(tempParam.isPassByValue()) {
					argValDef = new ArgumentValueDefinition(ArgumentValueType.PassByValue, tempParam.getName(), tempParam.getValue());
				} else if(tempParam.isPassByReference()) {
					argValDef = new ArgumentValueDefinition(ArgumentValueType.PassByReference, tempParam.getName(), tempParam.getValue());
				}
				rhsDef.addArgumentValueDefinition(argValDef);
			}
		}
		if(functionDef.getSupportingFunctionName() != null) {
			rhsDef.setSupportingMethodName(functionDef.getSupportingFunctionName());
			int noOfSupParams = functionDef.getSupportingFunctionParameters().size();
			if(noOfSupParams > 0) {
				//List<ParameterDefinition> params = functionDef.getParameters();
				List<ParameterDefinition> params = functionDef.getSupportingFunctionParameters();
				ParameterDefinition tempParam = null;
				ArgumentValueDefinition argValDef = null;
				for(int supParamIndex = 0; supParamIndex < noOfSupParams; supParamIndex ++) {
					tempParam = params.get(supParamIndex);
					if(tempParam.isPassByValue()) {
						argValDef = new ArgumentValueDefinition(ArgumentValueType.PassByValue, tempParam.getName(), tempParam.getValue());
					} else if(tempParam.isPassByReference()){
						argValDef = new ArgumentValueDefinition(ArgumentValueType.PassByReference, tempParam.getName(), tempParam.getValue());
					}
					rhsDef.addSupportingMethodArgumentValueDefinition(argValDef);
				}
			}
		}
		return rhsDef;
	}

//	private Condition getCondition(Conditions conditions) {
//		List<ConditionDefinition> conDefs = conditions.getConditions();
//		Condition condition = null;
//		int noOfConditions = conDefs.size();
//		for(int index = 0; index < noOfConditions; index ++) {
//			if(conDefs.get(index).isSuperCondition()) {
//				condition = conDefs.get(index).getCondition();
//				break;
//			}
//		}
//		return condition;
//	}


	public Constructor getConstructor(CrawlerDefinition crawlerDef) {
		Constructor cons = new Constructor("public", crawlerDef.getClientName() + "_" + crawlerDef.getSiteName(), true, true);
		//Constructor cons = new Constructor("public", crawlerDef.getName(), true, true);

		//current constructor parameters
		List<ParameterDefinition> params = crawlerDef.getCurrentConstructor().getParameters().getParameters();
		int noOfParams = params.size();
		for(int paramIndex = 0; paramIndex < noOfParams; paramIndex ++) {
			cons.addParameters(getParameter(params.get(paramIndex)));
		}
		//source map config - to be implemented later
		//crawlerDef.getCurrentConstructor().getSourceMapConfig().getType()
		//super constructor parameters
		List<ParameterDefinition> superParams = crawlerDef.getSuperConstructor().getParameters().getParameters();
		noOfParams = superParams.size();
		for(int superParamIndex = 0; superParamIndex < noOfParams; superParamIndex ++) {
			cons.addSuperParameters(getParameter(superParams.get(superParamIndex)));
		}
		//updating browser settings
		updatePrimaryBrowserSettings(cons, crawlerDef.getPrimaryBrowserDefinition());
		updateSecondaryBrowserSettings(cons, crawlerDef.getSecondaryBrowserDefinition());
		updateTertiaryBrowserSettings(cons, crawlerDef.getTertiaryBrowserDefinition());

		return cons;
	}

	public boolean anyBrowserAcccessingSecuredWebPage(CrawlerDefinition crawlerDef) {
		boolean securedAccess = false;
		if(crawlerDef.getPrimaryBrowserDefinition().isSecuredWebPage()
			|| crawlerDef.getSecondaryBrowserDefinition().isSecuredWebPage()
			|| crawlerDef.getTertiaryBrowserDefinition().isSecuredWebPage()) {
			securedAccess = true;
		}
		return securedAccess;
	}

	public void updatePrimaryBrowserSettings(Constructor cons, BrowserDefinition browserDef) {
		cons.setPrimaryBrowserDownloadImages(browserDef.isDownloadImages());
		cons.setPrimaryBrowserDownloadScript(browserDef.isDownloadJavaScripts());
	}

	public void updateSecondaryBrowserSettings(Constructor cons, BrowserDefinition browserDef) {
		cons.setSecondaryBrowserDownloadImages(browserDef.isDownloadImages());
		cons.setSecondaryBrowserDownloadScript(browserDef.isDownloadJavaScripts());
	}

	public void updateTertiaryBrowserSettings(Constructor cons, BrowserDefinition browserDef) {
		cons.setTertiaryBrowserDownloadImages(browserDef.isDownloadImages());
		cons.setTertiaryBrowserDownloadScript(browserDef.isDownloadJavaScripts());
	}

	public Variable getParameter(ParameterDefinition parDef) {
		Variable var = new Variable(null, false, false, parDef.getType(), parDef.getName(),
			parDef.getValue(), false, false, false, null);
		return var;
	}

}
