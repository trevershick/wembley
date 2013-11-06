package com.github.trevershick.wembley.api;
/**
 * Who's the source of the notification?  This is generally going to be an application.
 * This will be used for determining the pipeline and templating, etc...
 * 
 * @author trevershick
 *
 */
public interface NotificationSource {
	String identifier();
}
