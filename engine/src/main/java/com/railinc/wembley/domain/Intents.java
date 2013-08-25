package com.railinc.wembley.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.railinc.wembley.api.Intent;

@Entity
public class Intents {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long id;
	
	@ManyToOne(optional=false)
	private Message message;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(length=25)
	private Intent intent = Intent.Email;

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

	
	
}
