package com.railinc.wembley.legacy.senders;

public interface EmailSenderService extends SenderService {
	/**
	 * Simple email sending method. 
	 * @param from
	 * @param to
	 * @param subject
	 * @param body
	 * @param attachment
	 * @param contentType
	 * @param fileName
	 */
	public void sendEmail(String from, String to, String subject, String body, byte[] attachment, String contentType, String fileName);
	

}
