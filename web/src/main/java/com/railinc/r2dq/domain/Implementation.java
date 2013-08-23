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

@Table(name="SOURCE_SYS_IMPLEMENTATION")
@Entity(name="implementation")
public class Implementation implements SoftDelete, Auditable, Comparable<Implementation> {

	public static final int MAX_LENGTH_RULENUMBER = 20;
	public static final int MAX_LENGTH_PRECEDENCE = 10;
	public static final int MAX_LENGTH_TYPE = 50;
	
	@Id
	@SequenceGenerator(name="SOURCE_SYS_IMPLEMENTATION_SEQ", sequenceName="SOURCE_SYS_IMPLEMENTATION_SEQ", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SOURCE_SYS_IMPLEMENTATION_SEQ")
	@Column(name="SOURCE_SYS_IMPLEMENTATION_ID")
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
	@Column(name="RULE_NBR_FROM", nullable=true, length=19)
	private Long ruleNumberFrom;
	
	public static final String PROPERTY_RULENUMBER_THRU = "ruleNumberThru";
	@Basic(optional=true)
	@Column(name="RULE_NBR_THRU", nullable=true, length=19)
	private Long ruleNumberThru;
	
	@Basic(optional = false)
	@Column(name="PRECEDENCE_SEQ_NBR", nullable=false, length=MAX_LENGTH_PRECEDENCE)
	private int precedence;
	public static final String PROPERTY_PRECEDENCE = "precedence";
	
	public static final String PROPERTY_IMPLEMENTATION_TYPE = "type";
	
	@Basic(optional = false)
	@Column(name="IMPLEMENTATION_CODE", nullable=false, length=MAX_LENGTH_TYPE)
	@Enumerated(EnumType.STRING)
	private ImplementationType type;
	

	@Embedded
	@AttributeOverrides({
        @AttributeOverride(name="notes", column = @Column(name="IMPLEMENTATION_NOTE") )
	})
	private Note note = new Note();
	
	@Embedded
	private AuditData auditData = new AuditData();
	
	public static final String PROPERTY_DELETED = "deleted";
	@Basic
	@Enumerated(value=EnumType.STRING)
	@Column(name="DELETED_IND",nullable=false,updatable=true,length=YesNo.MAX_LENGTH, columnDefinition="char")
	private YesNo deleted = YesNo.N;
	
	
	
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

	public RuleNumberType getRuleNumberType() {
		return ruleNumberType;
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
	
	public void setPrecedence(int precedence) {
		this.precedence = precedence;
	}
	
	public int getPrecedence() {
		return precedence;
	}
	
	public ImplementationType getType() {
		return type;
	}
	
	public void setType(ImplementationType type) {
		this.type = type;
	}
	
	public Note getNote() {
		this.note = Optional.fromNullable(note).or(new Note());
		return this.note;
	}
	
	public void setNote(Note note) {
		this.note = note;
	}
	
	public String getNoteText() {
		return getNote().getText();
	}
	
	public void setNoteText(String v) {
		setNote(new Note(v));
	}

	public void setAuditData(AuditData auditData) {
		this.auditData = auditData;
	}

	@Override
	public AuditData getAuditData() {
		this.auditData = Optional.fromNullable(this.auditData).or(new AuditData());
		return this.auditData;
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
	
	public boolean isDefaultRule(){
		return RuleNumberType.DEFAULT.equals(getRuleNumberType());
	}
	
	public boolean isApplicableForSingleRule(){
		return RuleNumberType.SINGLE.equals(getRuleNumberType());
	}
	
	public boolean isApplicableForRangeOfRules(){
		return RuleNumberType.RANGE.equals(getRuleNumberType());
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
	
	public static final Predicate<Implementation> isSourceSystemMatch(final SourceSystem sourceSystem){
		return new Predicate<Implementation>() {
			@Override
			public boolean apply(Implementation input) {
				return (input.getSourceSystem()!=null && input.getSourceSystem().equals(sourceSystem));
			}
		};
	}
	
	public static final Predicate<Implementation> isRuleNumberMatch(final Long ruleNumber){
		return new Predicate<Implementation>() {
			@Override
			public boolean apply(Implementation input) {
				return input!=null && (input.isDefaultRule() || ((input.isApplicableForSingleRule() && input.getRuleNumberFrom().equals(ruleNumber))
				    	|| (input.isApplicableForRangeOfRules() && ruleNumber>= input.getRuleNumberFrom() && ruleNumber<= input.getRuleNumberThru())));
			}
		};
	}
	
	public ImplementationType getDerivedType(){
		if(getType() == null){
			return ImplementationType.StormDrain;
		}
		
		if((getType().isPassThrough() || getType().isAutomatic() ) && !getSourceSystem().isAutomatic()){
			return ImplementationType.ForceStormDrain;
		}
		return getType();
	}
	
	@Override
	public int compareTo(Implementation other) {
		return new CompareToBuilder().append(this.precedence, other.getPrecedence()).toComparison();
	}

}
