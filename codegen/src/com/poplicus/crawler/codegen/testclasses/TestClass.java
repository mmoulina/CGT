package com.poplicus.crawler.codegen.testclasses;

import java.util.ArrayList;
import java.util.List;

import com.poplicus.crawler.codegen.Action.ActionType;
import com.poplicus.crawler.codegen.Action.ElementFieldType;
import com.poplicus.crawler.codegen.Action.ElementTagType;
import com.poplicus.crawler.codegen.ArgumentValueDefinition.ArgumentValueType;
import com.poplicus.crawler.codegen.Condition.ConditionOperator;
import com.poplicus.crawler.codegen.DataGroup;
import com.poplicus.crawler.codegen.DataSet.TableName;
import com.poplicus.crawler.codegen.Datum.DatumIdentifier;
import com.poplicus.crawler.codegen.Expression;
import com.poplicus.crawler.codegen.Expression.ExpressionOperator;
import com.poplicus.crawler.codegen.ForConstruct;
import com.poplicus.crawler.codegen.ForConstruct.ForConstructType;
import com.poplicus.crawler.codegen.IfElseStatement;
import com.poplicus.crawler.codegen.IfElseStatement.IfElseStatementType;
import com.poplicus.crawler.codegen.LineOfCode.LOC_LHS_Type;
import com.poplicus.crawler.codegen.LineOfCode.LOC_RHS_Type;
import com.poplicus.crawler.codegen.Action;
import com.poplicus.crawler.codegen.ArgumentValueDefinition;
import com.poplicus.crawler.codegen.Class;
import com.poplicus.crawler.codegen.Condition;
import com.poplicus.crawler.codegen.Constants;
import com.poplicus.crawler.codegen.Constructor;
import com.poplicus.crawler.codegen.DataSet;
import com.poplicus.crawler.codegen.Datum;
import com.poplicus.crawler.codegen.LineOfCode;
import com.poplicus.crawler.codegen.Method;
import com.poplicus.crawler.codegen.RHSDefinition;
import com.poplicus.crawler.codegen.RHSTask;
import com.poplicus.crawler.codegen.Variable;
import com.poplicus.crawler.codegen.WebPage;
import com.poplicus.crawler.codegen.Method.MethodName;
import com.poplicus.crawler.codegen.RHSTask.RHSTaskType;
import com.poplicus.crawler.codegen.WebPage.BrowserType;
import com.poplicus.crawler.codegen.WebPage.PageType;

public class TestClass {
	
	public static void main(String[] args) throws Exception {
		
		Class clazz = new Class(null, "Poplicus_FED_ARMY1", "PoplicusBaseCrawler");
		
		//generate the constructor - start
		Constructor con = new Constructor("public", "Poplicus_FED_ARMY1", true, true);
		con.addParameters(new Variable(null, false, false, "cEXWB", "primaryBrowser", null, false, false, false, null));
		con.addParameters(new Variable(null, false, false, "cEXWB", "secondaryBrowser", null, false, false, false, null));
		con.addParameters(new Variable(null, false, false, "cEXWB", "tertiaryBrowser", null, false, false, false, null));
		con.addParameters(new Variable(null, false, false, "UserInput", "userInput", null, false, false, false, null));
		
		con.addSuperParameters(new Variable(null, false, false, "cEXWB", "primaryBrowser", null, false, false, false, null));
		con.addSuperParameters(new Variable(null, false, false, "cEXWB", "secondaryBrowser", null, false, false, false, null));
		con.addSuperParameters(new Variable(null, false, false, "cEXWB", "tertiaryBrowser", null, false, false, false, null));
		con.addSuperParameters(new Variable(null, false, false, "String", "CLIENT_NAME", null, false, false, false, null));
		con.addSuperParameters(new Variable(null, false, false, "String", "SITE_NAME", null, false, false, false, null));
		con.addSuperParameters(new Variable(null, false, false, "String", "SOURCE_ID", null, false, false, false, null));
		
		con.setPrimaryBrowserDownloadImages(true);
		con.setPrimaryBrowserDownloadScript(true);

		con.setSecondaryBrowserDownloadImages(true);
		con.setSecondaryBrowserDownloadScript(true);

		con.setTertiaryBrowserDownloadImages(true);
		con.setTertiaryBrowserDownloadScript(true);

		clazz.addConstructor(con);
		//generate the constructor - end
		
		//closing security alert pop-up
		clazz.setSecurityAlertPopup(true);//default is true, if the site we are crawling doesn't throw a security alert popup then it is mandatory to set this to false
		
		//generate instance variables - start
		clazz.addInstanceVariable(new Variable("protected", false, true, "string", "CLIENT_NAME", "Poplicus", true, false, false, null));
		clazz.addInstanceVariable(new Variable("protected", false, true, "string", "SITE_NAME", "FED_ARMY_1", true, false, false, null));
		clazz.addInstanceVariable(new Variable("protected", false, true, "string", "SOURCE_ID", "100012", true, false, false, null));
		clazz.addInstanceVariable(new Variable("protected", false, true, "string", "SOURCE_NAME", "FED-ARMY-1", true, false, false, null));
		Variable varTemp = new Variable("protected", false, true, "string", "temporaryVariable", "", true, false, false, null);
		clazz.addInstanceVariable(varTemp);
		//generate instance variables - end
		
		//generate methods - start
		Method methodPBC = new Method("public", true, "void", MethodName.PrimaryBrowserControl);
		methodPBC.setContainingClass(clazz); // this is mandatory
		methodPBC.setSecureAccess(true); //https websites with certificates
		methodPBC.setInvokeBasePBC(true); //do you want to invoke CrawlerBase#PBC
		
		//adding webpages to PBC method
		List<WebPage> webPagesPBC = new ArrayList<WebPage>();

		try {
			
			clazz.addInstanceVariable(new Variable("protected", false, false, "bool", "_searching", "true", true, false, false, null));
			
			LineOfCode locPBC = new LineOfCode(LOC_LHS_Type.DeclareMethodVariableAndInitialize, LOC_RHS_Type.AssignValueToAnExistingVariable, clazz);
			locPBC.setLHSVariable(new Variable(null, false, false, "string", "optionValue", "", false, true, false, null));
			RHSDefinition rhsDefPBC = new RHSDefinition("Functions", "Text", "Functions.Text");
			rhsDefPBC.setMethodNameKey("TextBeforeTag1UntilTag2");
			ArgumentValueDefinition argValDef1PBC = new ArgumentValueDefinition(ArgumentValueType.PassByReference, "input", "primaryBrowserHTML");
			ArgumentValueDefinition argValDef2PBC = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "tag1", ">ALL(");
			ArgumentValueDefinition argValDef3PBC = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "tag2", "=");
			rhsDefPBC.addArgumentValueDefinition(argValDef1PBC);
			rhsDefPBC.addArgumentValueDefinition(argValDef2PBC);
			rhsDefPBC.addArgumentValueDefinition(argValDef3PBC);
			locPBC.setRHSDefinition(rhsDefPBC);
			methodPBC.addInitialLinesOfCode(locPBC);
			
