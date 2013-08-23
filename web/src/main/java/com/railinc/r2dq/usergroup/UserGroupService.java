package com.railinc.r2dq.usergroup;

import java.util.Collection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.railinc.r2dq.domain.UserGroup;

@Service
@Transactional
public interface UserGroupService {
	Collection<UserGroup> all(String filter);
	void save(UserGroup s);
	void delete(UserGroup s);
	UserGroup get(String id);
	void undelete(UserGroup ss);
	Collection<UserGroup> groupsForUser(String userLogin);
}
