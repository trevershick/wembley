package com.railinc.wembley.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.railinc.wembley.api.Intent;

@Entity
public class MessageContent {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long id;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="MSG_ID", nullable=false)
	@NotNull
	private Message message;

	
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
	@ManyToOne
	@JoinColumn(name="DEST_ID", nullable=true)
	private Destination destination;
	/**
	 * May be null if the mesage going out isnt' template based.
	 */
	@ManyToOne
	@JoinColumn(name="TMPK_ID", nullable=true)
	private TemplatePack templatePack;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
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
	public Destination getDestination() {
		return destination;
	}
	public void setDestination(Destination destination) {
		this.destination = destination;
	}
	public TemplatePack getTemplatePack() {
		return templatePack;
	}
	public void setTemplatePack(TemplatePack templatePack) {
		this.templatePack = templatePack;
	}
	
	
}
