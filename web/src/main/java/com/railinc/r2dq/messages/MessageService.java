package com.railinc.r2dq.messages;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.railinc.r2dq.domain.GenericMessage;
import com.railinc.r2dq.domain.InboundMessage;
import com.railinc.r2dq.domain.OutboundMessage;
import com.railinc.r2dq.domain.views.RawInboundMessageView;
import com.railinc.r2dq.util.PagedCollection;

@Service
@Transactional
public interface MessageService {
	PagedCollection<RawInboundMessageView> all(MessageSearchCriteria criteria);
	void save(InboundMessage s);
	void delete(InboundMessage s);
	GenericMessage get(Long id);
	void sendMessage(String data);
	void markAsProcessed(InboundMessage inboundMessage);
	void send(OutboundMessage outboundMessage);
	void log(OutboundMessage outboundMessage);
	void save(OutboundMessage outboundMessage);
	void resend(OutboundMessage outboundMessage);
}
