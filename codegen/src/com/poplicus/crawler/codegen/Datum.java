package com.poplicus.crawler.codegen;

public class Datum {
	
	public enum TextFunction {
		GetTextStringBetweenTags {
			public String toString() {
				return  "GetTextStringBetweenTags";
			}
		}, 
		TextBetweenTags {
			public String toString() {
				return  "TextBetweenTags";
			}
		}, 
		TextBeforeTag {
			public String toString() {
				return  "TextBeforeTag";
			}
		}, 
		TextAfterTag {
			public String toString() {
				return  "TextAfterTag";
			}
		}
	}
	
	public enum DatumIdentifier {
		Normal, ProposalType, SourceURL, SourceName, SiteName, SourceID, ScanID, GrantActionType, GrantAssistanceType, GrantRecordType
	}
	
	private String name = null;
	
	private String type = "string";
	
	/** startTag will/should be populated only through constructor.*/
	private String startTag = null;
	
	/** endTag will/should be populated only through constructor.*/
	private String endTag = null;
	
	//private String value = null;
	
	private TextFunction textFunction = null;
	
	private Object htmlSource = null;
	
	private DatumIdentifier datumIdentifier = null;
	
	private String columnName = null;
	
//	/** <code>industryType</code>, to be used only in case of inserting industry types/code. Couldn't think of a better solution that this.
//	 *  May be in future, will revisit this.
//	 */
//	private String industryType = null;
//	
//	public String getIndustryType() {
//		return industryType;
//	}
//
//	public void setIndustryType(String industryType) {
//		this.industryType = industryType;
//	}

	public Object getHtmlSource() {
		return htmlSource;
	}

	public Datum(DatumIdentifier datumIdentifier) {
		this.datumIdentifier = datumIdentifier;
		switch (this.datumIdentifier) {
			case ProposalType:
				type = Constants.STRING;
				name = "proposalType";
				columnName = "ProposalType";
			break;
			
			case GrantActionType:
				type = Constants.STRING;
				name = "grantActionType";
				columnName = "GrantActionType";
			break;
			
			case GrantAssistanceType:
				type = Constants.STRING;
				name = "grantAssistanceType";
				columnName = "GrantAssistanceType";
			break;
			
			case GrantRecordType:
				type = Constants.STRING;
				name = "grantRecordType";
				columnName = "GrantRecordType";
			break;
			
			case SourceURL:
				type = Constants.STRING;
				name = "sourceURL";
				columnName = "URL";
			break;

			case SourceName:
				type = Constants.STRING;
				name = "SOURCE_NAME";
				columnName = "SourceName";
			break;
			
			case SiteName:
				type = Constants.STRING;
				name = "SITE_NAME";
				columnName = "SiteName";
			break;
			
			case SourceID:
				type = Constants.STRING;
				name = "SOURCE_ID";
				columnName = "SourceID";
			break;
			
			case ScanID:
				type = Constants.STRING;
				name = "_scanStatus.ScanID";
				columnName = "ScanID";
			break;
		
			default:
			break;
		}
	}
	
	public String getType() {
		return this.type;
	}
	
	public DatumIdentifier getDatumIdentifier() {
		return datumIdentifier;
	}
	
	/**
	 * 
	 * @param htmlSource
	 * @param startTag
	 * @param endTag
	 * @param type
	 * @param name
	 * @param columnName
	 * @param isWholeText
	 * @param isAfterTag
	 * @throws DatumException
	 */
	public Datum(Object htmlSource, String startTag, String endTag, String type, String name, 
									String columnName, boolean isWholeText, boolean isAfterTag) throws DatumException {
		if(startTag != null) {
			this.startTag = startTag;
			if(endTag != null) {
				this.endTag = endTag;
				if(isWholeText) {
					textFunction = TextFunction.TextBetweenTags;
				} else {
					textFunction = TextFunction.GetTextStringBetweenTags;
				}
			} else {
				if(isAfterTag) {
					textFunction = TextFunction.TextAfterTag;
				} else {
					textFunction = TextFunction.TextBeforeTag;
				}
			}
		} else {
			if(endTag != null) {
				this.endTag = endTag;
				if(isAfterTag) {
					textFunction = TextFunction.TextAfterTag;
				} else {
					textFunction = TextFunction.TextBeforeTag;
				}
			}
		}
		if(type == null) {
			throw new DatumException("One of the Datum has null value for type.");
		}	
		this.type = type;

		if(name == null) {
			if(htmlSource instanceof Datum) {
				this.name = ((Datum) htmlSource).getName();
			} else {
				this.name = Constants.HTML;
			}
		} else {
			this.name = name;
		}
		this.htmlSource = htmlSource;
		this.datumIdentifier = DatumIdentifier.Normal;
		this.columnName = columnName;
	}
	
