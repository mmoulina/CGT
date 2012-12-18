package com.poplicus.crawler.codegen.definitions;

import com.poplicus.crawler.codegen.Constants;
import com.poplicus.crawler.codegen.Datum.DatumIdentifier;

public class DatumDefinition {

	private String uniqueName = null;

	private String fieldName = null;

	private String dataType = null;

	private String variableName = null;

	private String htmlSource = "html";

	private String startTag = null;

	private String endTag = null;

	private boolean entireString = false;

	private boolean afterTag = false;

	private boolean configurable = true;

	private DatumIdentifier datumIdentifier = null;

	public DatumDefinition(String fieldName, String dataType, String variableName, String htmlSource) {
		this.uniqueName = DatumNameGenerator.getInstance().getUniqueName();
		this.fieldName = fieldName;
		this.dataType = dataType;
		this.variableName = variableName;
		if(htmlSource != null && htmlSource.length() > 0) {
			this.htmlSource = htmlSource;
		}
	}

	public DatumDefinition(DatumIdentifier datumIdentifier) {
		this.uniqueName = DatumNameGenerator.getInstance().getUniqueName();
		this.datumIdentifier = datumIdentifier;
		switch (this.datumIdentifier) {
			case ProposalType:
				dataType = Constants.STRING;
				variableName = "proposalType";
				fieldName = "ProposalType";
				configurable = false;
			break;

			case GrantActionType:
				dataType = Constants.STRING;
				variableName = "grantActionType";
				fieldName = "GrantActionType";
			break;

			case GrantAssistanceType:
				dataType = Constants.STRING;
				variableName = "grantAssistanceType";
				fieldName = "GrantAssistanceType";
			break;

			case GrantRecordType:
				dataType = Constants.STRING;
				variableName = "grantRecordType";
				fieldName = "GrantRecordType";
			break;

			case SourceURL:
				dataType = Constants.STRING;
				variableName = "sourceURL";
				fieldName = "URL";
				configurable = false;
			break;

			case SourceName:
				dataType = Constants.STRING;
				variableName = "SOURCE_NAME";
				fieldName = "SourceName";
				configurable = false;
			break;

			case SiteName:
				dataType = Constants.STRING;
				variableName = "SITE_NAME";
				fieldName = "SiteName";
				configurable = false;
			break;

			case SourceID:
				dataType = Constants.STRING;
				variableName = "SOURCE_ID";
				fieldName = "SourceID";
				configurable = false;
			break;

			case ScanID:
				dataType = Constants.STRING;
				variableName = "_scanStatus.ScanID";
				fieldName = "ScanID";
				configurable = false;
			break;

			default:
			break;
		}
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getHtmlSource() {
		return htmlSource;
	}

	public void setHtmlSource(String htmlSource) {
		this.htmlSource = htmlSource;
	}

	public String getStartTag() {
		return startTag;
	}

	public void setStartTag(String startTag) {
		this.startTag = startTag;
	}

	public String getEndTag() {
		return endTag;
	}

	public void setEndTag(String endTag) {
		this.endTag = endTag;
	}

	public boolean isEntireString() {
		return entireString;
	}

	public void setEntireString(boolean entireString) {
		this.entireString = entireString;
	}

	public boolean isAfterTag() {
		return afterTag;
	}

	public void setAfterTag(boolean afterTag) {
		this.afterTag = afterTag;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}

	public DatumIdentifier getDatumIdentifier() {
		return datumIdentifier;
	}

	public void setDatumIdentifier(DatumIdentifier datumIdentifier) {
		this.datumIdentifier = datumIdentifier;
	}

	public boolean isConfigurable() {
		return configurable;
	}

	public void setConfigurable(boolean configurable) {
		this.configurable = configurable;
	}

	public Object[] toArray() {
		Object[] array = new Object[] {uniqueName, fieldName, dataType,
			variableName, htmlSource, startTag == null ? "" : startTag,
			endTag == null ? "" : endTag, entireString ? "Yes" : "No",
			afterTag ? "Yes" : "No"};

		return array;
	}

}
