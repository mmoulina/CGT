package com.poplicus.crawler.codegen.testclasses;

import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;

import com.poplicus.crawler.codegen.Action;
import com.poplicus.crawler.codegen.ActionDefinitionException;
import com.poplicus.crawler.codegen.ArgumentValueDefinition;
import com.poplicus.crawler.codegen.ArgumentValueDefinitionException;
import com.poplicus.crawler.codegen.Class;
import com.poplicus.crawler.codegen.ClassDefinitionException;
import com.poplicus.crawler.codegen.Condition;
import com.poplicus.crawler.codegen.Constants;
import com.poplicus.crawler.codegen.Constructor;
import com.poplicus.crawler.codegen.DataGroup;
import com.poplicus.crawler.codegen.DataSet;
import com.poplicus.crawler.codegen.Datum;
import com.poplicus.crawler.codegen.Expression;
import com.poplicus.crawler.codegen.FileGenerationException;
import com.poplicus.crawler.codegen.Expression.ExpressionOperator;
import com.poplicus.crawler.codegen.IfElseStatement;
import com.poplicus.crawler.codegen.LineOfCode;
import com.poplicus.crawler.codegen.LineOfCodeException;
import com.poplicus.crawler.codegen.Method;
import com.poplicus.crawler.codegen.RHSDefinition;
import com.poplicus.crawler.codegen.Variable;
import com.poplicus.crawler.codegen.WebPage;
import com.poplicus.crawler.codegen.Action.ActionType;
import com.poplicus.crawler.codegen.Action.ElementFieldType;
import com.poplicus.crawler.codegen.Action.ElementTagType;
import com.poplicus.crawler.codegen.ArgumentValueDefinition.ArgumentValueType;
import com.poplicus.crawler.codegen.Condition.ConditionOperator;
import com.poplicus.crawler.codegen.DataSet.TableName;
import com.poplicus.crawler.codegen.Datum.DatumIdentifier;
import com.poplicus.crawler.codegen.IfElseStatement.IfElseStatementType;
import com.poplicus.crawler.codegen.LineOfCode.LOC_LHS_Type;
import com.poplicus.crawler.codegen.LineOfCode.LOC_RHS_Type;
import com.poplicus.crawler.codegen.Method.MethodName;
import com.poplicus.crawler.codegen.RHSTask.RHSTaskType;
import com.poplicus.crawler.codegen.WebPage.BrowserType;
import com.poplicus.crawler.codegen.WebPage.PageType;
import com.poplicus.crawler.codegen.RHSTask;
import com.poplicus.crawler.codegen.RHSTaskException;