			LineOfCode locPBC2 = new LineOfCode(LOC_LHS_Type.DeclareMethodVariableAndInitialize, LOC_RHS_Type.NoRHS, clazz);
			locPBC2.setLHSVariable(new Variable(null, false, false, "string", "optionValue2", "testingMethodInitialize", false, true, false, null));
			methodPBC.addInitialLinesOfCode(locPBC2);
			
			//... Web Page 1
			WebPage wPg1 = new WebPage();
			wPg1.setPageName("Page_1");
			wPg1.setUrl("https://acquisition.army.mil/asfi/solicitation_search_form.cfm"); // we can use this to auto generate SQL queries -- in the radar
			wPg1.setBrowserToNavigate(BrowserType.Primary);
			wPg1.setNavigationOrder(1); // we can use this to auto generate SQL queries -- in the radar
			wPg1.setPageType(PageType.Search); // we can use this to auto generate SQL queries -- in the radar
			Condition con1 = new Condition(null, ConditionOperator.DoesNotContain, "value=\"Most recently posted solicitations\"");
			Condition con2 = new Condition("B", ConditionOperator.LogicalAND, "C");
			Condition con3 = new Condition(con1, ConditionOperator.LogicalOR, con2);
			Action act1 = new Action(ActionType.ClickAnElement, BrowserType.Primary, ElementTagType.Input, ElementFieldType.Value, "Most recently posted solicitations", false);
			wPg1.setCondition(con3);
			IfElseStatement iesWPg1 = new IfElseStatement(IfElseStatementType.If);
			Condition con1WPg1 = new Condition("_primaryBrowser", ConditionOperator.NotEquals, "null");
			iesWPg1.setCondition(con1WPg1);
			iesWPg1.addCode(act1);
			
			ForConstruct forCon1 = new ForConstruct(ForConstructType.For);
			forCon1.setVariable(new Variable("int", "index", "0"));
			forCon1.setCondition(new Condition("index", ConditionOperator.LessThanAndEquals, "5"));
			forCon1.setIncrementDecrementOperator("index ++");
			
			IfElseStatement iesForCon1 = new IfElseStatement(IfElseStatementType.If);
			Condition conForCon1 = new Condition("clickSuccess", ConditionOperator.LogicalComplement, null);
			iesForCon1.setCondition(conForCon1);
			iesForCon1.addCode(act1);
				ForConstruct iesForCon11 = new ForConstruct(ForConstructType.For);
				iesForCon11.setVariable(new Variable("int", "index", "0"));
				iesForCon11.setCondition(new Condition("index", ConditionOperator.LessThanAndEquals, "5"));
				iesForCon11.setIncrementDecrementOperator("index ++");
				iesForCon11.addCode(act1);
					
					ForConstruct forCon111 = new ForConstruct(ForConstructType.For);
					forCon111.setVariable(new Variable("int", "index", "0"));
					forCon111.setCondition(new Condition("index", ConditionOperator.LessThanAndEquals, "99999"));
					forCon111.setIncrementDecrementOperator("index ++");
					forCon111.addCode(act1);
					forCon111.addCode(new Variable(null, false, false, "string", "orange", "", false, true, false, null));
					
						IfElseStatement iesForCon111 = new IfElseStatement(IfElseStatementType.If);
						iesForCon111.addCode(new Variable(null, false, false, "string", "apple", "", false, true, false, null));
						Condition conIesForCon111 = new Condition("_primaryBrowser", ConditionOperator.NotEquals, "null");
						iesForCon111.setCondition(conIesForCon111);
						iesForCon111.addCode(act1);
						
						iesForCon111.addCode(forCon111);

						
					iesForCon11.addCode(iesForCon111);
					//iesForCon11.addCode(forCon111);
					
				iesForCon1.addCode(iesForCon11);
			forCon1.addCode(iesForCon1);
			
