package com.railinc.r2dq.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import com.railinc.r2dq.integration.FlowEntity;
import com.railinc.r2dq.integration.FromJson;
import com.railinc.r2dq.integration.ToJson;
import com.railinc.r2dq.util.GsonUtil;

@DiscriminatorValue("O")
@FlowEntity
@Entity
public class OutboundMessage extends GenericMessage {

	
	
	@Transient
	private String sourceEntity;
	@Transient
	private String entityId;
	
	public void setOutbound(String outbound) {
		this.outbound = outbound;
	}
	
	
	public String getOutbound() {
		return outbound;
	}
	
	public String getSourceEntity() {
		return sourceEntity;
	}

	public void setSourceEntity(String sourceEntity) {
		this.sourceEntity = sourceEntity;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	@ToJson
	public String toJsonString() {
	    return GsonUtil.toJson(this);
    }
	
	@FromJson
	public static OutboundMessage fromJsonString(String jsonString){
		Validate.notNull(jsonString);
		 return GsonUtil.fromJson(jsonString, OutboundMessage.class);
	}

	public static boolean isOutbound(String type) {
		// TODO : outboundMessage isOutbound should return 'true', not look at the 'type'
		return StringUtils.isNotBlank(type) && "O".equalsIgnoreCase(type);
	}

}
