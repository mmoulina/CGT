package com.poplicus.crawler.codegen.utilities;

import java.util.List;

import com.poplicus.crawler.codegen.Action;
import com.poplicus.crawler.codegen.Condition;
import com.poplicus.crawler.codegen.DataSet;
import com.poplicus.crawler.codegen.Expression;
import com.poplicus.crawler.codegen.LineOfCode;
import com.poplicus.crawler.codegen.WebPage;
import com.poplicus.crawler.codegen.definitions.ConditionDefinition;
import com.poplicus.crawler.codegen.definitions.Conditions;
import com.poplicus.crawler.codegen.definitions.ConstructorDefinition;
import com.poplicus.crawler.codegen.definitions.CrawlerDefinition;
import com.poplicus.crawler.codegen.definitions.ParameterDefinition;
import com.poplicus.crawler.codegen.definitions.Parameters;
import com.poplicus.crawler.codegen.definitions.SourceMapConfig;
import com.poplicus.crawler.codegen.definitions.ConstructorDefinition.ConstructorType;

public class CodeGenUtil {

	private static CodeGenUtil codeGenUtil = null;

	private CodeGenUtil() {

	}

	public static CodeGenUtil getInstance() {
		if(codeGenUtil == null) {
			codeGenUtil = new CodeGenUtil();
		}
		return codeGenUtil;
	}

	public LineOfCode.LOC_RHS_Type getRHSType(String type) {
		LineOfCode.LOC_RHS_Type rhsType = null;
		if(type.equalsIgnoreCase("AssignValueToAnExistingVariable")) {
			rhsType = LineOfCode.LOC_RHS_Type.AssignValueToAnExistingVariable;
		} else if(type.equalsIgnoreCase("PerformATask")) {
			rhsType = LineOfCode.LOC_RHS_Type.PerformATask;
		} else if(type.equalsIgnoreCase("ReturnValueFromAction")) {
			rhsType = LineOfCode.LOC_RHS_Type.ReturnValueFromAction;
		} else if(type.equalsIgnoreCase("Expression")) {
			rhsType = LineOfCode.LOC_RHS_Type.Expression;
		} else if(type.equalsIgnoreCase("NoRHS")) {
			rhsType = LineOfCode.LOC_RHS_Type.NoRHS;
		}
		return rhsType;
	}

	public LineOfCode.LOC_LHS_Type getLHSType(String type) {
		LineOfCode.LOC_LHS_Type lhsType = null;
		if(type.equalsIgnoreCase("DeclareInstanceVariableAndInitialize")) {
			lhsType = LineOfCode.LOC_LHS_Type.DeclareInstanceVariableAndInitialize;
		} else if(type.equalsIgnoreCase("DeclareMethodVariableAndInitialize")) {
			lhsType = LineOfCode.LOC_LHS_Type.DeclareMethodVariableAndInitialize;
		} else if(type.equalsIgnoreCase("DeclareInstanceVariable")) {
			lhsType = LineOfCode.LOC_LHS_Type.DeclareInstanceVariable;
		} else if(type.equalsIgnoreCase("DeclareMethodVariable")) {
			lhsType = LineOfCode.LOC_LHS_Type.DeclareMethodVariable;
		} else if(type.equalsIgnoreCase("AssignValueToAnExistingVariable")) {
			lhsType = LineOfCode.LOC_LHS_Type.AssignValueToAnExistingVariable;
		} else if(type.equalsIgnoreCase("NoLHS")) {
			lhsType = LineOfCode.LOC_LHS_Type.NoLHS;
		}
		return lhsType;
	}

	public WebPage.PageType getPageType(String page) {
		WebPage.PageType pageType = null;
		if(page.equalsIgnoreCase("search")) {
			pageType = WebPage.PageType.Search;
		} else if(page.equalsIgnoreCase("resultlinks")) {
			pageType = WebPage.PageType.ResultLinks;
		} else if(page.equalsIgnoreCase("result")) {
			pageType = WebPage.PageType.Result;
		}
		return pageType;
	}

