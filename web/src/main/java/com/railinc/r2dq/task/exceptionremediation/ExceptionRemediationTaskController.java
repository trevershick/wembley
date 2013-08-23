package com.railinc.r2dq.task.exceptionremediation;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.google.common.base.Function;
import com.railinc.r2dq.domain.tasks.ExceptionRemediationTask;
import com.railinc.r2dq.domain.views.TaskView;
import com.railinc.r2dq.task.AbstractTaskController;
import com.railinc.r2dq.task.AlreadyDoneException;
import com.railinc.r2dq.task.MyTasksController;
import com.railinc.r2dq.task.TaskFormSupport;
import com.railinc.r2dq.web.FlashMessages;

@SessionAttributes(types={ExceptionRemediationTaskForm.class})
@RequestMapping("/s/tasks/remediateexception")
public class ExceptionRemediationTaskController extends AbstractTaskController {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	public static final Function<TaskView, String> TASK_TO_REDIRECT_FUNCTION = new Function<TaskView, String>() {
		@Override
		public String apply(TaskView input) {
			return "redirect:/s/tasks/remediateexception/task/" + input.getId();
		}
		public String toString() {
			return ExceptionRemediationTask.class.getSimpleName();
		}
	};

	
	@RequestMapping(value="/task/{taskid}", method=RequestMethod.GET)
	public String showTaskForm(@PathVariable("taskid") Long taskIdToPerform, Model model) {
		ExceptionRemediationTaskForm taskForm = new ExceptionRemediationTaskForm();
		log.debug("taskForm={}",taskForm);
		TaskView task = getTaskService().getTask(taskIdToPerform);
		
		new TaskFormSupport().populate(taskForm, task);
		model.addAttribute(ExceptionRemediationTaskForm.DEFAULT_FORM_NAME, taskForm);
		return "tasks/remediateexception/task";
	}

	@RequestMapping(value="/task/{taskid}", method=RequestMethod.POST,params="_cancel")
	public String handleTaskSubmission(SessionStatus s) {
		s.setComplete();
		return MyTasksController.redirectTo();
	}

	@RequestMapping(value="/task/{taskid}", method=RequestMethod.POST)
	public String handleTaskSubmission(
			HttpServletRequest request,
			@PathVariable("taskid") Long taskIdToPerform, 
			@Valid @ModelAttribute(ExceptionRemediationTaskForm.DEFAULT_FORM_NAME) ExceptionRemediationTaskForm taskForm, 
			BindingResult result,
			SessionStatus status) {
		
		log.debug("taskForm={}",taskForm);
		log.debug("result={}", result);
		
		if (result.hasErrors()) {
			return "tasks/remediateexception/task";
		}
		
		ExceptionRemediationTaskCompletion r = new ExceptionRemediationTaskCompletion();
		r.setTaskId(taskIdToPerform);

		for (ExceptionRemediationFormRow s : taskForm.getExceptions()) {
			Long exceptionId = s.getExceptionId();
			if (exceptionId == null) {
				continue;
			}
			r.addComment(exceptionId, s.getUserComment());
			String userSuggested = s.getUserSuggestedValue();
			if (s.isApproval()) {
				r.approveMdmValue(exceptionId);
			} else if (s.isDisapproval()) {
				r.disapproveMdmValue(exceptionId);
			} else if (s.isIgnore()) {
				r.ignoreMdmException(exceptionId);
			} else if (s.isSuggestion()) {
				r.resolveExceptionWithSuggestion(exceptionId, userSuggested);
			} 
		}

		try {
			getTaskService().completeTask(r);
			FlashMessages.add(request, "info.taskcompleted", new Object[]{}, "The task has been marked completed.");
		} catch (AlreadyDoneException ade) {
			FlashMessages.addError(request, "error.thetaskisalreadycomplete", new Object[]{}, "The task has already been completed");
		} catch (Exception e) {
			FlashMessages.addError(request, "error.exception", new Object[]{ e }, e.getMessage());
		}
		status.setComplete();
		return MyTasksController.redirectTo();
	}
	
	
	
}