public class FedGrands2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws ClassDefinitionException, ArgumentValueDefinitionException, 
												  LineOfCodeException, ActionDefinitionException, RHSTaskException, 
												  FileGenerationException {
		// TODO Auto-generated method stub
		
		
		Class clazz = new Class(null, "Poplicus_Fed_Grants_2", "PoplicusBaseCrawler");
		Constructor con = new Constructor("public", "Poplicus_Fed_Grants_2", true, true);
		
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
		
		con.setPrimaryBrowserDownloadScript(true);
		con.setSecondaryBrowserDownloadImages(false);
		
		clazz.addConstructor(con);
		
		//closing security alert pop-up
		clazz.setSecurityAlertPopup(false);
		
		//generate instance variables - start
		clazz.addInstanceVariable(new Variable("protected", false, true, "string", "CLIENT_NAME", "Poplicus", true, false, false, null));
		clazz.addInstanceVariable(new Variable("protected", false, true, "string", "SITE_NAME", "Fed_Grants_2", true, false, false, null));
		clazz.addInstanceVariable(new Variable("protected", false, true, "string", "SOURCE_ID", "100012", true, false, false, null));
		clazz.addInstanceVariable(new Variable("protected", false, true, "string", "SOURCE_NAME", "Fed Grants 2", true, false, false, null));
				
		clazz.addInstanceVariable(new Variable("protected", false, true, "string", "USASPEND_FILE_PATH", "Poplicus/TEMP/", true, false, false, null));
		clazz.addInstanceVariable(new Variable("protected", false, false, "string", "fileName", "FedGrants2.csv", true, false, false, null));
		clazz.addInstanceVariable(new Variable("protected", false, false, "string", "localFileName", "", true, false, false, null));
		clazz.addInstanceVariable(new Variable("protected", false, false, "string", "sourceProposalType", "", true, false, false, null));
		//clazz.addInstanceVariable(new Variable("protected", false, false, "string", "_searching", "true", true, false, false, null));
		clazz.addInstanceVariable(new Variable("protected", false, false, "string", "_searching2", "true", true, false, false, null));
		//clazz.addInstanceVariable(new Variable("protected", false, false, "string", "Yesterday", "", true, false, false, null));
		clazz.addInstanceVariable(new Variable("protected", false, false, "string", "GrantRecordType", "", true, false, false, null));
		clazz.addInstanceVariable(new Variable("protected", false, false, "string", "grantActionType", "", true, false, false, null));
		clazz.addInstanceVariable(new Variable("protected", false, false, "string", "grantAssistanceType", "", true, false, false, null));
		clazz.addInstanceVariable(new Variable("protected", false, false, "int", "bidGrantID", "", true, false, false, null));
		clazz.addInstanceVariable(new Variable("protected", false, false, "int", "bidID", "", true, false, false, null));
		clazz.addInstanceVariable(new Variable("protected", false, false, "int", "bidGrantVendorID", "", true, false, false, null));
				
		Method methodPBC = new Method("public", true, "void", MethodName.PrimaryBrowserControl);
		methodPBC.setContainingClass(clazz); 				
		methodPBC.setInvokeBasePBC(false);			
		
		//adding webpages to PBC method
		List<WebPage> webPagesPBC = new ArrayList<WebPage>();
		
		try {	
			
			IfElseStatement mainIFElse = new IfElseStatement(IfElseStatementType.None);
			
			//Web Page 1
			WebPage wPg1 = new WebPage();
			wPg1.setPageName("Page_1");
			wPg1.setBrowserToNavigate(BrowserType.Primary);
			wPg1.setNavigationOrder(1);
			wPg1.setPageType(PageType.Search);
			Condition con1ForWPg1 = new Condition("primaryBrowserHTML", ConditionOperator.DoesNotContain, "submit_image");
			Condition con2ForWPg1 = new Condition("primaryBrowserHTML", ConditionOperator.DoesNotContain, "Download Data");
			Condition con3ForWPg1 = new Condition(con1ForWPg1, ConditionOperator.LogicalOR, con2ForWPg1);
			wPg1.setCondition(con3ForWPg1);
			
			IfElseStatement iesWPg1 = new IfElseStatement(IfElseStatementType.None);
			
			Action actionWPg1 = new Action(ActionType.StartTimer, BrowserType.Primary);
			iesWPg1.addCode(actionWPg1);
			
			LineOfCode locWPg1 = new LineOfCode(LOC_LHS_Type.NoLHS, LOC_RHS_Type.PerformATask, clazz);
			RHSTask rhsTask = new RHSTask(RHSTaskType.ReturnNothing);
			RHSDefinition returnNothing = new RHSDefinition(rhsTask);
			locWPg1.setRHSDefinition(returnNothing);
			iesWPg1.addCode(locWPg1);
			
			wPg1.addCodeToEvaluate(iesWPg1);
			webPagesPBC.add(wPg1);
			
			//Web Page 2
			WebPage wPg2 = new WebPage();
			wPg2.setPageName("Page_2");
			wPg2.setBrowserToNavigate(BrowserType.Primary);
			wPg2.setNavigationOrder(2);
			wPg2.setPageType(PageType.Search);
			Condition con1ForWPg2 = new Condition("primaryBrowserHTML", ConditionOperator.Contains, "submit_image");
			wPg2.setCondition(con1ForWPg2);
			
			IfElseStatement iesWPg2 = new IfElseStatement(IfElseStatementType.None);
			
			LineOfCode loc1WPg2 = new LineOfCode(LOC_LHS_Type.DeclareInstanceVariableAndInitialize, LOC_RHS_Type.AssignValueToAnExistingVariable, clazz);
			loc1WPg2.setLHSVariable(new Variable(null, false, false, "string", "yesterday", "", false, true, false, null));
			RHSDefinition rhsDef1WPg2 = new RHSDefinition("System", "DateTime", "System.DateTime");
			rhsDef1WPg2.setMethodNameKey("Now.AddDays");
			ArgumentValueDefinition argValDef22 = new ArgumentValueDefinition(ArgumentValueType.PassByReference, "value", "-1");
			rhsDef1WPg2.setSupportingMethodName("ToString_String");
			ArgumentValueDefinition argValDef12S1 = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "format", "yyyy-MM-dd");
			rhsDef1WPg2.addArgumentValueDefinition(argValDef22);
			rhsDef1WPg2.addSupportingMethodArgumentValueDefinition(argValDef12S1);
			loc1WPg2.setRHSDefinition(rhsDef1WPg2);
			iesWPg2.addCode(loc1WPg2);			
			
			Action act1WPg2 = new Action(ActionType.ClickAnElement, BrowserType.Primary, ElementTagType.Input, ElementFieldType.ID, "PrimeType", false);
			iesWPg2.addCode(act1WPg2);
			
			Action act2WPg2 = new Action(ActionType.ClickAnElement, BrowserType.Primary, ElementTagType.Input, ElementFieldType.ID, "Grants", false);
			iesWPg2.addCode(act2WPg2);
			
			Action act3WPg2 = new Action(ActionType.ClickAnElement, BrowserType.Primary, ElementTagType.Input, ElementFieldType.ID, "Complete", false);
			iesWPg2.addCode(act3WPg2);
			
			Action act4WPg2 = new Action(ActionType.ClickAnElement, BrowserType.Primary, ElementTagType.Input, ElementFieldType.ID, "Everything", false);
			iesWPg2.addCode(act4WPg2);
			
			Action act5WPg2 = new Action(ActionType.ClickAnElement, BrowserType.Primary, ElementTagType.Input, ElementFieldType.ID, "CSV2", false);
			iesWPg2.addCode(act5WPg2);
			
			Action act6WPg2 = new Action(ActionType.SelectOptionItemWithOnChange, BrowserType.Primary);
			act6WPg2.setPackageName("csExWB");
			act6WPg2.setClassName("cEXWB");
			act6WPg2.setMethodName("SelectOptionItemWithOnChange");
			ArgumentValueDefinition argValDef1 = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "selectName", "ou_code"); 
			ArgumentValueDefinition argValDef2 = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "optionValue", "All");
			ArgumentValueDefinition argValDef3 = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "optionText", "");
			act6WPg2.addArgumentValueDefinition(argValDef1);
			act6WPg2.addArgumentValueDefinition(argValDef2);
			act6WPg2.addArgumentValueDefinition(argValDef3);
			iesWPg2.addCode(act6WPg2);
			
			Action act7WPg2 = new Action(ActionType.SelectOptionItemWithOnChange, BrowserType.Primary);
			act7WPg2.setPackageName("csExWB");
			act7WPg2.setClassName("cEXWB");
			act7WPg2.setMethodName("SelectOptionItemWithOnChange");
			ArgumentValueDefinition argValDef4 = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "selectName", "fiscal_year"); 
			ArgumentValueDefinition argValDef5 = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "optionValue", "2012");
			ArgumentValueDefinition argValDef6 = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "optionText", "");
			act7WPg2.addArgumentValueDefinition(argValDef4);
			act7WPg2.addArgumentValueDefinition(argValDef5);
			act7WPg2.addArgumentValueDefinition(argValDef6);
			iesWPg2.addCode(act7WPg2);
			
			Action act8WPg2 = new Action(ActionType.SelectOptionItemWithOnChange, BrowserType.Primary);
			act8WPg2.setPackageName("csExWB");
			act8WPg2.setClassName("cEXWB");
			act8WPg2.setMethodName("SelectOptionItemWithOnChange");
			ArgumentValueDefinition argValDef7 = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "selectName", "recipient_state"); 
			ArgumentValueDefinition argValDef8 = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "optionValue", "All");
			ArgumentValueDefinition argValDef9 = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "optionText", "");
			act8WPg2.addArgumentValueDefinition(argValDef7);
			act8WPg2.addArgumentValueDefinition(argValDef8);
			act8WPg2.addArgumentValueDefinition(argValDef9);
			iesWPg2.addCode(act8WPg2);
			
			Action act9WPg2 = new Action(ActionType.SelectOptionItemWithOnChange, BrowserType.Primary);
			act9WPg2.setPackageName("csExWB");
			act9WPg2.setClassName("cEXWB");
			act9WPg2.setMethodName("SelectOptionItemWithOnChange");
			ArgumentValueDefinition argValDef10 = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "selectName", "pop_state"); 
			ArgumentValueDefinition argValDef11 = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "optionValue", "All");
			ArgumentValueDefinition argValDef12 = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "optionText", "");
			act9WPg2.addArgumentValueDefinition(argValDef10);
			act9WPg2.addArgumentValueDefinition(argValDef11);
			act9WPg2.addArgumentValueDefinition(argValDef12);
			iesWPg2.addCode(act9WPg2);
			
			//Action act10WPg2 = new Action(ActionType.AutomationTask_PerformEnterData, BrowserType.Primary);
			Action act10WPg2 = new Action(ActionType.FunctionCall, BrowserType.Primary);
			act10WPg2.setPackageName("csExWB");
			act10WPg2.setClassName("cEXWB");
			act10WPg2.setMethodName("AutomationTask_PerformEnterData");
			ArgumentValueDefinition argValDef13 = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "inputname", "since"); 
			ArgumentValueDefinition argValDef14 = new ArgumentValueDefinition(ArgumentValueType.PassByReference, "strValue","Yesterday" );
			act10WPg2.addArgumentValueDefinition(argValDef13);
			act10WPg2.addArgumentValueDefinition(argValDef14);
			iesWPg2.addCode(act10WPg2);
			
			Action act11WPg2 = new Action(ActionType.ClickAnElement, BrowserType.Primary, ElementTagType.Button, ElementFieldType.InnerText, "submit_image", true);
			iesWPg2.addCode(act11WPg2);
			
			Action act12WPg2 = new Action(ActionType.StartTimer, BrowserType.Primary);
			iesWPg2.addCode(act12WPg2);
		
			wPg2.addCodeToEvaluate(iesWPg2);
			webPagesPBC.add(wPg2);
			
			//Web Page 3
			WebPage wPg3 = new WebPage();
			wPg3.setPageName("Page_3");
			wPg3.setBrowserToNavigate(BrowserType.Primary);
			wPg3.setNavigationOrder(3);
			wPg3.setPageType(PageType.Search);
			Condition con1ForWPg3 = new Condition("primaryBrowserHTML", ConditionOperator.Contains, "Download Data");
			wPg3.setCondition(con1ForWPg3);
			
			IfElseStatement iesWPg3 = new IfElseStatement(IfElseStatementType.None);	
			
			IfElseStatement iesWPg31 = new IfElseStatement(IfElseStatementType.If);
			Condition conIesWPg31 = new Condition("primaryBrowserHTML", ConditionOperator.Contains,"There are no records for the selected filter criteria.");
			iesWPg31.setCondition(conIesWPg31);
			iesWPg3.addCode(iesWPg31);
			
			LineOfCode loc2WPg3 = new LineOfCode(LOC_LHS_Type.NoLHS, LOC_RHS_Type.PerformATask, clazz);
			RHSTask rhsTaskWPg3 = new RHSTask(RHSTaskType.ReturnNothing);
			RHSDefinition returnNothingWPg3 = new RHSDefinition(rhsTaskWPg3);
			loc2WPg3.setRHSDefinition(returnNothingWPg3);
			iesWPg31.addCode(loc2WPg3);
			
