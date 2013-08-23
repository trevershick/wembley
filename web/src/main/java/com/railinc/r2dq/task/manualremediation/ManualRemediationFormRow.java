package com.railinc.r2dq.task.manualremediation;

import static org.apache.commons.lang.StringUtils.abbreviate;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.trimToNull;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.railinc.r2dq.dataexception.TaskCapitalizer;
import com.railinc.r2dq.domain.DataException;



public class ManualRemediationFormRow {
	enum Scenario {
		nn(new String[]{ACTION_APPROVE}),
		// first letter is 'mdm'
		// second letter is 'source system'
		// n = 'null'
		// v = 'has Value'
		nv(new String[]{ACTION_DISAPPROVE,ACTION_IGNORE}),
		vn(new String[]{ACTION_APPROVE,ACTION_DISAPPROVE,ACTION_IGNORE}),
		vv(new String[]{ACTION_APPROVE,ACTION_DISAPPROVE,ACTION_IGNORE});
		
		private String[] possibleCommands;

		private Scenario(String[] possibleCommands) {
			this.possibleCommands = possibleCommands;
		}
		private boolean can(String command) {
			return ArrayUtils.contains(possibleCommands, command);
		}
	}


	private static final String ACTION_APPROVE = "approve";
	private static final String ACTION_DISAPPROVE = "disapprove";
	private static final String ACTION_IGNORE = "ignore";
	
	@NotNull
	private String action = ACTION_APPROVE;
	private String actualValue;

	@NotNull
	private Long exceptionId;
	
	private String expectedValue;
	
	private String field;
	private String fieldTitle;

	private Long ruleNumber;
	private Scenario scenario;
	private String sourceSystem;
	private String userComment;
	private String userSuggestedValue;
	private String violationDescription;

	
	public String getAction() {
		return action;
	}

	private String getActionKey(String action) {
		String l = String.format("task.manremed.%s.%s.%d",scenario().name(), action, ruleNumber);
		return l;
	}
	
	public String getActualValue() {
		return actualValue;
	}
	public String getApproveActionKey() {
		return getActionKey(ACTION_APPROVE);
	}
	public String getDescriptionKey() {
		return String.format("task.manremed.%s.description.%d", scenario().name(), this.ruleNumber);
	}
	public String getDisapproveActionKey() {
		return getActionKey(ACTION_DISAPPROVE);
	}
	public Long getExceptionId() {
		return exceptionId;
	}
	public String getExpectedValue() {
		return expectedValue;
	}
	
	public String getField() {
		return field;
	}
	public String getFieldTitle() {
		return new TaskCapitalizer().apply(fieldTitle);

	}

	public String getIgnoreActionKey() {
		return getActionKey(ACTION_IGNORE);
	}

	public Long getRuleNumber() {
		return ruleNumber;
	}

	public String getSourceSystem() {
		return sourceSystem;
	}

	public String getSuggestActionKey() {
		return getActionKey("suggest");
	}

	public String getUserComment() {
		return userComment;
	}

	public String getUserSuggestedValue() {
		return userSuggestedValue;
	}

	public String getViolationDescription() {
		return violationDescription;
	}

	public boolean isApprove() {
		return StringUtils.equals(action, ACTION_APPROVE);
	}

	public boolean isCanApprove() {
		return scenario().can(ACTION_APPROVE);
	}

	public boolean isCanDisapprove() {
		return scenario().can(ACTION_DISAPPROVE);
	}

	public boolean isCanIgnore() {
		return scenario().can(ACTION_IGNORE);
	}

	public boolean isDisapprove() {
		return StringUtils.equals(action, ACTION_DISAPPROVE);
	}

	public boolean isFailingAssertion() {
		return StringUtils.equals(expectedValue, actualValue) == false;
	}

	public boolean isIgnore() {
		return StringUtils.equals(action, ACTION_IGNORE);
	}

	private Scenario scenario() {
		if (this.scenario == null) {
			String tmp = (isBlank(expectedValue) ? "n" : "v") + (isBlank(actualValue) ? "n" : "v");
			this.scenario = Scenario.valueOf(tmp);
		}
		return this.scenario;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setActualValue(String actualValue) {
		this.actualValue = trimToNull(actualValue);
	}

	public void setExceptionId(Long exceptionId) {
		this.exceptionId = exceptionId;
	}

	public void setExpectedValue(String expectedValue) {
		this.expectedValue = trimToNull(expectedValue);
	}

	public void setField(String field) {
		this.field = field;
	}

	public void setFieldTitle(String fieldTitle) {
		this.fieldTitle = trimToNull(fieldTitle);
	}

	public void setRuleNumber(Long ruleNumber) {
		this.ruleNumber = ruleNumber;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = trimToNull(sourceSystem);
	}

	public void setUserComment(String v) {
		this.userComment = abbreviate(trimToNull(v), DataException.MAX_LENGTH_USER_COMMENT);
	}


	public void setUserSuggestedValue(String userSuggestedValue) {
		this.userSuggestedValue = trimToNull(userSuggestedValue);
	}

	public void setViolationDescription(String violationDescription) {
		this.violationDescription = violationDescription;
	}

	

}
