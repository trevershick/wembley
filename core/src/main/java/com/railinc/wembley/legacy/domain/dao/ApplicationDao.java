package com.railinc.wembley.legacy.domain.dao;

import java.util.List;
import java.util.Map;

import com.railinc.wembley.legacysvc.domain.ApplicationVo;

public interface ApplicationDao {

	List<ApplicationVo> getAllApplications();
	ApplicationVo getApplication(String appId);
	void insertApplication(ApplicationVo vo);
	ApplicationVo updateApplication(String id, ApplicationVo vo);
	void deleteApplication(String appId);

	Map<String, ApplicationVo> getAllApplicationsByKey();

}