//			LineOfCode locWPg3 = new LineOfCode(LOC_LHS_Type.DeclareInstanceVariableAndInitialize, LOC_RHS_Type.AssignValueToAnExistingVariable, clazz);
//			locWPg3.setLHSVariable(new Variable(null, false, false, "string", "localFileName", "", false, true, false, null));
//			RHSDefinition rhsDef3 = new RHSDefinition("Functions", "FileOperation", "Functions.FileOperation");
//			rhsDef3.setMethodNameKey("GetRootPath");
//			ArgumentValueDefinition argValDef23 = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "folderName", "TEMP");
//			rhsDef3.addArgumentValueDefinition(argValDef23);
//			locWPg3.setRHSDefinition(rhsDef3);
//			iesWPg3.addCode(locWPg3);
			
			LineOfCode locWPg3 = new LineOfCode(LOC_LHS_Type.DeclareInstanceVariableAndInitialize, LOC_RHS_Type.Expression, clazz);
			locWPg3.setLHSVariable(new Variable(null, false, false, "string", "localFileName", "", false, true, false, null));
			
			RHSDefinition rhsDef3 = new RHSDefinition("Functions", "FileOperation", "Functions.FileOperation");
			rhsDef3.setMethodNameKey("GetRootPath");
			ArgumentValueDefinition argValDef23 = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "folderName", "TEMP");
			rhsDef3.addArgumentValueDefinition(argValDef23);
			
			Expression exp = new Expression();
			exp.setOperand(rhsDef3);
			exp.setOperator(ExpressionOperator.Plus);
			exp.setOperand(new Variable(null, false, false, "string", "localFileName", "", false, true, false, null));
			
			locWPg3.setRHSDefinition(exp);
			iesWPg3.addCode(locWPg3);

			
			//Action act1WPg3 = new Action(ActionType.SetNextFileDownloadPathAndName, BrowserType.Primary);
			Action act1WPg3 = new Action(ActionType.FunctionCall, BrowserType.Primary);
			act1WPg3.setPackageName("csExWB");
			act1WPg3.setClassName("cEXWB");
			act1WPg3.setMethodName("SetNextFileDownloadPathAndName");
			ArgumentValueDefinition argValDef15 = new ArgumentValueDefinition(ArgumentValueType.PassByReference, "pathandFilename", "localFileName");  
			act1WPg3.addArgumentValueDefinition(argValDef15); 
			iesWPg3.addCode(act1WPg3);

			Action act2WPg3 = new Action(ActionType.ClickAnElement, BrowserType.Primary, ElementTagType.Button, ElementFieldType.InnerText, "Download Data", true);
			iesWPg3.addCode(act2WPg3);
			
			wPg3.addCodeToEvaluate(iesWPg3);			
			webPagesPBC.add(wPg3);
			
			//Web Page 4
			WebPage wPg4 = new WebPage();
			wPg4.setPageName("Page_4");
			wPg4.setBrowserToNavigate(BrowserType.Primary);
			wPg4.setNavigationOrder(4);
			wPg4.setPageType(PageType.Search);
			
			IfElseStatement iesWPg4 = new IfElseStatement(IfElseStatementType.None);
			iesWPg4.addCode(new StringBuffer("ReadCsv(path)"));
			iesWPg4.addCode(new StringBuffer("EndCrawl()"));
			
			wPg4.addCodeToEvaluate(iesWPg4);
			webPagesPBC.add(wPg4);
			
			
			//web Page 5
			/* WebPage wPg5 = new WebPage();
			wPg5.setPageName("Page_5");
			wPg5.setBrowserToNavigate(BrowserType.Primary);
			wPg5.setNavigationOrder(5);
			wPg5.setPageType(PageType.Result);
			
			IfElseStatement iesWPg5 = new IfElseStatement(IfElseStatementType.None);
			DataGroup dataGroup = new DataGroup();
			
			DataSet dsBid = new DataSet(TableName.PopBid, null);
			
			Datum dtSourceURL = new Datum(DatumIdentifier.SourceURL);
			Datum dtSourceID = new Datum(DatumIdentifier.SourceID);	
			Datum dtSourceName = new Datum(DatumIdentifier.SourceName);
			Datum dtSourceSiteName = new Datum(DatumIdentifier.SiteName);
			Datum dtScanID = new Datum(DatumIdentifier.ScanID);
						
			dsBid.addBidDatum(dtSourceURL);		         
			dsBid.addBidDatum(dtSourceName);
			dsBid.addBidDatum(dtSourceID);
			dsBid.addBidDatum(dtScanID);
			dsBid.addBidDatum(dtSourceSiteName);			
			dataGroup.addDataSet(dsBid);
			wPg5.addDataGroup(dataGroup);
			
			wPg5.addCodeToEvaluate(iesWPg5);
			webPagesPBC.add(wPg5); */
			
			mainIFElse.setWebPages(webPagesPBC);
			
			Variable clickSuccess = new Variable("protected", false, false, "bool", "_clickSuccess", "false", false, true, false, null);
			
			IfElseStatement adhocIf = new IfElseStatement(IfElseStatementType.If);
			Condition conAdhocIf = new Condition(clickSuccess, ConditionOperator.LogicalComplement, null);
			adhocIf.setCondition(conAdhocIf);
			Action actionAdhocIf = new Action(ActionType.StartTimer, BrowserType.Primary);
			adhocIf.addCode(actionAdhocIf);
			methodPBC.setAdhocIfElseStatement(adhocIf);
			
			methodPBC.addCodeToEvaluateWebPages(mainIFElse);
			
		}catch ( Exception ex ) {
			ex.printStackTrace();
		}
		clazz.addMethod(methodPBC);
		
		clazz.generateCode();

	}

}
