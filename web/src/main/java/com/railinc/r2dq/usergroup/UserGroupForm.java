package com.railinc.r2dq.usergroup;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static org.apache.commons.lang.StringUtils.trimToNull;
import static org.apache.commons.lang.StringUtils.upperCase;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.google.common.collect.Iterables;
import com.railinc.r2dq.domain.AuditData;

public class UserGroupForm {

	public static final String DEFAULT_FORM_NAME = "usergroup";

	@NotNull
	@Size(min=1,max=25)
	String id;
	

	@NotNull
	@Size(min=1,max=50)
	String name;

	@NotNull
	private Integer version;

	
	private boolean deleted;
	
	private AuditData auditData;
	
	private final Set<String> userIds = newHashSet();



	private int userIdExcerptSize = 3;
	
	public int getMemberCount() {
		return userIds.size();
	}


	public AuditData getAuditData() {
		return auditData;
	}

	public void setAuditData(AuditData auditData) {
		this.auditData = (AuditData) auditData.clone();
	}

	public UserGroupForm() {
		version = 0;
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

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = trimToNull(upperCase(id));
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = trimToNull(name);
	}

	public void setUserId(String[] values) {
		setUserIds(Arrays.asList(values));
	}
	public String[] getUserId() {
		return this.userIds.toArray(new String[userIds.size()]);
	}
	
	public void setUserIds(Collection<String> transform) {
		this.userIds.clear();
		this.userIds.addAll(transform);
	}

	public int getUserIdsExcerptSize() {
		return userIdExcerptSize;
	}
	
	public Iterable<String> getUserIdsExcerpt() {
		return Iterables.limit(this.userIds, getUserIdsExcerptSize());
	}
	
	public Set<String> getUserIds() {
		return Collections.unmodifiableSet(userIds);
	}
	
	public Map<String,Boolean> getRequired() {
		Map<String,Boolean> tt = newHashMap();
		tt.put("name", isRequired("name"));
		tt.put("id", isRequired("id"));
		tt.put("userIds", isRequired("userIds"));
		return tt;
	}

	private boolean isRequired(String string) {
		try {
			NotNull req = getClass().getDeclaredField(string).getAnnotation(NotNull.class);
			return req != null;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
	}


	public Map<String,String> getTooltips() {
		Map<String,String> tt = newHashMap();
		tt.put("name", getNameTooltip());
		tt.put("id", "Required - Whatever you, make a tooltip!");
		return tt;
	}
	
	public String getNameTooltip() {
		try {
			Size size = getClass().getDeclaredField("name").getAnnotation(Size.class);
			return String.format("Required - All CAPS, and between %d and %d characters",  
					size.min(),
					size.max());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}

}
