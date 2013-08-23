package com.railinc.r2dq.task.manualremediation;

import java.util.List;

import org.springframework.util.AutoPopulatingList;

import com.google.gson.Gson;
import com.railinc.r2dq.task.TaskForm;


public class ManualRemediationTaskForm extends TaskForm {

	public static final String DEFAULT_FORM_NAME = "taskform";

	
	
	private List<ManualRemediationFormRow> exceptions = new AutoPopulatingList<ManualRemediationFormRow>(ManualRemediationFormRow.class);

	
	public void setExceptions(List<ManualRemediationFormRow> v) {
		this.exceptions = v;
	}


	public List<ManualRemediationFormRow> getExceptions() {
		return exceptions;
	}
	
	public String toString() {
		return new Gson().toJson(this);
	}
}
