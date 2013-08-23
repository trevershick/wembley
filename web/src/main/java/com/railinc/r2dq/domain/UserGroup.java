package com.railinc.r2dq.domain;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Iterables.tryFind;
import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang.StringUtils.equalsIgnoreCase;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;

@Table(name="USER_GROUP")
@Entity(name="usergroup")
@EntityListeners(value=AuditListener.class)
public class UserGroup implements SoftDelete, Auditable {

	public static final String PROPERTY_ID = "identifier";

	public static final String PROPERTY_NAME = "name";

	@Id
    @Column(name="USER_GROUP_REFERENCE",length=25,nullable=false)
	private String identifier;

	@Version
    @Column(name="VERSION")
    private Integer version;

	@Basic
	@Column(name="USER_GROUP_DESCR",length=50,nullable=false)
	private String name;

	@Embedded
	private AuditData auditData = new AuditData();
	
	@Basic
	@Enumerated(value=EnumType.STRING)
	@Column(name="DELETED_IND",nullable=false,updatable=true,length=1, columnDefinition="char")
	private YesNo deleted = YesNo.N;

	public static final String PROPERTY_MEMBERS = "members";

	public static final String PROPERTY_MEMBERS_SSO_ID = PROPERTY_MEMBERS +"." + UserGroupMember.PROPERTY_SSO_ID;
	
	@OneToMany(mappedBy="userGroup",fetch=FetchType.EAGER,cascade=CascadeType.ALL,orphanRemoval=true) // have to have cascade all here or child objects dont' save
	private Set<UserGroupMember> members = new HashSet<UserGroupMember>();
	
	
	public Set<UserGroupMember> getMembers() {

		return members;
	}

	public void setMembers(Set<UserGroupMember> members) {
		this.members = members;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
		if (isBlank(name)) {
			setName(identifier);
		}
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public void setAuditData(AuditData auditData) {
		this.auditData = auditData;
	}

	public YesNo getDeleted() {
		return deleted;
	}

	public void setDeleted(YesNo deleted) {
		this.deleted = deleted;
	}

	public void removerUser(final String ssoUserId) {
		Optional<UserGroupMember> mem = tryFind(this.getMembers(), new Predicate<UserGroupMember>() {
			@Override
			public boolean apply(UserGroupMember arg0) {
				return equalsIgnoreCase(arg0.getSsoId(), ssoUserId);
			}});
		if (mem.isPresent()) {
			this.members.remove(mem.get());
		}
	}
	public void addUser(final String ssoUserId) {
		Optional<UserGroupMember> mem = tryFind(this.getMembers(), new Predicate<UserGroupMember>() {
			@Override
			public boolean apply(UserGroupMember arg0) {
				return equalsIgnoreCase(arg0.getSsoId(), ssoUserId);
			}});
		if (mem.isPresent() && mem.get().isDeleted()) {
			mem.get().undelete();
		}
		if (! mem.isPresent()) {
			members.add(new UserGroupMember(this, ssoUserId));
		}
	}

	@SuppressWarnings("unchecked")
	public Collection<UserGroupMember> getActiveMembers() {
		return filter(getMembers(), Predicates.and(SoftDelete.NOT_DELETED));
	}
	
	
	public void setUserIds(Collection<String> userIds) {
		
		Collection<String> toRemove = newArrayList(getUserIds());
		Iterables.removeAll(toRemove, userIds);
		
		for (String userId : toRemove) {
			removerUser(userId);
		}
		for (String userId : userIds) {
			addUser(userId);
		}
		
	}
	/**
	 * returns active user ids
	 * @return
	 */
	public Collection<String> getUserIds() {
		return transform(getActiveMembers(), new Function<UserGroupMember, String>() {
			@Override
			public String apply(UserGroupMember arg0) {
				return arg0.getSsoId();
			}});
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((identifier == null) ? 0 : identifier.hashCode());
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
		UserGroup other = (UserGroup) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
