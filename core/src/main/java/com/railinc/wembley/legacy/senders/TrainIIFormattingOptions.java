package com.railinc.wembley.legacy.senders;

import com.railinc.wembley.api.core.NotificationService;
/* RAPID PDM 3321 */
public interface TrainIIFormattingOptions extends NotificationService {

	boolean indentXml();
	int indentXmlSize();

	boolean insertCRLFAfterHeader();
	boolean insertCRLFBeforeTrailer();

	boolean allowCRLFAfterXmlDecl();

	boolean addXmlDecl();

	boolean trimXml();

	String xmlEncoding();

	boolean convertCRLFInXmltoLF();
}
