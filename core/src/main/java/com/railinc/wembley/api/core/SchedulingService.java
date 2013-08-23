package com.railinc.wembley.api.core;


/**
 * Primary interface for dealing with scheduled messages.
 * 
 * @author SDWXD09
 *
 */
public interface SchedulingService extends NotificationService {
	
	/**
	 * Receive an event from some mechanism (e.g., MQ)
	 * 
	 * @param scheduleXml
	 */
	void receiveScheduledEvent( String scheduleXml );
}
