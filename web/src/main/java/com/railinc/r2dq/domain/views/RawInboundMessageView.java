package com.railinc.r2dq.domain.views;

import org.apache.commons.lang.StringUtils;

import com.railinc.r2dq.domain.AuditData;

public class RawInboundMessageView {
	private Long identifier;
	private boolean processed = false;
	private String sourceOrDest;
	private String data;
	private String type;
	private AuditData audit = new AuditData();
	
	public AuditData getAuditData() {
		return audit;
	}
	
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
	
	public void setSourceOrDest(String sourceOrDest) {
		this.sourceOrDest = sourceOrDest;
	}
	
	public String getSourceOrDest() {
		return sourceOrDest;
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
	
	public AuditData getAudit() {
		return audit;
	}
	public void setAudit(AuditData audit) {
		this.audit = audit;
	}

	
}
