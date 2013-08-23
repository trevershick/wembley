package com.railinc.r2dq.mdm;

import java.util.Date;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.integration.annotation.Transformer;

import com.google.common.base.Function;
import com.railinc.r2dq.integration.msg.InboundMDMExceptionMessage;


public class MDMExceptionToMDMExceptionStatusTransformer  implements Function<InboundMDMExceptionMessage, String>{
	private MDMExceptionStatusType statusType;

	@Transformer
	public String apply(InboundMDMExceptionMessage input) {
		MDMExceptionStatus status = new MDMExceptionStatus();
		status.setMDMExceptionId(input.getMdmExceptionId());
		status.setMdmExceptionStatusType(getStatusType());
		status.setCreatedDate(new Date());
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
