package com.poplicus.crawler.codegen.definitions;

import java.util.ArrayList;
import java.util.List;

public class CodeBlockDefinition implements IfElseElementsDef, MainIfElementsDef {

	private List<CodeBlockElementsDef> codeBlockElements = new ArrayList<CodeBlockElementsDef>();
	
	public void setCodeBlockElement(CodeBlockElementsDef codeBlockElement) {
		this.codeBlockElements.add(codeBlockElement);
	}
	
	public List<CodeBlockElementsDef> getCodeBlockElements() {
		return this.codeBlockElements;
	}
	
	public String toString() {
		StringBuffer sbf = new StringBuffer();
		for(int index = 0; index < codeBlockElements.size(); index ++) {
			if(index > 0) {
				sbf.append(", ");
			}
			sbf.append("[").append(codeBlockElements.get(index).toString()).append("]");
		}
		return sbf.toString();
	}
	
}
 