package com.poplicus.crawler.codegen.docmgr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import com.poplicus.crawler.codegen.docmgr.IgnoreAnchor.IgnoreCondition;

public class DocManager {

	private DocParser docParser = new DocParser();

	public List<PopDoc> getPopDocsFromHTML(String html, IgnoreAnchor ignoreAnchor, boolean onlyDocs, String sourceURL) {

		/* html = "<UL>"
			+ "<LI><A href=\"media/802-12-16395-r_rfp.pdf\">802-12-16395-r rfp</A> <SPAN class=\"nowrap smalltext\"><IMG style=\"FLOAT: none\" class=nomargin alt=\"media download\" src=\"/images/media.gif\">(<A title=\"Find out more about the Adobe Portable Document Format.\" href=\"/site/software/#adobe\" jQuery1640935842691442216=\"14\">PDF</A> <SPAN class=help title=\"2.8 minutes to download at 32.8 kilobits per second\">688.8</SPAN> <ABBR class=help title=kilobytes>KB</ABBR>)</SPAN> 2012-06-21 17:21:14"
			+ "<LI><A href=\"media/802-12-16395-r_sheet_a1.pdf\">802-12-16395-r sheet a1</A> <SPAN class=\"nowrap smalltext\"><IMG style=\"FLOAT: none\" class=nomargin alt=\"media download\" src=\"/images/media.gif\">(<A title=\"Find out more about the Adobe Portable Document Format.\" href=\"/site/software/#adobe\" jQuery1640935842691442216=\"15\">PDF</A> <SPAN class=help title=\"2 minutes to download at 32.8 kilobits per second\">495.1</SPAN> <ABBR class=help title=kilobytes>KB</ABBR>)</SPAN> 2012-01-17 10:54:28"
			+ "<LI><A href=\"media/802-12-16395-r_sheet_c1.pdf\">802-12-16395-r sheet c1</A> <SPAN class=\"nowrap smalltext\"><IMG style=\"FLOAT: none\" class=nomargin alt=\"media download\" src=\"/images/media.gif\">(<A title=\"Find out more about the Adobe Portable Document Format.\" href=\"/site/software/#adobe\" jQuery1640935842691442216=\"16\">PDF</A> <SPAN class=help title=\"4.3 minutes to download at 32.8 kilobits per second\">1</SPAN> <ABBR class=help title=megabytes>MB</ABBR>)</SPAN> 2012-01-17 10:54:31"
			+ "<LI><A href=\"media/802-12-16395-r_sheet_e1.pdf\">802-12-16395-r sheet e1</A> <SPAN class=\"nowrap smalltext\"><IMG style=\"FLOAT: none\" class=nomargin alt=\"media download\" src=\"/images/media.gif\">(<A title=\"Find out more about the Adobe Portable Document Format.\" href=\"/site/software/#adobe\" jQuery1640935842691442216=\"17\">PDF</A> <SPAN class=help title=\"2.9 minutes to download at 32.8 kilobits per second\">710.9</SPAN> <ABBR class=help title=kilobytes>KB</ABBR>)</SPAN> 2012-01-17 10:54:31"
			+ "<LI><A href=\"media/802-12-16395-r_sheet_g1.pdf\">802-12-16395-r sheet g1</A> <SPAN class=\"nowrap smalltext\"><IMG style=\"FLOAT: none\" class=nomargin alt=\"media download\" src=\"/images/media.gif\">(<A title=\"Find out more about the Adobe Portable Document Format.\" href=\"/site/software/#adobe\" jQuery1640935842691442216=\"18\">PDF</A> <SPAN class=help title=\"3.2 minutes to download at 32.8 kilobits per second\">788.8</SPAN> <ABBR class=help title=kilobytes>KB</ABBR>)</SPAN> 2012-01-17 10:54:28"
			+ "<LI><A href=\"media/802-12-16395-r_sheet_p1.pdf\">802-12-16395-r sheet p1</A> <SPAN class=\"nowrap smalltext\"><IMG style=\"FLOAT: none\" class=nomargin alt=\"media download\" src=\"/images/media.gif\">(<A title=\"Find out more about the Adobe Portable Document Format.\" href=\"/site/software/#adobe\" jQuery1640935842691442216=\"19\">PDF</A> <SPAN class=help title=\"2.2 minutes to download at 32.8 kilobits per second\">549</SPAN> <ABBR class=help title=kilobytes>KB</ABBR>)</SPAN> 2012-01-17 10:54:32 </LI></UL>";
		*/
		List<PopDoc> popDocs = docParser.getPopDocsFromHTML(html, ignoreAnchor, onlyDocs, sourceURL);

		return popDocs;
	}

