package com.poplicus.crawler.codegen;

import java.util.ArrayList;
import java.util.List;

import com.poplicus.crawler.codegen.ArgumentValueDefinition.ArgumentValueType;
import com.poplicus.crawler.codegen.WebPage.BrowserType;

public class Action {
	
	private Indentation indentation = new Indentation(Indentation.INDENTATION_LEVEL_4);
	
	//do we really need ActionType?? Need to take a call on this based on final code outcome.
	public enum ActionType {
		ClickAnElement, LinkClick, URLNavigation, CollectResultLinks, ParseResult, SelectOptionItemWithOnChange, StartTimer, AutomationTask_PerformEnterData, SetNextFileDownloadPathAndName, LocationUrl, FunctionCall
	}
	
	public enum ElementTagType {
		Image, Anchor, Input, Button
	}
	
	public enum ElementFieldType {
		InnerHTML, Value, Name, Source, Type, HREF, ID, ClassName, CheckedAttribute, InnerText
	}

	private ActionType actionType = null;
	
	private BrowserType browserType = null;
	
	private ElementTagType elemTagType = null;
	
	private ElementFieldType elemFieldType = null;
	
	private String matchingValue = null;
	
	private String navigationURL = null;
	
	private String webPageName = null;
	
	private boolean exactMatch = false;
	
	private String packageName = null;
	
	private String className = null;
	
	private String methodName = null;
	
	private String supportingMethodName = null;
	
	private List<ArgumentValueDefinition> argValueDefs = new ArrayList<ArgumentValueDefinition>();
	
	private List<ArgumentValueDefinition> supportingArgValueDefs = new ArrayList<ArgumentValueDefinition>();
	
	/**
	 * 
	 * @param actionType
	 * @param browserType
	 */
	public Action(ActionType actionType, BrowserType browserType) {
		this.actionType = actionType;
		this.browserType = browserType;
	}
	
	/**
	 * 
	 * @param actionType
	 * @param browserType
	 * @param elemTagType
	 * @param elemFieldType
	 * @param matchingValue
	 * @param exactMatch
	 */
	public Action(ActionType actionType, BrowserType browserType, ElementTagType elemTagType, 
			      ElementFieldType elemFieldType, String matchingValue, boolean exactMatch) {
		this.actionType = actionType;
		this.browserType = browserType;
		this.elemTagType = elemTagType;
		this.elemFieldType = elemFieldType;
		this.matchingValue = matchingValue;
		this.exactMatch = exactMatch;
	}
	
	/**
	 * 
	 * @param actionType
	 * @param browserType
	 * @param navigationURL
	 */
	public Action(ActionType actionType, BrowserType browserType, String navigationURL) {
		this.actionType = actionType;
		this.browserType = browserType;
		this.navigationURL = navigationURL;
	}
	
	public String getNavigationURL() {
		return navigationURL;
	}

	public void setNavigationURL(String navigationURL) {
		this.navigationURL = navigationURL;
	}
	
	public BrowserType getBrowserType() {
		return browserType;
	}

	public void setBrowserType(BrowserType browserType) {
		this.browserType = browserType;
	}

	public String getMatchingValue() {
		return matchingValue;
	}

	public void setMatchingValue(String matchingValue) {
		this.matchingValue = matchingValue;
	}

	public boolean isExactMatch() {
		return exactMatch;
	}

