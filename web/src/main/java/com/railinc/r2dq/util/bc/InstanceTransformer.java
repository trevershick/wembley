package com.railinc.r2dq.util.bc;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONObject;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;

public class InstanceTransformer {

	public InstanceTransformer() {
		
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject apply(JobInstance e) {

		JSONObject o = new JSONObject();
		o.put("id", e.getId());
		o.put("jobName", e.getJobName());
		Map<String, JobParameter> parameters = e.getJobParameters().getParameters();
		Map<String,String> ps  = new HashMap<String, String>();
		for (Entry<String, JobParameter> s : parameters.entrySet()) {
			ps.put(s.getKey(), String.valueOf(s.getValue().getValue()));
		}
		o.put("jobParameters", new JSONObject(ps));
		
		return o;
	}
}
