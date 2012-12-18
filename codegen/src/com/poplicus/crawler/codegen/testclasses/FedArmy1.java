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
import com.poplicus.crawler.codegen.IfElseStatement;
import com.poplicus.crawler.codegen.LineOfCode;
import com.poplicus.crawler.codegen.Method;
import com.poplicus.crawler.codegen.RHSDefinition;
import com.poplicus.crawler.codegen.Variable;
import com.poplicus.crawler.codegen.WebPage;
import com.poplicus.crawler.codegen.Method.MethodName;
import com.poplicus.crawler.codegen.WebPage.BrowserType;
import com.poplicus.crawler.codegen.WebPage.PageType;

public class FedArmy1 {
	
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
		con.addSuperParameters(new Variable(null, false, false, "UserInput", "userInput", null, false, false, false, null));
		
		con.setPrimaryBrowserDownloadScript(true);
		con.setSecondaryBrowserDownloadImages(false);

		clazz.addConstructor(con);
		//generate the constructor - end
		
		//closing security alert pop-up
		clazz.setSecurityAlertPopup(true);//default is true, if the site we are crawling doesn't throw a security alert popup then it is mandatory to set this to false
		
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
			//... Web Page 1
			WebPage wPg1 = new WebPage();
			wPg1.setPageName("Page_1");
			wPg1.setUrl("https://acquisition.army.mil/asfi/solicitation_search_form.cfm"); // we can use this to auto generate SQL queries -- in the radar
			wPg1.setBrowserToNavigate(BrowserType.Primary);
			wPg1.setNavigationOrder(1); // we can use this to auto generate SQL queries -- in the radar
			wPg1.setPageType(PageType.Search); // we can use this to auto generate SQL queries -- in the radar
			Condition con1 = new Condition(null, ConditionOperator.Contains, "value=\"Most recently posted solicitations\"");
			Action act1 = new Action(ActionType.ClickAnElement, BrowserType.Primary, ElementTagType.Input, ElementFieldType.Value, "Most recently posted solicitations", false);
			wPg1.setCondition(con1);
			IfElseStatement iesWPg1 = new IfElseStatement(IfElseStatementType.None);
			iesWPg1.addCode(act1);
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
			Condition con4 = new Condition(null, ConditionOperator.Contains, "Navigation to the webpage was canceled");
			Action act2 = new Action(ActionType.URLNavigation, BrowserType.Primary, "https://acquisition.army.mil/asfi/solicitation_search_form.cfm");
			wPg2.setCondition(con4);
			IfElseStatement iesWPg2 = new IfElseStatement(IfElseStatementType.None);
			iesWPg2.addCode(act2);
			wPg2.addCodeToEvaluate(iesWPg2);
			//wPg2.addAction(act2);
			webPagesPBC.add(wPg2);		

			//... Web Page 3
			WebPage wPg3 = new WebPage();
			wPg3.setPageName("Page_3");
			wPg3.setUrl("https://acquisition.army.mil/asfi/solicitation_search_form.cfm"); // we can use this to auto generate SQL queries -- in the radar
			wPg3.setBrowserToNavigate(BrowserType.Primary);
			wPg3.setNavigationOrder(3); // we can use this to auto generate SQL queries -- in the radar
			wPg3.setPageType(PageType.Search); // we can use this to auto generate SQL queries -- in the radar
			Condition con5 = new Condition(null, ConditionOperator.Contains, ">This program cannot display the webpage<");
			Action act3 = new Action(ActionType.URLNavigation, BrowserType.Primary, "https://acquisition.army.mil/asfi/solicitation_search_form.cfm");
			wPg3.setCondition(con5);
			IfElseStatement iesWPg3 = new IfElseStatement(IfElseStatementType.None);
			iesWPg3.addCode(act3);
			wPg3.addCodeToEvaluate(iesWPg3);
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
			//iesWPg7.addCode(loc2);
			//wPg7.setLineOfCode(loc2);
			
