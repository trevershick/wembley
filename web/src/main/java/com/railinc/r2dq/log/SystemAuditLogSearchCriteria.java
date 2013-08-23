package com.railinc.r2dq.log;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import com.railinc.r2dq.util.CriteriaValue;
import com.railinc.r2dq.util.CriteriaWithPaging;

public class SystemAuditLogSearchCriteria extends CriteriaWithPaging {
	
	private static final long serialVersionUID = 4907077624454865906L;
	
	private Direction direction = Direction.Up;
	private CriteriaValue<String> entityName = CriteriaValue.unspecified();
	private CriteriaValue<String> entityId = CriteriaValue.unspecified();
	private CriteriaValue<ArrayList<String>> masterIds = CriteriaValue.unspecified();
	private CriteriaValue<ArrayList<String>> data = CriteriaValue.unspecified();
	private boolean findAllExceptions = false;
	
	
	
	public void setEntityName(String s) {
		entityName = CriteriaValue.orUnspecified(s);
	}
	
	public void setEntityId(String s) {
		entityId = CriteriaValue.orUnspecified(s);
	}
	
	public void addMasterId(String s) {
		if (StringUtils.isNotBlank(s)) {
			if (masterIds.isUnspecifiedOrNull()) {
				masterIds = CriteriaValue.of(new ArrayList<String>());
			}
			masterIds.value().add(s);
		}
	}
	
	
	public void addData(String s) {
		if (StringUtils.isNotBlank(s)) {
			if (data.isUnspecifiedOrNull()) {
				data = CriteriaValue.of(new ArrayList<String>());
			}
			data.value().add(s);
		}
	}
	
	public void clearEntityName() {
		if (null != entityName.value()) {
			entityName = CriteriaValue.unspecified();
		}
	}
	
	public void clearEntityId() {
		if (null != entityId.value()) {
			entityId = CriteriaValue.unspecified();
		}
	}
	
	public void clearMasterIds() {
		if (null != masterIds.value()) {
			masterIds.value().clear();
		}
	}
	
	public void clearData() {
		if (null != data.value()) {
			data.value().clear();
		}
	}
	
	public void clearAll() {
		clearEntityName();
		clearEntityId();
		clearMasterIds();
		clearData();
	}
	
	public CriteriaValue<String> getEntityId() {
		return entityId;
	}
	
	public CriteriaValue<String> getEntityName() {
		return entityName;
	}
	
	public CriteriaValue<ArrayList<String>> getData() {
		return data;
	}

	public CriteriaValue<ArrayList<String>> getMasterIds() {
		return masterIds;
	}

	public void setFindAllExceptions(boolean findAllExceptions) {
		this.findAllExceptions = findAllExceptions;
	}

	public boolean isFindAllExceptions() {
		return findAllExceptions;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Direction getDirection() {
		return direction;
	}

	public boolean isUp() {
		return this.direction == Direction.Up;
	}



}
