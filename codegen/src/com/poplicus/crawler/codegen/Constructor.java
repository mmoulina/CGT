package com.poplicus.crawler.codegen;

import java.util.ArrayList;
import java.util.List;

public class Constructor {
	
	private Indentation indentation = new Indentation(Indentation.INDENTATION_LEVEL_2);
	
	private String accessModifier = "public";
	
	private String className = "";
	
	private boolean defaultConstructor = false;
	
	private boolean superConstructor = false;
	
	private List<Variable> parameters = new ArrayList<Variable>();
	
	private List<Variable> superParameters = new ArrayList<Variable>();
	
	private boolean primaryBrowserDownloadScript = false;
	
	private boolean primaryBrowserDownloadImages = false; 
	
	private boolean secondaryBrowserDownloadScript = false;
	
	private boolean secondaryBrowserDownloadImages = false;
	
	private boolean tertiaryBrowserDownloadScript = false;
	
	private boolean tertiaryBrowserDownloadImages = false;
	
	/**
	 * 
	 * @param accessModifier
	 * @param className
	 */
	public Constructor(String accessModifier, String className) {
		if(accessModifier != null) {
			this.accessModifier = accessModifier;
		}
		this.className = className;
	}

	/**
	 * 
	 * @param accessModifier
	 * @param className
	 * @param defaultConstructor
	 * @param superConstructor
	 */
	public Constructor(String accessModifier, String className, boolean defaultConstructor, boolean superConstructor) {
		if(accessModifier != null) {
			this.accessModifier = accessModifier;
		}
		this.className = className;
		this.defaultConstructor = defaultConstructor;
		this.superConstructor = superConstructor;
	}

	public boolean isSuperConstructor() {
		return superConstructor;
	}

	public void setSuperConstructor(boolean superConstructor) {
		this.superConstructor = superConstructor;
	}

	public boolean isDefaultConstructor() {
		return defaultConstructor;
	}

	public void setDefaultConstructor(boolean defaultConstructor) {
		this.defaultConstructor = defaultConstructor;
	}

	public String getAccessModifier() {
		return accessModifier;
	}

