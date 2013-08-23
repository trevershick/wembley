package com.railinc.r2dq.integration;

import com.google.common.base.Function;
import com.railinc.r2dq.dataexception.implementation.ImplementDataException;
import com.railinc.r2dq.domain.OutboundMessage;

public class ImplementDataExceptionToOutboundMessageTransformer implements
		Function<ImplementDataException, OutboundMessage> {

	@Override
	public OutboundMessage apply(ImplementDataException input) {
		OutboundMessage outboundMessage=  new OutboundMessage();
		outboundMessage.setData(input.toJsonString());
		outboundMessage.setOutbound(input.getSourceSystem().getOutboundQueue());
		return outboundMessage;
	}

}
