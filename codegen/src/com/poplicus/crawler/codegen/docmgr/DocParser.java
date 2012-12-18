package com.poplicus.crawler.codegen.docmgr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.poplicus.crawler.codegen.Constants;
import com.poplicus.crawler.codegen.docmgr.IgnoreAnchor.IgnoreCondition;

public class DocParser {

	private String validDocTypes = "RTF, rtf, pdf, PDF, doc, DOC, docx, DOCX, dgn, DGN,	xml, XML, zip, ZIP, XLS, xls, XLSX, xlsx, PPT, ppt, PPTX, pptx, TXT, txt, CSV, csv";

    public List<PopDoc> getPopDocs(String html, String url, IgnoreAnchor ignoreAnchor, boolean onlyDoc) throws IOException {
        List<PopDoc> popDocs = null;
        if(html == null) {
            popDocs = getPopDocsFromURL(url, ignoreAnchor, onlyDoc);
        } else {
            popDocs = getPopDocsFromHTML(html, ignoreAnchor, onlyDoc, url);
        }
        return popDocs;
    }

	public List<PopDoc> getPopDocsFromHTML(String html, IgnoreAnchor ignoreAnchor, boolean onlyDocs, String sourceURL) {
		return getPopDocsFromHTMLDocument(Jsoup.parse(html), ignoreAnchor,
			sourceURL.substring(0, sourceURL.lastIndexOf("/") + 1), onlyDocs);
	}

	public List<PopDoc> getPopDocsFromURL(String url, IgnoreAnchor ignoreAnchor, boolean onlyDocs) throws IOException {
		return getPopDocsFromHTMLDocument(Jsoup.parse(new URL(url), 10000), ignoreAnchor,
			url.substring(0, url.lastIndexOf("/") + 1), onlyDocs);
	}

	public String getValidDocTypes() {
		return this.validDocTypes;
	}

	public void setValidDocTypes(String validDocTypes) {
		this.validDocTypes = validDocTypes;
	}

	public void appendValidDocTypes(String validDocTypes) {
		if(validDocTypes != null) {
			this.validDocTypes = this.validDocTypes + ", " + validDocTypes;
		} else {
			this.validDocTypes = validDocTypes;
		}
	}

