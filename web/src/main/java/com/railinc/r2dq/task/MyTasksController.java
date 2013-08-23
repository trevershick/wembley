package com.railinc.r2dq.task;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.base.Function;
import com.railinc.r2dq.domain.views.TaskView;
import com.railinc.r2dq.task.exceptionremediation.ExceptionRemediationTaskController;
import com.railinc.r2dq.task.manualremediation.ManualRemediationTaskController;
import com.railinc.r2dq.util.PagedCollection;

@Controller
@RequestMapping("/s/tasks")
public class MyTasksController {
	private final MyTaskRoutes routes = new MyTaskRoutes();
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	private TaskService taskService;

	/**
	 * stores a map of task 'type' to a function that returns a redirect url when passed a taskview
	 */
	private final Map<String, Function<TaskView,String>> taskRedirectFunctions = newHashMap();

	public MyTasksController() {
		taskRedirectFunctions.put(ManualRemediationTaskController.TASK_TO_REDIRECT_FUNCTION.toString(), ManualRemediationTaskController.TASK_TO_REDIRECT_FUNCTION);
		taskRedirectFunctions.put(ExceptionRemediationTaskController.TASK_TO_REDIRECT_FUNCTION.toString(), ExceptionRemediationTaskController.TASK_TO_REDIRECT_FUNCTION);
	}
	
	@RequestMapping("/mytasks/{taskid}")
	public String showMyTask(
			HttpServletRequest request, 
			@PathVariable("taskid") Long taskIdToPerform) {
		// figure out what type of task it is and redirect to that location
		TaskView task = taskService.getTask(taskIdToPerform);
		
		log.debug("Show {}", task);
		Function<TaskView, String> function = taskRedirectFunctions.get(task.getType());
		String redirect = function.apply(task);
		log.debug("Redirect to {}", redirect);
		return redirect;
	}
	
	@RequestMapping()
	public String defaultRoute() {
		return "redirect:/s/tasks/mytasks";
	}
	
	@RequestMapping("/mytasks")
	public String showMyTasks(@ModelAttribute(TaskSearchForm.DEFAULT_TASK_NAME) TaskSearchForm mytasks) {
		TaskCriteria criteria = mytasks.getCriteria();
		PagedCollection<TaskView> all = taskService.getMyTasks(criteria);
		mytasks.setResults(new PagedCollection<TaskView>(all, all.getPaging()));
		return "mytasks/mytasks";
	}


	@Required
	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public static String redirectTo() {
		return "redirect:/s/tasks";
	}
}
