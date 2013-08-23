package com.railinc.wembley.legacy.senders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractMessageBuilder implements MessageBuilder {

	private static final Logger log = LoggerFactory.getLogger(AbstractMessageBuilder.class);
	
	
	public AbstractMessageBuilder() {
		log.info( "Instantiating AbstractMessageBuilder"  );
	}

}
