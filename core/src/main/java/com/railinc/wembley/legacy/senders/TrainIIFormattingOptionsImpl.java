package com.railinc.wembley.legacy.senders;
/* RAPID PDM 3321 */
public class TrainIIFormattingOptionsImpl implements
		TrainIIFormattingOptions {

	public boolean isIndentXml() {
		return indentXml;
	}

	public void setIndentXml(boolean indentXml) {
		this.indentXml = indentXml;
	}

	public boolean isInsertCRLFBeforeTrailer() {
		return insertCRLFBeforeTrailer;
	}

	public void setInsertCRLFBeforeTrailer(boolean insertCRLFBeforeTrailer) {
		this.insertCRLFBeforeTrailer = insertCRLFBeforeTrailer;
	}

	public boolean isAllowCRLFAfterXmlDecl() {
		return allowCRLFAfterXmlDecl;
	}

	public void setAllowCRLFAfterXmlDecl(boolean allowCRLFAfterXmlDecl) {
		this.allowCRLFAfterXmlDecl = allowCRLFAfterXmlDecl;
	}

	public boolean isAddXmlDecl() {
		return addXmlDecl;
	}

	public void setAddXmlDecl(boolean addXmlDecl) {
		this.addXmlDecl = addXmlDecl;
	}

	public boolean isTrimXml() {
		return trimXml;
	}

	public void setTrimXml(boolean trimXml) {
		this.trimXml = trimXml;
	}

	public String getXmlEncoding() {
		return xmlEncoding;
	}

	public void setXmlEncoding(String xmlEncoding) {
		this.xmlEncoding = xmlEncoding;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	private String appId;
	private boolean indentXml = true;
	private int indentXmlSize = 4;
	public int getIndentXmlSize() {
		return indentXmlSize;
	}

	public void setIndentXmlSize(int indentXmlSize) {
		this.indentXmlSize = indentXmlSize;
	}

	private boolean insertCRLFBeforeTrailer = true;
	private boolean allowCRLFAfterXmlDecl = true;
	private boolean addXmlDecl = true;
	private boolean trimXml = false;
	private String xmlEncoding = "ISO-8859-1";
	private boolean insertCRLFAfterHeader = false;
	private boolean convertCRLFInXmltoLF = true;
	
	public boolean isConvertCRLFInXmltoLF() {
		return convertCRLFInXmltoLF;
	}

	public void setConvertCRLFInXmltoLF(boolean convertCRLFInXmltoLF) {
		this.convertCRLFInXmltoLF = convertCRLFInXmltoLF;
	}

	public boolean isInsertCRLFAfterHeader() {
		return insertCRLFAfterHeader;
	}

	public void setInsertCRLFAfterHeader(boolean insertCRLFAfterHeader) {
		this.insertCRLFAfterHeader = insertCRLFAfterHeader;
	}

	public String getAppId() {
		return appId;
	}

	public boolean indentXml() {
		// Auto-generated method stub
		return indentXml;
	}

	public boolean insertCRLFBeforeTrailer() {
		// Auto-generated method stub
		return insertCRLFBeforeTrailer;
	}

	public boolean allowCRLFAfterXmlDecl() {
		// Auto-generated method stub
		return allowCRLFAfterXmlDecl;
	}

	public boolean addXmlDecl() {
		// Auto-generated method stub
		return addXmlDecl;
	}

	public boolean trimXml() {
		// Auto-generated method stub
		return trimXml;
	}

	public String xmlEncoding() {
		// Auto-generated method stub
		return xmlEncoding;
	}

	public boolean insertCRLFAfterHeader() {
		return insertCRLFAfterHeader;
	}

	public boolean convertCRLFInXmltoLF() {
		
		return convertCRLFInXmltoLF;
	}

	public int indentXmlSize() {
		return this.indentXmlSize;
	}

}
