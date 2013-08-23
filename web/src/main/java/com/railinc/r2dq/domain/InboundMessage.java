package com.railinc.r2dq.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import com.railinc.r2dq.integration.FlowEntity;
import com.railinc.r2dq.integration.FromJson;
import com.railinc.r2dq.integration.ToJson;
import com.railinc.r2dq.util.GsonUtil;

@FlowEntity
@DiscriminatorValue("I")
@Entity
public class InboundMessage extends GenericMessage {
	
	
	public InboundSource getSource() {
		return source;
	}
	
	public void setSource(InboundSource source) {
		this.source = source;
	}
	
	public static boolean isInbound(String type){
		// TODO : seriously? InboundMEssage returns true, outboundMessage returns false?
		return StringUtils.isNotBlank(type) && "I".equalsIgnoreCase(type);
	}
	
	@ToJson
	public String toJsonString() {
	    return GsonUtil.toJson(this);
    }
	
	@FromJson
	public static InboundMessage fromJsonString(String jsonString){
		Validate.notNull(jsonString);
		 return GsonUtil.fromJson(jsonString, InboundMessage.class);
	}

}
