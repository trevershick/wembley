package com.railinc.r2dq.task.notify;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.annotation.Before;
import org.junit.Test;

import com.railinc.r2dq.correspondence.BasicTemplateTest;
import com.railinc.r2dq.correspondence.Correspondence;
import com.railinc.r2dq.domain.DataException;
import com.railinc.r2dq.domain.SourceSystem;
import com.railinc.r2dq.domain.tasks.ExceptionRemediationTask;
import com.railinc.r2dq.domain.tasks.Task;

public class YouHaveTasksTest extends BasicTemplateTest {

	
	
	@Test
	public void worksAtAll() {
		when(this.configServiceMock.getRailincUrl()).thenReturn("http://www.whatup.com");
		
		YouHaveTasks t = new YouHaveTasks();
		
		t.setTasks(new ArrayList<Task>());
		Correspondence c = process(t);
		assertIsComplete(c);
	}
	
	

	@Test
	public void showsTasksInList() {
		when(this.configServiceMock.getRailincUrl()).thenReturn("http://www.whatup.com");
		when(this.configServiceMock.getApplicationUrl()).thenReturn("http://localhost:8080/r2dq");
		
		YouHaveTasks t = new YouHaveTasks();
		
		DataException de1 = new DataException(); de1.setSourceSystem(new SourceSystem("FUR", "FindUs.Rail"));
		DataException de2 = new DataException(); de2.setSourceSystem(new SourceSystem("SSO", "Single-Sign On"));
		
		ExceptionRemediationTask t1 = new ExceptionRemediationTask();
		t1.addDataException(de1);
		t1.setId(1L);
		
		ExceptionRemediationTask t2 = new ExceptionRemediationTask();
		t2.addDataException(de2);
		t2.setId(2L);
		
		List<Task> tasks = newArrayList();
		tasks.add(t1);
		tasks.add(t2);
		
		t.setTasks(tasks);
		Correspondence c = process(t);
		System.out.println(c.getTextHtml());
		
		assertIsComplete(c);
	}
	
}
