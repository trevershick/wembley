package com.railinc.r2dq.messages;

import org.apache.commons.lang.StringUtils;

import com.railinc.r2dq.domain.AuditData;

public class MessageForm {

	public static final String DEFAULT_FORM_NAME = "message";

	private Long identifier;
	private boolean processed;
	private String source;
	private String data;
	private String type;
	private AuditData auditData = new AuditData();
	public Long getIdentifier() {
		return identifier;
	}
	public void setIdentifier(Long identifier) {
		this.identifier = identifier;
	}
	public boolean isProcessed() {
		return processed;
	}
	public void setProcessed(boolean processed) {
		this.processed = processed;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public boolean isInbound(){
		return StringUtils.isNotBlank(type) && "I".equalsIgnoreCase(type);
	}
	
	public boolean isOutbound(){
		return StringUtils.isNotBlank(type) && "O".equalsIgnoreCase(type);
	}
	public AuditData getAuditData() {
		return auditData;
	}
	public void setAuditData(AuditData v) {
		if (v != null) {
			this.auditData = (AuditData) v.clone();
		}
	}
	
	
}
