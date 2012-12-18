package com.poplicus.crawler.codegen.definitions;

public class BrowserDefinition {

	public enum WhichBrowser {
		Primary {
			public String toString() {
				return "Primary";
			}
		}, 
		Secondary {
			public String toString() {
				return "Secondary";
			}
		}, 
		Tertiary {
			public String toString() {
				return "Tertiary";
			}
		}
	}
	
	private WhichBrowser whichBrowser = null;
	
	private boolean securedWebPage = false;
	
	private boolean downloadImages = false;
	
	private boolean downloadJavaScripts = false;
	
	private Conditions conditions = null;

	public WhichBrowser getWhichBrowser() {
		return whichBrowser;
	}
	
	public BrowserDefinition() {
		
	}

	public void setWhichBrowser(WhichBrowser whichBrowser) {
		this.whichBrowser = whichBrowser;
	}

	public boolean isSecuredWebPage() {
		return securedWebPage;
	}

	public void setSecuredWebPage(boolean securedWebPage) {
		this.securedWebPage = securedWebPage;
	}
	
	public Conditions getConditions() {
		return conditions;
	}

	public void setConditions(Conditions conditions) {
		this.conditions = conditions;
	}

	public boolean isDownloadImages() {
		return downloadImages;
	}

	public void setDownloadImages(boolean downloadImages) {
		this.downloadImages = downloadImages;
	}

	public boolean isDownloadJavaScripts() {
		return downloadJavaScripts;
	}

	public void setDownloadJavaScripts(boolean downloadJavaScripts) {
		this.downloadJavaScripts = downloadJavaScripts;
	}
	
	public String toString() {
		StringBuffer sbf = new StringBuffer("securedWebPage : ");
		sbf.append(securedWebPage);
		sbf.append(", downloadImages : ").append(downloadImages);
		sbf.append(", downloadJavaScripts : ").append(downloadJavaScripts);
		return sbf.toString();
	}
	
}
