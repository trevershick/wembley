package com.railinc.r2dq.domain.views;

import com.google.common.base.Function;
import com.railinc.r2dq.domain.AuditData;
import com.railinc.r2dq.domain.GenericMessage;
import com.railinc.r2dq.domain.InboundMessage;
import com.railinc.r2dq.domain.OutboundMessage;

public class RawInboundMessageToView implements Function<GenericMessage,RawInboundMessageView> {

	@Override
	public RawInboundMessageView apply(GenericMessage in) {
		RawInboundMessageView v = new RawInboundMessageView();
		v.setAudit(new AuditData(in.getAuditData()));
		v.setData(in.getData());
		v.setIdentifier(in.getIdentifier());
		v.setProcessed(in.isProcessed());
		if(in instanceof InboundMessage){
			v.setSourceOrDest(((InboundMessage)in).getSource().name());
			v.setType("I");
		}
		if(in instanceof OutboundMessage){
			v.setSourceOrDest(((OutboundMessage)in).getOutbound());
			v.setType("O");
		}
		
		return v;
	}

}