	public void setExactMatch(boolean exactMatch) {
		this.exactMatch = exactMatch;
	}
	
	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}
	
	public ElementTagType getElemTagType() {
		return elemTagType;
	}

	public void setElemTagType(ElementTagType elemTagType) {
		this.elemTagType = elemTagType;
	}

	public ElementFieldType getElemFieldType() {
		return elemFieldType;
	}

	public void setElemFieldType(ElementFieldType elemFieldType) {
		this.elemFieldType = elemFieldType;
	}

	public String getWebPageName() {
		return webPageName;
	}

	public void setWebPageName(String webPageName) {
		this.webPageName = webPageName;
	}
	
	public void addArgumentValueDefinition(ArgumentValueDefinition argValDef) {
		this.argValueDefs.add(argValDef);
	}
	
	public List<ArgumentValueDefinition> getArgumentValueDefinitions() {
		return this.argValueDefs;
	}
	
	public void addSupportingMethodArgumentValueDefinition(ArgumentValueDefinition argValDef) {
		this.supportingArgValueDefs.add(argValDef);
	}
	
	public List<ArgumentValueDefinition> getSupportingMethodArgumentValueDefinitions() {
		return this.supportingArgValueDefs;
	}
 	
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public void setSupportingMethodName(String supportingMethodName) {
		this.supportingMethodName = supportingMethodName;
	}
	
	public String getSupportingMethodName() {
		return this.supportingMethodName;
	}
	
	public void setIndentationLevel(int level) {
		this.indentation.setLevel(level);
	}

	public StringBuffer toStringBuffer() throws ActionDefinitionException, ClassDefinitionException, ArgumentValueDefinitionException {
		StringBuffer code = null;
		switch (actionType) {
			case ClickAnElement:
				code = getBrowserHandler();
				code.append(Constants.DOT).append(Constants.CLICK_ELEMENT).append(Constants.OPEN_PARENTHESIS);
				code.append(getElementTagTypeCodeSnippet()).append(Constants.COMMA).append(Constants.SPACE);
				code.append(getElementFieldTypeCodeSnippet()).append(Constants.COMMA).append(Constants.SPACE);
				code.append(Constants.DOUBLE_QUOTE).append(matchingValue.toString().replace("\"", "\\\"")).append(Constants.DOUBLE_QUOTE);
				code.append(Constants.COMMA).append(Constants.SPACE).append(exactMatch).append(Constants.CLOSE_PARENTHESIS).append(Constants.SEMI_COLON);
			break;
			
			case LinkClick:
			
			break;
			
			case URLNavigation:
				code = getBrowserHandler();
				code.append(Constants.DOT).append(Constants.NAVIGATE).append(Constants.OPEN_PARENTHESIS).append(Constants.DOUBLE_QUOTE).append(navigationURL);
				code.append(Constants.DOUBLE_QUOTE).append(Constants.CLOSE_PARENTHESIS).append(Constants.SEMI_COLON);
			break;
			
			case CollectResultLinks:
				code = new StringBuffer(Constants.COLLECT_RESULT_LINKS);
				if(webPageName != null) {
					code.append(Constants.FROM).append(webPageName);
				}
				code.append(Constants.OPEN_PARENTHESIS).append(getBrowserInnerHTMLHandler()).append(Constants.CLOSE_PARENTHESIS).append(Constants.SEMI_COLON);
				code.append(Constants.NEW_LINE).append(indentation.getTabs()).append(Constants.NEXT_ACTION).append(Constants.OPEN_PARENTHESIS);
				code.append(Constants.CLOSE_PARENTHESIS).append(Constants.SEMI_COLON);
			break;
			
			case ParseResult:
				code = new StringBuffer(Constants.PARSE_RESULTS);
				if(webPageName != null) {
					code.append(Constants.FROM).append(webPageName);
				}
				code.append(Constants.OPEN_PARENTHESIS);
				code.append(getBrowserInnerHTMLHandler()).append(Constants.COMMA).append(Constants.SPACE);
				code.append(getBrowserInnerHTMLHandler()).append(Constants.CLOSE_PARENTHESIS).append(Constants.SEMI_COLON);
				code.append(Constants.NEW_LINE).append(indentation.getTabs()).append(Constants.NEXT_ACTION).append(Constants.OPEN_PARENTHESIS);
				code.append(Constants.CLOSE_PARENTHESIS).append(Constants.SEMI_COLON);
			break;
			
			case SelectOptionItemWithOnChange:
				if((argValueDefs == null) || (argValueDefs.size() <= 0)) {
					throw new ActionDefinitionException("Argument value definitions for csExWB.cEXWB.SelectOptionItemWithOnChange method are missing.");
				}
				code = getBrowserHandler();
				code.append(Constants.DOT).append(Constants.SELECT_OPTION_ITEM_WITH_ON_CHANGE).append(Constants.OPEN_PARENTHESIS);
				code.append(getArguments()).append(Constants.CLOSE_PARENTHESIS).append(Constants.SEMI_COLON);
				
			break;
			
            case StartTimer:				
				code = getBrowserTimerHandler();
				code.append(Constants.DOT).append(Constants.START).append(Constants.OPEN_PARENTHESIS);
				code.append(Constants.CLOSE_PARENTHESIS).append(Constants.SEMI_COLON);
				
			break;
			
            case AutomationTask_PerformEnterData:				
   				code = getBrowserHandler();
   				code.append(Constants.DOT).append(Constants.AUTOMATION_TASK_PERFORM_ENTER_DATA).append(Constants.OPEN_PARENTHESIS);
				code.append(getArguments()).append(Constants.CLOSE_PARENTHESIS).append(Constants.SEMI_COLON);
				
		    break;
				
            case SetNextFileDownloadPathAndName:				
   				code = getBrowserHandler();
   				code.append(Constants.DOT).append(Constants.SET_NEXT_FILE_DOWNLOAD_PATH_AND_NAME).append(Constants.OPEN_PARENTHESIS);
				code.append(getArguments()).append(Constants.CLOSE_PARENTHESIS).append(Constants.SEMI_COLON);
				
		    break;
		    
            case LocationUrl:				
   				code = getBrowserHandler();
   				code.append(Constants.DOT).append(Constants.LOCATION_URL).append(Constants.OPEN_PARENTHESIS);
				code.append(getArguments()).append(Constants.CLOSE_PARENTHESIS).append(Constants.SEMI_COLON);
				
		    break;
		    
			case FunctionCall:
				if((argValueDefs == null) || (argValueDefs.size() <= 0)) {
					throw new ActionDefinitionException("Argument value definitions for csExWB.cEXWB.SelectOptionItemWithOnChange method are missing.");
				}
				code = getBrowserHandler();
				code.append(Constants.DOT).append(methodName).append(Constants.OPEN_PARENTHESIS);
				code.append(getArguments()).append(Constants.CLOSE_PARENTHESIS);
				if(supportingMethodName != null) {
					code.append(Constants.DOT).append(supportingMethodName).append(Constants.OPEN_PARENTHESIS);
					if(getSupportingMethodArgumentValueDefinitions().size() > 0) {
						code.append(getSupportingArguments());
					} 
					code.append(Constants.CLOSE_PARENTHESIS);
				}
				code.append(Constants.SEMI_COLON);
				
			break;

			default:
				//do nothing  
			break;
		}
		return code;
	}
	
	public ArgumentValueDefinition getArgumentValueDefinition(String argumentName) {
		for(ArgumentValueDefinition argValDef : argValueDefs) {
			if(argValDef.getArgumentName().equals(argumentName)) {
				return argValDef;
			}
		}
		return null;
	}
	
	public ArgumentValueDefinition getSupportingMethodArgumentValueDefinition(String argumentName) {
		for(ArgumentValueDefinition argValDef : supportingArgValueDefs) {
			if(argValDef.getArgumentName().equals(argumentName)) {
				return argValDef;
			}
		}
		return null;
	}

	private StringBuffer getArguments() throws ClassDefinitionException, ArgumentValueDefinitionException {
		StringBuffer code = new StringBuffer();
		ArgumentDefinition argDef = null;
		ArgumentValueDefinition argValDef = null;
		List<ArgumentDefinition> masterArgDefs = ClassDefinitions.getArgumentDefinitions(packageName, className, null, methodName);
		int noOfArgs = masterArgDefs.size();
		if(argValueDefs.size() != noOfArgs) {
			throw new ArgumentValueDefinitionException("Number of arguments passed doesn't match number of arguments defined for this class.method.");
		}

		//adding arguments to method
		for(int argIndex = 0; argIndex < noOfArgs; argIndex ++) {
			if(argIndex > 0) {
				code.append(Constants.COMMA).append(Constants.SPACE);
			}
			argDef = masterArgDefs.get(argIndex);
			
			argValDef = this.getArgumentValueDefinition(argDef.getArgumentName());
			if(argValDef == null) {
				throw new ArgumentValueDefinitionException("One or more required ArgumentValueDefinitions are missing in RHSDefinition.");
			}
			
			if(argValDef.getArgValueType().equals(ArgumentValueType.PassByReference)) {
				code.append(argValDef.getValue());
			} else if(argValDef.getArgValueType().equals(ArgumentValueType.PassByValue)) {
				code.append(Constants.DOUBLE_QUOTE).append(argValDef.getValue()).append(Constants.DOUBLE_QUOTE);
			}
		}
		return code;
	}
	
	private StringBuffer getSupportingArguments() throws ClassDefinitionException, ArgumentValueDefinitionException {
		StringBuffer code = new StringBuffer();
		ArgumentDefinition argDef = null;
		ArgumentValueDefinition argValDef = null;
		MethodDefinition methodDef = ClassDefinitions.getMethodDefinition(packageName, className, null, methodName);
		MethodDefinition supportingMethodDef = methodDef.getSupportingMethodDefinition(supportingMethodName);
		List<ArgumentDefinition> masterArgDefs = supportingMethodDef.getArgumentDefinitions();
		int noOfArgs = masterArgDefs.size();
		if(argValueDefs.size() != noOfArgs) {
			throw new ArgumentValueDefinitionException("Number of arguments passed doesn't match number of arguments defined for this class.method.");
		}

		//adding arguments to method
		for(int argIndex = 0; argIndex < noOfArgs; argIndex ++) {
			if(argIndex > 0) {
				code.append(Constants.COMMA).append(Constants.SPACE);
			}
			argDef = masterArgDefs.get(argIndex);
			
			argValDef = this.getSupportingMethodArgumentValueDefinition(argDef.getArgumentName());
			if(argValDef == null) {
				throw new ArgumentValueDefinitionException("One or more required ArgumentValueDefinitions are missing in RHSDefinition.");
			}
			
			if(argValDef.getArgValueType().equals(ArgumentValueType.PassByReference)) {
				code.append(argValDef.getValue());
			} else if(argValDef.getArgValueType().equals(ArgumentValueType.PassByValue)) {
				code.append(Constants.DOUBLE_QUOTE).append(argValDef.getValue()).append(Constants.DOUBLE_QUOTE);
			}
		}
		return code;
	}
	
	private StringBuffer getBrowserInnerHTMLHandler() {
		StringBuffer code = new StringBuffer("");
		switch (browserType) {
			case Primary:
				code.append("primaryBrowserHTML");
			break;
			
			case Secondary:
				code.append("secondaryBrowserHTML");				
			break;
			
			case Tertiary:
				code.append("tertiaryBrowserHTML");
			break;
			
			default:
				//do nothing
			break;
		}
		return code;
	}

	private StringBuffer getBrowserHandler() {
		StringBuffer code = new StringBuffer();
		switch (browserType) {
			case Primary:
				code.append("_primaryBrowser");
			break;
			
			case Secondary:
				code.append("_secondaryBrowser");
			break;
			
			case Tertiary:
				code.append("_tertiaryBrowser");
			break;

			default:
				//do nothing
			break;
		}
		return code;
	}
	
	private StringBuffer getBrowserTimerHandler() {
		StringBuffer code = new StringBuffer();
		switch (browserType) {
			case Primary:
				code.append("_primaryBrowserTimer");
			break;
			
			case Secondary:
				code.append("_secondaryBrowserTimer");
			break;
			
			case Tertiary:
				code.append("_tertiaryBrowserTimer");
			break;	
			
			default:
				//do nothing
			break;
		}
		return code;
	}
	
	private StringBuffer getElementTagTypeCodeSnippet() {
		StringBuffer code = new StringBuffer();
		switch (elemTagType) {
			case Image:
				code.append("BrowserControlLibrary.ElementTagType.img");
			break;
			
			case Anchor:
				code.append("BrowserControlLibrary.ElementTagType.a");
			break;
			
			case Input:
				code.append("BrowserControlLibrary.ElementTagType.input");
			break;
			
			case Button:
				code.append("BrowserControlLibrary.ElementTagType.button");
			break;

			default:
				//do nothing
			break;
		}
		return code;
	}
	
	private StringBuffer getElementFieldTypeCodeSnippet() {
		StringBuffer code = new StringBuffer();
		switch (elemFieldType) {
			case InnerHTML:
				code.append("BrowserControlLibrary.ElementFieldType.innerHTML");
			break;

			case Value:
				code.append("BrowserControlLibrary.ElementFieldType.value");
			break;
			
			case Name:
				code.append("BrowserControlLibrary.ElementFieldType.name");
			break;
			
			case Source:
				code.append("BrowserControlLibrary.ElementFieldType.src");
			break;
			
			case Type:
				code.append("BrowserControlLibrary.ElementFieldType.type");
			break;
			
			case HREF:
				code.append("BrowserControlLibrary.ElementFieldType.href");
			break;
			
			case ID:
				code.append("BrowserControlLibrary.ElementFieldType.id");
			break;
			
			case ClassName:
				code.append("BrowserControlLibrary.ElementFieldType.className");
			break;
			
			case CheckedAttribute:
				code.append("BrowserControlLibrary.ElementFieldType.checkeda");
			break;
			
			case InnerText:
				code.append("BrowserControlLibrary.ElementFieldType.innerText");
			break;
			
			default:
				//do nothing
			break;
		}
		return code;
	}
	
}
