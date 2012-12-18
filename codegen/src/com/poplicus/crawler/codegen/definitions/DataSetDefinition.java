package com.poplicus.crawler.codegen.definitions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSetDefinition {

	private String tableName = null;
	
	private Map<String,String> additionalDataByReference = new HashMap<String,String>();
	
	private Map<String,String> additionalDataByValue = new HashMap<String,String>();
	
	private String additionalInformation = null;
	
	private List<DatumDefinition> data = new ArrayList<DatumDefinition>();
	
//	private List<DatumDefinition> bidData = new ArrayList<DatumDefinition>();
//	
//	private List<DatumDefinition> bidIndustryData = new ArrayList<DatumDefinition>();
//	
//	private List<DatumDefinition> bidLocationData = new ArrayList<DatumDefinition>();
//	
//	private List<DatumDefinition> bidContactData = new ArrayList<DatumDefinition>();
//	
//	private List<DatumDefinition> bidSetAsideData = new ArrayList<DatumDefinition>();
//	
//	private List<DatumDefinition> bidDocumentData = new ArrayList<DatumDefinition>();
//	
//	private List<DatumDefinition> bidGrantData = new ArrayList<DatumDefinition>();
//	
//	private List<DatumDefinition> bidVendorData = new ArrayList<DatumDefinition>();
//	
//	private List<DatumDefinition> bidVendorAgencyIdData = new ArrayList<DatumDefinition>();
//	
//	private List<DatumDefinition> bidVendorAgencyOrgData = new ArrayList<DatumDefinition>();

	public DataSetDefinition(String tableName, String additionalInformation) {
		this.tableName = tableName;
		this.additionalInformation = additionalInformation;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Map<String, String> getAdditionalDataByReference() {
		return additionalDataByReference;
	}

	public Map<String, String> getAdditionalDataByValue() {
		return additionalDataByValue;
	}

	public void addAdditionalData(String key, String value, boolean passByValue) {
		if(passByValue) {
			this.additionalDataByValue.put(key, value);	
		} else {
			this.additionalDataByReference.put(key, value);
		}
	}
	
	public void addAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}
	
	public String getAdditionalInformation() {
		return this.additionalInformation;
	}
	
	public void addDatum(DatumDefinition bidDatum) {
		this.data.add(bidDatum);
	}
	
	public void setData(List<DatumDefinition> bidData) {
		this.data = bidData;
	}
	
	public List<DatumDefinition> getData() {
		return this.data;
	}

	
//	public void addBidDatum(DatumDefinition bidDatum) {
//		this.bidData.add(bidDatum);
//	}
//	
//	public void setBidData(List<DatumDefinition> bidData) {
//		this.bidData = bidData;
//	}
//	
//	public List<DatumDefinition> getBidData() {
//		return this.bidData;
//	}
//	
//	public void addBidIndustryDatum(DatumDefinition bidIndustryDatum) {
//		this.bidIndustryData.add(bidIndustryDatum);
//	}
//	
//	public void setBidIndustryData(List<DatumDefinition> bidIndustryData) {
//		this.bidIndustryData = bidIndustryData;
//	}
//	
//	public List<DatumDefinition> getBidIndustryData() {
//		return this.bidIndustryData;
//	}
//	
//	public void addBidLocationDatum(DatumDefinition bidLocationDatum) {
//		this.bidLocationData.add(bidLocationDatum);
//	}
//	
//	public void setBidLocationData(List<DatumDefinition> bidLocationData) {
//		this.bidLocationData = bidLocationData;
//	}
//	
//	public List<DatumDefinition> getBidLocationData() {
//		return this.bidLocationData;
//	}
//	
//	public void addBidContactDatum(DatumDefinition bidContactDatum) {
//		this.bidContactData.add(bidContactDatum);
//	}
//	
//	public void setBidContactData(List<DatumDefinition> bidContactData) {
//		this.bidContactData = bidContactData;
//	}
//	
//	public List<DatumDefinition> getBidContactData() {
//		return this.bidContactData;
//	}
//	
//	public void addBidSetAsideDatum(DatumDefinition bidSetAsideDatum) {
//		this.bidSetAsideData.add(bidSetAsideDatum);
//	}
//	
//	public void setBidSetAsideData(List<DatumDefinition> bidSetAsideData) {
//		this.bidSetAsideData = bidSetAsideData;
//	}
//	
//	public List<DatumDefinition> getBidSetAsideData() {
//		return this.bidSetAsideData;
//	}
//
//	public void addBidDocumentDatum(DatumDefinition bidDocumentDatum) {
//		this.bidDocumentData.add(bidDocumentDatum);
//	}
//	
//	public void setBidDocumentData(List<DatumDefinition> bidDocumentData) {
//		this.bidDocumentData = bidDocumentData;
//	}
//	
//	public List<DatumDefinition> getBidDocumentData() {
//		return this.bidDocumentData;
//	}
//	
//	public void addBidGrantDatum(DatumDefinition bidGrantDatum) {
//		this.bidGrantData.add(bidGrantDatum);
//	}
//	
//	public void setBidGrantData(List<DatumDefinition> bidGrantData) {
//		this.bidGrantData = bidGrantData;
//	}
//	
//	public List<DatumDefinition> getBidGrantData() {
//		return this.bidGrantData;
//	}
//	
//	public void addBidVendorDatum(DatumDefinition bidVendorDatum) {
//		this.bidVendorData.add(bidVendorDatum);
//	}
//	
//	public void setBidVendorData(List<DatumDefinition> bidVendorData) {
//		this.bidVendorData = bidVendorData;
//	}
//	
//	public List<DatumDefinition> getBidVendorData() {
//		return this.bidVendorData;
//	}
//	
//	public void addBidVendorAgencyIdDatum(DatumDefinition bidVendorAgencyIdDatum) {
//		this.bidVendorAgencyIdData.add(bidVendorAgencyIdDatum);
//	}
//	
//	public void setBidVendorAgencyIdData(List<DatumDefinition> bidVendorAgencyIdData) {
//		this.bidVendorAgencyIdData = bidGrantData;
//	}
//	
//	public List<DatumDefinition> getBidVendorAgencyIdData() {
//		return this.bidVendorAgencyIdData;
//	}
//	
//	public void addBidVendorAgencyOrgDatum(DatumDefinition bidVendorAgencyOrgDatum) {
//		this.bidVendorAgencyOrgData.add(bidVendorAgencyOrgDatum);
//	}
//	
//	public void setBidVendorAgencyOrgData(List<DatumDefinition> bidVendorAgencyOrgData) {
//		this.bidVendorAgencyOrgData = bidVendorAgencyOrgData;
//	}
//	
//	public List<DatumDefinition> getBidVendorAgencyOrgData() {
//		return this.bidVendorAgencyOrgData;
//	}

}
