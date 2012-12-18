package com.poplicus.crawler.codegen.testclasses;

import java.util.ArrayList;
import java.util.List;

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
import com.poplicus.crawler.codegen.FileGenerationException;
import com.poplicus.crawler.codegen.IfElseStatement;
import com.poplicus.crawler.codegen.Indentation;
import com.poplicus.crawler.codegen.LineOfCode;
import com.poplicus.crawler.codegen.LineOfCodeException;
import com.poplicus.crawler.codegen.Method;
import com.poplicus.crawler.codegen.RHSDefinition;
import com.poplicus.crawler.codegen.RHSTaskException;
import com.poplicus.crawler.codegen.Variable;
import com.poplicus.crawler.codegen.WebPage;
import com.poplicus.crawler.codegen.Action.ActionType;
import com.poplicus.crawler.codegen.Action.ElementFieldType;
import com.poplicus.crawler.codegen.Action.ElementTagType;
import com.poplicus.crawler.codegen.ArgumentValueDefinition.ArgumentValueType;
import com.poplicus.crawler.codegen.Condition.ConditionOperator;
import com.poplicus.crawler.codegen.DataSet.TableName;
import com.poplicus.crawler.codegen.IfElseStatement.IfElseStatementType;
import com.poplicus.crawler.codegen.LineOfCode.LOC_LHS_Type;
import com.poplicus.crawler.codegen.LineOfCode.LOC_RHS_Type;
import com.poplicus.crawler.codegen.Method.MethodName;
import com.poplicus.crawler.codegen.WebPage.BrowserType;
import com.poplicus.crawler.codegen.WebPage.PageType;

public class TPWD1 {

