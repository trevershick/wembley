package com.railinc.wembley.legacy.domain.impl.sso;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SSORestUserSaxHandler extends DefaultHandler {

	private SSOUserInfo user;
	private List<SSOUserInfo> users = new ArrayList<SSOUserInfo>();

	private StringBuilder characters = new StringBuilder();
	private String envelope;
	
	public SSORestUserSaxHandler(String envelopeElementName) {
		this.envelope = envelopeElementName;
		
	}
	
	public List<SSOUserInfo> getUsers() {
		// yeah, unsafe but ok here
		return users;
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		String elementText = characters.toString().trim();
		characters.delete(0, characters.length());
		
		if (user == null) { return; }
		if ("status".equals(localName)) {
			user.setStatus(elementText);
		}
		if ("userId".equals(localName)) {
			user.setUsername(elementText);
		}
		if ("emailAddress".equals(localName)) {
			user.setEmail(elementText);
		}
		
		
		if (envelope.equals(localName)) {
			users.add(user);
			user = null;
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes arg3) throws SAXException {
		
		if (envelope.equals(localName)) {
			user = new SSOUserInfo();
		} 
		if (user == null) {
			return;
		}
	}
	

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		characters.append(ch, start, length);
	}

}
