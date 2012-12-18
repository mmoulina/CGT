package com.poplicus.crawler.codegen.docmgr;

public class IgnoreAnchor {
	
	public enum IgnoreCondition {
		DontIgnoreAny, ContainsAttribute
	}
	
	private IgnoreCondition ignoreCondition = IgnoreCondition.DontIgnoreAny;
	
	private String containsAttributeName = null;

	public IgnoreAnchor(IgnoreCondition ignoreCondition) {
		this.ignoreCondition = ignoreCondition;
	}
	
	public IgnoreCondition getIgnoreCondition() {
		return ignoreCondition;
	}

	public void setIgnoreCondition(IgnoreCondition ignoreCondition) {
		this.ignoreCondition = ignoreCondition;
	}
	
	public String getContainsAttributeName() {
		return this.containsAttributeName;
	}

	public void setContainsAttributeName(String containsAttributeName) {
		this.containsAttributeName = containsAttributeName;
	}

	public void validateIgnoreAnchor() throws IgnoreAnchorException {
		switch (ignoreCondition) {
			case ContainsAttribute:
				if( ! ( containsAttributeName != null && ( containsAttributeName.length() > 0 ) ) ) {
					throw new IgnoreAnchorException("Contains attribute is either null or empty for a IgnoreCondition.");
				}
			break;

			default:
			break;
		}
	}
	
}
