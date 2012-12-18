package com.poplicus.crawler.codegen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSet {
	
	public enum TableName {
		PopBid {
			public String toString() {
				return "PopBid";
			}
		},
		PopBidIndustryCode {
			public String toString() {
				return "PopBidIndustryCode";
			}
		},
		PopBidLocation {
			public String toString() {
				return "PopBidLocation";
			}
		},
		PopBidContact {
			public String toString() {
				return "PopBidContact";
			}
		},
		PopBidSetAside {
			public String toString() {
				return "PopBidSetAside";
			}
		},
		PopBidDocument {
			public String toString() {
				return "PopBidDocument";
			}
		},
		PopBidGrant {
			public String toString() {
				return "PopBidGrant";
			}
		},
		PopBidVendor {
			public String toString() {
				return "PopBidVendor";
			}
		},
		PopBidVendorAgencyId {
			public String toString() {
				return "PopBidVendorAgencyId";
			}
		},
		PopBidVendorAgencyOrg {
			public String toString() {
				return "PopBidVendorAgencyOrg";
			}
		},		
		None {
			public String toString() {
				return "None";
			}
		}
	}
	
	private TableName tableName = null;
	
	//private List<Datum> datumList = new ArrayList<Datum>();
	
	private Map<String,String> additionalDataByReference = new HashMap<String,String>();
	
	private Map<String,String> additionalDataByValue = new HashMap<String,String>();
	
	private String additionalInformation = null;
	
	private List<Datum> data = new ArrayList<Datum>();
	
//	private List<Datum> bidData = new ArrayList<Datum>();
//	
//	private List<Datum> bidIndustryData = new ArrayList<Datum>();
//	
//	private List<Datum> bidLocationData = new ArrayList<Datum>();
//	
//	private List<Datum> bidContactData = new ArrayList<Datum>();
//	
//	private List<Datum> bidSetAsideData = new ArrayList<Datum>();
//	
//	private List<Datum> bidDocumentData = new ArrayList<Datum>();
//	
//	private List<Datum> bidGrantData = new ArrayList<Datum>();
//	
//	private List<Datum> bidVendorData = new ArrayList<Datum>();
//	
//	private List<Datum> bidVendorAgencyIdData = new ArrayList<Datum>();
//	
//	private List<Datum> bidVendorAgencyOrgData = new ArrayList<Datum>();
	
//	private ParseResultConstruct parseResultConstruct = null;
	
	
	public DataSet(TableName tableName, String additionalInformation) throws DataSetException{
		if(tableName == null) {
			throw new DataSetException("tableName used to create DataSet is either Null or Empty.");
		}
		this.tableName = tableName;
		this.additionalInformation = additionalInformation;
	}
	
	public TableName getTableName() {
		return tableName;
	}

	public void setTableName(TableName tableName) {
		this.tableName = tableName;
	}

////	public void addDatum(Datum value) {
////		datumList.add(value);
////	}
////	
////	public List<Datum> getDatumList() {
////		return datumList;
////	}
//	
////	public ParseResultConstruct getParseResultConstruct() {
////		return parseResultConstruct;
////	}
////
////	public void setParseResultConstruct(ParseResultConstruct parseResultConstruct) {
////		this.parseResultConstruct = parseResultConstruct;
////	}

	public Map<String,String> getAdditionalDataByReference() {
		return this.additionalDataByReference;
	}
	
	public Map<String,String> getAdditionalDataByValue() {
		return this.additionalDataByValue;
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @param passByValue
	 */
	public void addAdditionalData(String key, String value, boolean passByValue) {
		if(passByValue) {
			this.additionalDataByValue.put(key, value);	
		} else {
			this.additionalDataByReference.put(key, value);
		}
	}
	
	public void addAdditionalDataByReference(Map<String, String> map) {
		this.additionalDataByReference.putAll(map);
	}
	
	public void addAdditionalDataByValue(Map<String, String> map) {
		this.additionalDataByValue.putAll(map);
	}
	
	public void addAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}
	
	public String getAdditionalInformation() {
		return this.additionalInformation;
	}
	
	public void addDatum(Datum bidDatum) {
		this.data.add(bidDatum);
	}
	
	public void setData(List<Datum> bidData) {
		this.data = bidData;
	}
	
	public List<Datum> getData() {
		return this.data;
	}

//	public void addBidDatum(Datum bidDatum) {
//		this.bidData.add(bidDatum);
//	}
//	
//	public void setBidData(List<Datum> bidData) {
//		this.bidData = bidData;
//	}
//	
//	public List<Datum> getBidData() {
//		return this.bidData;
//	}
//	
//	public void addBidIndustryDatum(Datum bidIndustryDatum) {
//		this.bidIndustryData.add(bidIndustryDatum);
//	}
//	
//	public void setBidIndustryData(List<Datum> bidIndustryData) {
//		this.bidIndustryData = bidIndustryData;
//	}
//	
//	public List<Datum> getBidIndustryData() {
//		return this.bidIndustryData;
//	}
//	
//	public void addBidLocationDatum(Datum bidLocationDatum) {
//		this.bidLocationData.add(bidLocationDatum);
//	}
//	
//	public void setBidLocationData(List<Datum> bidLocationData) {
//		this.bidLocationData = bidLocationData;
//	}
//	
//	public List<Datum> getBidLocationData() {
//		return this.bidLocationData;
//	}
//	
//	public void addBidContactDatum(Datum bidContactDatum) {
//		this.bidContactData.add(bidContactDatum);
//	}
//	
//	public void setBidContactData(List<Datum> bidContactData) {
//		this.bidContactData = bidContactData;
//	}
//	
//	public List<Datum> getBidContactData() {
//		return this.bidContactData;
//	}
//	
//	public void addBidSetAsideDatum(Datum bidSetAsideDatum) {
//		this.bidSetAsideData.add(bidSetAsideDatum);
//	}
//	
//	public void setBidSetAsideData(List<Datum> bidSetAsideData) {
//		this.bidSetAsideData = bidSetAsideData;
//	}
//	
//	public List<Datum> getBidSetAsideData() {
//		return this.bidSetAsideData;
//	}
//
//	public void addBidDocumentDatum(Datum bidDocumentDatum) {
//		this.bidDocumentData.add(bidDocumentDatum);
//	}
//	
//	public void setBidDocumentData(List<Datum> bidDocumentData) {
//		this.bidDocumentData = bidDocumentData;
//	}
//	
//	public List<Datum> getBidDocumentData() {
//		return this.bidDocumentData;
//	}
//	
//	public void addBidGrantDatum(Datum bidGrantDatum) {
//		this.bidGrantData.add(bidGrantDatum);
//	}
//	
//	public void setBidGrantData(List<Datum> bidGrantData) {
//		this.bidGrantData = bidGrantData;
//	}
//	
//	public List<Datum> getBidGrantData() {
//		return this.bidGrantData;
//	}
//	
//	public void addBidVendorDatum(Datum bidVendorDatum) {
//		this.bidVendorData.add(bidVendorDatum);
//	}
//	
//	public void setBidVendorData(List<Datum> bidVendorData) {
//		this.bidVendorData = bidVendorData;
//	}
//	
//	public List<Datum> getBidVendorData() {
//		return this.bidVendorData;
//	}
//	
//	public void addBidVendorAgencyIdDatum(Datum bidVendorAgencyIdDatum) {
//		this.bidVendorAgencyIdData.add(bidVendorAgencyIdDatum);
//	}
//	
//	public void setBidVendorAgencyIdData(List<Datum> bidVendorAgencyIdData) {
//		this.bidVendorAgencyIdData = bidGrantData;
//	}
//	
//	public List<Datum> getBidVendorAgencyIdData() {
//		return this.bidVendorAgencyIdData;
//	}
//	
//	public void addBidVendorAgencyOrgDatum(Datum bidVendorAgencyOrgDatum) {
//		this.bidVendorAgencyOrgData.add(bidVendorAgencyOrgDatum);
//	}
//	
//	public void setBidVendorAgencyOrgData(List<Datum> bidVendorAgencyOrgData) {
//		this.bidVendorAgencyOrgData = bidVendorAgencyOrgData;
//	}
//	
//	public List<Datum> getBidVendorAgencyOrgData() {
//		return this.bidVendorAgencyOrgData;
//	}
	
}