	public WebPage.BrowserType getBrowserType(String browser) {
		WebPage.BrowserType browserType = null;
		if(browser.equalsIgnoreCase("primary")) {
			browserType = WebPage.BrowserType.Primary;
		} else if(browser.equalsIgnoreCase("secondary")) {
			browserType = WebPage.BrowserType.Secondary;
		} else if(browser.equalsIgnoreCase("tertiary")) {
			browserType = WebPage.BrowserType.Tertiary;
		}
		return browserType;
	}

	public Action.ActionType getActionType(String action) {
		Action.ActionType actionType = null;
		if(action.equalsIgnoreCase("ClickAnElement")) {
			actionType = Action.ActionType.ClickAnElement;
		} else if(action.equalsIgnoreCase("LinkClick")) {
			actionType = Action.ActionType.LinkClick;
		} else if(action.equalsIgnoreCase("URLNavigation")) {
			actionType = Action.ActionType.URLNavigation;
		} else if(action.equalsIgnoreCase("CollectResultLinks")) {
			actionType = Action.ActionType.CollectResultLinks;
		} else if(action.equalsIgnoreCase("ParseResult")) {
			actionType = Action.ActionType.ParseResult;
		} else if(action.equalsIgnoreCase("SelectOptionItemWithOnChange")) {
			actionType = Action.ActionType.SelectOptionItemWithOnChange;
		} else if(action.equalsIgnoreCase("StartTimer")) {
			actionType = Action.ActionType.StartTimer;
		} else if(action.equalsIgnoreCase("AutomationTask_PerformEnterData")) {
			actionType = Action.ActionType.AutomationTask_PerformEnterData;
		} else if(action.equalsIgnoreCase("SetNextFileDownloadPathAndName")) {
			actionType = Action.ActionType.SetNextFileDownloadPathAndName;
		} else if(action.equalsIgnoreCase("LocationUrl")) {
			actionType = Action.ActionType.LocationUrl;
		} else if(action.equalsIgnoreCase("FunctionCall")) {
			actionType = Action.ActionType.FunctionCall;
		}
		return actionType;
	}

	public String getBrowserTypeAsString(String browser) {
		String browserType = null;
		if(browser.equalsIgnoreCase("primary")) {
			browserType = "Primary";
		} else if(browser.equalsIgnoreCase("secondary")) {
			browserType = "Secondary";
		} else if(browser.equalsIgnoreCase("tertiary")) {
			browserType = "Tertiary";
		}
		return browserType;
	}

	public Action.ElementTagType getElementTagType(String tag) {
		Action.ElementTagType tagType = null;
		if(tag.equalsIgnoreCase("Image")) {
			tagType = Action.ElementTagType.Image;
		} else if(tag.equalsIgnoreCase("Anchor")) {
			tagType = Action.ElementTagType.Anchor;
		} else if(tag.equalsIgnoreCase("Input")) {
			tagType = Action.ElementTagType.Input;
		} else if(tag.equalsIgnoreCase("Button")) {
			tagType = Action.ElementTagType.Button;
		}
		return tagType;
	}

	public Action.ElementFieldType getElementFieldType(String field) {
		Action.ElementFieldType fieldType = null;
		if(field.equalsIgnoreCase("InnerHTML")) {
			fieldType = Action.ElementFieldType.InnerHTML;
		} else if(field.equalsIgnoreCase("Value")){
			fieldType = Action.ElementFieldType.Value;
		} else if(field.equalsIgnoreCase("Name")){
			fieldType = Action.ElementFieldType.Name;
		} else if(field.equalsIgnoreCase("Source")){
			fieldType = Action.ElementFieldType.Source;
		} else if(field.equalsIgnoreCase("Type")){
			fieldType = Action.ElementFieldType.Type;
		} else if(field.equalsIgnoreCase("HREF")){
			fieldType = Action.ElementFieldType.HREF;
		} else if(field.equalsIgnoreCase("ID")){
			fieldType = Action.ElementFieldType.ID;
		} else if(field.equalsIgnoreCase("ClassName")){
			fieldType = Action.ElementFieldType.ClassName;
		} else if(field.equalsIgnoreCase("CheckedAttribute")){
			fieldType = Action.ElementFieldType.CheckedAttribute;
		} else if(field.equalsIgnoreCase("InnerText")){
			fieldType = Action.ElementFieldType.InnerText;
		}
		return fieldType;
	}

