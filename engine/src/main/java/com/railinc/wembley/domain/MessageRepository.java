package com.railinc.wembley.domain;

import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository {
	Message byId(Long id);
}