	public List<PopDoc> getPopDocs(String html, String url, IgnoreAnchor ignoreAnchor, boolean onlyDoc) throws IOException {

		List<PopDoc> popDocs = docParser.getPopDocs(html, url, ignoreAnchor, onlyDoc);

		return popDocs;
	}

	public List<PopDoc> getPopDocsFromURL(String url, IgnoreAnchor ignoreAnchor, boolean onlyDocs) throws IOException {

		List<PopDoc> popDocs = docParser.getPopDocsFromURL(url, ignoreAnchor, onlyDocs);

		return popDocs;
	}

	public void printPopDocs(List<PopDoc> popDocs) {
		if(popDocs != null) {
			for(PopDoc popDoc : popDocs) {
				System.out.println(popDoc);
			}
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

	public static void main(String[] args) throws Exception {
		DocManager docManager = new DocManager();
		IgnoreAnchor ignoreAnchor = new IgnoreAnchor(IgnoreCondition.ContainsAttribute);
		ignoreAnchor.setContainsAttributeName("title");

//      old code ============================= don't use below code.
		/*URL testURL = new URL("http://www.dhcd.state.md.us/Website/Procure/ProcureDetails.aspx?&ProcurementID=159");
		System.out.println("<Content> " + docManager.getHTMLFromURL("http://www.dhcd.state.md.us/Website/Procure/ProcureDetails.aspx?&ProcurementID=159"));

		URL url1 = new URL("http://www.seattle.gov/financedepartment/1217adoptedcip/default.htm");
		System.out.println(docManager.getPopDocsFromURL(url1, null, "http://www.seattle.gov/financedepartment/1217adoptedcip/", true));

		URL url2 = new URL("http://leap.leg.wa.gov/leap/budget/lbns/2012toc.htm");
		System.out.println(docManager.getPopDocsFromURL(url2, null, "http://leap.leg.wa.gov/leap/budget/lbns/", true));

		URL url3 = new URL("http://www.nyc.gov/html/omb/html/publications/finplan06_12.shtml");
		System.out.println(docManager.getPopDocsFromURL(url3, null, "http://www.nyc.gov/html/omb/html/publications/", true));

		URL url4 = new URL("http://www.visithelsinki.fi/en/come/tourist-information/brochures");
		System.out.println(docManager.getPopDocsFromURL(url4, null, null, true));

		System.out.println(docManager.getPopDocsFromURL(new URL("http://purchasing.alabama.gov/pages/../pages/notes/t191a_note.aspx"),
            null, "http://purchasing.alabama.gov/pages/../pages/notes/", false));

		docManager.printPopDocs(docManager.getPopDocsFromURL(new URL("http://purchasing.alabama.gov/pages/active_contracts.aspx"),
			null, "http://purchasing.alabama.gov/pages/", false));

		URL url5 = new URL("http://www.iard.com/");
		System.out.println(docManager.getPopDocsFromURL(url5, null, "http://www.iard.com/", true));*/

//      latest code ============================= use below code

//		docManager.printPopDocs(docManager.getPopDocsFromURL("http://purchasing.alabama.gov/pages/active_contracts.aspx", null, true));

//		docManager.printPopDocs(docManager.getPopDocsFromHTML(
//			docManager.getHTMLFromURL("http://www.dhcd.state.md.us/Website/Procure/ProcureDetails.aspx?&ProcurementID=159"),
//			null, true, "http://www.dhcd.state.md.us/Website/Procure/ProcureDetails.aspx?&ProcurementID=159"));

//		docManager.printPopDocs(docManager.getPopDocs(
//			docManager.getHTMLFromURL("http://www.capital.ucla.edu/projects_currently_bidding.asp"),
//			"http://www.capital.ucla.edu/projects_currently_bidding.asp", null, true));

//		docManager.printPopDocs(docManager.getPopDocsFromHTML(
//			docManager.getHTMLFromURL("http://www.capital.ucla.edu/projects_currently_bidding.asp"),
//			null, true, "http://www.capital.ucla.edu/projects_currently_bidding.asp"));

//		docManager.printPopDocs(docManager.getPopDocsFromHTML(
//			docManager.getHTMLFromURL("http://wisbuildnet.doa.state.wi.us/(S(rrfoyw5wwpsp01cjphaptrr0))/public/bid_documents.aspx?projnum=12B1S"),
//			null, true, "http://wisbuildnet.doa.state.wi.us/(S(rrfoyw5wwpsp01cjphaptrr0))/public/bid_documents.aspx?projnum=12B1S"));

//		docManager.printPopDocs(docManager.getPopDocsFromHTML(
//			docManager.getHTMLFromURL("http://www.gssa.state.co.us/PriceAwd.nsf/93dad434d57223d687256a3f0078fc4e/d3ac496ebac7cd9287257817005cccc0?OpenDocument"),
//			null, true, "http://www.gssa.state.co.us/PriceAwd.nsf/93dad434d57223d687256a3f0078fc4e/d3ac496ebac7cd9287257817005cccc0?OpenDocument"));

//		docManager.printPopDocs(docManager.getPopDocsFromHTML(
//			docManager.getHTMLFromURL("http://www.gssa.state.co.us/PriceAwd.nsf/b0b1c9ac1b0f27fd87256794006211b8/3cd51a0abeb408f487257695007c7374?OpenDocument"),
//			null, true, "http://www.gssa.state.co.us/PriceAwd.nsf/b0b1c9ac1b0f27fd87256794006211b8/3cd51a0abeb408f487257695007c7374?OpenDocument"));

//		docManager.printPopDocs(docManager.getPopDocsFromURL("http://www.gssa.state.co.us/PriceAwd.nsf/b0b1c9ac1b0f27fd87256794006211b8/3cd51a0abeb408f487257695007c7374?OpenDocument", null, true));

//		docManager.printPopDocs(docManager.getPopDocsFromURL("http://vendornet.state.wi.us/vendornet/asp/AwardDetail.asp?SystemBidNumber=17757", null, true));

		docManager.printPopDocs(docManager.getPopDocs(
			docManager.getHTMLFromURL("http://vendornet.state.wi.us/vendornet/asp/AwardDetail.asp?SystemBidNumber=17757"),
			"http://vendornet.state.wi.us/vendornet/asp/AwardDetail.asp?SystemBidNumber=17757", null, true));

//		docManager.printPopDocs(docManager.getPopDocsFromHTML(
//			docManager.getHTMLFromURL("http://vendornet.state.wi.us/vendornet/asp/AwardDetail.asp?SystemBidNumber=17757"),
//			null, true, "http://vendornet.state.wi.us/vendornet/asp/AwardDetail.asp?SystemBidNumber=17757"));

//		docManager.printPopDocs(docManager.getPopDocs(
//			docManager.getHTMLFromURL("http://www.seattle.gov/financedepartment/1217adoptedcip/default.htm"),
//			"http://www.seattle.gov/financedepartment/1217adoptedcip/default.htm", null, true));

//		docManager.printPopDocs(docManager.getPopDocs(
//			docManager.getHTMLFromURL("http://leap.leg.wa.gov/leap/budget/lbns/2012toc.htm"),
//			"http://leap.leg.wa.gov/leap/budget/lbns/2012toc.htm", null, true));

//		docManager.printPopDocs(docManager.getPopDocs(
//			docManager.getHTMLFromURL("http://www.nyc.gov/html/omb/html/publications/finplan06_12.shtml"),
//			"http://www.nyc.gov/html/omb/html/publications/finplan06_12.shtml", null, true));

//		docManager.printPopDocs(docManager.getPopDocs(
//			docManager.getHTMLFromURL("http://www.visithelsinki.fi/en/come/tourist-information/brochures"),
//			"http://www.visithelsinki.fi/en/come/tourist-information/brochures", null, true));

//		docManager.printPopDocs(docManager.getPopDocs(
//			docManager.getHTMLFromURL("http://www.iard.com/"), "http://www.iard.com/", null, true));

//		docManager.printPopDocs(docManager.getPopDocs(
//			docManager.getHTMLFromURL("http://purchasing.alabama.gov/pages/active_contracts.aspx"),
//			"http://purchasing.alabama.gov/pages/active_contracts.aspx", null, true));


	}

}
