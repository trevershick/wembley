package com.railinc.r2dq.mdm;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.google.gson.GsonBuilder;

@Table(name = "MDM_EXCPTN_STATUS")
@Entity
public class MDMExceptionStatus {
	@EmbeddedId
	private MDMExceptionStatusPK exceptionStatusPK;

	public Long getMDMExceptionId() {
		return exceptionStatusPK != null ? exceptionStatusPK
				.getMDMExceptionId() : null;
	}

	public void setMDMExceptionId(Long mdmExceptionId) {
		createEmptyStatusPKIfNull();
		exceptionStatusPK.setMDMExceptionId(mdmExceptionId);
	}

	public MDMExceptionStatusType getMdmExceptionStatusType() {
		return exceptionStatusPK != null ? exceptionStatusPK
				.getMdmExceptionStatusType() : null;
	}

	public void setMdmExceptionStatusType(MDMExceptionStatusType mdmExceptionStatusType) {
		createEmptyStatusPKIfNull();
		exceptionStatusPK.setMdmExceptionStatusType(mdmExceptionStatusType);

	}

	private void createEmptyStatusPKIfNull() {
		if (exceptionStatusPK == null) {
			exceptionStatusPK = new MDMExceptionStatusPK();
		}
	}
	
	public void setCreatedDate(Date createdDate) {
		createEmptyStatusPKIfNull();
		exceptionStatusPK.setCreatedDate(createdDate);
	}
	
	public Date getCreatedDate(){
		return exceptionStatusPK != null ? exceptionStatusPK.getCreatedDate() : null;
	}

	public static MDMExceptionStatus fromJson(String jsonString) {
		return new GsonBuilder().setDateFormat("yyyy-mm-dd HH:MM").create()
				.fromJson(jsonString, MDMExceptionStatus.class);
	}

	public String toJson() {
		return new GsonBuilder().setDateFormat("yyyy-mm-dd HH:MM").create()
				.toJson(this);
	}

}