	public Expression.ExpressionOperator getExpressionOperator(String expression) {
		Expression.ExpressionOperator expOperator = null;
		if(expression.equalsIgnoreCase("Plus")) {
			expOperator = Expression.ExpressionOperator.Plus;
		} else if(expression.equalsIgnoreCase("Minus")) {
			expOperator = Expression.ExpressionOperator.Minus;
		} else if(expression.equalsIgnoreCase("Multiply")) {
			expOperator = Expression.ExpressionOperator.Multiply;
		} else if(expression.equalsIgnoreCase("Divide")) {
			expOperator = Expression.ExpressionOperator.Divide;
		} else if(expression.equalsIgnoreCase("Modulus")) {
			expOperator = Expression.ExpressionOperator.Modulus;
		}
		return expOperator;
	}

	public Condition.ConditionOperator getConditionOperator(String condition) {
		Condition.ConditionOperator conOperator = null;
		if(condition.equalsIgnoreCase("Contains")) {
			conOperator = Condition.ConditionOperator.Contains;
		} else if(condition.equalsIgnoreCase("DoesNotContain")) {
			conOperator = Condition.ConditionOperator.DoesNotContain;
		} else if(condition.equalsIgnoreCase("LogicalAND")) {
			conOperator = Condition.ConditionOperator.LogicalAND;
		} else if(condition.equalsIgnoreCase("LogicalOR")) {
			conOperator = Condition.ConditionOperator.LogicalOR;
		} else if(condition.equalsIgnoreCase("Equals")) {
			conOperator = Condition.ConditionOperator.Equals;
		} else if(condition.equalsIgnoreCase("NotEquals")) {
			conOperator = Condition.ConditionOperator.NotEquals;
		} else if(condition.equalsIgnoreCase("LessThan")) {
			conOperator = Condition.ConditionOperator.LessThan;
		} else if(condition.equalsIgnoreCase("LessThanAndEquals")) {
			conOperator = Condition.ConditionOperator.LessThanAndEquals;
		} else if(condition.equalsIgnoreCase("Boolean")) {
			conOperator = Condition.ConditionOperator.Boolean;
		} else if(condition.equalsIgnoreCase("LogicalComplement")) {
			conOperator = Condition.ConditionOperator.LogicalComplement;
		}
		return conOperator;
	}

	public String getValidLHSTypeString(String lhsType) {
		String tempLhsType = null;
		if(lhsType != null) {
			if(lhsType.equals("Declare Instance Variable")) {
				tempLhsType = "DeclareInstanceVariable";
			} else if(lhsType.equals("Declare Instance Variable and Initialize")) {
				tempLhsType = "DeclareInstanceVariableAndInitialize";
			} else if(lhsType.equals("Declare Method Variable")) {
				tempLhsType = "DeclareMethodVariable";
			} else if(lhsType.equals("Declare Method Variable and Initialize")) {
				tempLhsType = "DeclareMethodVariableAndInitialize";
			} else if(lhsType.equals("Assign Value To An Existing Variable")) {
				tempLhsType = "AssignValueToAnExistingVariable";
			} else if(lhsType.equals("No LHS")) {
				tempLhsType = "NoLHS";
			}
		}
		return tempLhsType;
	}

	public String getValidRHSTypeString(String rhsType) {
		String tempRhsType = null;
		if(rhsType != null) {
			if(rhsType.equals("Assign Value To An Existing Variable")) {
				tempRhsType = "AssignValueToAnExistingVariable";
			} else if(rhsType.equals("Perform a Task")) {
				tempRhsType = "PerformATask";
			} else if(rhsType.equals("Return Value From Action")) {
				tempRhsType = "ReturnValueFromAction";
			} else if(rhsType.equals("Expression")) {
				tempRhsType = "Expression";
			} else if(rhsType.equals("No RHS")) {
				tempRhsType = "NoRHS";
			} else if(rhsType.equals("Perform an Action")) {
				tempRhsType = "PerformAnAction";
			}
		}
		return tempRhsType;
	}