	public static void main(String[] args) 
		throws ClassDefinitionException, ArgumentValueDefinitionException, 
			   LineOfCodeException, ActionDefinitionException, RHSTaskException,
			   FileGenerationException {
		
		Class clazz = new Class(null, "Poplicus_TX_TPWD_1", "PoplicusBaseCrawler");
		
		//generate the constructor - start
		Constructor con = new Constructor("public", "Poplicus_TX_TPWD_1", true, true);
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
		
		clazz.addConstructor(con);
		//generate the constructor - end
		
		//generate instance variables - start
		clazz.addInstanceVariable(new Variable("protected", false, true, "string", "CLIENT_NAME", "Poplicus", true, false, false, null));
		clazz.addInstanceVariable(new Variable("protected", false, true, "string", "SITE_NAME", "TX_TPWD_1", true, false, false, null));
		clazz.addInstanceVariable(new Variable("protected", false, true, "string", "SOURCE_ID", "100060", true, false, false, null));
		clazz.addInstanceVariable(new Variable("protected", false, true, "string", "SOURCE_NAME", "TX-TPWD-1", true, false, false, null));		
		//generate instance variables - end
		
		//generate methods - start
		Method methodPBC = new Method("public", true, "void", MethodName.PrimaryBrowserControl);
		methodPBC.setContainingClass(clazz); // this is mandatory
		methodPBC.setSecureAccess(false); //https websites with certificates
		methodPBC.setInvokeBasePBC(true); //do you want to invoke CrawlerBase#PBC
		
		//adding webpages to PBC method
		List<WebPage> webPagesPBC = new ArrayList<WebPage>();

		try {
			//... Main Page
			WebPage mainPage = new WebPage();
			mainPage.setPageName("MainPage");
			mainPage.setUrl("https://acquisition.army.mil/asfi/solicitation_search_form.cfm"); // we can use this to auto generate SQL queries -- in the radar
			mainPage.setBrowserToNavigate(BrowserType.Primary);
			mainPage.setNavigationOrder(1); // we can use this to auto generate SQL queries -- in the radar
			mainPage.setPageType(PageType.ResultLinks); // we can use this to auto generate SQL queries -- in the radar
			Condition con5 = new Condition(null, ConditionOperator.Contains, "specific group of opportunities:");
			Action act3 = new Action(ActionType.CollectResultLinks, BrowserType.Primary);
			act3.setWebPageName(mainPage.getPageName()); //this is mandatory if PageType is ResultLinks
			mainPage.setCondition(con5);
			IfElseStatement iesmainPage = new IfElseStatement(IfElseStatementType.None);
			iesmainPage.addCode(act3);
			mainPage.addCodeToEvaluate(iesmainPage);
			//mainPage.addAction(act3);
			webPagesPBC.add(mainPage);	
			
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
			//... Web Page 4
			WebPage goodsPage = new WebPage();
			goodsPage.setRestoreHTMLSymbols(true);
			goodsPage.setPageName("GoodsPage");
			goodsPage.setUrl("http://www.tpwd.state.tx.us/business/bidops/current_bid_opportunities/goods_or_commodities/"); 
			goodsPage.setBrowserToNavigate(BrowserType.Secondary);
			goodsPage.setNavigationOrder(2); // we can use this to auto generate SQL queries -- in the radar
			goodsPage.setPageType(PageType.Result); // we can use this to auto generate SQL queries -- in the radar
			
			// <<<< Code to extract data should come here  >>>>
			
			Condition con6 = new Condition("secondaryBrowserHTML", ConditionOperator.Contains, "<H1>Bid Opportunities - Goods</H1>");
			Action act4 = new Action(ActionType.ParseResult, BrowserType.Secondary, ElementTagType.Input, ElementFieldType.Value, 
				"value=\"Most recently posted solicitations\"", false);
			act4.setWebPageName(goodsPage.getPageName());
			goodsPage.setCondition(con6);
			IfElseStatement iesgoodsPage = new IfElseStatement(IfElseStatementType.None);
			iesgoodsPage.addCode(act4);
			goodsPage.addCodeToEvaluate(iesgoodsPage);

			//goodsPage.addAction(act4);
			webPagesSBC.add(goodsPage);
			
			
			DataGroup dataGroup = new DataGroup();
			dataGroup.setStartTag("<HR>"); // need to set start tag and end tag only 
			
			//pre-processing instructions - start
			LineOfCode instruction1 = new LineOfCode(LOC_LHS_Type.AssignValueToAnExistingVariable, 
				LOC_RHS_Type.AssignValueToAnExistingVariable, clazz);
			instruction1.setIndentationLevel(3);
			instruction1.setStartInNewLine(false);
			instruction1.setLHSVariable(new Variable(null, false, false, "string", "html", "", false, true, false, null));
			RHSDefinition rhsDef1 = new RHSDefinition("Functions", "Text", "Functions.Text");
			rhsDef1.setMethodNameKey("TextBetweenTags");
			ArgumentValueDefinition argValDef11 = new ArgumentValueDefinition(ArgumentValueType.PassByReference, "input", "html");
			ArgumentValueDefinition argValDef12 = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "tag1", "<P>Each recent item below is flagged with one of the following:");
			ArgumentValueDefinition argValDef13 = new ArgumentValueDefinition(ArgumentValueType.PassByValue, "tag2", "<!--PAGEWATCH CODE");
			rhsDef1.addArgumentValueDefinition(argValDef11);
			rhsDef1.addArgumentValueDefinition(argValDef12);
			rhsDef1.addArgumentValueDefinition(argValDef13);
			instruction1.setRHSDefinition(rhsDef1);
			
			goodsPage.addPreParsingInstruction(instruction1);

			
			//pre-processing instructions - end
			
			//...Group Datum that needs to be extracted from a WebPage in to logical DataSet(s)
			DataSet dsBid = new DataSet(TableName.PopBid, null);
			Datum html = 
				new Datum(Constants.HTML, "<P>Each recent item below is flagged with one of the following:", "<!--PAGEWATCH CODE", 
				Constants.STRING, "html", Constants.NO_COLUMN_MAPPING, true, false);
			
			
			/*Datum dtSourceProposalType = 
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
			Datum dtScanID = new Datum(DatumIdentifier.ScanID);*/
			
			//dsBid.addBidDatum(html);
			/*dsBid.addBidDatum(dtSourceProposalType);			
			dsBid.addBidDatum(dtBidNumber);
			dsBid.addBidDatum(dtPostedDate);
			
			dsBid.addBidDatum(dtProposalType);
			dsBid.addBidDatum(dtAmendmentVersion);

			dsBid.addBidDatum(dtSubmittalDate);
			dsBid.addBidDatum(dtTitle);
			dsBid.addBidDatum(dtSourceURL);
			dsBid.addBidDatum(dtSourceName);
			dsBid.addBidDatum(dtSourceID);
			dsBid.addBidDatum(dtScanID);
			dsBid.addBidDatum(dtSourceSiteName);*/
			dataGroup.addDataSet(dsBid);
			
			/*
			DataSet dsBidIndustryCodeNaics = new DataSet(TableName.PopBidIndustryCode, "Naics");
			Datum bidIndusCodeNaics = 
					new Datum("_resultLinkSource[_currentResult]", "<B>NAICS code:</B>", "<B>Noun:</B>", Constants.STRING, "naicsCode", Constants.INDUSTRY_CODE_ID, false, false);
			dsBidIndustryCodeNaics.addBidIndustryDatum(bidIndusCodeNaics);
			dsBidIndustryCodeNaics.addAdditionalData("BidID", "bidID", false);
			dsBidIndustryCodeNaics.addAdditionalData("IndustryType", "NAICS", true);
			dataGroup.addDataSet(dsBidIndustryCodeNaics);
			
			DataSet dsBidIndustryCodeFSC = new DataSet(TableName.PopBidIndustryCode, "Fsc");
			Datum bidIndusCodeFsc = 
					new Datum("_resultLinkSource[_currentResult]", "<B>FSC code:</B>", "<B>Noun:</B>", Constants.STRING, "fscCode", Constants.INDUSTRY_CODE_ID, false, false);
			dsBidIndustryCodeFSC.addBidIndustryDatum(bidIndusCodeFsc);
			dsBidIndustryCodeFSC.addAdditionalData("BidID", "bidID", false);
			dsBidIndustryCodeFSC.addAdditionalData("IndustryType", "FSC", true);
			dataGroup.addDataSet(dsBidIndustryCodeFSC);
			
			DataSet dsBidLocation = new DataSet(TableName.PopBidLocation, null);
			dsBidLocation.addAdditionalData("BidID", "bidID", false);
			dsBidLocation.addAdditionalData("LocationOf", "Agency", true);
			//passing null for htmlSource, startTag and endTag will just create the variable and leave it empty. thus created variable will be used
			//to assign value to setValue method
			Datum bidLocCity = new Datum(null, null, null, Constants.STRING, "agencyLocationCity", Constants.CITY, false, false);

			Datum bidLocState = new Datum(null, null, null, Constants.STRING, "agencyLocationState", Constants.STATE, false, false);

			Datum bidLocUnparsed = new Datum(null, null, null, Constants.STRING, "agencyLocation", Constants.UNPARSED_CONTENT, false, false);
			dsBidLocation.addBidLocationDatum(bidLocCity);
			dsBidLocation.addBidLocationDatum(bidLocState);
			dsBidLocation.addBidLocationDatum(bidLocUnparsed);
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
			
			dsBidContact.addBidContactDatum(bidContactEmail);
			dsBidContact.addBidContactDatum(bidContactPhone);
			dsBidContact.addBidContactDatum(bidContactFax);
			dsBidContact.addBidContactDatum(bidContactName);
			dsBidContact.addBidContactDatum(bidContactFName);
			dsBidContact.addBidContactDatum(bidContactLName);
			dsBidContact.addBidContactDatum(bidContactMName);			
			dataGroup.addDataSet(dsBidContact);

			DataSet dsBidSetAside = new DataSet(TableName.PopBidSetAside, null);
			Datum bidSetAsideCode = 
					new Datum(Constants.HTML, ">Set-Aside:<", "</TR>", Constants.STRING, "dbeSetAsideCode", Constants.DBE_CODE, false, false);
			dsBidSetAside.addBidSetAsideDatum(bidSetAsideCode);
			dsBidSetAside.addAdditionalData("BidID", "bidID", false);
			dataGroup.addDataSet(dsBidSetAside);

			DataSet dsBidDocument = new DataSet(TableName.PopBidDocument, null);
			dsBidDocument.addAdditionalData("BidID", "bidID", false);
			dataGroup.addDataSet(dsBidDocument);*/

			goodsPage.addDataGroup(dataGroup);
			
			//adding WebPage(s) to SecondaryBrowserControl function	
			IfElseStatement ifElseStatementSBC = new IfElseStatement(IfElseStatementType.None);
			ifElseStatementSBC.setWebPages(webPagesSBC);
			methodPBC.addCodeToEvaluateWebPages(ifElseStatementSBC);
			//methodSBC.addWebPages(webPagesSBC);
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
		
		clazz.addMethod(methodSBC);
		//generate methods - end
		
		clazz.generateCode();
		
	}

}