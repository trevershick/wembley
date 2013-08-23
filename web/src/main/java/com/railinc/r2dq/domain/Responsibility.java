package com.railinc.r2dq.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.commons.lang.builder.CompareToBuilder;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;

@Entity
@Table(name="RESPONSIBILITY")
public class Responsibility implements SoftDelete, Auditable, Comparable<Responsibility> {
	@Override
	public String toString() {
		return "Responsibility [id=" + id + ", version=" + version
				+ ", sourceSystem=" + sourceSystem + ", ruleNumber="
				+ getRuleNumber() + ", responsiblePerson=" + responsiblePerson
				+ ", note=" + note + ", auditData=" + auditData
				+ ", deleted=" + deleted + "]";
	}

	public static final int MAX_LENGTH_RULENUMBER = 20;
	public static final int MAX_LENGTH_PRECEDENCE = 5;
	
	@Id
	@SequenceGenerator(name="RESPONSIBILITY_SEQ", sequenceName="RESPONSIBILITY_SEQ", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="RESPONSIBILITY_SEQ")
	@Column(name="RESPONSIBILITY_ID")
	private Long id;
	
	@Version
    @Column(name="VERSION")
    private Integer version;
	
	
	
	public static final String PROPERTY_SOURCESYSTEM = "sourceSystem";
	@ManyToOne(optional=true)
	@JoinColumn(name="SOURCE_SYSTEM_CODE", nullable=true)
	SourceSystem sourceSystem;

	public static final String PROPERTY_RULENUMBER_TYPE = "ruleNumberType";
	@Basic(optional=false)
	@Enumerated(value=EnumType.STRING)
	@Column(name="RULE_NBR_TYPE_CODE", nullable=false, length=RuleNumberType.MAX_LENGTH)
	private RuleNumberType ruleNumberType;
	
	public static final String PROPERTY_RULENUMBER_FROM = "ruleNumberFrom";
	@Basic(optional=true)
	@Column(name="RULE_NBR_FROM", nullable=true, length=38)
	private Long ruleNumberFrom;
	
	public static final String PROPERTY_RULENUMBER_THRU = "ruleNumberThru";
	@Basic(optional=true)
	@Column(name="RULE_NBR_THRU", nullable=true, length=38)
	private Long ruleNumberThru;

	public static final String PROPERTY_RESPONSIBLE_PERSON_TYPE = "responsiblePerson." + Identity.PROPERTY_TYPE;
	public static final String PROPERTY_RESPONSIBLE_PERSON_ID = "responsiblePerson." + Identity.PROPERTY_ID;
	
	@Embedded
	@AttributeOverrides( {
        @AttributeOverride(name="type", column = @Column(name="CANDIDATE_RESP_TYPE") ),
        @AttributeOverride(name="id", column = @Column(name="CANDIDATE_RESP_REFERENCE") )
	} )
	private Identity responsiblePerson = new Identity();


	@Embedded
	private Note note = new Note();
	
	@Embedded
	private AuditData auditData = new AuditData();
	
	public static final String PROPERTY_DELETED = "deleted";
	@Basic
	@Enumerated(value=EnumType.STRING)
	@Column(name="DELETED_IND",nullable=false,updatable=true,length=YesNo.MAX_LENGTH, columnDefinition="char")
	private YesNo deleted = YesNo.N;
	
	@Basic(optional = false)
	@Column(name="PRECEDENCE_SEQ_NBR", nullable=false, length=MAX_LENGTH_PRECEDENCE)
	private int precedence;
	public static final String PROPERTY_PRECEDENCE = "precedence";
	
	
	public boolean isDefaultSourceSystemMatch() {
		return sourceSystem == null;
	}
	
