package com.railinc.r2dq.task;

import static com.google.common.collect.Maps.newHashMap;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.railinc.r2dq.domain.views.DataExceptionView;
import com.railinc.r2dq.domain.views.TaskView;
import com.railinc.r2dq.task.exceptionremediation.ExceptionRemediationFormRow;
import com.railinc.r2dq.task.exceptionremediation.ExceptionRemediationTaskForm;
import com.railinc.r2dq.task.manualremediation.ManualRemediationFormRow;
import com.railinc.r2dq.task.manualremediation.ManualRemediationTaskForm;

public class TaskFormSupport {

	public TaskForm populate(ExceptionRemediationTaskForm f, TaskView tv) {
		_populate(f, tv);
		List<DataExceptionView> es = tv.getExceptions();

		// collect all source system object data.
		Map<String, String> sourceSystemObjectData = newHashMap();
		for (DataExceptionView dev : es) {
			sourceSystemObjectData.putAll(dev.getSourceSystemObjectData());
		}

		for (DataExceptionView dev : es) {
			sourceSystemObjectData.remove(dev.getMdmObjectAttribute());

			ExceptionRemediationFormRow rf = new ExceptionRemediationFormRow();
			rf.setExceptionId(dev.getId());
			rf.setSourceSystem(dev.getSourceSystem().getName());
			rf.setRuleNumber(dev.getRuleNumber());
			rf.setViolationDescription(dev.getDescription());
			rf.setActualValue(dev.getSourceSystemValue());
			rf.setExpectedValue(dev.getMdmAttributeValue());
			rf.setField(dev.getMdmObjectAttribute());
			rf.setFieldTitle(dev.getMdmObjectAttribute());
			f.getExceptions().add(rf);
		}

		for (Entry<String, String> dev : sourceSystemObjectData.entrySet()) {
			ExceptionRemediationFormRow rf = new ExceptionRemediationFormRow();
			rf.setActualValue(dev.getValue());
			rf.setExpectedValue(dev.getValue());
			rf.setField(dev.getKey());
			rf.setFieldTitle(dev.getKey());
			f.getExceptions().add(rf);
		}

		return f;
	}
	
	
	
	public TaskForm populate(ManualRemediationTaskForm f, TaskView tv) {
		_populate(f, tv);
		List<DataExceptionView> es = tv.getExceptions();
		
		
		// collect all source system object data.
		Map<String, String> sourceSystemObjectData = newHashMap();
		for (DataExceptionView dev : es) {
			sourceSystemObjectData.putAll(dev.getSourceSystemObjectData());
		}
		
		for (DataExceptionView dev : es) {
			sourceSystemObjectData.remove(dev.getMdmObjectAttribute());

			ManualRemediationFormRow rf = new ManualRemediationFormRow();
			rf.setExceptionId(dev.getId());
			rf.setSourceSystem(dev.getSourceSystem().getName());
			rf.setRuleNumber(dev.getRuleNumber());
			rf.setViolationDescription(dev.getDescription());
			rf.setActualValue(dev.getSourceSystemValue());
			rf.setExpectedValue(dev.getMdmAttributeValue());
			rf.setField(dev.getMdmObjectAttribute());
			rf.setFieldTitle(dev.getMdmObjectAttribute());
			f.getExceptions().add(rf);
		}
		
		for (Entry<String, String> dev : sourceSystemObjectData.entrySet()) {
			ManualRemediationFormRow rf = new ManualRemediationFormRow();
			rf.setActualValue(dev.getValue());
			rf.setExpectedValue(dev.getValue());
			rf.setField(dev.getKey());
			rf.setFieldTitle(dev.getKey());
			f.getExceptions().add(rf);
		}
		





		

		return f;
	}
	protected TaskForm _populate(TaskForm f, TaskView tv) {
		f.setId(tv.getId());
		f.setType(tv.getType());
		f.setName(tv.getName());
		f.setDescription(tv.getDescription());
		f.setAssigned(tv.isAssigned());
		f.setWho(tv.getWho());
		f.setDue(tv.getDue());
		f.setCreated(tv.getCreated());
		f.setDone(tv.isDone());
		f.setNotificationSent(tv.getNotificationSent());
		return f;
	}
}