	public String getHTMLFromURL(String urlAsString) {
		URL url = null;
		BufferedReader reader = null;
		String inputLine = null;
		StringBuffer htmlSource = new StringBuffer();
		try {
			url = new URL(urlAsString);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
	        while ((inputLine = reader.readLine()) != null) {
	        	htmlSource.append(inputLine);
	        }
	        reader.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return htmlSource.toString();
	}

	private List<PopDoc> getPopDocsFromHTMLDocument(Document document, IgnoreAnchor ignoreAnchor, String prependURL, boolean onlyDocs) {
		Elements anchorTags = document.getElementsByTag("a");
		Element element = null;
		Iterator<Element> itrAnchor = anchorTags.iterator();
		List<Element> validAnchors = new ArrayList<Element>();
		while(itrAnchor.hasNext()) {
			element = itrAnchor.next();
			if(ignoreAnchor != null && ! ignoreAnchor.getIgnoreCondition().equals(IgnoreCondition.DontIgnoreAny)) {
				if(isValidAnchor(element, ignoreAnchor)) {
					validAnchors.add(element);
				}
			} else {
				validAnchors.add(element);
			}
		}
		if(onlyDocs) {
			validAnchors = getAnchorsForDocuments(validAnchors);
		}
		return getPopDocsFromAnchorElements(validAnchors, prependURL, onlyDocs);
	}


	private boolean isValidAnchor(Element element, IgnoreAnchor ignoreAnchor) {
		boolean validElement = true;
		switch (ignoreAnchor.getIgnoreCondition()) {
			case ContainsAttribute:
				validElement = containsAttributeCheckSatisfied(element, ignoreAnchor, validElement);
			break;

			default:
			break;
		}
		return validElement;
	}

	private boolean containsAttributeCheckSatisfied(Element element,
													IgnoreAnchor ignoreAnchor, boolean validElement) {
		Attributes attrs = element.attributes();
		Iterator<Attribute> itrAttribute = attrs.iterator();
		Attribute attr = null;
		while(itrAttribute.hasNext()) {
			attr = itrAttribute.next();
			if(attr.getKey().equalsIgnoreCase(ignoreAnchor.getContainsAttributeName())) {
				validElement = false;
				break;
			}
		}
		return validElement;
	}

	private List<PopDoc> getPopDocsFromAnchorElements(List<Element> validAnchors, String prependURL, boolean onlyDocs) {
		Attributes attrs = null;
		Attribute attr = null;
		Iterator<Attribute> itrAttr = null;
		List<PopDoc> popDocs = new ArrayList<PopDoc>();
		PopDoc popDoc = null;
		String attrValue = null;
		boolean addURL = false;
		for(Element element : validAnchors) {
			popDoc = new PopDoc();
			attrs = element.attributes();
			itrAttr = attrs.iterator();
			while(itrAttr.hasNext()) {
				attr = itrAttr.next();
				if(attr.getKey().equalsIgnoreCase(Constants.HREF)) {
					attrValue = attr.getValue();
					if(attrValue.startsWith(Constants.HTTP_COLON_2_SLASHES)
						|| attrValue.startsWith(Constants.HTTPS_COLON_2_SLASHES)
						|| attrValue.startsWith(Constants.WWW)) {
						if(attrValue.contains(Constants.DOT_DOT + Constants.FORWARD_SLASH)) {
							attrValue = attrValue.replace(Constants.DOT_DOT + Constants.FORWARD_SLASH, "");
						}
						popDoc.setDocURL(attrValue);
						addURL = true;
					} else {
						if(! (attrValue.startsWith("mailto:")
							|| attrValue.startsWith("#")
							|| attrValue.startsWith("javascript:"))) {
							addURL = true;
							popDoc.setDocURL(getURLAfterValidation(prependURL, attrValue));
						}
					}
					if(addURL) {
						popDoc.setTitle(element.text());
						popDocs.add(popDoc);
						addURL = false;
					}
					break;
				}
			}
		}
		return popDocs;
	}

	private String getURLAfterValidation(String prependURL, String appendURL) {
		String validURL = null;
		String tempAppendURL = appendURL.substring(appendURL.lastIndexOf(Constants.FORWARD_SLASH) + 1);
		tempAppendURL = tempAppendURL.substring(0, tempAppendURL.lastIndexOf(Constants.DOT));
		if(tempAppendURL.equalsIgnoreCase("default")) {
			String tempValidURL = null;
			String tempPrependURL = null;
			if(prependURL.startsWith(Constants.HTTP_COLON_2_SLASHES)) {
				tempValidURL = Constants.HTTP_COLON_2_SLASHES;
				tempPrependURL = prependURL.substring(prependURL.indexOf(Constants.HTTP_COLON_2_SLASHES) + 7);
			} else if(prependURL.startsWith(Constants.HTTPS_COLON_2_SLASHES)) {
				tempValidURL = Constants.HTTPS_COLON_2_SLASHES;
				tempPrependURL = prependURL.substring(prependURL.indexOf(Constants.HTTPS_COLON_2_SLASHES) + 8);
			}
			tempValidURL = tempValidURL + tempPrependURL.substring(0, tempPrependURL.indexOf(Constants.FORWARD_SLASH) + 1);
			if(appendURL.startsWith(Constants.FORWARD_SLASH)) {
				validURL = tempValidURL + appendURL.substring(1);
			} else {
				validURL = tempValidURL + appendURL;
			}

		} else {
			validURL = getValidURL(prependURL, appendURL);
		}
		return validURL;
	}

	private String getValidURL(String prependURL, String appendURL) {
		String validURL = null;
		String tempPrependURL = null;
		if(prependURL.startsWith(Constants.HTTP_COLON_2_SLASHES)) {
			validURL = Constants.HTTP_COLON_2_SLASHES;
			tempPrependURL = prependURL.substring(prependURL.indexOf(Constants.HTTP_COLON_2_SLASHES) + 7);
		} else if(prependURL.startsWith(Constants.HTTPS_COLON_2_SLASHES)) {
			validURL = Constants.HTTPS_COLON_2_SLASHES;
			tempPrependURL = prependURL.substring(prependURL.indexOf(Constants.HTTPS_COLON_2_SLASHES) + 8);
		}
		validURL = validURL + tempPrependURL.substring(0, tempPrependURL.indexOf(Constants.FORWARD_SLASH) + 1);
		tempPrependURL = tempPrependURL.substring(tempPrependURL.indexOf(Constants.FORWARD_SLASH) + 1);
		String tempAppendURL = "";
		List<String> prependURLTokens = new ArrayList<String>(getURLTokens(tempPrependURL));
		List<String> appendURLTokens = new ArrayList<String>(getURLTokens(appendURL));
		if(prependURLTokens.size() > 0 && appendURLTokens.size() > 0) {
			if(prependURLTokens.get(0).length() == 0) {
				prependURLTokens.remove(0);
			}
			if(appendURLTokens.get(0).length() == 0) {
				appendURLTokens.remove(0);
			}
			if(((String) appendURLTokens.get(0)).equals(Constants.DOT_DOT)) {
				int tempSize = -1;
				while(((String) appendURLTokens.get(0)).equals(Constants.DOT_DOT)) {
					tempSize = prependURLTokens.size() - 1;
					prependURLTokens.remove(tempSize);
					appendURLTokens.remove(0);
				}
			} else {
				int commonIndex = 0;
				boolean commonPath = false;
				while((! commonPath) && (commonIndex < appendURLTokens.size() && (prependURLTokens.size() > 0 && commonIndex < prependURLTokens.size()))
					&& appendURLTokens.get(commonIndex).equalsIgnoreCase(prependURLTokens.get(commonIndex))) {
					commonPath = true;
					commonIndex ++;
				}
				if(commonPath) {
					int prependCount = prependURLTokens.size();
					int tempIndexCounter = commonIndex - 1;
					for(int tempIndex = (prependCount - 1); tempIndex >= tempIndexCounter; tempIndex --) {
						prependURLTokens.remove(tempIndex);
					}
				}
			}
		}
		if(prependURLTokens.size() > 0) {
			int tempCounter = 0;
			tempPrependURL = "";
			for(String token : prependURLTokens) {
				if(tempCounter == 0) {
	 				tempPrependURL = token;
				} else {
	 				tempPrependURL = tempPrependURL + Constants.FORWARD_SLASH + token;
				}
				tempCounter ++;
			}
			validURL += tempPrependURL;
			validURL += Constants.FORWARD_SLASH;
		}
		if(appendURLTokens.size() > 0) {
			int tempCounter = 0;
			for(String token : appendURLTokens) {
				if(tempCounter == 0) {
					tempAppendURL = token;
				} else {
					tempAppendURL = tempAppendURL + Constants.FORWARD_SLASH + token;
				}
				tempCounter ++;
			}
			validURL += tempAppendURL;
		}
		return validURL;
	}

	private List<String> getURLTokens(String url) {
		Pattern pattern = Pattern.compile("[/]");
		String[] tokens = pattern.split(url);
		return Arrays.asList(tokens);
	}

	private List<Element> getAnchorsForDocuments(List<Element> elements) {
		Attributes attrs = null;
		Attribute attr = null;
		String hrefValue = null;
		String fileExtension = null;
		Iterator<Attribute> itrAttr = null;
		List<Element> docAnchors = new ArrayList<Element>();
		List<String> validDocTypes = getValidDocTypesAsList();
		for(Element element : elements) {
			attrs = element.attributes();
			itrAttr = attrs.iterator();
			while(itrAttr.hasNext()) {
				attr = itrAttr.next();
				if(attr.getKey().equalsIgnoreCase(Constants.HREF)) {
					hrefValue = attr.getValue();
					if(hrefValue.contains(Constants.DOT)) {
						fileExtension = hrefValue.substring(hrefValue.lastIndexOf(Constants.DOT) + 1);
						if(validDocTypes.contains(fileExtension)) {
							docAnchors.add(element);
						}
						break;
					}
				}
			}
		}
		return docAnchors;
	}

	private List<String> getValidDocTypesAsList() {
		if(this.validDocTypes != null) {
			Pattern pattern = Pattern.compile("[,]");
			return getStringArrayAsList(pattern.split(this.validDocTypes));
		}
		return null;
	}

	private List<String> getStringArrayAsList(String[] array) {
		List<String> docTypes = null;
		if(array != null) {
			docTypes = new ArrayList<String>(array.length);
			for(int index = 0; index < array.length; index ++) {
				docTypes.add(array[index].trim());
			}
		}
		return docTypes;
	}

}
