package com.poplicus.crawler.codegen.docmgr;

public class PopDoc {

	private String docURL = null;

	private String title = null;

	private String date = null;

	public String getDocURL() {
		return docURL;
	}

	public void setDocURL(String docURL) {
		this.docURL = docURL;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String toString() {
		return "Doc URL : " + docURL + ", Title : " + title + ", Date : " + date;
	}

}