			Action act7 = new Action(ActionType.SelectOptionItemWithOnChange, BrowserType.Primary);
			act7.setPackageName("csExWB");
			act7.setClassName("cEXWB");
			act7.setMethodName("SelectOptionItemWithOnChange");
			ArgumentValueDefinition argValDef4 = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "selectName", "RecordsPerPage"); //RecordsPerPage is the tag the user has to pass
			ArgumentValueDefinition argValDef5 = new ArgumentValueDefinition(ArgumentValueType.PassByReference, "optionValue", loc.getLHSVariableName());//optionValue is the name of the variable defined before this line
			ArgumentValueDefinition argValDef6 = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "optionText", "");
			act7.addArgumentValueDefinition(argValDef4);
			act7.addArgumentValueDefinition(argValDef5);
			act7.addArgumentValueDefinition(argValDef6);
			
			act7.setWebPageName(wPg7.getPageName()); //this is mandatory if PageType is ResultLinks
			wPg7.setCondition(con11);
			
			iesWPg7.addCode(act7);
			wPg7.addCodeToEvaluate(iesWPg7);
			//wPg7.addAction(act7);
			wPg7.setOneTimeExecution(true);
			webPagesPBC.add(wPg7);	

			
			//adding WebPage(s) to PrimaryBrowserControl function	
			IfElseStatement ifElseStatementPBC = new IfElseStatement(IfElseStatementType.None);
			ifElseStatementPBC.setWebPages(webPagesPBC);
			methodPBC.addCodeToEvaluateWebPages(ifElseStatementPBC);
			//methodPBC.addWebPages(webPagesPBC);
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
		//... dataToExtract, nextPage and nextPageURL are pending
		
		clazz.addMethod(methodPBC);
		
		
		Method methodSBC = new Method("public", true, "void", MethodName.SecondaryBrowserControl);
		methodSBC.setContainingClass(clazz); // this is mandatory
		//methodSBC.setSecureAccess(true); //https websites with certificates
		methodSBC.setInvokeBaseSBC(true); //do you want to invoke CrawlerBase#SBC
		
		List<WebPage> webPagesSBC = new ArrayList<WebPage>();
		
		try {
			
			WebPage wPg6 = new WebPage();
			wPg6.setRestoreHTMLSymbols(true);
			wPg6.setPageName("Page_6");
			wPg6.setUrl("https://acquisition.army.mil/asfi/solicitation_search_form.cfm"); // we can use this to auto generate SQL queries -- in the radar
			wPg6.setBrowserToNavigate(BrowserType.Secondary);
			wPg6.setNavigationOrder(6); // we can use this to auto generate SQL queries -- in the radar
			wPg6.setPageType(PageType.Result); // we can use this to auto generate SQL queries -- in the radar
			
			DataGroup dataGroup = new DataGroup();
			
			//...Group Datum that needs to be extracted from a WebPage in to logical DataSet(s)
			DataSet dsBid = new DataSet(TableName.PopBid, null);
			
//			ParseResultIfElseStatement psConstruct = new ParseResultIfElseStatement(IfElseStatementType.If);
//			Condition prCon1 = new Condition("x", ConditionOperator.NotEquals, "y");
//			psConstruct.setCondition(prCon1);
//			dsBid.setParseResultConstruct(psConstruct);
			
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
			
			Condition con10 = new Condition("secondaryBrowserHTML", ConditionOperator.Contains, "Solicitation View for");
			Action act6 = new Action(ActionType.ParseResult, BrowserType.Secondary, ElementTagType.Input, ElementFieldType.Value, "value=\"Most recently posted solicitations\"", false);
			act6.setWebPageName(wPg6.getPageName());
			wPg6.setCondition(con10);
			IfElseStatement iesWPg6 = new IfElseStatement(IfElseStatementType.None);
			iesWPg6.addCode(act6);
			wPg6.addCodeToEvaluate(iesWPg6);
			//wPg6.addAction(act6);
			webPagesSBC.add(wPg6);
			
			//adding WebPage(s) to SecondaryBrowserControl function	
			IfElseStatement ifElseStatementSBC = new IfElseStatement(IfElseStatementType.None);
			ifElseStatementSBC.setWebPages(webPagesSBC);
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
