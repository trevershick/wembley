package com.railinc.r2dq.task.exceptionremediation;

import static org.apache.commons.lang.StringUtils.abbreviate;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.trimToNull;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.railinc.r2dq.dataexception.TaskCapitalizer;
import com.railinc.r2dq.domain.DataException;

public class ExceptionRemediationFormRow {
	// TODO - replace with remediation dispo enum?
	private enum Scenario {
		nn(new String[] { ACTION_SUGGEST }), // first letter is 'mdm'
		// second letter is 'source system'
		// n = 'null'
		// v = 'has Value'
		nv(new String[] { ACTION_DISAPPROVE, ACTION_IGNORE, ACTION_SUGGEST }), vn(
				new String[] { ACTION_APPROVE, ACTION_DISAPPROVE, ACTION_IGNORE, ACTION_IGNORE }), vv(new String[] {
				ACTION_APPROVE, ACTION_DISAPPROVE, ACTION_IGNORE, ACTION_IGNORE });

		private String[] possibleCommands;

		private Scenario(String[] possibleCommands) {
			this.possibleCommands = possibleCommands;
		}

		private boolean can(String command) {
			return ArrayUtils.contains(possibleCommands, command);
		}
	}
	public static final String ACTION_APPROVE = "approve";
	public static final String ACTION_DISAPPROVE = "disapprove";
	public static final String ACTION_IGNORE = "ignore";
	public static final String ACTION_SUGGEST = "suggest";

	@NotNull
	private String action = "accept";

	private String actualValue;

	@NotNull
	private Long exceptionId;

	private String expectedValue;
	private String field;
	private String fieldTitle;
	private Long ruleNumber;
	private Scenario scenario;
	private String violationDescription;

	private String sourceSystem;
	/**
	 * filled in by the user
	 */
	private String userComment;
	/**
	 * filled in by the user
	 */
	private String userSuggestedValue;


	public String getAcceptActionKey() {
		return getActionKey(ACTION_APPROVE);
	}

	public String getAction() {
		return action;
	}

	private String getActionKey(String action) {
		String l = String.format("task.remed.%s.%s.%d", scenario().name(), action, ruleNumber);
		return l;
	}

	public String getActualValue() {
		return actualValue;
	}

	public String getDescriptionKey() {
		return String.format("task.remed.%s.description.%d", scenario().name(), this.ruleNumber);
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
		return getActionKey(ACTION_SUGGEST);
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

	public boolean isApproval() {
		return StringUtils.equals(action, ACTION_APPROVE);
	}

	public boolean isCanAccept() {
		return scenario().can(ACTION_APPROVE);
	}

	public boolean isCanDisapprove() {
		return scenario().can(ACTION_DISAPPROVE);
	}

	public boolean isCanIgnore() {
		return scenario().can(ACTION_IGNORE);
	}

	public boolean isCanSuggest() {
		return scenario().can(ACTION_SUGGEST);
	}

	public boolean isDisapproval() {
		return StringUtils.equals(action, ACTION_DISAPPROVE);
	}

	public boolean isFailingAssertion() {
		return StringUtils.equals(expectedValue, actualValue) == false;
	}

	public boolean isIgnore() {
		return StringUtils.equals(action, ACTION_IGNORE);
	}

	public boolean isSuggestion() {
		return StringUtils.equals(action, ACTION_SUGGEST);
	}

	private Scenario scenario() {
		if (this.scenario == null) {
			String tmp = (isBlank(expectedValue) ? "n" : "v") + (isBlank(actualValue) ? "n" : "v");
			this.scenario = Scenario.valueOf(tmp);
		}
		return this.scenario;
	}

	public void setAction(String action) {
		this.action = trimToNull(action);
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
		this.field = trimToNull(field);
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
		this.violationDescription = trimToNull(violationDescription);
	}

}
