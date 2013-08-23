package com.railinc.r2dq.integration;

import com.google.common.base.Function;
import com.railinc.r2dq.domain.DataException;
import com.railinc.r2dq.domain.OutboundMessage;

public class DataExceptionToOutboundMessageTransformer implements
		Function<DataException, OutboundMessage> {

	@Override
	public OutboundMessage apply(DataException input) {
		OutboundMessage message = new OutboundMessage();
		message.setData(input.toJsonString());
		message.setOutbound(input.getSourceSystem().getOutboundQueue());
		message.setSourceEntity(input.getClass().getSimpleName());
		message.setEntityId(input.getId() != null ? input.getId().toString() : "");
		return message;
	}

}
