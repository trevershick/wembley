package com.railinc.r2dq.correspondence;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.unmodifiableMap;

import java.util.Collection;
import java.util.Map;

public class CorrespondenceTemplate {
	private String templateName;
	private final Collection<Contact> recipients = newArrayList();
	private Contact from;
	private Contact replyTo;
	
	private Map<String,Object> data = newHashMap();
	
	public CorrespondenceTemplate() {
		templateName = getClass().getSimpleName().toLowerCase();
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public Collection<Contact> getRecipients() {
		return unmodifiableCollection(recipients);
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

	public void addRecipient(Contact user) {
		if (user != null) {
			this.recipients.add(user);
		}
	}

	public Map<String,Object> data() {
		return unmodifiableMap(this.data);
	}

	public void addData(String string, Object value) {
		this.data.put(string, value);
	}

	public void addRecipients(Iterable<String> emails) {
		for (String em : emails) {
			addRecipient(new SimpleContact(null, em));
		}
	}

	public void setFrom(String fromAddress, String fromName) {
		this.setFrom(new SimpleContact(fromName, fromAddress));
	}

	public void setReplyTo(String replyToAddress, String replyToName) {
		this.setReplyTo(new SimpleContact(replyToName, replyToAddress));
	}

	public void addData(Map<String, Object> messageContent) {
		if (messageContent != null) {
			this.data.putAll(messageContent);
		}
	}

	@Override
	public String toString() {
		return "CorrespondenceTemplate [templateName=" + templateName + ", recipients=" + recipients + ", from=" + from
				+ ", replyTo=" + replyTo + ", data=" + data + "]";
	}
	
}
