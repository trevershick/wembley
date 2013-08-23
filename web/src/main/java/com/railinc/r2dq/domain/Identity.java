package com.railinc.r2dq.domain;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.railinc.r2dq.sso.SsoRoleType;

public class Identity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9132804756188970264L;


	public static final int MAX_LENGTH_ID = 100;
	
	
	public static final String PROPERTY_TYPE = "type";
	@Column(name="IDENTITY_TYPE",nullable=false,length=IdentityType.MAX_LENGTH)
	@Enumerated(EnumType.STRING)
	private IdentityType type;

	public static final String PROPERTY_ID = "id";


	
	
	@Column(name="IDENTITY_ID",nullable=true,length=MAX_LENGTH_ID)
	private String id;

	
	public Identity() {}
	
	public Identity(IdentityType type, String id) {
		this.type = type;
		this.id = id;
	}
	
	public Identity(Identity from) {
		Preconditions.checkArgument(from != null,"from cannot be null");
		this.type = from.type;
		this.id = from.id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((id == null) ? 0 : id
						.hashCode());
		result = prime
				* result
				+ ((type == null) ? 0 : type
						.hashCode());
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
		Identity other = (Identity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (type != other.type)
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "Identity [type=" + type + ", id=" + id + "]";
	}

	public IdentityType getType() {
		return type;
	}

	public void setType(IdentityType value) {
		this.type = value;
	}
	public void setType(String value) {
		this.type = IdentityType.find(value);
	}

	public String getId() {
		return id;
	}

	public void setId(String value) {
		this.id = value;
	}

	
	public boolean isSingular() {
		return type.isSingular();
	}

	public static Identity forSsoId(String ssoId) {
		Preconditions.checkArgument(isNotBlank(ssoId), "ssoId cannot be blank");
		return new Identity(IdentityType.SsoId, ssoId);
	}
	public static Identity forLocalUserGroup(String groupId) {
		Preconditions.checkArgument(isNotBlank(groupId), "groupId cannot be blank");
		return new Identity(IdentityType.LocalGroup, groupId);
	}
	public static Identity forSsoRole(String roleId) {
		Preconditions.checkArgument(isNotBlank(roleId), "roleId cannot be blank");
		return new Identity(IdentityType.SsoRole, roleId);
	}
	public static Identity forSsoRole(SsoRoleType roleType) {
		return forSsoRole(roleType.roleId());
	}

	public static Identity forExternalEmailUser(String email) {
		Preconditions.checkArgument(isNotBlank(email), "email cannot be blank");
		return new Identity(IdentityType.ExternalEmailUser, email);
	}



	
	public static final Function<Identity, IdentityType> IDENTITY_TYPE_FUNCTION = new Function<Identity, IdentityType>() {
		@Override
		public IdentityType apply(Identity input) {
			return input.getType();
		}};

	public static final Predicate<Identity> TYPE_IS_SSO = new Predicate<Identity>() {
		@Override
		public boolean apply(Identity input) {
			return input.getType() == IdentityType.SsoId;
		}};


	public static final Function<Identity,String> ID_FUNCTION = new Function<Identity, String>() {
		@Override
		public String apply(Identity input) {
			return input.getId();
		}};


	public boolean isEmail() {
		return this.type.isEmail();
	}

	public boolean isSsoUser() {
		return this.type.isSsoUser();
	}


}