			LineOfCode loc3 = new LineOfCode(LOC_LHS_Type.DeclareMethodVariableAndInitialize, LOC_RHS_Type.AssignValueToAnExistingVariable, clazz);
			loc3.setLHSVariable(new Variable(null, false, false, "string", "yesterday", "", false, true, false, null));
			RHSDefinition rhsDef33 = new RHSDefinition("System", "DateTime", "System.DateTime");
			rhsDef33.setMethodNameKey("Now.AddDays");
			ArgumentValueDefinition argValDef312 = new ArgumentValueDefinition(ArgumentValueType.PassByReference, "value", "-1");
			rhsDef33.setSupportingMethodName("ToString_String");
			ArgumentValueDefinition argValDef312S1 = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "format", "yyyy-MM-dd");
			rhsDef33.addArgumentValueDefinition(argValDef312);
			rhsDef33.addSupportingMethodArgumentValueDefinition(argValDef312S1);
			loc3.setRHSDefinition(rhsDef33);
				
			
			forCon1.addCode(loc3);
			
			iesWPg1.addCode(forCon1);
			
			wPg1.addCodeToEvaluate(iesWPg1);
			//wPg1.addAction(act1);
			webPagesPBC.add(wPg1);
			
			//... Web Page 2
			WebPage wPg2 = new WebPage();
			wPg2.setPageName("Page_2");
			wPg2.setUrl("https://acquisition.army.mil/asfi/solicitation_search_form.cfm"); // we can use this to auto generate SQL queries -- in the radar
			wPg2.setBrowserToNavigate(BrowserType.Primary);
			wPg2.setNavigationOrder(2); // we can use this to auto generate SQL queries -- in the radar
			wPg2.setPageType(PageType.Search); // we can use this to auto generate SQL queries -- in the radar
			Condition con4 = new Condition(null, ConditionOperator.DoesNotContain, "value=\"Most recently posted solicitations\"");
			Action act2 = new Action(ActionType.URLNavigation, BrowserType.Primary, "https://acquisition.army.mil/asfi/solicitation_search_form.cfm");
			wPg2.setCondition(con4);
			
			IfElseStatement iesWPg21 = new IfElseStatement(IfElseStatementType.If);
			Condition con1WPg2 = new Condition("_primaryBrowser", ConditionOperator.NotEquals, "null");
			iesWPg21.setCondition(con1WPg2);
			iesWPg21.addCode(act2);
			
			IfElseStatement iesWPg22 = new IfElseStatement(IfElseStatementType.ElseIf);
			Condition con2WPg2 = new Condition("_primaryBrowser", ConditionOperator.Equals, "null");
			iesWPg22.setCondition(con2WPg2);
			iesWPg22.addCode(act2);
			
			iesWPg21.setPeerIfElseStatement(iesWPg22);
			
			wPg2.addCodeToEvaluate(iesWPg21);			
			//wPg2.addAction(act2);
			webPagesPBC.add(wPg2);		

			//... Web Page 3
			WebPage wPg3 = new WebPage();
			wPg3.setPageName("Page_3");
			wPg3.setUrl("https://acquisition.army.mil/asfi/solicitation_search_form.cfm"); // we can use this to auto generate SQL queries -- in the radar
			wPg3.setBrowserToNavigate(BrowserType.Primary);
			wPg3.setNavigationOrder(3); // we can use this to auto generate SQL queries -- in the radar
			wPg3.setPageType(PageType.ResultLinks); // we can use this to auto generate SQL queries -- in the radar
			Condition con5 = new Condition(null, ConditionOperator.DoesNotContain, "value=\"Most recently posted solicitations\"");
			Action act3 = new Action(ActionType.CollectResultLinks, BrowserType.Primary);
			act3.setWebPageName(wPg3.getPageName()); //this is mandatory if PageType is ResultLinks
			wPg3.setCondition(con5);
			
			IfElseStatement iesWPg31 = new IfElseStatement(IfElseStatementType.If);
			Condition con1WPg3 = new Condition(new Variable("protected", false, true, "string", "CLIENT_NAME", "Poplicus", true, false, false, null), ConditionOperator.LogicalComplement, null);
			iesWPg31.setCondition(con1WPg3);			
			iesWPg31.addCode(act3);
			
			IfElseStatement iesWPg32 = new IfElseStatement(IfElseStatementType.ElseIf);
			Condition con2WPg3 = new Condition("_primaryBrowser", ConditionOperator.Equals, "null");
			iesWPg32.setCondition(con2WPg3);
			iesWPg32.addCode(act3);
			
			IfElseStatement iesWPg33 = new IfElseStatement(IfElseStatementType.ElseIf);
			Condition con3WPg3 = new Condition("_primaryBrowser", ConditionOperator.NotEquals, "null");
			iesWPg33.setCondition(con3WPg3);			
			iesWPg33.addCode(act3);
			
			IfElseStatement iesWPg34 = new IfElseStatement(IfElseStatementType.Else);
			iesWPg34.addCode(act3);
			
				IfElseStatement iesWPg341 = new IfElseStatement(IfElseStatementType.If);
				Condition conWPg341 = new Condition("_primaryBrowser", ConditionOperator.NotEquals, "null");
				iesWPg341.setCondition(conWPg341);
				iesWPg341.addCode(act3);
				
					IfElseStatement iesWPg3411 = new IfElseStatement(IfElseStatementType.If);
					Condition conWPg3411 = new Condition("_primaryBrowser", ConditionOperator.NotEquals, "null");
					iesWPg3411.setCondition(conWPg3411);
					iesWPg3411.addCode(act3);
					//iesWPg3411.addCode(forCon1);
					
					IfElseStatement iesWPg3412 = new IfElseStatement(IfElseStatementType.Else);
					Condition conWPg3412 = new Condition("_primaryBrowser", ConditionOperator.NotEquals, "null");
					iesWPg3412.setCondition(conWPg3412);
					iesWPg3412.addCode(act3);
					
					iesWPg341.addCode(iesWPg3411);
					iesWPg341.addCode(iesWPg3412);
				
				IfElseStatement iesWPg342 = new IfElseStatement(IfElseStatementType.Else);
				Condition conWPg342 = new Condition("_primaryBrowser", ConditionOperator.NotEquals, "null");
				iesWPg342.setCondition(conWPg342);
				iesWPg342.addCode(act3);
				
				iesWPg34.addCode(iesWPg341);
				iesWPg34.addCode(iesWPg342);
			
			iesWPg31.setPeerIfElseStatement(iesWPg32);
			iesWPg31.setPeerIfElseStatement(iesWPg33);
			iesWPg31.setPeerIfElseStatement(iesWPg34);
			
			wPg3.addCodeToEvaluate(iesWPg31);			
			
			//wPg3.addAction(act3);
			webPagesPBC.add(wPg3);	
			
			IfElseStatement iesWPg7 = new IfElseStatement(IfElseStatementType.None);
			
			//... Web Page 7
			WebPage wPg7 = new WebPage();
			wPg7.setPageName("Page_7");
			//wPg3.setUrl("https://acquisition.army.mil/asfi/solicitation_search_form.cfm"); // we can use this to auto generate SQL queries -- in the radar
			wPg7.setBrowserToNavigate(BrowserType.Primary);
			wPg7.setNavigationOrder(7); // we can use this to auto generate SQL queries -- in the radar
			wPg7.setPageType(PageType.Search); // we can use this to auto generate SQL queries -- in the radar
			Condition con11 = new Condition(null, ConditionOperator.Contains, "Solicitation search results for most recently posted solicitations");
			
			LineOfCode loc = new LineOfCode(LOC_LHS_Type.DeclareMethodVariableAndInitialize, LOC_RHS_Type.AssignValueToAnExistingVariable, clazz);
			loc.setLHSVariable(new Variable(null, false, false, "string", "optionValue", "", false, true, false, null));
			RHSDefinition rhsDef = new RHSDefinition("Functions", "Text", "Functions.Text");
			rhsDef.setMethodNameKey("TextBeforeTag1UntilTag2");
			ArgumentValueDefinition argValDef1 = new ArgumentValueDefinition(ArgumentValueType.PassByReference, "input", "primaryBrowserHTML");
			ArgumentValueDefinition argValDef2 = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "tag1", ">ALL(");
			ArgumentValueDefinition argValDef3 = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "tag2", "=");
			rhsDef.addArgumentValueDefinition(argValDef1);
			rhsDef.addArgumentValueDefinition(argValDef2);
			rhsDef.addArgumentValueDefinition(argValDef3);
			
			loc.setRHSDefinition(rhsDef);
			iesWPg7.addCode(loc);
			//wPg7.setLineOfCode(loc);
			
			LineOfCode loc2 = new LineOfCode(LOC_LHS_Type.DeclareMethodVariableAndInitialize, LOC_RHS_Type.AssignValueToAnExistingVariable, clazz);
			loc2.setLHSVariable(new Variable(null, false, false, "string", "yesterday", "", false, true, false, null));
			RHSDefinition rhsDef2 = new RHSDefinition("System", "DateTime", "System.DateTime");
			rhsDef2.setMethodNameKey("Now.AddDays");
			ArgumentValueDefinition argValDef12 = new ArgumentValueDefinition(ArgumentValueType.PassByReference, "value", "-1");
			rhsDef2.setSupportingMethodName("ToString_String");
			ArgumentValueDefinition argValDef12S1 = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "format", "yyyy-MM-dd");
			rhsDef2.addArgumentValueDefinition(argValDef12);
			rhsDef2.addSupportingMethodArgumentValueDefinition(argValDef12S1);
			loc2.setRHSDefinition(rhsDef2);
			iesWPg7.addCode(loc2);
			//wPg7.setLineOfCode(loc2);

			
			//Action act7 = new Action(ActionType.SelectOptionItemWithOnChange, BrowserType.Primary);
			Action act7 = new Action(ActionType.FunctionCall, BrowserType.Primary);
			act7.setPackageName("csExWB");
			act7.setClassName("cEXWB");
			act7.setMethodName("SelectOptionItemWithOnChange");
			ArgumentValueDefinition argValDef4 = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "selectName", "RecordsPerPage"); //RecordsPerPage is the tag the user has to pass
			ArgumentValueDefinition argValDef5 = new ArgumentValueDefinition(ArgumentValueType.PassByReference, "optionValue", loc.getLHSVariableName());//optionValue is the name of the variable defined before this line
			ArgumentValueDefinition argValDef6 = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "optionText", "");
			act7.addArgumentValueDefinition(argValDef4);
			act7.addArgumentValueDefinition(argValDef5);
			act7.addArgumentValueDefinition(argValDef6);
			
			//Adding multiple actions..
			//Action act8 = new Action(ActionType.SelectOptionItemWithOnChange, BrowserType.Primary);
			Action act8 = new Action(ActionType.FunctionCall, BrowserType.Primary);
			act8.setPackageName("csExWB");
			act8.setClassName("cEXWB");
			act8.setMethodName("SelectOptionItemWithOnChange");
			//ArgumentValueDefinition argValDef4 = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "selectName", "RecordsPerPage"); //RecordsPerPage is the tag the user has to pass
			//ArgumentValueDefinition argValDef5 = new ArgumentValueDefinition(ArgumentValueType.PassByReference, "optionValue", loc.getLHSVariableName());//optionValue is the name of the variable defined before this line
			//ArgumentValueDefinition argValDef6 = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "optionText", "");
			act8.addArgumentValueDefinition(argValDef4);
			act8.addArgumentValueDefinition(argValDef5);
			act8.addArgumentValueDefinition(argValDef6);
			
			LineOfCode locActionToVar = new LineOfCode(LOC_LHS_Type.DeclareMethodVariableAndInitialize, LOC_RHS_Type.ReturnValueFromAction, clazz);
			locActionToVar.setLHSVariable(new Variable(null, false, false, "string", "selectionOption", "", false, true, false, null));
			locActionToVar.setRHSDefinition(act8);
			iesWPg7.addCode(locActionToVar);

			iesWPg7.addCode(act8);
			
			LineOfCode locExpression = new LineOfCode(LOC_LHS_Type.AssignValueToAnExistingVariable, LOC_RHS_Type.Expression, clazz);
			locExpression.setLHSVariable(varTemp);
			Expression expression = new Expression();
			expression.setOperand(rhsDef2);
			expression.setOperator(ExpressionOperator.Plus);
			expression.setOperand(varTemp);
			locExpression.setRHSDefinition(expression);
			iesWPg7.addCode(locExpression);


				
			//wPg7.addAction(act8);

			act7.setWebPageName(wPg7.getPageName()); //this is mandatory if PageType is ResultLinks
			wPg7.setCondition(con11);
			
			iesWPg7.addCode(act7);
			wPg7.addCodeToEvaluate(iesWPg7);			

			
			//wPg7.addAction(act7);
			wPg7.setOneTimeExecution(true);
			
			//Adding RHSTask
			LineOfCode locRhsTask = new LineOfCode(LOC_LHS_Type.NoLHS, LOC_RHS_Type.PerformATask, clazz);
			//RHSTask rhsTask = new RHSTask(RHSTaskType.ReturnNothing);

			//To return a variable - uncomment below two lines
			RHSTask rhsTask = new RHSTask(RHSTaskType.ReturnAVariable);
			rhsTask.setReturnVariable(new Variable(null, false, false, "String", "SITE_NAME", null, false, false, false, null));
			
			RHSDefinition returnNothing = new RHSDefinition(rhsTask);
			locRhsTask.setRHSDefinition(returnNothing);
			iesWPg7.addCode(locRhsTask);
			//wPg7.setLineOfCode(locRhsTask);
			
			webPagesPBC.add(wPg7);	

			
			//adding WebPage(s) to PrimaryBrowserControl function	
			IfElseStatement ifElseStatementPBC = new IfElseStatement(IfElseStatementType.If);
			Condition conIEPBC = new Condition("_primaryBrowser.GetInnerHTML()", ConditionOperator.NotEquals, "null");
			ifElseStatementPBC.setCondition(conIEPBC);
			ifElseStatementPBC.setWebPages(webPagesPBC);
			methodPBC.addCodeToEvaluateWebPages(ifElseStatementPBC);
			//methodPBC.addWebPages(webPagesPBC);
			
			
			//Expression Example
			Expression subexp1 = new Expression();
			subexp1.setOperand("C");
			subexp1.setOperator(ExpressionOperator.Multiply);
			subexp1.setOperand("D");
			
			Expression exp = new Expression();
			exp.setOperand("A");
			exp.setOperand(ExpressionOperator.Plus);
			exp.setOperand(subexp1);
			exp.setOperand(ExpressionOperator.Plus);
			exp.setOperand("B");
			
			System.out.println("<Expression> " + exp.toStringBuffer());
			
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
		//... dataToExtract, nextPage and nextPageURL are pending
		
		clazz.addMethod(methodPBC);
		
		
		Method methodSBC = new Method("public", true, "void", MethodName.SecondaryBrowserControl);
		methodSBC.setContainingClass(clazz); // this is mandatory
		methodSBC.setSecureAccess(true); //https websites with certificates
		methodSBC.setInvokeBaseSBC(true); //do you want to invoke CrawlerBase#SBC
		
		List<WebPage> webPagesSBC = new ArrayList<WebPage>();
		
		try {
			LineOfCode locSBC = new LineOfCode(LOC_LHS_Type.DeclareMethodVariableAndInitialize, LOC_RHS_Type.AssignValueToAnExistingVariable, clazz);
			locSBC.setLHSVariable(new Variable(null, false, false, "string", "optionValue", "", false, true, false, null));
			RHSDefinition rhsDefSBC = new RHSDefinition("Functions", "Text", "Functions.Text");
			rhsDefSBC.setMethodNameKey("TextBeforeTag1UntilTag2");
			ArgumentValueDefinition argValDef1SBC = new ArgumentValueDefinition(ArgumentValueType.PassByReference, "input", "primaryBrowserHTML");
			ArgumentValueDefinition argValDef2SBC = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "tag1", ">ALL(*******");
			ArgumentValueDefinition argValDef3SBC = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "tag2", "=");
			rhsDefSBC.addArgumentValueDefinition(argValDef1SBC);
			rhsDefSBC.addArgumentValueDefinition(argValDef2SBC);
			rhsDefSBC.addArgumentValueDefinition(argValDef3SBC);
			locSBC.setRHSDefinition(rhsDefSBC);
			methodSBC.addInitialLinesOfCode(locSBC);
			
			//... Web Page 4
			WebPage wPg4 = new WebPage();
			wPg4.setPageName("Page_4");
			wPg4.setUrl("https://acquisition.army.mil/asfi/solicitation_search_form.cfm"); // we can use this to auto generate SQL queries -- in the radar
			wPg4.setBrowserToNavigate(BrowserType.Secondary);
			wPg4.setNavigationOrder(4); // we can use this to auto generate SQL queries -- in the radar
			wPg4.setPageType(PageType.Search); // we can use this to auto generate SQL queries -- in the radar
			Condition con6 = new Condition("secondaryBrowserHTML", ConditionOperator.DoesNotContain, "value=\"Most recently posted solicitations\"");
			Condition con7 = new Condition("B", ConditionOperator.LogicalOR, "C");
			Condition con8 = new Condition(con6, ConditionOperator.LogicalAND, con7);
			Action act4 = new Action(ActionType.ClickAnElement, BrowserType.Secondary, ElementTagType.Input, ElementFieldType.Value, "value=\"Most recently posted solicitations\"", false);
			wPg4.setCondition(con8);
			
			IfElseStatement iesWPg4 = new IfElseStatement(IfElseStatementType.None);
			iesWPg4.addCode(act4);
			wPg4.addCodeToEvaluate(iesWPg4);
			
			//wPg4.addAction(act4);
			webPagesSBC.add(wPg4);
			
			//... Web Page 5
			WebPage wPg5 = new WebPage();
			wPg5.setPageName("Page_5");
			wPg5.setUrl("https://acquisition.army.mil/asfi/solicitation_search_form.cfm"); // we can use this to auto generate SQL queries -- in the radar
			wPg5.setBrowserToNavigate(BrowserType.Secondary);
			wPg5.setNavigationOrder(5); // we can use this to auto generate SQL queries -- in the radar
			wPg5.setPageType(PageType.Search); // we can use this to auto generate SQL queries -- in the radar
			Condition con9 = new Condition("secondaryBrowserHTML", ConditionOperator.DoesNotContain, "value=\"Most recently posted solicitations\"");
			Action act5 = new Action(ActionType.URLNavigation, BrowserType.Secondary, "https://acquisition.army.mil/asfi/solicitation_search_form.cfm");
			wPg5.setCondition(con9);
			
			IfElseStatement iesWPg5 = new IfElseStatement(IfElseStatementType.None);
			iesWPg5.addCode(act5);
			wPg5.addCodeToEvaluate(iesWPg5);
			
			//wPg5.addAction(act5);
			webPagesSBC.add(wPg5);
			
			WebPage wPg6 = new WebPage();
			wPg6.setPageName("Page_6");
			wPg6.setRestoreHTMLSymbols(true); // code to restore html symbols
			wPg6.setUrl("https://acquisition.army.mil/asfi/solicitation_search_form.cfm"); // we can use this to auto generate SQL queries -- in the radar
			wPg6.setBrowserToNavigate(BrowserType.Secondary);
			wPg6.setNavigationOrder(6); // we can use this to auto generate SQL queries -- in the radar
			wPg6.setPageType(PageType.Result); // we can use this to auto generate SQL queries -- in the radar
			
			//Data Group -- can be used to extract data that is seen repeating between tags in a single html file
			DataGroup dataGroup = new DataGroup();
			dataGroup.setStartTag("<TR>");
			//dataGroup.setEndTag("</TR>");
			
			//...Group Datum that needs to be extracted from a WebPage in to logical DataSet(s)
			DataSet dsBid = new DataSet(TableName.PopBid, null);
			Datum fullBid = 
					new Datum(Constants.HTML, ">Solicitation - Amendment Number/Status:<", "</TR>", Constants.STRING, "fullBid", Constants.NO_COLUMN_MAPPING, false, false);
			Datum dtSourceProposalType = 
					new Datum(fullBid, null, Constants.FORWARD_SLASH, Constants.STRING, "sourceProposalType", Constants.SOURCE_PROPOSAL_TYPE, false, true);
			Datum dtBidNumber = new Datum(fullBid, null, Constants.HYPEN, Constants.STRING, "bidNumber", Constants.BID_NUMBER, false, false);
			Datum dtPostedDate = 
				new Datum(Constants.HTML, ">Solicitation/Amendment Effective Date:<", "</TR>", Constants.STRING, "postedDate", Constants.POSTED_DATE, false, false);
			Datum dtProposalType = new Datum(DatumIdentifier.ProposalType);
			Datum dtAmendmentVersion = 
				new Datum(fullBid, Constants.HYPEN, Constants.FORWARD_SLASH, Constants.STRING, "amendmentVersion", Constants.AMENDMENT_VERSION,true, false);
			Datum dtSubmittalDate = 
				new Datum(Constants.HTML, ">Response Deadline:<", "</TR>", Constants.STRING, "submittalDate", Constants.SUBMITTAL_DATE, false, false);
			Datum dtTitle = 
				new Datum("_resultLinkSource[_currentResult]", "<B>Noun:</B>", "</TD>", Constants.STRING, "title", Constants.TITLE, false, false);
			Datum dtSourceURL = new Datum(DatumIdentifier.SourceURL);
			Datum dtSourceID = new Datum(DatumIdentifier.SourceID);	
			Datum dtSourceName = new Datum(DatumIdentifier.SourceName);
			Datum dtSourceSiteName = new Datum(DatumIdentifier.SiteName);
			Datum dtScanID = new Datum(DatumIdentifier.ScanID);
			
