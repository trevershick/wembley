package com.railinc.r2dq.task.admin;

import static com.google.common.collect.Collections2.transform;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import com.google.common.base.Function;
import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.IdentityType;
import com.railinc.r2dq.domain.views.TaskView;
import com.railinc.r2dq.task.AbstractTaskController;
import com.railinc.r2dq.task.MyTasksController;
import com.railinc.r2dq.task.TaskFormSupport;
import com.railinc.r2dq.task.TaskService;
import com.railinc.r2dq.task.manualremediation.ManualRemediationTaskForm;
import com.railinc.r2dq.util.ArgumentCapturingPredicate;
import com.railinc.r2dq.util.PagedCollection;
import com.railinc.r2dq.util.WebFormConstants;
import com.railinc.r2dq.web.FlashMessages;
import com.railinc.r2dq.web.Routes;

@Controller
@RequestMapping(AdminTaskRoutes.ROOT_PATH)
public class AdminTasksController extends AbstractTaskController {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private static final String VIEW_LIST = "admintasks/list";
	private static final String VIEW_RO_TASK = "admintasks/ro";
	private final AdminTaskRoutes routes = new AdminTaskRoutes();

	private TaskService taskService;

	public AdminTasksController() {

	}
	
	@ModelAttribute("personTypes")
	public Collection<String> personTypes() {
		return transform(Arrays.asList(IdentityType.values()), new Function<IdentityType,String>(){
			@Override
			public String apply(IdentityType input) {
				return input.toString();
			}});
	}
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, WebFormConstants.timestampPropertyEditor());
		binder.registerCustomEditor(IdentityType.class, WebFormConstants.identityTypeEditor());
	}

	
	@RequestMapping
	private String redirectToList() {
		return routes.redirectRoute(Routes.STANDARD_LIST);
	}

	@RequestMapping(value=AdminTaskRoutes.RO_PATH, method=RequestMethod.POST,params="_close")
	public String onCloseFromReadOnlyView(SessionStatus s) {
		return redirectToList();
	}
	
	@RequestMapping(value=AdminTaskRoutes.RO_PATH)
	public String showTaskReadOnly(
			HttpServletRequest request, 
			@PathVariable("id") Long taskIdToPerform,
			Model model) {
		// figure out what type of task it is and redirect to that location
		TaskView task = taskService.getTask(taskIdToPerform);
		
		// assuming manual remediation forms at this point.
		ManualRemediationTaskForm taskForm = new ManualRemediationTaskForm();
		new TaskFormSupport().populate(taskForm, task);
		model.addAttribute(ManualRemediationTaskForm.DEFAULT_FORM_NAME, taskForm);
		
		log.debug("Show {}", task);
		return VIEW_RO_TASK;

	}
	


	
	@RequestMapping(value=AdminTaskRoutes.CANCEL_PATH,method=RequestMethod.POST)
	public String cancelTasks(@ModelAttribute(AdminTaskSearchForm.DEFAULT_TASK_NAME) AdminTaskSearchForm mytasks, BindingResult result) {
		throw new RuntimeException("not implemented");
		
	}
	
	@RequestMapping(value=AdminTaskRoutes.NOTIFY_PATH,method=RequestMethod.POST)
	public String resendNotifications(@ModelAttribute(AdminTaskSearchForm.DEFAULT_TASK_NAME) AdminTaskSearchForm mytasks, BindingResult result) {
		throw new RuntimeException("not implemented");
	}
	
	
	@RequestMapping(value=AdminTaskRoutes.LIST_PATH, method=RequestMethod.POST, params="_reassign")
	public String onReassignTasks(HttpServletRequest request,
			@ModelAttribute(AdminTaskSearchForm.DEFAULT_TASK_NAME) AdminTaskSearchForm mytasks, 
			BindingResult result) {
		
		Long[] selectedTaskIds = mytasks.getSelectedTaskIds();

		javax.validation.Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<AdminTaskSearchForm>> violations = validator.validate(mytasks, new Class[]{ AdminTaskSearchForm.ReassignmentChecks.class });
		for (ConstraintViolation<AdminTaskSearchForm> violation : violations) {
			result.reject(violation.getMessageTemplate(), violation.getExecutableParameters(), violation.getMessage());
		}

//		ValidationUtils.rejectIfEmpty(result, "newPersonType", "javax.validation.constraints.NotNull.message");
//		ValidationUtils.rejectIfEmpty(result, "newPerson", "javax.validation.constraints.NotNull.message");
//		if (selectedTaskIds.length == 0) {
//			result.reject("javax.validation.constraints.NotNull.message");
//		}
		if (result.hasErrors()) {
			return VIEW_LIST;
		}

		IdentityType type = mytasks.getNewPersonType();
		String id = mytasks.getNewPerson();
		ArgumentCapturingPredicate<Long> reassigned = new ArgumentCapturingPredicate<Long>();
		ArgumentCapturingPredicate<Long> notreassigned = new ArgumentCapturingPredicate<Long>();
		
		this.taskService.reassignTasksTo(selectedTaskIds, new Identity(type, id), notreassigned, reassigned);
		
		if (! reassigned.captured().isEmpty()) {
			FlashMessages.add(request, "reassignment.completed", new Object[]{ reassigned.captured(), id}, 
				String.format("Task(s) %s reassigned to %s", reassigned.captured(), id));
		}
		if (! notreassigned.captured().isEmpty()) {  
			FlashMessages.add(request, "reassignment.notpossible", new Object[]{ notreassigned.captured(), id}, 
				String.format("Task(s) %s could not be reassigned to %s", notreassigned.captured(), id));
		}
		return showAllTasks(mytasks);
	}

	
	@RequestMapping(value=AdminTaskRoutes.LIST_PATH)
	public String showAllTasks(@ModelAttribute(AdminTaskSearchForm.DEFAULT_TASK_NAME) AdminTaskSearchForm mytasks) {
		AdminTaskCriteria criteria = mytasks.getCriteria();
		PagedCollection<TaskView> all = taskService.all(criteria);
		mytasks.setResults(new PagedCollection<TaskView>(all, all.getPaging()));
		return VIEW_LIST;
	}


	@Required
	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}
}