	public StringBuffer toStringBuffer() {
		StringBuffer code = new StringBuffer();
		code.append(this.name).append(Constants.SPACE).append(Constants.EQUALS).append(Constants.SPACE);
		if(getFunctionName() != null && getFunctionName().length() > 0) {
			code.append(Constants.TEXT).append(Constants.DOT).append(getFunctionName()).append(Constants.SEMI_COLON);
		} else {
			code.append(Constants.DOUBLE_QUOTE).append(Constants.DOUBLE_QUOTE).append(Constants.SEMI_COLON).append(Constants.LINE_OF_CODE_IMPLEMENTATION_WARNING);
		}

		return code;
	}
	
	public String getName() {
		return name;
	}
	
	public Object getSourceAttributeName() {
		if(this.getHtmlSource() instanceof Datum) {
			return ((Datum)this.getHtmlSource()).getName();
		} else {
			return this.htmlSource;
		}
	}
	
	public String getColumnName() {
		return this.columnName;
	}
	
	public StringBuffer getFunctionName() {
		StringBuffer funcName = new StringBuffer();
		if(textFunction == null) {
			return funcName;
		}
		switch (this.textFunction) {
			case GetTextStringBetweenTags:
				funcName.append(this.textFunction).append(Constants.OPEN_PARENTHESIS).append(this.getSourceAttributeName());
				funcName.append(Constants.COMMA).append(Constants.SPACE).append(Constants.DOUBLE_QUOTE).append(startTag);
				funcName.append(Constants.DOUBLE_QUOTE).append(Constants.COMMA).append(Constants.SPACE);
				funcName.append(Constants.DOUBLE_QUOTE).append(endTag).append(Constants.DOUBLE_QUOTE).append(Constants.CLOSE_PARENTHESIS);
			break;
	
			case TextBetweenTags:
				funcName.append(this.textFunction).append(Constants.OPEN_PARENTHESIS).append(this.getSourceAttributeName());
				funcName.append(Constants.COMMA).append(Constants.SPACE).append(Constants.DOUBLE_QUOTE).append(startTag);
				funcName.append(Constants.DOUBLE_QUOTE).append(Constants.COMMA).append(Constants.SPACE);
				funcName.append(Constants.DOUBLE_QUOTE).append(endTag).append(Constants.DOUBLE_QUOTE).append(Constants.CLOSE_PARENTHESIS);
			break;
			
			case TextBeforeTag:
				funcName.append(this.textFunction).append(Constants.OPEN_PARENTHESIS).append(this.getSourceAttributeName());
				funcName.append(Constants.COMMA).append(Constants.SPACE);
				if(startTag != null) {
					funcName.append(Constants.DOUBLE_QUOTE);
					funcName.append(startTag);
					funcName.append(Constants.DOUBLE_QUOTE);
				} else {
					if(endTag != null) {
						funcName.append(Constants.DOUBLE_QUOTE);
						funcName.append(endTag);
						funcName.append(Constants.DOUBLE_QUOTE);
					}
				}
				funcName.append(Constants.CLOSE_PARENTHESIS);
			break;
			
			case TextAfterTag:
				funcName.append(this.textFunction).append(Constants.OPEN_PARENTHESIS).append(this.getSourceAttributeName());
				funcName.append(Constants.COMMA).append(Constants.SPACE);
				if(startTag != null) {
					funcName.append(Constants.DOUBLE_QUOTE);
					funcName.append(startTag);
					funcName.append(Constants.DOUBLE_QUOTE);
				} else {
					if(endTag != null) {
						funcName.append(Constants.DOUBLE_QUOTE);
						funcName.append(endTag);
						funcName.append(Constants.DOUBLE_QUOTE);
					}
				}
				funcName.append(Constants.CLOSE_PARENTHESIS);
			break;
			
			default:
			break;
		}
		return funcName;
	}

}