//			dsBid.addBidDatum(fullBid);
//			dsBid.addBidDatum(dtSourceProposalType);			
//			dsBid.addBidDatum(dtBidNumber);
//			dsBid.addBidDatum(dtPostedDate);
//			
//			dsBid.addBidDatum(dtProposalType);
//			dsBid.addBidDatum(dtAmendmentVersion);
//
//			dsBid.addBidDatum(dtSubmittalDate);
//			dsBid.addBidDatum(dtTitle);
//			dsBid.addBidDatum(dtSourceURL);
//			//dsBid.addDatum(Constants.AGENCY_NAME, agencyName);          
//			dsBid.addBidDatum(dtSourceName);
//			dsBid.addBidDatum(dtSourceID);
//			dsBid.addBidDatum(dtScanID);
//			dsBid.addBidDatum(dtSourceSiteName);

			dsBid.addDatum(fullBid);
			dsBid.addDatum(dtSourceProposalType);			
			dsBid.addDatum(dtBidNumber);
			dsBid.addDatum(dtPostedDate);
			
			dsBid.addDatum(dtProposalType);
			dsBid.addDatum(dtAmendmentVersion);

			dsBid.addDatum(dtSubmittalDate);
			dsBid.addDatum(dtTitle);
			dsBid.addDatum(dtSourceURL);
			//dsBid.addDatum(Constants.AGENCY_NAME, agencyName);          
			dsBid.addDatum(dtSourceName);
			dsBid.addDatum(dtSourceID);
			dsBid.addDatum(dtScanID);
			dsBid.addDatum(dtSourceSiteName);
			//wPg6.addDataSet(dsBid);
			dataGroup.addDataSet(dsBid);
			
			DataSet dsBidIndustryCodeNaics = new DataSet(TableName.PopBidIndustryCode, "Naics");
			Datum bidIndusCodeNaics = 
					new Datum("_resultLinkSource[_currentResult]", "<B>NAICS code:</B>", "<B>Noun:</B>", Constants.STRING, "naicsCode", Constants.INDUSTRY_CODE_ID, false, false);
			//dsBidIndustryCodeNaics.addBidIndustryDatum(bidIndusCodeNaics);
			dsBidIndustryCodeNaics.addDatum(bidIndusCodeNaics);
			dsBidIndustryCodeNaics.addAdditionalData("BidID", "bidID", false);
			dsBidIndustryCodeNaics.addAdditionalData("IndustryType", "NAICS", true);
			//wPg6.addDataSet(dsBidIndustryCodeNaics);
			dataGroup.addDataSet(dsBidIndustryCodeNaics);
			
			DataSet dsBidIndustryCodeFSC = new DataSet(TableName.PopBidIndustryCode, "Fsc");
			Datum bidIndusCodeFsc = 
					new Datum("_resultLinkSource[_currentResult]", "<B>FSC code:</B>", "<B>Noun:</B>", Constants.STRING, "fscCode", Constants.INDUSTRY_CODE_ID, false, false);
			//dsBidIndustryCodeFSC.addBidIndustryDatum(bidIndusCodeFsc);
			dsBidIndustryCodeFSC.addDatum(bidIndusCodeFsc);
			dsBidIndustryCodeFSC.addAdditionalData("BidID", "bidID", false);
			dsBidIndustryCodeFSC.addAdditionalData("IndustryType", "FSC", true);
			//wPg6.addDataSet(dsBidIndustryCodeFSC);
			dataGroup.addDataSet(dsBidIndustryCodeFSC);
			
			DataSet dsBidLocation = new DataSet(TableName.PopBidLocation, null);
			dsBidLocation.addAdditionalData("BidID", "bidID", false);
			dsBidLocation.addAdditionalData("LocationOf", "Agency", true);
			//passing null for htmlSource, startTag and endTag will just create the variable and leave it empty. thus created variable will be used
			//to assign value to setValue method
			Datum bidLocCity = new Datum(null, null, null, Constants.STRING, "agencyLocationCity", Constants.CITY, false, false);

			Datum bidLocState = new Datum(null, null, null, Constants.STRING, "agencyLocationState", Constants.STATE, false, false);

			Datum bidLocUnparsed = new Datum(null, null, null, Constants.STRING, "agencyLocation", Constants.UNPARSED_CONTENT, false, false);
