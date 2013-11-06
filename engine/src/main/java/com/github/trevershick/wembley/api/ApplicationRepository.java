package com.github.trevershick.wembley.api;

import org.springframework.stereotype.Repository;

import com.github.trevershick.wembley.domain.Application;

@Repository
public interface ApplicationRepository {

	Application byIdOrDefault(String id);
}
