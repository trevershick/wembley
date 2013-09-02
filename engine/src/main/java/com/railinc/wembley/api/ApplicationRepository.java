package com.railinc.wembley.api;

import org.springframework.stereotype.Repository;

import com.railinc.wembley.domain.Application;

@Repository
public interface ApplicationRepository {

	Application byIdOrDefault(String id);
}
