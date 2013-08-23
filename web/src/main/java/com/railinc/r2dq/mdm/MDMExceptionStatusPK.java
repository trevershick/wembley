package com.railinc.r2dq.mdm;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

public class MDMExceptionStatusPK implements Serializable {
	private static final long serialVersionUID = 1L;

	@Basic(optional=false)
    @Column(name="EXCPTN_ID")
	@NotNull
	private Long mdmExceptionId;
	
	@Basic(optional=false)
	@Column(name="EXCPTN_STATUS", nullable=false)
	@Enumerated(EnumType.STRING)
	@NotNull
	private MDMExceptionStatusType mdmExceptionStatusType;
	
	
	@Basic(optional = false)
	@Column(name = "CREATED_TS", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date createdDate;
	
	
	public Long getMDMExceptionId() {
		return mdmExceptionId;
	}
	
	public void setMDMExceptionId(Long mdmExceptionId) {
		this.mdmExceptionId = mdmExceptionId;
	}
	
	public MDMExceptionStatusType getMdmExceptionStatusType() {
		return mdmExceptionStatusType;
	}
	
	public void setMdmExceptionStatusType(MDMExceptionStatusType mdmExceptionStatusType) {
		this.mdmExceptionStatusType = mdmExceptionStatusType;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}