	public String getValidActionTypeString(String actionType) {
		String tempActionType = null;
		if(actionType != null) {
			if(actionType.equals("Click An Element")) {
				tempActionType = "ClickAnElement";
			} else if(actionType.equals("URL Navigation")) {
				tempActionType = "URLNavigation";
			} else if(actionType.equals("Collect Result Links")) {
				tempActionType = "CollectResultLinks";
			} else if(actionType.equals("Parse Result")) {
				tempActionType = "ParseResult";
			} else if(actionType.equals("Start Timer")) {
				tempActionType = "StartTimer";
			} else if(actionType.equals("Function Call")) {
				tempActionType = "FunctionCall";
			}
		}
		return tempActionType;
	}

	public String getDisplayActionTypeString(String actionType) {
		String tempActionType = null;
		if(actionType != null) {
			if(actionType.equals("ClickAnElement")) {
				tempActionType = "Click An Element";
			} else if(actionType.equals("URLNavigation")) {
				tempActionType = "URL Navigation";
			} else if(actionType.equals("CollectResultLinks")) {
				tempActionType = "Collect Result Links";
			} else if(actionType.equals("ParseResult")) {
				tempActionType = "Parse Result";
			} else if(actionType.equals("StartTimer")) {
				tempActionType = "Start Timer";
			} else if(actionType.equals("FunctionCall")) {
				tempActionType = "Function Call";
			}
		}
		return tempActionType;
	}

	public DataSet.TableName getValidTableName(String tableName) {
		DataSet.TableName dsTableName = null;
		if(tableName.equalsIgnoreCase("PopBid")) {
			dsTableName = DataSet.TableName.PopBid;
		} else if(tableName.equalsIgnoreCase("PopBidIndustryCode")) {
			dsTableName = DataSet.TableName.PopBidIndustryCode;
		} else if(tableName.equalsIgnoreCase("PopBidLocation")) {
			dsTableName = DataSet.TableName.PopBidLocation;
		} else if(tableName.equalsIgnoreCase("PopBidContact")) {
			dsTableName = DataSet.TableName.PopBidContact;
		} else if(tableName.equalsIgnoreCase("PopBidSetAside")) {
			dsTableName = DataSet.TableName.PopBidSetAside;
		} else if(tableName.equalsIgnoreCase("PopBidDocument")) {
			dsTableName = DataSet.TableName.PopBidDocument;
		} else if(tableName.equalsIgnoreCase("PopBidGrant")) {
			dsTableName = DataSet.TableName.PopBidGrant;
		} else if(tableName.equalsIgnoreCase("PopBidVendor")) {
			dsTableName = DataSet.TableName.PopBidVendor;
		} else if(tableName.equalsIgnoreCase("PopBidVendorAgencyId")) {
			dsTableName = DataSet.TableName.PopBidVendorAgencyId;
		} else if(tableName.equalsIgnoreCase("PopBidVendorAgencyOrg")) {
			dsTableName = DataSet.TableName.PopBidVendorAgencyOrg;
		} else {
			dsTableName = DataSet.TableName.None;
		}
		return dsTableName;
	}

	public String getOperatorSymbol(String operator) {
		String operatorSymbol = null;
		if(operator.equalsIgnoreCase("Plus")) {
			operatorSymbol = "+";
		} else if(operator.equalsIgnoreCase("Minus")) {
			operatorSymbol = "-";
		} else if(operator.equalsIgnoreCase("Multiply")) {
			operatorSymbol = "*";
		} else if(operator.equalsIgnoreCase("Divide")) {
			operatorSymbol = "/";
		} else if(operator.equalsIgnoreCase("Modulus")) {
			operatorSymbol = "%";
		}
		return operatorSymbol;
	}

