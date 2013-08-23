package com.railinc.r2dq.integration;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.integration.annotation.Transformer;

import com.google.common.base.Function;
import com.railinc.r2dq.dataexception.event.DataExceptionEvent;
import com.railinc.r2dq.mdm.MDMExceptionStatus;
import com.railinc.r2dq.mdm.MDMExceptionStatusType;

public class DataExceptionEventToMDMExceptionStatusTransformer implements Function<DataExceptionEvent, String> {
	private MDMExceptionStatusType statusType;
	

	public DataExceptionEventToMDMExceptionStatusTransformer() {
	}
	
	@Transformer
	public String apply(DataExceptionEvent dataExceptionEvent) {
		MDMExceptionStatus status = new MDMExceptionStatus();
		status.setMDMExceptionId(dataExceptionEvent.getMdmExceptionId());
		status.setMdmExceptionStatusType(statusType);
		status.setCreatedDate(dataExceptionEvent.getEventDate());
		return status.toJson();
	}
	
	public MDMExceptionStatusType getStatusType() {
		return statusType;
	}
	
	
	@Required
	public void setStatusType(MDMExceptionStatusType statusType) {
		this.statusType = statusType;
	}

}
