package com.github.trevershick.wembley.api;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.Collection;

import com.github.trevershick.wembley.api.address.Address;
import com.github.trevershick.wembley.api.address.EmailAddress;
import com.github.trevershick.wembley.api.attachment.Attachment;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class Notification {
	String app;
	final Collection<Intent> intents = Sets.newHashSet();
	final Collection<Address> addresses = Sets.newHashSet();
	String subject;
	String text;
	String html;
	final Collection<Attachment> attachments = Lists.newArrayList();

	String template;
	int templateVersion;
	String templateData;
	
	
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public int getTemplateVersion() {
		return templateVersion;
	}
	public void setTemplateVersion(int templateVersion) {
		this.templateVersion = templateVersion;
	}
	public String getTemplateData() {
		return templateData;
	}
	public void setTemplateData(String templateData) {
		this.templateData = templateData;
	}
	public Collection<Intent> getIntents() {
		return intents;
	}
	public Collection<Address> getAddresses() {
		return addresses;
	}
	public Collection<Attachment> getAttachments() {
		return attachments;
	}
	public void addAddress(EmailAddress emailAddress) {
		if (emailAddress != null) {
			this.addresses.add(emailAddress);
		}
		
	}
	public void addIntent(Intent email) {
		this.intents.add(email);
	}
	public boolean isSimple() {
		return isBlank(template);
	}
	public boolean hasSubject() {
		return isNotBlank(subject);
	}
	public boolean hasTextBody() {
		return isNotBlank(this.text);
	}
	public boolean hasHtmlBody() {
		return isNotBlank(html);
	}
	
	
	
}