//			dsBidLocation.addBidLocationDatum(bidLocCity);
//			dsBidLocation.addBidLocationDatum(bidLocState);
//			dsBidLocation.addBidLocationDatum(bidLocUnparsed);
			dsBidLocation.addDatum(bidLocCity);
			dsBidLocation.addDatum(bidLocState);
			dsBidLocation.addDatum(bidLocUnparsed);

			//wPg6.addDataSet(dsBidLocation);
			dataGroup.addDataSet(dsBidLocation);

			DataSet dsBidContact = new DataSet(TableName.PopBidContact, null);
			dsBidContact.addAdditionalData("BidID", "bidID", false);
			dsBidContact.addAdditionalData("LocationOf", "Agency", true);
			//passing null for htmlSource, startTag and endTag will just create the variable and leave it empty. thus created variable will be used
			//to assign value to setValue method
			Datum bidContactEmail = new Datum(null, null, null, Constants.STRING, "agencyContactEmail", Constants.EMAIL, false, false);

			Datum bidContactPhone = new Datum(null, null, null, Constants.STRING, "agencyContactPhone", Constants.PHONE, false, false);

			Datum bidContactFax = new Datum(null, null, null, Constants.STRING, "agencyContactFax", Constants.FAX, false, false);

			Datum bidContactName = new Datum(null, null, null, Constants.STRING, "agencyContactName", Constants.UNPARSED_CONTENT, false, false);

			Datum bidContactFName = new Datum(null, null, null, Constants.STRING, "agencyContactFirstName", Constants.FIRST_NAME, false, false);

			Datum bidContactLName = new Datum(null, null, null, Constants.STRING, "agencyContactLastName", Constants.LAST_NAME, false, false);

			Datum bidContactMName = new Datum(null, null, null, Constants.STRING, "agencyContactMiddleName", Constants.MIDDLE_NAME, false, false);
			
