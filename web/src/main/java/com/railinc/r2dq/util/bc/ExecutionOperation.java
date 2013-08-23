package com.railinc.r2dq.util.bc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.BatchStatus;

public enum ExecutionOperation {

	PAUSE("pause", "Pause", "Pause a started or running job", BatchStatus.STARTING, BatchStatus.STARTED),
	RESTART("restart", "Restart","Restart a stopped or failed execution", BatchStatus.FAILED, BatchStatus.STOPPED),
	ABANDON("abandon","Abandon","Abandon a dead execution", BatchStatus.STOPPING),
	NOOP("noop", "No-Op","No Operation");
	
	private String r;
	private String d;
	private final List<BatchStatus> availableIf;
	private String o;

	ExecutionOperation(String op, String readable, String description, BatchStatus ... availableIf) {
		this.o = op;
		this.d = description;
		this.r = readable;
		this.availableIf = Arrays.asList(availableIf);
	}
	
	public Map<String,String> asMap() {
		Map<String,String> m = new HashMap<String, String>();
		m.put("op", this.o);
		m.put("name", this.r);
		m.put("description", this.d);
		return m;
	}
	
	public static ExecutionOperation find(String nameOrValue) {
		if (nameOrValue == null) {
			return NOOP;
		}
		ExecutionOperation valueOf = ExecutionOperation.valueOf(nameOrValue);
		if (valueOf != null) {
			return valueOf;
		}
		for (ExecutionOperation s : values()) {
			if (s.r.equalsIgnoreCase(nameOrValue)) {
				return s;
			}
		}
		return NOOP;
	}
	public boolean isNoop() {
		return this == NOOP;
	}

	public static ExecutionOperation getAvailableCommand(BatchStatus status) {
		if (status == null) { return NOOP; }
		for (ExecutionOperation s : values()) {
			if (s.availableIf.contains(status)) {
				return s;
			}
		}
		return NOOP;
	}
}
