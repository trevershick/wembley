package com.railinc.wembley.legacy.senders;


public class DefaultTrainIIOptions implements TrainIIFormattingOptions {

	public String getAppId() {
		return TrainIIFormattingOptions.DEFAULT_APP_ID;
	}

	public boolean indentXml() {
		return true;
	}

	public boolean insertCRLFBeforeTrailer() {
		return true;
	}

	public boolean allowCRLFAfterXmlDecl() {
		return true;
	}

	public boolean addXmlDecl() {
		return true;
	}

	public boolean trimXml() {
		return false;
	}

	public String xmlEncoding() {
		return "ISO-8859-1";
	}

	public boolean insertCRLFAfterHeader() {
		return false;
	}

	public boolean convertCRLFInXmltoLF() {
		return true;
	}

	public int indentXmlSize() {
		return 4;
	}

}