//			dsBidContact.addBidContactDatum(bidContactEmail);
//			dsBidContact.addBidContactDatum(bidContactPhone);
//			dsBidContact.addBidContactDatum(bidContactFax);
//			dsBidContact.addBidContactDatum(bidContactName);
//			dsBidContact.addBidContactDatum(bidContactFName);
//			dsBidContact.addBidContactDatum(bidContactLName);
//			dsBidContact.addBidContactDatum(bidContactMName);		
			
			dsBidContact.addDatum(bidContactEmail);
			dsBidContact.addDatum(bidContactPhone);
			dsBidContact.addDatum(bidContactFax);
			dsBidContact.addDatum(bidContactName);
			dsBidContact.addDatum(bidContactFName);
			dsBidContact.addDatum(bidContactLName);
			dsBidContact.addDatum(bidContactMName);			

			//wPg6.addDataSet(dsBidContact);
			dataGroup.addDataSet(dsBidContact);

			DataSet dsBidSetAside = new DataSet(TableName.PopBidSetAside, null);
			Datum bidSetAsideCode = 
					new Datum(Constants.HTML, ">Set-Aside:<", "</TR>", Constants.STRING, "dbeSetAsideCode", Constants.DBE_CODE, false, false);
			//dsBidSetAside.addBidSetAsideDatum(bidSetAsideCode);
			dsBidSetAside.addDatum(bidSetAsideCode);
			dsBidSetAside.addAdditionalData("BidID", "bidID", false);
			//wPg6.addDataSet(dsBidSetAside);
			dataGroup.addDataSet(dsBidSetAside);

			DataSet dsBidDocument = new DataSet(TableName.PopBidDocument, null);
			/*Datum bidDocumentTitle = 
					new Datum(null, null, null, Constants.STRING, "documentTitle", Constants.TITLE, false, false);
			Datum bidDocumentURL = 
					new Datum(null, null, null, Constants.STRING, "documentURL", Constants.URL, false, false);
			Datum bidDocumentPostedDate = 
					new Datum(null, null, null, Constants.STRING, "documentPosted", Constants.POSTED_DATE, false, false);
			
			dsBidDocument.addBidDocumentDatum(bidDocumentTitle);
			dsBidDocument.addBidDocumentDatum(bidDocumentURL);
			dsBidDocument.addBidDocumentDatum(bidDocumentPostedDate);*/
			dsBidDocument.addAdditionalData("BidID", "bidID", false);
			//wPg6.addDataSet(dsBidDocument);
			dataGroup.addDataSet(dsBidDocument);

			wPg6.addDataGroup(dataGroup);
			
			Condition con10 = new Condition("secondaryBrowserHTML", ConditionOperator.Contains, "xyz");
			//Action act6 = new Action(ActionType.ParseResult, BrowserType.Secondary, ElementTagType.Input, ElementFieldType.Value, "value=\"Most recently posted solicitations\"", false);
			Action act6 = new Action(ActionType.ParseResult, BrowserType.Secondary);
			act6.setWebPageName(wPg6.getPageName());
			wPg6.setCondition(con10);
			
			IfElseStatement iesWPg6 = new IfElseStatement(IfElseStatementType.None);
			iesWPg6.addCode(act6);
			wPg6.addCodeToEvaluate(iesWPg6);
			
			//wPg6.addAction(act6);
			webPagesSBC.add(wPg6);
			
			//adding WebPage(s) to SecondaryBrowserControl function	
			IfElseStatement ifElseStatementSBC = new IfElseStatement(IfElseStatementType.If);
			Condition conIESBC = new Condition("_secondaryBrowser.GetInnerHTML()", ConditionOperator.NotEquals, "null");
			ifElseStatementSBC.setCondition(conIESBC);
			ifElseStatementSBC.setWebPages(webPagesSBC);
			
			Variable clickSuccess = new Variable("protected", false, false, "bool", "_clickSuccess", "false", false, true, false, null);
			IfElseStatement adhocIf = new IfElseStatement(IfElseStatementType.If);
			Condition conAdhocIf = new Condition(clickSuccess, ConditionOperator.LogicalComplement, null);
			adhocIf.setCondition(conAdhocIf);
			Action actionAdhocIf = new Action(ActionType.StartTimer, BrowserType.Primary);
			adhocIf.addCode(actionAdhocIf);
			methodPBC.setAdhocIfElseStatement(adhocIf);

			methodSBC.addCodeToEvaluateWebPages(ifElseStatementSBC);
			//methodSBC.addWebPages(webPagesSBC);
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
		
		clazz.addMethod(methodSBC);
		//generate methods - end
		
		clazz.generateCode();
		
	}

}
