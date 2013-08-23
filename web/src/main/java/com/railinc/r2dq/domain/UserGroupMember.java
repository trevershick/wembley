package com.railinc.r2dq.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.google.common.base.Optional;

@Table(name="USER_GROUP_MEMBER")
@Entity(name="usergroupmember")
@EntityListeners(value=AuditListener.class)
public class UserGroupMember implements Auditable, SoftDelete {

	
	@Id
	@SequenceGenerator(name="USER_GROUP_MEMBER_SEQ", sequenceName="USER_GROUP_MEMBER_SEQ", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="USER_GROUP_MEMBER_SEQ")
    @Column(name="USER_GROUP_MEMBER_ID")
	private Long identifier;

	
	@ManyToOne
	@JoinColumn(name="USER_GROUP_REFERENCE",nullable=false,updatable=false)
	UserGroup userGroup;

	public static final String PROPERTY_SSO_ID = "ssoId";
	@Basic
	@Column(name="SSO_USER_LOGIN",length=25,nullable=false,updatable=false)
	private String ssoId;

	@Basic
	@Enumerated(value=EnumType.STRING)
	@Column(name="DELETED_IND",nullable=false,updatable=true,length=1, columnDefinition="char")
	private YesNo deleted = YesNo.N;

	@Embedded
	private AuditData audit = new AuditData();

	public UserGroupMember(){}
	public UserGroupMember(UserGroup userGroup2, String ssoUserId) {
		this.userGroup = userGroup2;
		this.ssoId = ssoUserId;
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
		this.audit = Optional.fromNullable(this.audit).or(new AuditData());
		return this.audit;
	}

	public AuditData getAudit() {
		return audit;
	}

	public void setAudit(AuditData audit) {
		this.audit = audit;
	}

	public YesNo getDeleted() {
		return deleted;
	}

	public void setDeleted(YesNo deleted) {
		this.deleted = deleted;
	}

	

	public Long getIdentifier() {
		return identifier;
	}
	public String getSsoId() {
		return ssoId;
	}
	public void setSsoId(String ssoId) {
		this.ssoId = ssoId;
	}
	public void setIdentifier(Long identifier) {
		this.identifier = identifier;
	}
	public UserGroup getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ssoId == null) ? 0 : ssoId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserGroupMember other = (UserGroupMember) obj;
		if (ssoId == null) {
			if (other.ssoId != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}



}
