package com.railinc.r2dq.sso;

import java.beans.ExceptionListener;
import java.util.Collection;

import org.springframework.stereotype.Service;

@Service
public interface SingleSignOn {

	Collection<String> groupsForUser(String userId, ExceptionListener orNull);

	Collection<String> usersInGroup(String groupId, ExceptionListener orNull);
	Collection<String> userLoginsByEmail(String email, ExceptionListener orNull);

	String emailForUser(String userLogin, ExceptionListener orNull);

	Collection<SingleSignOnUser> usersByEmail(String emailAddress, ExceptionListener orNull);

	SingleSignOnUser userByLogin(String userLogin, ExceptionListener orNull);
}
