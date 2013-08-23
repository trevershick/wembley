package com.railinc.r2dq.usergroup;

import static com.google.common.collect.Collections2.transform;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.base.Function;
import com.railinc.r2dq.domain.UserGroup;
import com.railinc.r2dq.responsibility.ResponsibilityRoutes;
import com.railinc.r2dq.util.WebFormConstants;
import com.railinc.r2dq.web.FlashMessages;

@Controller
@RequestMapping(UserGroupRoutes.ROOT_PATH)
public class UserGroupController {

	private static final String VIEW_LIST = "usergroup/list";

	private static final String VIEW_NEW_FORM = "usergroup/new";

	private static final String VIEW_EDIT_FORM = "usergroup/edit";

	private static final String MODEL_ATTR_USERGROUP = "usergroup";

	private UserGroupService service;

	private final UserGroupRoutes routes = new UserGroupRoutes();
	
	@Required
	public void setService(UserGroupService service) {
		this.service = service;
	}

	private static final Function<UserGroup, UserGroupForm> TO_FORM = new Function<UserGroup, UserGroupForm>() {
		@Override
		public UserGroupForm apply(UserGroup arg0) {
			UserGroupForm f = new UserGroupForm();
			f.setId(arg0.getIdentifier());
			f.setDeleted(arg0.isDeleted());
			f.setName(arg0.getName());
			f.setVersion(arg0.getVersion());
			f.setAuditData(arg0.getAuditData());
			f.setUserIds(arg0.getUserIds());
			return f;
		}
	};
	
	@InitBinder
	public void initBinder(WebDataBinder b) {
		b.registerCustomEditor(Date.class, WebFormConstants.timestampPropertyEditor());
	}
	
	private String redirectToList() {
		return routes.redirectRoute(ResponsibilityRoutes.STANDARD_LIST);
	}


	@RequestMapping(value=UserGroupRoutes.LIST_PATH,method=RequestMethod.GET)
	public String list(Model model, @RequestParam(value="q",required=false) String q) {
		model.addAttribute("results", transform(service.all(q), TO_FORM));
		return VIEW_LIST;
	}
	
	@RequestMapping(value=UserGroupRoutes.NEW_PATH,method=RequestMethod.GET)
	public String newForm(Model model) {
		model.addAttribute(MODEL_ATTR_USERGROUP, new UserGroupForm());
		return VIEW_NEW_FORM;
	}

	@RequestMapping(value=UserGroupRoutes.NEW_PATH,method=RequestMethod.POST,params="_cancel")
	public String cancelNewForm(HttpServletRequest request, @ModelAttribute(MODEL_ATTR_USERGROUP) @Valid UserGroupForm form, BindingResult result) {
		return redirectToList();
	}
	

	@RequestMapping(value=UserGroupRoutes.NEW_PATH,method=RequestMethod.POST,params="_save")
	public String submitNewForm(HttpServletRequest request, @ModelAttribute(MODEL_ATTR_USERGROUP) @Valid UserGroupForm form, BindingResult result) {
		if (result.hasErrors()) {
			return VIEW_NEW_FORM;
		}
		
		UserGroup ss = new UserGroup();
		ss.setIdentifier(form.getId());
		ss.setName(form.getName());
		ss.setUserIds(form.getUserIds());
		service.save(ss);
		
		FlashMessages.add(request, "usergroup.succesfullyadded", 
				new Object[]{ss.getIdentifier(), ss.getName()}, 
				"Successfully added the User Group");

		
		return redirectToList();
	}


	@RequestMapping(value=UserGroupRoutes.DELETE_PATH,method=RequestMethod.GET)
	public String delete(HttpServletRequest request, Model model, @PathVariable(UserGroupRoutes.PATH_VAR_ID) String id) {
		UserGroup ss = this.service.get(id);
		this.service.delete(ss);
		
		FlashMessages.add(request, "usergroup.succesfullydeleted", 
				new Object[]{ss.getIdentifier(), ss.getName()}, 
				"Successfully deleted the User Group");

		
		return redirectToList();
	}
	
	@RequestMapping(value=UserGroupRoutes.UNDELETE_PATH,method=RequestMethod.GET)
	public String undelete(HttpServletRequest request, Model model, @PathVariable(UserGroupRoutes.PATH_VAR_ID) String id) {
		UserGroup ss = this.service.get(id);
		this.service.undelete(ss);
		
		FlashMessages.add(request, "usergroup.succesfullydeleted", 
				new Object[]{ss.getIdentifier(), ss.getName()}, 
				"Successfully deleted the User Group");

		
		return redirectToList();
	}
	

	@RequestMapping(value=UserGroupRoutes.EDIT_PATH,method=RequestMethod.GET)
	public String editForm(Model model, @PathVariable(UserGroupRoutes.PATH_VAR_ID) String id) {
		UserGroup ss = this.service.get(id);
		model.addAttribute(MODEL_ATTR_USERGROUP, TO_FORM.apply(ss));
		return VIEW_EDIT_FORM;
	}
	
	
	
	@RequestMapping(value=UserGroupRoutes.EDIT_PATH,method=RequestMethod.POST,params="_cancel")
	public String cancelEditForm(HttpServletRequest request, @ModelAttribute(MODEL_ATTR_USERGROUP) @Valid UserGroupForm form, BindingResult result, @PathVariable(UserGroupRoutes.PATH_VAR_ID) String id) {

		return redirectToList();
	}

	@RequestMapping(value=UserGroupRoutes.EDIT_PATH,method=RequestMethod.POST,params="_save")
	public String submitEditForm(HttpServletRequest request, @ModelAttribute(MODEL_ATTR_USERGROUP) @Valid UserGroupForm form, BindingResult result, @PathVariable(UserGroupRoutes.PATH_VAR_ID) String id) {
		if (result.hasErrors()) {
			return VIEW_EDIT_FORM;
		}
		UserGroup ss = this.service.get(id);
		ss.setName(form.getName());
		ss.setUserIds(form.getUserIds());
		this.service.save(ss);
		
		FlashMessages.add(request, "usergroup.succesfullyupdated", 
				new Object[]{ss.getIdentifier(), ss.getName()}, 
				"Successfully updated the User Group");

		return redirectToList();
	}

}