	public void populateDefaultConstructors(CrawlerDefinition crawlerDef) {
		//current constructor
		ConstructorDefinition currentConstructorDef = new ConstructorDefinition(ConstructorType.CurrentConstructor);

		Parameters currentConstructorParameters = new Parameters();

		ParameterDefinition cParam1 = new ParameterDefinition("primaryBrowser", "cEXWB");
		cParam1.setPassByReference(false);
		cParam1.setPassByValue(true);
		cParam1.setValue("");
		currentConstructorParameters.addParameter(cParam1);

		ParameterDefinition cParam2 = new ParameterDefinition("secondaryBrowser", "cEXWB");
		cParam2.setPassByReference(false);
		cParam2.setPassByValue(true);
		cParam2.setValue("");
		currentConstructorParameters.addParameter(cParam2);

		ParameterDefinition cParam3 = new ParameterDefinition("tertiaryBrowser", "cEXWB");
		cParam3.setPassByReference(false);
		cParam3.setPassByValue(true);
		cParam3.setValue("");
		currentConstructorParameters.addParameter(cParam3);

		ParameterDefinition cParam4 = new ParameterDefinition("userInput", "UserInput");
		cParam4.setPassByReference(false);
		cParam4.setPassByValue(true);
		cParam4.setValue("");
		currentConstructorParameters.addParameter(cParam4);

		SourceMapConfig currentSourceMapConfig = new SourceMapConfig("proposalType");

		currentConstructorDef.setParameters(currentConstructorParameters);
		currentConstructorDef.setSourceMapConfig(currentSourceMapConfig);
		crawlerDef.setCurrentConstructor(currentConstructorDef);

		//super constructor
		ConstructorDefinition superConstructorDef = new ConstructorDefinition(ConstructorType.SuperConstructor);

		Parameters superConstructorParameters = new Parameters();

		ParameterDefinition sParam1 = new ParameterDefinition("primaryBrowser", "cEXWB");
		sParam1.setPassByReference(false);
		sParam1.setPassByValue(true);
		sParam1.setValue("");
		superConstructorParameters.addParameter(sParam1);

		ParameterDefinition sParam2 = new ParameterDefinition("secondaryBrowser", "cEXWB");
		sParam2.setPassByReference(false);
		sParam2.setPassByValue(true);
		sParam2.setValue("");
		superConstructorParameters.addParameter(sParam2);

		ParameterDefinition sParam3 = new ParameterDefinition("tertiaryBrowser", "cEXWB");
		sParam3.setPassByReference(false);
		sParam3.setPassByValue(true);
		sParam3.setValue("");
		superConstructorParameters.addParameter(sParam3);

		ParameterDefinition sParam4 = new ParameterDefinition("CLIENT_NAME", "string");
		sParam4.setPassByReference(false);
		sParam4.setPassByValue(true);
		sParam4.setValue("");
		superConstructorParameters.addParameter(sParam4);

		ParameterDefinition sParam5 = new ParameterDefinition("SITE_NAME", "string");
		sParam5.setPassByReference(false);
		sParam5.setPassByValue(true);
		sParam5.setValue("");
		superConstructorParameters.addParameter(sParam5);

		ParameterDefinition sParam6 = new ParameterDefinition("SOURCE_ID", "string");
		sParam6.setPassByReference(false);
		sParam6.setPassByValue(true);
		sParam6.setValue("");
		superConstructorParameters.addParameter(sParam6);

		ParameterDefinition sParam7 = new ParameterDefinition("userInput", "UserInput");
		sParam7.setPassByReference(false);
		sParam7.setPassByValue(true);
		sParam7.setValue("");
		superConstructorParameters.addParameter(sParam7);

		superConstructorDef.setParameters(superConstructorParameters);

		crawlerDef.setSuperConstructor(superConstructorDef);
	}

	public Condition getCondition(Conditions conditions) {
		List<ConditionDefinition> conDefs = conditions.getConditions();
		Condition condition = null;
		int noOfConditions = conDefs.size();
		for(int index = 0; index < noOfConditions; index ++) {
			if(conDefs.get(index).isSuperCondition()) {
				condition = conDefs.get(index).getCondition();
				break;
			}
		}
		return condition;
	}

}
