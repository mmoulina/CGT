package com.poplicus.crawler.codegen.definitions;

import java.io.Serializable;
import java.util.List;

public class CrawlerDefinition implements Serializable {

	private static final long serialVersionUID = 5089377100646878729L;

	private String name = null;

	private boolean handlePopup = false;

	private ConstructorDefinition currentConstructor = null;

	private ConstructorDefinition superConstructor = null;

	private BrowserDefinition primaryBrowserDefinition = new BrowserDefinition();

	private BrowserDefinition secondaryBrowserDefinition = new BrowserDefinition();

	private BrowserDefinition tertiaryBrowserDefinition = new BrowserDefinition();

	private WebPages webPages = null;

	private String superClass = "PoplicusBaseCrawler";

	private String siteName = null;

	private String clientName = null;

	private String sourceID = null;

	private String sourceName = null;

	private boolean invokeBasePBC = false;

	private boolean invokeBaseSBC = false;

	private boolean invokeBaseTBC = false;

	public boolean isInvokeBasePBC() {
		return invokeBasePBC;
	}

	public void setInvokeBasePBC(boolean invokeBasePBC) {
		this.invokeBasePBC = invokeBasePBC;
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

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getSourceID() {
		return sourceID;
	}

	public void setSourceID(String sourceID) {
		this.sourceID = sourceID;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isHandlePopup() {
		return handlePopup;
	}

	public void setHandlePopup(boolean handlePopup) {
		this.handlePopup = handlePopup;
	}

	public ConstructorDefinition getCurrentConstructor() {
		return currentConstructor;
	}

	public void setCurrentConstructor(ConstructorDefinition currentConstructor) {
		this.currentConstructor = currentConstructor;
	}

	public ConstructorDefinition getSuperConstructor() {
		return superConstructor;
	}

	public void setSuperConstructor(ConstructorDefinition superConstructor) {
		this.superConstructor = superConstructor;
	}

	public BrowserDefinition getPrimaryBrowserDefinition() {
		return primaryBrowserDefinition;
	}

	public void setPrimaryBrowserDefinition(
			BrowserDefinition primaryBrowserDefinition) {
		this.primaryBrowserDefinition = primaryBrowserDefinition;
	}

	public BrowserDefinition getSecondaryBrowserDefinition() {
		return secondaryBrowserDefinition;
	}

	public void setSecondaryBrowserDefinition(
			BrowserDefinition secondaryBrowserDefinition) {
		this.secondaryBrowserDefinition = secondaryBrowserDefinition;
	}

	public BrowserDefinition getTertiaryBrowserDefinition() {
		return tertiaryBrowserDefinition;
	}

	public void setTertiaryBrowserDefinition(
			BrowserDefinition tertiaryBrowserDefinition) {
		this.tertiaryBrowserDefinition = tertiaryBrowserDefinition;
	}

	public WebPages getWebPages() {
		return webPages;
	}

	public void setWebPages(WebPages webPages) {
		this.webPages = webPages;
	}

	public void removeWebPage(String uniqueName) {
		this.webPages.removeWebPage(uniqueName);
	}

	public void removeWebPageByName(String name) {
		this.webPages.removeWebPageByName(name);
	}

	public void replaceWebPage(WebPageDefinition webPage) {
		if(webPages != null) {
			WebPageDefinition webPageDef = null;
			int noOfWebPages = webPages.size();
			for(int index = 0; index < noOfWebPages; index ++) {
				webPageDef = webPages.get(index);
				if(webPageDef.getUniqueName().equals(webPage.getUniqueName())) {
					webPages.replace(index, webPageDef);
					break;
				}
			}
		}
	}

	public WebPageDefinition getWebPage(String uniqueName) {
		if(webPages != null) {
			WebPageDefinition webPageDef = null;
			int noOfWebPages = webPages.size();
			for(int index = 0; index < noOfWebPages; index ++) {
				webPageDef = webPages.get(index);
				if(webPageDef.getUniqueName().equalsIgnoreCase(uniqueName)) {
					return webPageDef;
				}
			}
		}
		return null;
	}

	public void setSuperClass(String superClass) {
		this.superClass = superClass;
	}

	public String getSuperClass() {
		return superClass;
	}

	public String toString() {
		StringBuffer sbf = new StringBuffer("Definition of Crawler : ");
		sbf.append(name).append(", has to handle Popup : ").append(handlePopup);
		sbf.append("\nConstructor Definitions : [");
		sbf.append("\n").append(currentConstructor.toString()).append("\n").append(superConstructor.toString()).append("]\n");
		sbf.append("\nPrimary Browser Definition : [").append(primaryBrowserDefinition).append("]\n");
		sbf.append("\nSecondary Browser Definition : [").append(secondaryBrowserDefinition).append("]\n");
		sbf.append("\nTertiary Browser Definition : [").append(tertiaryBrowserDefinition).append("]\n");
		List<WebPageDefinition> webPageDefs = webPages.getWebPage();
		for(int index = 0; index < webPageDefs.size(); index ++) {
			sbf.append(webPageDefs.get(index).toString());
		}
		return sbf.toString();
	}

}
