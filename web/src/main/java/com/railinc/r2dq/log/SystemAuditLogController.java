package com.railinc.r2dq.log;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.railinc.r2dq.domain.views.SystemAuditLogView;
import com.railinc.r2dq.util.PagedCollection;
import com.railinc.r2dq.web.FlashMessages;



@Controller
@RequestMapping("/s/support/auditlog")
public class SystemAuditLogController {
	
	private static final String VIEW_AUDIT_LOG_SEARCH_FORM = "support/auditlog/list";
	private static final String VIEW_AUDIT_LOG_SHOW_DETAILS = "support/auditlog/show";
	private static final String PATH_PARAM_LOG_ID = "logId";
	private static final String MODEL_ATTR_LOG_VIEW = "logView";
	private static final String REQUEST_PARAM_CLOSE = "_close";
	private static final String AUDITLOG_ERROR_INSUFFICIENT_PARAMS = "Error - Both Entity Id & Name must be specified for search";
	private final SystemAuditLogRoutes routes = new SystemAuditLogRoutes();
	
	private SystemAuditLogService service;
	
	@Required
	public void setService(SystemAuditLogService service) {
		this.service = service;
	}
	
	
	@RequestMapping(value=SystemAuditLogRoutes.LIST_PATH, method={RequestMethod.GET, RequestMethod.POST})
	public String showSearchForm(
			@Valid
			@ModelAttribute SystemAuditLogSearchForm searchForm,
			BindingResult result, ModelMap model) {
		
		SystemAuditLogSearchCriteria c = new SystemAuditLogSearchCriteria();
		
		if (null != searchForm.getEntityId() && !searchForm.getEntityId().isEmpty()) {
			c.setEntityId(searchForm.getEntityId());
		}
		if (null != searchForm.getEntityName() && !searchForm.getEntityName().isEmpty()) {
			c.setEntityName(searchForm.getEntityName());
		}
		c.setPage(0);
		
		PagedCollection<SystemAuditLogView> data = this.service.findLogsByNameAndId(c);
		searchForm.setResults(new PagedCollection<SystemAuditLogView>(data, data.getPaging()));
		model.put("systemAuditLogSearchForm", searchForm);
		return VIEW_AUDIT_LOG_SEARCH_FORM;
	}
	
	@RequestMapping(value=SystemAuditLogRoutes.LIST_PATH, method = RequestMethod.POST, params = "_cancel")
	public String resetForm(HttpServletRequest request,
			@ModelAttribute @Valid SystemAuditLogSearchForm searchForm,
			BindingResult result, ModelMap model) {
		searchForm.setEntityName(null);
		searchForm.setEntityId(null);
		return showSearchForm(searchForm, result, model);
	}
	
	@RequestMapping(value=SystemAuditLogRoutes.LIST_PATH, method = RequestMethod.POST, params = "_search")
	public String submitNewForm(HttpServletRequest request,
			@ModelAttribute @Valid SystemAuditLogSearchForm searchForm,
			BindingResult result, ModelMap model) {
		
		if (searchForm.getEntityId().isEmpty() && searchForm.getEntityName().isEmpty()) {
			//return unlinked paged list of all records if no criteria are specified for search
			return showSearchForm(searchForm, result, model);
		} else if (searchForm.getEntityId().isEmpty() || searchForm.getEntityName().isEmpty()) {
			//issue an error because we must have both id and name to do a linked search
			FlashMessages.addError(request, "insufficient.log.search.params", null, AUDITLOG_ERROR_INSUFFICIENT_PARAMS);
			return showSearchForm(searchForm, result, model);
		}
		
		if (result.hasErrors()) {
			return VIEW_AUDIT_LOG_SEARCH_FORM;
		}
		
		//must reset page to zero or QueryHelper.query will not return results
		SystemAuditLogSearchCriteria c = searchForm.getCriteria();
		if (c.getPage() > 0) {
			c.setPage(0);
		}
		
		PagedCollection<SystemAuditLogView> data = this.service.linkLogHistory(c);
		searchForm.setResults(new PagedCollection<SystemAuditLogView>(data, data.getPaging()));

		return VIEW_AUDIT_LOG_SEARCH_FORM;
	}
	
	@RequestMapping(value=SystemAuditLogRoutes.VIEW_PATH, method=RequestMethod.GET)
	public String show(@PathVariable(PATH_PARAM_LOG_ID) Long logId, Model model) {
		
		SystemAuditLogView logToShow = this.service.findLogByMasterId(logId);
		
		if (null != logToShow) {
			model.addAttribute(MODEL_ATTR_LOG_VIEW, logToShow);
		}
		return VIEW_AUDIT_LOG_SHOW_DETAILS;
	}  
	
	@RequestMapping(value=SystemAuditLogRoutes.LIST_PATH, params = "_findExceptions")
	public String loadExceptions(HttpServletRequest request,
			@ModelAttribute @Valid SystemAuditLogSearchForm searchForm,
			BindingResult result) {
		
		if (result.hasErrors()) {
			return VIEW_AUDIT_LOG_SEARCH_FORM;
		}
		PagedCollection<SystemAuditLogView> data = this.service.findAllLogExceptions();
		searchForm.setResults(new PagedCollection<SystemAuditLogView>(data, data.getPaging()));
		
		return VIEW_AUDIT_LOG_SEARCH_FORM;
		
	}
	
	@RequestMapping(value=SystemAuditLogRoutes.LIST_PATH, method=RequestMethod.POST, params = REQUEST_PARAM_CLOSE)
	public String closeShow(HttpServletRequest request,
			@ModelAttribute @Valid SystemAuditLogSearchForm searchForm,
			BindingResult result, ModelMap model) {
		
		return showSearchForm(searchForm, result, model);
		
	}

}
