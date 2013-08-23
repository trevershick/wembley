package com.railinc.r2dq.messages;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import com.railinc.r2dq.domain.InboundSource;
import com.railinc.r2dq.domain.YesNo;
import com.railinc.r2dq.util.CriteriaValue;
import com.railinc.r2dq.util.CriteriaWithPaging;

public class MessageSearchCriteria extends CriteriaWithPaging {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4907077624454865906L;

	private CriteriaValue<ArrayList<String>> data = CriteriaValue.unspecified();
	private CriteriaValue<ArrayList<InboundSource>> sources = CriteriaValue.unspecified();
	private CriteriaValue<YesNo> processed = CriteriaValue.unspecified();
	private CriteriaValue<String> type = CriteriaValue.unspecified();
	
	
	public void setProcessed(boolean b) {
		this.processed = CriteriaValue.of(YesNo.fromBoolean(b));
	}
	
	public void addData(String s) {
		if (StringUtils.isNotBlank(s)) {
			if (data.isUnspecifiedOrNull()) {
				data = CriteriaValue.of(new ArrayList<String>());
			}
			data.value().add(s);
		}
	}
	public void addInboundSource(InboundSource s) {
		if (s != null) {
			if (sources.isUnspecifiedOrNull()) {
				sources = CriteriaValue.of(new ArrayList<InboundSource>());
			}
			sources.value().add(s);
		}
	}
	
	
	public CriteriaValue<ArrayList<String>> getData() {
		return data;
	}

	public CriteriaValue<ArrayList<InboundSource>> getSources() {
		return sources;
	}

	public boolean hasProcessedCriteria() {
		return processed.isSpecified();
	}
	
	public CriteriaValue<YesNo> getProcessed() {
		return processed;
	}
	
	public void setType(String type) {
		this.type = CriteriaValue.of(type);
	}
	
	public CriteriaValue<String> getType() {
		return type;
	}

	
}
