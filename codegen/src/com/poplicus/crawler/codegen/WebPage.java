package com.poplicus.crawler.codegen;

import java.util.ArrayList;
import java.util.List;

import com.poplicus.crawler.codegen.IfElseStatement.IfElseStatementType;

/**
 * This class represents a web page in crawling process.
 *
 * @author mmoulina
 *
 */
public class WebPage {

	private Indentation indentation = new Indentation(Indentation.INDENTATION_LEVEL_3);

	public enum BrowserType {
		Primary, Secondary, Tertiary, Others
	}

	public enum PageType {
		Search, ResultLinks, Result
	}

	private String pageName = null;

	private String url = null;

	private BrowserType browserToNavigate = null;

	private int navigationOrder = 0;

	private PageType pageType = null;

	private boolean nextPage = false;

	private String nextPageUrl = null;

	private Condition condition = null;

	private boolean oneTimeExecution = false;

	private boolean restoreHTMLSymbols = false;

	private List<DataGroup> dataGroups = new ArrayList<DataGroup>();

	private List<LineOfCode> preParsingInstructions = new ArrayList<LineOfCode>();

	private IfElseStatement ifElseStatement = null;

	public boolean isRestoreHTMLSymbols() {
		return restoreHTMLSymbols;
	}

	public void setRestoreHTMLSymbols(boolean restoreHTMLSymbols) {
		this.restoreHTMLSymbols = restoreHTMLSymbols;
	}

	public boolean isOneTimeExecution() {
		return oneTimeExecution;
	}

	public void setOneTimeExecution(boolean oneTimeExecution) {
		this.oneTimeExecution = oneTimeExecution;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public boolean isNextPage() {
		return nextPage;
	}

	public void setIndentationLevel(int level) {
		indentation.setLevel(level);
		updateIndentationOfChilds();
	}

	public void addCodeToEvaluate(IfElseStatement ifElseStatement) {
		if(ifElseStatement != null) {
			if(ifElseStatement.getStatementType().equals(IfElseStatementType.None)) {
				ifElseStatement.setIndentationLevel(indentation.getLevel());
			} else {
				ifElseStatement.setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
			}

		}
		this.ifElseStatement = ifElseStatement;
	}

	public StringBuffer getEvaluationCode(StringBuffer tabs, boolean oneTabLeft) throws ActionDefinitionException, ClassDefinitionException,
																						ArgumentValueDefinitionException, LineOfCodeException,
																						RHSTaskException {
		if(oneTabLeft) {
			this.ifElseStatement.setIndentationLevel(indentation.getNumberOfTabs(tabs) - 1);
		} else {
			this.ifElseStatement.setIndentationLevel(indentation.getNumberOfTabs(tabs));
		}
		return this.ifElseStatement.toStringBuffer();
	}

	public String getNextPageUrl() {
		return nextPageUrl;
	}

	public void setNextPageUrl(String nextPageUrl) {
		this.nextPageUrl = nextPageUrl;
	}

	public void setNextPage(boolean nextPage) {
		this.nextPage = nextPage;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) throws WebPageException {
		if(pageName == null || pageName.length() <= 0) {
			throw new WebPageException("Page name cannot be null or empty.");
		}
		if(pageName.contains(Constants.SPACE)) {
			throw new WebPageException("Space is not allowed in WebPage name.");
		}
		this.pageName = pageName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public BrowserType getBrowserToNavigate() {
		return browserToNavigate;
	}

	public void setBrowserToNavigate(BrowserType browserToNavigate) {
		this.browserToNavigate = browserToNavigate;
	}

	public int getNavigationOrder() {
		return navigationOrder;
	}

	public void setNavigationOrder(int navigationOrder) {
		this.navigationOrder = navigationOrder;
	}

	public PageType getPageType() {
		return pageType;
	}

	public void setPageType(PageType pageType) {
		this.pageType = pageType;
	}

	public void addDataGroup(DataGroup dataGroup) {
		this.dataGroups.add(dataGroup);
	}

	public List<DataGroup> getDataGroups() {
		return this.dataGroups;
	}

	public void addPreParsingInstruction(LineOfCode loc) {
		if(loc != null) {
			loc.setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
			this.preParsingInstructions.add(loc);
		}
	}

	public List<LineOfCode> getPreParsingInstructions() {
		return this.preParsingInstructions;
	}

	public void updateIndentationOfChilds() {
		//updating pre-parsing instructions
		LineOfCode loc = null;
		int noOfPreparsingInstructions = preParsingInstructions.size();
		for(int index = 0; index < noOfPreparsingInstructions; index ++) {
			loc = preParsingInstructions.get(index);
			loc.setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
			preParsingInstructions.set(index, loc);
		}

		//updating the main if statement
		if(ifElseStatement.getStatementType().equals(IfElseStatementType.None)) {
			ifElseStatement.setIndentationLevel(indentation.getLevel());
		} else {
			ifElseStatement.setIndentationLevel(indentation.getNumberOfTabs(indentation.getTabsForNextLevel()));
		}
	}

	public Indentation getIndentation() {
		return this.indentation;
	}

}
