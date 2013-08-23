package com.railinc.r2dq.correspondence;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.util.Collections.unmodifiableCollection;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.StringUtils.trimToNull;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;

public class Correspondence {
	private String uuid = UUID.randomUUID().toString();
	private Collection<Contact> recipient = newArrayList();
	private Contact from;
	private Contact replyTo;
	
	private String textHtml;
	private String textPlain;
	private String subject;
	
	private Map<String,Pair<String,byte[]>> attachments = newHashMap();

	public Collection<Contact> getRecipients() {
		return unmodifiableCollection(recipient);
	}

	public Contact getFrom() {
		return from;
	}

	public void setFrom(Contact from) {
		if (from != null && from.hasEmail()) {
			this.from = from;
		}
	}

	public Contact getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(Contact replyTo) {
		if (replyTo != null && replyTo.hasEmail()) {
			this.replyTo = replyTo;
		}
	}

	public String getTextHtml() {
		return textHtml;
	}

	public void setTextHtml(String textHtml) {
		this.textHtml = trimToNull(textHtml);
	}

	public String getTextPlain() {
		return textPlain;
	}

	public void setTextPlain(String textPlain) {
		this.textPlain = trimToNull(textPlain);
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = trimToNull(subject);
	}

	public void addAttachment(String imageResourceName, String imageMimeType, byte[] image) {
		this.attachments.put(imageResourceName, new Pair<String,byte[]>(imageMimeType, image));
	}

	public Collection<String> attachmentNames() {
		return attachments.keySet();
	}

	public String attachmentMimeType(String inlineResourceId) {
		return attachments.get(inlineResourceId).left();
	}

	public byte[] attachmentData(String inlineResourceId) {
		return attachments.get(inlineResourceId).right();
	}

	public void addRecipients(Collection<Contact> recipients) {
		this.recipient.addAll(recipients);
	}

	public void addRecipient(SimpleContact simpleContact) {
		Preconditions.checkArgument(simpleContact != null, "contact cannot be null");
		Preconditions.checkArgument(simpleContact.hasEmail(), "contact must have email");
		this.recipient.add(simpleContact);
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

	public String getUuid() {
		return uuid;
	}

	public boolean isHtml() {
		return isNotBlank(this.textHtml);
	}
}
