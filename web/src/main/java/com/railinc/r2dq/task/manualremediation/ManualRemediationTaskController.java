package com.railinc.r2dq.task.manualremediation;

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
import com.railinc.r2dq.domain.tasks.ManualRemediationTask;
import com.railinc.r2dq.domain.views.TaskView;
import com.railinc.r2dq.task.AbstractTaskController;
import com.railinc.r2dq.task.AlreadyDoneException;
import com.railinc.r2dq.task.MyTasksController;
import com.railinc.r2dq.task.TaskFormSupport;
import com.railinc.r2dq.web.FlashMessages;

@SessionAttributes(types={ManualRemediationTaskForm.class})
@RequestMapping("/s/tasks/manualremediation")
public class ManualRemediationTaskController extends AbstractTaskController {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	public static final Function<TaskView, String> TASK_TO_REDIRECT_FUNCTION = new Function<TaskView, String>() {
		@Override
		public String apply(TaskView input) {
			return "redirect:/s/tasks/manualremediation/task/" + input.getId();
		}
		public String toString() {
			return ManualRemediationTask.class.getSimpleName();
		}
	};

	
	@RequestMapping(value="/task/{taskid}", method=RequestMethod.GET)
	public String showTaskForm(@PathVariable("taskid") Long taskIdToPerform, Model model) {
		ManualRemediationTaskForm taskForm = new ManualRemediationTaskForm();
		log.debug("taskForm={}",taskForm);
		TaskView task = getTaskService().getTask(taskIdToPerform);
		
		new TaskFormSupport().populate(taskForm, task);
		model.addAttribute(ManualRemediationTaskForm.DEFAULT_FORM_NAME, taskForm);
		return "tasks/manualremediation/task";
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
			@Valid @ModelAttribute(ManualRemediationTaskForm.DEFAULT_FORM_NAME) ManualRemediationTaskForm taskForm, 
			BindingResult result,
			SessionStatus status) {
		
		log.debug("taskForm={}",taskForm);
		log.debug("result={}", result);
		
		if (result.hasErrors()) {
			return "tasks/manualremediation/task";
		}
		
		ManualRemediationTaskCompletion r = new ManualRemediationTaskCompletion();
		r.setTaskId(taskIdToPerform);

		for (ManualRemediationFormRow s : taskForm.getExceptions()) {
			Long exceptionId = s.getExceptionId();
			if (exceptionId == null) {
				continue;
			}
			r.addComment(exceptionId, s.getUserComment());
			String userSuggested = s.getUserSuggestedValue();
			if (s.isApprove()) {
				r.approveMdmValue(exceptionId);
			} else if (s.isDisapprove()) {
				r.disapproveMdmValue(exceptionId);
			} else if (s.isIgnore()) {
				r.ignoreMdmException(exceptionId);
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
