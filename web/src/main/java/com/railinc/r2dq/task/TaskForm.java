package com.railinc.r2dq.task;

import java.util.Date;

import com.railinc.r2dq.domain.Identity;

public class TaskForm {
	public static final String DEFAULT_FORM_NAME = "taskForm";
	
	private Long id;
	private String type;
	private String name;
	private String description;
	
	private boolean assigned;
	private Identity who;
	private Date due;
	private Date created;
	private boolean done;
	
	private Date notificationSent;

	public boolean isNotified() {
		return notificationSent != null;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isAssigned() {
		return assigned;
	}
	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}
	public Identity getWho() {
		return who;
	}
	public void setWho(Identity who) {
		this.who = who;
	}
	public Date getDue() {
		return due;
	}
	public void setDue(Date due) {
		this.due = due;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public boolean isDone() {
		return done;
	}
	public void setDone(boolean done) {
		this.done = done;
	}

	public Date getNotificationSent() {
		return notificationSent;
	}

	public void setNotificationSent(Date notificationSent) {
		this.notificationSent = notificationSent;
	}


}
