package com.railinc.r2dq.task.exceptionremediation;

import java.util.List;

import org.springframework.util.AutoPopulatingList;

import com.google.gson.Gson;
import com.railinc.r2dq.task.TaskForm;


public class ExceptionRemediationTaskForm extends TaskForm {

	public static final String DEFAULT_FORM_NAME = "taskform";

	
	
	private List<ExceptionRemediationFormRow> exceptions = new AutoPopulatingList<ExceptionRemediationFormRow>(ExceptionRemediationFormRow.class);

	
	public void setExceptions(List<ExceptionRemediationFormRow> v) {
		this.exceptions = v;
	}


	public List<ExceptionRemediationFormRow> getExceptions() {
		return exceptions;
	}
	
	public String toString() {
		return new Gson().toJson(this);
	}
}
