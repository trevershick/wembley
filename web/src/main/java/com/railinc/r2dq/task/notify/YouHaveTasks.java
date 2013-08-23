package com.railinc.r2dq.task.notify;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;

import com.railinc.r2dq.correspondence.CorrespondenceTemplate;
import com.railinc.r2dq.domain.tasks.Task;
import com.railinc.r2dq.task.MyTaskRoutes;
import com.railinc.r2dq.util.GsonUtil;

public class YouHaveTasks extends CorrespondenceTemplate {
	public YouHaveTasks() {
		addData("routes", new MyTaskRoutes());
	}

	public void setTasks(Collection<Task> tasks) {
		Collection<Task> tmp = newArrayList();
		if (tasks != null) {
			tmp.addAll(tasks);
		}
		addData("tasks", tmp);
	}
	
	public String toJsonString(){
		return GsonUtil.toJson(this,"data");
	}
	
}
