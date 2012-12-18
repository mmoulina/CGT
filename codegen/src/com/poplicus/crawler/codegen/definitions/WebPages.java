package com.poplicus.crawler.codegen.definitions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WebPages implements Iterable<WebPageDefinition> {

	private List<WebPageDefinition> webPages = new ArrayList<WebPageDefinition>();

	public void addWebPage(WebPageDefinition webPageDef) {
		this.webPages.add(webPageDef);
	}

	public List<WebPageDefinition> getWebPage() {
		return this.webPages;
	}

	public boolean isImplementPBC() {
		return this.getWebPages("primary").size() > 0 ? true : false;
	}

	public boolean isImplementSBC() {
		return this.getWebPages("secondary").size() > 0 ? true : false;
	}

	public boolean isImplementTBC() {
		return this.getWebPages("tertiary").size() > 0 ? true : false;
	}

	public int size() {
		return webPages.size();
	}

	public WebPageDefinition get(int index) {
		return webPages.get(index);
	}

	public void replace(int index, WebPageDefinition webPageDef) {
		webPages.set(index, webPageDef);
	}

	public List<WebPageDefinition> getWebPages(String browser) {
		List<WebPageDefinition> webPageSubset = new ArrayList<WebPageDefinition>();
		int noOfWebPages = webPages.size();
		for(int index = 0; index < noOfWebPages; index ++) {
			if(webPages.get(index).getBrowser().equalsIgnoreCase(browser)) {
				webPageSubset.add(webPages.get(index));
			}
		}
		return webPageSubset;
	}

	public WebPageDefinition getWebPage(String uniqueWebPageName) {
		WebPageDefinition webPageDef = null;
		int noOfWebPages = webPages.size();
		for(int index = 0; index < noOfWebPages; index ++) {
			webPageDef = webPages.get(index);
			if(webPageDef.getUniqueName().equalsIgnoreCase(uniqueWebPageName)) {
				break;
			}
		}
		return webPageDef;
	}

	public WebPageDefinition getWebPageByName(String name) {
		WebPageDefinition webPageDef = null;
		int noOfWebPages = webPages.size();
		for(int index = 0; index < noOfWebPages; index ++) {
			webPageDef = webPages.get(index);
			if(webPageDef.getName().equalsIgnoreCase(name)) {
				break;
			}
		}
		return webPageDef;
	}

	public void removeWebPage(String uniqueName) {
		if(webPages != null) {
			WebPageDefinition wpDef = null;
			int noOfWebPages = webPages.size();
			for(int index = 0; index < noOfWebPages; index ++) {
				wpDef = webPages.get(index);
				if(wpDef.getUniqueName().equalsIgnoreCase(uniqueName)) {
					webPages.remove(wpDef);
					break;
				}
			}
			((ArrayList<WebPageDefinition>) webPages).trimToSize();
		}

	}

	public void removeWebPageByName(String name) {
		if(webPages != null) {
			WebPageDefinition wpDef = null;
			int noOfWebPages = webPages.size();
			for(int index = 0; index < noOfWebPages; index ++) {
				wpDef = webPages.get(index);
				if(wpDef.getName().equalsIgnoreCase(name)) {
					webPages.remove(wpDef);
					break;
				}
			}
			((ArrayList<WebPageDefinition>) webPages).trimToSize();
		}

	}

	@Override
	public Iterator<WebPageDefinition> iterator() {
		return webPages.iterator();
	}

}