	public void setAccessModifier(String accessModifier) {
		this.accessModifier = accessModifier;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
	public void addParameters(Variable variable) {
		this.parameters.add(variable);
	}
	
	public void addSuperParameters(Variable variable) {
		this.superParameters.add(variable);
	}
	
	public boolean isPrimaryBrowserDownloadScript() {
		return primaryBrowserDownloadScript;
	}

	public void setPrimaryBrowserDownloadScript(boolean primaryBrowserDownloadScript) {
		this.primaryBrowserDownloadScript = primaryBrowserDownloadScript;
	}

	public boolean isPrimaryBrowserDownloadImages() {
		return primaryBrowserDownloadImages;
	}

	public void setPrimaryBrowserDownloadImages(boolean primaryBrowserDownloadImages) {
		this.primaryBrowserDownloadImages = primaryBrowserDownloadImages;
	}

	public boolean isSecondaryBrowserDownloadScript() {
		return secondaryBrowserDownloadScript;
	}

	public void setSecondaryBrowserDownloadScript(
			boolean secondaryBrowserDownloadScript) {
		this.secondaryBrowserDownloadScript = secondaryBrowserDownloadScript;
	}

	public boolean isSecondaryBrowserDownloadImages() {
		return secondaryBrowserDownloadImages;
	}

	public void setSecondaryBrowserDownloadImages(
			boolean secondaryBrowserDownloadImages) {
		this.secondaryBrowserDownloadImages = secondaryBrowserDownloadImages;
	}
	
	public boolean isTertiaryBrowserDownloadScript() {
		return tertiaryBrowserDownloadScript;
	}

	public void setTertiaryBrowserDownloadScript(
			boolean tertiaryBrowserDownloadScript) {
		this.tertiaryBrowserDownloadScript = tertiaryBrowserDownloadScript;
	}

	public boolean isTertiaryBrowserDownloadImages() {
		return tertiaryBrowserDownloadImages;
	}

	public void setTertiaryBrowserDownloadImages(
			boolean tertiaryBrowserDownloadImages) {
		this.tertiaryBrowserDownloadImages = tertiaryBrowserDownloadImages;
	}
	
	public void setIndentationLevel(int level) {
		this.indentation.setLevel(level);
	}
	
	public String toStringBuffer() {
		int noOfParams = 1;
		int noOfSuperParams = 1;
		StringBuffer code = new StringBuffer();
		code.append("\n");
		code.append(indentation.getTabs());
		code.append(this.accessModifier).append(Constants.SPACE).append(this.className).append(Constants.OPEN_PARENTHESIS);
		for(Variable var : parameters) {
			if(noOfParams == 1) {
				code.append(var.toStringBuffer());
			} else {
				code.append(Constants.COMMA).append(Constants.SPACE).append(var.toStringBuffer());			
			}
			noOfParams++;
		}
		
		code.append(Constants.CLOSE_PARENTHESIS);
		
		if(superConstructor) {
			code.append(Constants.SPACE).append(Constants.COLON).append(Constants.SPACE).append(Constants.BASE);
			code.append(Constants.OPEN_PARENTHESIS);
			for(Variable superVar : superParameters) {
				if(noOfSuperParams == 1) {					
					code.append(superVar.getName());
				} else {
					code.append(Constants.COMMA).append(Constants.SPACE).append(superVar.getName());			
				}
				noOfSuperParams++;
			}
			//passing UserInput to super constructor, hard coding since this variable has to be passed from all crawlers
			//code.append(Constants.COMMA).append(Constants.SPACE).append(Constants.USER_INPUT);	
			code.append(Constants.CLOSE_PARENTHESIS);
		}
		
		//opening the constructor
		code.append(Constants.NEW_LINE).append(indentation.getTabs()).append("{");

		if(defaultConstructor) {
			code.append(getDefaultConstructorCodeSnippet());
		}

		//closing the constructor
		code.append(Constants.NEW_LINE).append(indentation.getTabs()).append("}");
		
		return code.toString();
	}
	
	public String getDefaultConstructorCodeSnippet() {
		StringBuffer buff = new StringBuffer();
		
		//tomorrow if we are planning to use any other crawler, we can put a switch here.
		getFicstarDefaultConstructorCodeSnippet(buff);
		
		return buff.toString();
	}
	
	private StringBuffer getFicstarDefaultConstructorCodeSnippet(StringBuffer buff) {
		buff.append(Constants.NEW_LINE).append(Constants.THREE_TABS).append("LoadSourceMapConfig(\"ProposalType\");");
		
		//we need to find out what is the most commonly used code across crawlers and try to generalize it and add that code dynamically - start
		if(primaryBrowserDownloadImages) {
			buff.append(Constants.NEW_LINE).append(Constants.THREE_TABS).append("_primaryBrowser.DownloadImages = true;");
		}
		
		if(primaryBrowserDownloadScript) {
			buff.append(Constants.NEW_LINE).append(Constants.THREE_TABS).append("_primaryBrowser.DownloadScripts = true;");
		}
		
		if(secondaryBrowserDownloadImages) {
			buff.append(Constants.NEW_LINE).append(Constants.THREE_TABS).append("_secondaryBrowser.DownloadImages = true;");
		}
		
		if(secondaryBrowserDownloadScript) {
			buff.append(Constants.NEW_LINE).append(Constants.THREE_TABS).append("_secondaryBrowser.DownloadScripts = true;");
		}
		
		if(tertiaryBrowserDownloadImages) {
			buff.append(Constants.NEW_LINE).append(Constants.THREE_TABS).append("_tertiaryBrowser.DownloadImages = true;");
		}
		
		if(tertiaryBrowserDownloadScript) {
			buff.append(Constants.NEW_LINE).append(Constants.THREE_TABS).append("_tertiaryBrowser.DownloadScripts = true;");
		}
		//we need to find out what is the most commonly used code across crawlers and try to generalize it and add that code dynamically - end

		return buff;
	}

}