	public boolean matches(Long rulenumber) {
		if (isDefaultRule()) { return true; }
		throw new RuntimeException("Not implemented");
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public SourceSystem getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(SourceSystem sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getRuleNumber() {
		if(RuleNumberType.SINGLE.equals(ruleNumberType)){
			return ruleNumberFrom.toString();
		}
		if(RuleNumberType.RANGE.equals(ruleNumberType)){
			return ruleNumberFrom.toString() +"," + ruleNumberThru.toString();
		}
		return null;
	}

	public RuleNumberType getRuleNumberType(){
		return this.ruleNumberType;
	}
	
	public void setRuleNumberType(RuleNumberType ruleNumberType) {
		this.ruleNumberType = ruleNumberType;
	}
	
	public Long getRuleNumberFrom() {
		return ruleNumberFrom;
	}
	
	public void setRuleNumberFrom(Long ruleNumberFrom) {
		this.ruleNumberFrom = ruleNumberFrom;
	}
	
	public Long getRuleNumberThru() {
		return ruleNumberThru;
	}
	
	public void setRuleNumberThru(Long ruleNumberThru) {
		this.ruleNumberThru = ruleNumberThru;
	}

	public Identity getResponsiblePerson() {
		return responsiblePerson;
	}
	
	
	public boolean isDefaultSourceSystem(){
		return sourceSystem == null;
	}
	
	public boolean isDefaultRule(){
		return RuleNumberType.DEFAULT.equals(getRuleNumberType());
	}
	
	public boolean isApplicableForSingleRule(){
		return RuleNumberType.SINGLE.equals(getRuleNumberType());
	}
	
	public boolean isApplicableForRangeOfRules(){
		return RuleNumberType.RANGE.equals(getRuleNumberType());
	}
	
	public static final Predicate<Responsibility> isSourceSystemMatch(final SourceSystem sourceSystem){
		return new Predicate<Responsibility>() {
			@Override
			public boolean apply(Responsibility input) {
				return (input.isDefaultSourceSystem() || input.getSourceSystem().equals(sourceSystem));
			}
		};
	}
	
	public static final Predicate<Responsibility> isRuleNumberMatch(final Long ruleNumber){
		return new Predicate<Responsibility>() {
			@Override
			public boolean apply(Responsibility input) {
				return (input.isDefaultRule() || ((input.isApplicableForSingleRule() && input.getRuleNumberFrom().equals(ruleNumber))
				    	|| (input.isApplicableForRangeOfRules() && ruleNumber>= input.getRuleNumberFrom() && ruleNumber<= input.getRuleNumberThru())));
			}
		};
	} 

	public void setResponsiblePerson(Identity value) {
		if (responsiblePerson == null) {
			value = new Identity();
		}
		this.responsiblePerson = value;
	}


	public IdentityType getResponsiblePersonType() {
		return responsiblePerson.getType();
	}


	public void setResponsiblePersonType(IdentityType value) {
		responsiblePerson.setType(value);
	}


	public String getResponsiblePersonId() {
		return responsiblePerson.getId();
	}


	public void setResponsiblePersonId(String responsiblePersonId) {
		this.responsiblePerson.setId(responsiblePersonId);
	}


	@Override
	public boolean isDeleted() {
		return this.deleted.toBoolean();
	}
	
	@Override
	public void delete() {
		this.deleted = YesNo.Y;
	}

	@Override
	public void undelete() {
		this.deleted = YesNo.N;
	}

	@Override
	public AuditData getAuditData() {
		this.auditData = Optional.fromNullable(this.auditData).or(new AuditData());
		return this.auditData;
	}

	public void setAuditData(AuditData value) {
		this.auditData = value;
	}
	
	public Note getNote() {
		this.note = Optional.fromNullable(note).or(new Note());
		return this.note;
	}
	
	public void setNote(Note note) {
		this.note = note;
	}

	public YesNo getDeleted() {
		return deleted;
	}

	public void setDeleted(YesNo deleted) {
		this.deleted = deleted;
	}


	public String getNoteText() {
		return getNote().getText();
	}
	public void setNoteText(String v) {
		setNote(new Note(v));
	}
	
	public void setPrecedence(int precedence) {
		this.precedence = precedence;
	}
	
	public int getPrecedence() {
		return precedence;
	}
	
	@Override
	public int compareTo(Responsibility other) {
		return new CompareToBuilder().append(this.precedence, other.getPrecedence()).toComparison();
	}
	
	
}
