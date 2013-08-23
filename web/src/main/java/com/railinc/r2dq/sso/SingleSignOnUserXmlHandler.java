package com.railinc.r2dq.sso;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.StringUtils.trimToNull;

import java.util.Collection;
import java.util.Collections;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class SingleSignOnUserXmlHandler extends DefaultHandler {
	private static final String NAMESPACE = "http://resources.sso.railinc.com/v2";

	private static final String ELEMENT_POSTAL_CODE = "postalCode";
	private static final String ELEMENT_STREET = "street";
	private static final String ELEMENT_CITY = "city";
	private static final String ELEMENT_REGION = "region";
	private static final String ELEMENT_COUNTRY = "country";
	private static final String ELEMENT_PHONE_NUMBER = "number";
	private static final String ELEMENT_USER = "user";
	private static final String ELEMENT_TITLE = "title";
	private static final String ELEMENT_SURNAME = "surname";
	private static final String ELEMENT_GIVEN_NAME = "givenName";
	private static final String ELEMENT_EMPLOYER = "employer";
	private static final String ELEMENT_PHONE = "phone";
	private static final String ELEMENT_ADDRESS = "address";
	private static final Object ELEMENT_PERMISSION = "permission";
	private static final Object ELEMENT_ENTITY = "entity";
	
	
	final StringBuilder buffer = new StringBuilder();
	SingleSignOnUser _tmp_user = null;
	Collection<SingleSignOnUser> results = newArrayList();
	private boolean inAddress;
	private boolean inPhone;
	
	private String _tmp_roleId;
	private Collection<String> _tmp_marks = newHashSet();
	
	Predicate<SingleSignOnUser> callback;

	public SingleSignOnUserXmlHandler() {

	}
	public SingleSignOnUserXmlHandler(Predicate<SingleSignOnUser> callback) {
		this.callback = callback;
	}
	
	public void reset() {
		inAddress  = inPhone  = false;
		_tmp_user = null;
		_tmp_roleId = null;
		_tmp_marks.clear();
		results.clear();
		buffer.delete(0, buffer.length());
	}
	public boolean hasUser() {
		return !results.isEmpty();
	}
	
	public SingleSignOnUser user() {
		return Iterables.getFirst(results, null);
	}
	
	public Collection<SingleSignOnUser> users() {
		return Collections.unmodifiableCollection(results);
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {

		buffer.append(ch,start,length);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (! uri.equals(NAMESPACE)) {
			return;
		}

		String tmp = trimToNull(buffer.toString());
		buffer.delete(0, buffer.length());

		maybeHandleAddressElement(localName, tmp);
		maybeHandlePhoneElement(localName, tmp);


		if (ELEMENT_ADDRESS.equals(localName)) {
			inAddress = false;
		} else if (ELEMENT_PHONE.equals(localName)) {
			inPhone = false;
		} else if (ELEMENT_PERMISSION.equals(localName)) {
			_tmp_user.addPermission(_tmp_roleId, _tmp_marks);
			_tmp_roleId = null;
			_tmp_marks.clear();
		} else if (ELEMENT_EMPLOYER.equals(localName)) {
			_tmp_user.setEmployer(tmp);
		} else if (ELEMENT_GIVEN_NAME.equals(localName)) {
			_tmp_user.setGivenName(tmp);
		} else if (ELEMENT_SURNAME.equals(localName)) {
			_tmp_user.setSurname(tmp);
		} else if (ELEMENT_TITLE.equals(localName)) {
			_tmp_user.setTitle(tmp);
		} else if (ELEMENT_USER.equals(localName)) {
			callback(_tmp_user);
			_tmp_user = null;
		}
	}

	private void callback(SingleSignOnUser user) throws SAXAbortionException {
		if (callback == null) {
			results.add(_tmp_user);
			return;
		}
		boolean continueParsing = callback.apply(user);
		if (continueParsing) {
			return;
		}
		throw new SAXAbortionException();
	}
	private void maybeHandlePhoneElement(String localName, String tmp) {
		if (!inPhone) {
			return;
		}
		if (ELEMENT_PHONE_NUMBER.equals(localName) && isNotBlank(tmp)) {
			this._tmp_user.setPhone(tmp);
		}
	}

	private void maybeHandleAddressElement(String localName, String tmp) {
		if (!inAddress) {
			return;
		}
		if (ELEMENT_COUNTRY.equals(localName)) {
			_tmp_user.setCountry(tmp);
		} else if (ELEMENT_REGION.equals(localName)) {
			_tmp_user.setRegion(tmp);
		} else if (ELEMENT_CITY.equals(localName)) {
			_tmp_user.setCity(tmp);
		} else if (ELEMENT_STREET.equals(localName)) {
			_tmp_user.setStreet(tmp);
		} else if (ELEMENT_POSTAL_CODE.equals(localName)) {
			_tmp_user.setPostalCode(tmp);
		} else if (ELEMENT_REGION.equals(localName)) {
			_tmp_user.setRegion(tmp);
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (! uri.equals(NAMESPACE)) {
			return;
		}
		if (ELEMENT_USER.equals(localName)) {
			_tmp_user = new SingleSignOnUser();
			_tmp_user.setId(attributes.getValue("id"));
			_tmp_user.setEmail(attributes.getValue("email"));
		} else if (ELEMENT_PERMISSION.equals(localName)) {
			_tmp_roleId = attributes.getValue("id");
		} else if (ELEMENT_ENTITY.equals(localName)) {
			String mark = attributes.getValue("id");
			String type = attributes.getValue("type");
			if (isBlank(type) || "mark".equals(type)) {
				_tmp_marks.add(mark);
			}
		} else if (ELEMENT_ADDRESS.equals(localName)) {
			inAddress = true;
		} else if (ELEMENT_PHONE.equals(localName)) {
			inPhone = true;
		}
	}
}
