package com.railinc.r2dq.domain.tasks;

import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Sets.newHashSet;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.apache.commons.lang.time.DateUtils;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.railinc.r2dq.domain.AuditData;
import com.railinc.r2dq.domain.Auditable;
import com.railinc.r2dq.domain.DataException;
import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.SourceSystem;
import com.railinc.r2dq.util.GsonUtil;

@Table(name="TASK")
@DiscriminatorColumn(length=32,discriminatorType=DiscriminatorType.STRING,name="TASK_TYPE_CODE")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@Entity
public abstract class Task implements Auditable{


	@Id
	@SequenceGenerator(name="TASK_SEQ", sequenceName="TASK_SEQ", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="TASK_SEQ")
	@Column(name="TASK_ID")
	private Long id;
	public static final String PROPERTY_ID = "id";

	@Version
    @Column(name="VERSION")
    private Integer version;

	@OneToOne(fetch=FetchType.LAZY,optional=true)
	@JoinColumn(name="DELEGATED_FROM_TASK_ID",nullable=true)
	Task delegatedFrom;
	
	@OneToOne(fetch=FetchType.LAZY,optional=true)
	@JoinColumn(name="DELEGATED_TO_TASK_ID",nullable=true)
	Task delegatedTo;

	/**
	 * this is used to create a link from one task to another.
	 * the reason for the link is dependent upon the task types.
	 * for example, you can link task A ( an exception remedation task) to task B ( an exception remediatoin approval task )
	 * because task B needs to know about task A
	 * 
	 */
	@OneToOne(fetch=FetchType.LAZY,optional=true)
	@JoinColumn(name="LINKED_FROM_TASK_ID",nullable=true)
	Task linkedFrom;
	
	@OneToOne(fetch=FetchType.LAZY,optional=true)
	@JoinColumn(name="LINKED_TO_TASK_ID",nullable=true)
	Task linkedTo;

	
	
	/**
	 * The disposition of the task 
	 */
	@Basic
	@Column(name="DISPOSITION_STATUS_CODE",nullable=true)
	@Enumerated(value=EnumType.STRING)
	TaskDisposition disposition = TaskDisposition.Available;
	public static final String PROPERTY_DISPOSITION = "disposition";
	
	
	@Basic
	@Column(name="DISPOSITION_DATE",nullable=true,updatable=true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dispositionDate;

	
	public static final String PROPERTY_CANDIDATE = "candidate";
	@Embedded
	@AttributeOverrides( {
        @AttributeOverride(name="type", column = @Column(name="CANDIDATE_RESP_TYPE_CODE") ),
        @AttributeOverride(name="id", column = @Column(name="CANDIDATE_RESP_REFERENCE") )
	} )
	private Identity candidate;
	
	/**
	 * When someone claims the task, it becomes assigned to him/her
	 */
	public static final String PROPERTY_CLAIMANT = "claimant";

	@Embedded
	@AttributeOverrides( {
        @AttributeOverride(name="type", column = @Column(name="ASSIGNED_RESP_TYPE_CODE") ),
        @AttributeOverride(name="id", column = @Column(name="ASSIGNED_RESP_REFERENCE") )
	} )
	private Identity claimant;
	
	
	@Basic
	@Column(name="DUE_DATE",nullable=true,updatable=true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date due;
	public static final String PROPERTY_DUE_DATE = "due";

	
	@Basic
	@Column(name="NOTIFICATION_SENT_TS",nullable=true,updatable=true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date notificationSent;
	public static final String PROPERTY_NOTIFICATION_SENT_WHEN = "notificationSent";


	
	
	@OneToMany(mappedBy="task",cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private Set<DataException> exceptions;
	
	@Embedded
	private AuditData auditData = new AuditData();

	
	
	//String taskView; // custom rendered view for the exception data.
	
	
	
	public Set<DataException> getExceptions() {
		return exceptions;
	}

	public void setExceptions(Set<DataException> exceptions) {
		this.exceptions = exceptions;
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
	
	public Identity getCandidate() {
		return candidate;
	}
	
	public void setCandidate(Identity candidate) {
		this.candidate = candidate;
	}
	
	public void reassign(Identity candidate) {
		if (candidate == null) {
			return;
		}
		this.disposition = TaskDisposition.Available;
		this.candidate = candidate;
		this.claimant = null;
		this.notificationSent = null;
	}
	
	public boolean claim(Identity candidate) {
		if (isClaimed()) { return false; }
		if (candidate == null) { return false; }
		if (candidate.isSingular()) {
			this.claimant = new Identity(candidate);
			this.disposition = TaskDisposition.Claimed;
			return true;
		}	
		return false;
	}

	public void addDataException(DataException dataException){
		if(exceptions == null){
			exceptions = new HashSet<DataException>();
		}
		dataException.setTask(this);
		exceptions.add(dataException);
	}
	
	public Set<DataException> getDataExceptions(){
		return this.exceptions;
	}

	@Override
	public AuditData getAuditData() {
		this.auditData = Optional.fromNullable(this.auditData).or(new AuditData());
		return this.auditData;
	}

	public abstract String getTaskName();
	public abstract String getTaskDescription();
	
	public Collection<SourceSystem> getSourceSystems() {
		return newHashSet(transform(this.exceptions, new Function<DataException,SourceSystem>(){
			@Override
			public SourceSystem apply(DataException input) {
				return input.getSourceSystem();
			}}));
	
	}

	public boolean isClaimed() {
		return this.claimant != null;
	}
	public boolean isDone() {
		return this.disposition.isFinalState();
	}
	public boolean isDelegated() {
		return this.disposition.isDelegated();
	}
	
	
	public boolean isDue() {
		return hasDueDate() && new Date().after(due);
	}
	
	public boolean hasDueDate() {
		return this.due != null;
	}
	
	public int daysUntilDue() {
		Date d = DateUtils.truncate(due, Calendar.DATE);
		Date n = DateUtils.truncate(new Date(), Calendar.DATE);
		return (int) (d.getTime() - n.getTime()) / (1000 * 60 * 60 * 24);
	}
	
	public Date getDue() {
		return due;
	}

	public void setDue(Date due) {
		this.due = due;
	}

	public Identity getClaimant() {
		return this.claimant;
	}

	public void markComplete() {
		this.dispositionDate = new Date();
		this.disposition = TaskDisposition.Completed;
	}

	public boolean canBeDelegated() {
		return true;
	}

	public Task getLinkedFrom() {
		return linkedFrom;
	}

	public void setLinkedFrom(Task linkedFrom) {
		this.linkedFrom = linkedFrom;
	}

	public Task getLinkedTo() {
		return linkedTo;
	}

	public void setLinkedTo(Task linkedTo) {
		this.linkedTo = linkedTo;
	}
	
	public void markAwaitingApproval() {
		this.disposition = TaskDisposition.AwaitingApproval;
	}

	public TaskDisposition getDisposition() {
		return disposition;
	}

	public void setDisposition(TaskDisposition disposition) {
		this.disposition = disposition;
	}

	public Date getDispositionDate() {
		return dispositionDate;
	}

	public void setDispositionDate(Date dispositionDate) {
		this.dispositionDate = dispositionDate;
	}

	public Date getNotificationSent() {
		return notificationSent;
	}

	public void setNotificationSent(Date d) {
		this.notificationSent = d;
	}

	public boolean canBeReassigned() {
		boolean can = !isClaimed();
		can |= !isDone();
		return can;
	}
	
	public String toJsonString(){
		return GsonUtil.toJson(this, "exceptions.task");
	}


}
