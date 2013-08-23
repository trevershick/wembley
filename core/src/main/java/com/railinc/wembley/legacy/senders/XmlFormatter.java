package com.railinc.wembley.legacy.senders;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;

public class XmlFormatter {
	private String elementAsText(Element bodyElement, 
			boolean indentXml,
			int indentXmlSize, 
			boolean convertCRLFtoLF, 
			String encoding) throws TransformerFactoryConfigurationError,
				TransformerConfigurationException, TransformerException {
		
		String body;
		StringWriter out = new StringWriter();

		TransformerFactory factory = TransformerFactory.newInstance();

		Transformer transformer = factory.newTransformer();
		applyTransformerProperties(transformer, indentXml,indentXmlSize, encoding);

		transformer.transform(new DOMSource(bodyElement), new StreamResult(out));
		
		body = out.getBuffer().substring(0, out.getBuffer().length());
		if (convertCRLFtoLF) {
			// windows sticks in \r\n, but the original implementation used the dom serializer
			// which only put in \n. So to duplicate the functionality on ALL platforms
			// i need to do the replacement.
			body = body.replaceAll("\\r\\n","\n");
		}
		return body;
	}

	private void applyTransformerProperties(Transformer transformer,
			boolean indentXml, int indentXmlSize, String encoding) {
		

		
		transformer.setOutputProperty(OutputKeys.INDENT, indentXml ? XML_OUTPUTKEYS_YES : XML_OUTPUTKEYS_NO);
		transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
		transformer.setOutputProperty(OutputKeys.METHOD, XML_OUTPUT_METHOD);
		
		transformer.setOutputProperty( OutputKeys.OMIT_XML_DECLARATION, XML_OUTPUTKEYS_YES); // always YES
//		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		transformer.setOutputProperty(NS_XALAN + XML_OUTPUT_INDENT_AMOUNT, String.valueOf(indentXmlSize));
		//System.out.println(transformer.getOutputProperties());
	}

	public String formatXml(Element bodyElement,
			boolean addXmlDecl,
			boolean allowCRLFAfterXmlDecl, 
			boolean indentXml, 
			int indentXmlSize,
			boolean trimXml,
			boolean convertCRLFtoLF,
			String xmlEncoding)
			throws TransformerFactoryConfigurationError,
			TransformerConfigurationException, TransformerException {

		StringBuilder out = new StringBuilder();

		out.append(elementAsText((Element) bodyElement, indentXml,indentXmlSize, convertCRLFtoLF, xmlEncoding ));
		if (addXmlDecl && allowCRLFAfterXmlDecl) {
			out.insert(0, "\n");
		}
		if (addXmlDecl) {
			out.insert(0, String.format(
					"<?xml version=\"1.0\" encoding=\"%s\"?>", xmlEncoding));
		}

		String body = out.toString();
		if (trimXml) {
			body = body.trim();
		}
		return body;
	}

	private static final String NS_XALAN = "{http://xml.apache.org/xalan}";
	
	 private static final String XML_OUTPUT_INDENT_AMOUNT = "indent-amount";
	 
//	 private static final String XML_OUTPUT_INDENT_PROP1 =
//	 "{http://xml.apache.org/xslt}indent-amount";
	protected static final String XML_OUTPUTKEYS_YES = "yes";
	protected static final String XML_OUTPUTKEYS_NO = "no";
	protected static final String XML_OUTPUT_METHOD = "xml";
}
