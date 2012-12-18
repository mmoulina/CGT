package com.poplicus.crawler.codegen.docmgr;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HTMLUtility {

	public String getFullyFormedHTML(String html) {
		Document document = Jsoup.parse(html);
		return document.html();
	}
	
}
