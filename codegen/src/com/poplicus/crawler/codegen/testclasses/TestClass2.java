package com.poplicus.crawler.codegen.testclasses;

import java.util.ArrayList;
import java.util.List;

import com.poplicus.crawler.codegen.Action;
import com.poplicus.crawler.codegen.ArgumentValueDefinition;
import com.poplicus.crawler.codegen.Class;
import com.poplicus.crawler.codegen.Condition;
import com.poplicus.crawler.codegen.Constants;
import com.poplicus.crawler.codegen.Constructor;
import com.poplicus.crawler.codegen.DataGroup;
import com.poplicus.crawler.codegen.DataSet;
import com.poplicus.crawler.codegen.Datum;
import com.poplicus.crawler.codegen.ForConstruct;
import com.poplicus.crawler.codegen.IfElseStatement;
import com.poplicus.crawler.codegen.LineOfCode;
import com.poplicus.crawler.codegen.Method;
import com.poplicus.crawler.codegen.RHSDefinition;
import com.poplicus.crawler.codegen.RHSTask;
import com.poplicus.crawler.codegen.Variable;
import com.poplicus.crawler.codegen.WebPage;
import com.poplicus.crawler.codegen.WhileConstruct;
import com.poplicus.crawler.codegen.Action.ActionType;
import com.poplicus.crawler.codegen.Action.ElementFieldType;
import com.poplicus.crawler.codegen.Action.ElementTagType;
import com.poplicus.crawler.codegen.ArgumentValueDefinition.ArgumentValueType;
import com.poplicus.crawler.codegen.Condition.ConditionOperator;
import com.poplicus.crawler.codegen.DataSet.TableName;
import com.poplicus.crawler.codegen.Datum.DatumIdentifier;
import com.poplicus.crawler.codegen.ForConstruct.ForConstructType;
import com.poplicus.crawler.codegen.IfElseStatement.IfElseStatementType;
import com.poplicus.crawler.codegen.LineOfCode.LOC_LHS_Type;
import com.poplicus.crawler.codegen.LineOfCode.LOC_RHS_Type;
import com.poplicus.crawler.codegen.Method.MethodName;
import com.poplicus.crawler.codegen.RHSTask.RHSTaskType;
import com.poplicus.crawler.codegen.WebPage.BrowserType;
import com.poplicus.crawler.codegen.WebPage.PageType;
import com.poplicus.crawler.codegen.WhileConstruct.WhileConstructType;

public class TestClass2 {
	
public static void main(String[] args) throws Exception {
		
		Class clazz = new Class(null, "Poplicus_TEST_2", "PoplicusBaseCrawler");
		
		//generate the constructor - start
		Constructor con = new Constructor("public", "Poplicus_TEST_2", true, true);
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
		clazz.setSecurityAlertPopup(false);//default is true, if the site we are crawling doesn't throw a security alert popup then it is mandatory to set this to false
		
		//generate instance variables - start
		clazz.addInstanceVariable(new Variable("protected", false, true, "string", "CLIENT_NAME", "Poplicus", true, false, false, null));
		clazz.addInstanceVariable(new Variable("protected", false, true, "string", "SITE_NAME", "FED_ARMY_1", true, false, false, null));
		clazz.addInstanceVariable(new Variable("protected", false, true, "string", "SOURCE_ID", "100012", true, false, false, null));
		clazz.addInstanceVariable(new Variable("protected", false, true, "string", "SOURCE_NAME", "FED-ARMY-1", true, false, false, null));
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
//				whileCon1.addCode(iesForCon1);
			
			
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
					
			IfElseStatement ifElseStatementPBC = new IfElseStatement(IfElseStatementType.If);
			Condition conIEPBC = new Condition("_primaryBrowser.GetInnerHTML()", ConditionOperator.NotEquals, "null");
			ifElseStatementPBC.setCondition(conIEPBC);
			ifElseStatementPBC.setWebPages(webPagesPBC);
			
			// Adding While loop functionality
						Variable clickSuccess2 = new Variable("protected", false, false, "bool", "_clickSuccess", "false", false, true, false, null);
						Action whileAct1 = new Action(ActionType.ClickAnElement, BrowserType.Primary, ElementTagType.Input, ElementFieldType.Value, "Most recently posted solicitations", false);	
						
							IfElseStatement adhocIf2 = new IfElseStatement(IfElseStatementType.None);
							
							WhileConstruct whileCon3=new WhileConstruct(WhileConstructType.While);
							whileCon3.setCondition(new Condition("index", ConditionOperator.LessThanAndEquals, "5"));
							Action actionWhile3 = new Action(ActionType.StartTimer, BrowserType.Primary);
							whileCon3.addCode(actionWhile3);
							
							IfElseStatement adhocIf3 = new IfElseStatement(IfElseStatementType.If);
							Condition conAdhocIf2 = new Condition(clickSuccess2, ConditionOperator.LogicalComplement, null);
							adhocIf3.setCondition(conAdhocIf2);
							adhocIf3.addCode(whileCon3);
							
							WhileConstruct whileCon2=new WhileConstruct(WhileConstructType.While);
							whileCon2.setCondition(new Condition("index", ConditionOperator.LessThanAndEquals, "5"));
							Action actionWhile2 = new Action(ActionType.StartTimer, BrowserType.Primary);
							whileCon2.addCode(actionWhile2);
							whileCon2.addCode(adhocIf3);
							
							
							ForConstruct iesWhileForCon11 = new ForConstruct(ForConstructType.For);
							iesWhileForCon11.setVariable(new Variable("int", "index", "0"));
							iesWhileForCon11.setCondition(new Condition("index", ConditionOperator.LessThanAndEquals, "5"));
							iesWhileForCon11.setIncrementDecrementOperator("index ++");
							iesWhileForCon11.addCode(whileAct1);
							iesWhileForCon11.addCode(whileCon2);
							adhocIf2.addCode(iesWhileForCon11);
							
							WhileConstruct whileCon1=new WhileConstruct(WhileConstructType.While);
							whileCon1.setCondition(new Condition("index", ConditionOperator.LessThanAndEquals, "5"));
							Action actionWhile = new Action(ActionType.StartTimer, BrowserType.Primary);
							whileCon1.addCode(actionWhile);
							whileCon1.addCode(adhocIf3);
							whileCon1.addCode(iesWhileForCon11);
							adhocIf2.addCode(whileCon1);					
			
			
							ForConstruct iesForEachCon = new ForConstruct(ForConstructType.ForEach);
							iesForEachCon.setVariable(new Variable("string", "s", ""));							
							iesForEachCon.setIncrementDecrementOperator("links");							
							iesForEachCon.addCode(whileCon3);							
							adhocIf2.addCode(iesForEachCon);
							
							methodPBC.setAdhocIfElseStatement(adhocIf2);
			
			methodPBC.addCodeToEvaluateWebPages(ifElseStatementPBC);
	
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
		//... dataToExtract, nextPage and nextPageURL are pending
		
		clazz.addMethod(methodPBC);	
		
		
		clazz.generateCode();
		
	}

}
