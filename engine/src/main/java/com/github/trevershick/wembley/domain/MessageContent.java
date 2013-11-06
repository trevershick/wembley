package com.github.trevershick.wembley.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import com.github.trevershick.wembley.api.Intent;

@Entity
public class MessageContent {
	
	private Long id;
	
	private Long messageId;

	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(length=25)
	private Intent intent = Intent.Email;

	@Basic
	@Column(length=2048)
	private String content;
	
	@Basic
	@Column(length=25)
	private String contentType;
	/**
	 * May be null.  If this is set, then this message content is the specific content for the destination
	 */
	private Long destinationId;
	
	
	public MessageContent(long id, long messageId, Intent intent, String content, String contentType,
			long destinationId) {
		this.id = id;
		this.messageId = messageId;
		this.intent = intent;
		this.content = content;
		this.contentType = contentType;
		this.destinationId = destinationId;
	}
	/**
	 * May be null if the mesage going out isnt' template based.
	 */

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Intent getIntent() {
		return intent;
	}
	public void setIntent(Intent intent) {
		this.intent = intent;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}
