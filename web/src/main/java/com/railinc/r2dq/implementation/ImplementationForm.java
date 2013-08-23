package com.railinc.r2dq.implementation;

import java.text.ParseException;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.railinc.r2dq.domain.AuditData;
import com.railinc.r2dq.domain.Implementation;
import com.railinc.r2dq.domain.ImplementationType;
import com.railinc.r2dq.domain.Note;
import com.railinc.r2dq.domain.RuleNumberType;
import com.railinc.r2dq.domain.SourceSystem;
import com.railinc.r2dq.util.NumberRangeParser;

public class ImplementationForm {

	public static final String DEFAULT_FORM_NAME = "implementation";

	@NotNull
	private Long id;

	@NotNull
	private SourceSystem sourceSystem;

	@Size(min=1,max=Implementation.MAX_LENGTH_RULENUMBER)
	@Pattern(regexp="-?\\d+(,-?\\d+)?", message="Rule number should be either null or 1 or 1,3")//,message="Must be either blank or a numeric range like 1,3"
    private String ruleNumber;

	@NotNull
	private ImplementationType implementationType;

	private boolean deleted;
	
	@Size(min=1,max=Note.MAX_LENGTH_NOTES)
	private String note;
	
	private AuditData auditData = new AuditData();
	
	private int version;
	
	@NotNull
	private Integer precedence;
	
	public AuditData getAuditData() {
		return auditData;
	}

	public void setAuditData(AuditData auditData) {
		this.auditData = (AuditData) auditData.clone();
	}

	public ImplementationForm() {
		id = 0L;
		version = 0;
	}
	
	
	
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = StringUtils.trimToNull(note);
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SourceSystem getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(SourceSystem sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getRuleNumber() {
		return ruleNumber;
	}
	
	@AssertTrue
	public boolean isValidImplementionType(){
		return implementationType==null || !(implementationType.isAutomatic() || implementationType.isPassThrough())? true :
		 getSourceSystem()!=null && getSourceSystem().isAutomatic() ;
	}
	
	public RuleNumberType getRuleNumberType(){
		if(getRuleNumberFrom()!=null && getRuleNumberThru()!=null){
			return RuleNumberType.RANGE;
		}
		
		if(getRuleNumberFrom()!=null){
			return RuleNumberType.SINGLE;
		}
		
		return RuleNumberType.DEFAULT;
	}
	
	public Long getRuleNumberFrom(){
		if(StringUtils.isBlank(ruleNumber)){
			return null;
		}
		try {
			return NumberRangeParser.parse(ruleNumber).getMinimumLong();
		} catch (ParseException e) {
		}
		return null;
	}
	
	public Long getRuleNumberThru(){
		if(StringUtils.isBlank(ruleNumber)){
			return null;
		}
		try {
			if(!NumberRangeParser.isMinimumAndMaximumNumbersSame(ruleNumber)){
				return NumberRangeParser.parse(ruleNumber).getMaximumLong();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setRuleNumber(String ruleNumber) {
		this.ruleNumber = StringUtils.trimToNull(ruleNumber);
	}
	
	

	public ImplementationType getImplementationType() {
		return implementationType;
	}

	public void setImplementationType(ImplementationType implementationType) {
		this.implementationType = implementationType;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void setPrecedence(Integer precedence) {
		this.precedence = precedence;
	}
	
	public Integer getPrecedence() {
		return precedence;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
