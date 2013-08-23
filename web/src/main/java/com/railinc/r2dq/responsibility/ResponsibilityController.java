package com.railinc.r2dq.responsibility;

import static com.google.common.collect.Collections2.transform;

import java.util.Arrays;
import java.util.Collection;
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

import com.google.common.base.Function;
import com.railinc.r2dq.domain.IdentityType;
import com.railinc.r2dq.domain.Responsibility;
import com.railinc.r2dq.domain.SourceSystem;
import com.railinc.r2dq.sourcesystem.SourceSystemService;
import com.railinc.r2dq.util.PagedCollection;
import com.railinc.r2dq.util.WebFormConstants;
import com.railinc.r2dq.web.FlashMessages;
import com.railinc.r2dq.web.Routes;

@Controller
@RequestMapping(ResponsibilityRoutes.ROOT_PATH)
public class ResponsibilityController {

	private static final String MODEL_ATTR_PERSON_TYPES = "personTypes";
	private static final String MODEL_ATTR_SOURCE_SYSTEMS = "sourceSystems";
	private static final String MODEL_ATTR_RESPONSIBILITY = "responsibility";

	private static final String MESSAGE_SUCCESSFULLY_ADDED_DEFAULT = "Successfully added the Responsibility";
	private static final String MESSAGE_SUCCESFULLYADDED = "responsibility.succesfullyadded";
	private static final String MESSAGE_SUCCESSFULLY_UPDATED_DEFAULT = "Successfully updated the Responsibility";
	private static final String MESSAGE_SUCCESFULLYUPDATED = "responsibility.succesfullyupdated";
	private static final String MESSAGE_SUCCESSFULLY_DELETED_DEFAULT = "Successfully deleted the Responsibility";
	private static final String MESSAGE_SUCCESFULLYDELETED = "responsibility.succesfullydeleted";

	private static final String VIEW_EDIT_FORM = "responsibility/edit";
	private static final String VIEW_NEW_FORM = "responsibility/new";
	private static final String VIEW_LIST = "responsibility/list";
	private final Routes routes = new ResponsibilityRoutes();

	private ResponsibilityService service;
	
	private SourceSystemService sourceSystemService;
	
	
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, WebFormConstants.timestampPropertyEditor());
		binder.registerCustomEditor(IdentityType.class, WebFormConstants.identityTypeEditor());
		binder.registerCustomEditor(SourceSystem.class, WebFormConstants.sourceSystemEditor(sourceSystemService));
	}
	
	@ModelAttribute(MODEL_ATTR_SOURCE_SYSTEMS)
	public Collection<SourceSystem> sourceSystems() {
		return sourceSystemService.active();
	}
	
	@ModelAttribute(MODEL_ATTR_PERSON_TYPES)
	public Collection<String> personTypes() {
		return transform(Arrays.asList(IdentityType.values()), new Function<IdentityType,String>(){
			@Override
			public String apply(IdentityType input) {
				return input.toString();
			}});
	}
	

	@RequestMapping(value=ResponsibilityRoutes.LIST_PATH)
	public String list(@ModelAttribute(ResponsibilitySearchForm.DEFAULT_FORM_NAME) @Valid ResponsibilitySearchForm form, BindingResult result) {
		
		PagedCollection<Responsibility> all = service.all(form.getCriteria());
		form.setResults(new PagedCollection<ResponsibilityForm>(transform(all, TO_FORM), all.getPaging()));
		
		return VIEW_LIST;
	}
	
	@RequestMapping(value=ResponsibilityRoutes.NEW_PATH,method=RequestMethod.GET)
	public String newForm(Model model) {
		model.addAttribute(MODEL_ATTR_RESPONSIBILITY, new ResponsibilityForm());
		return VIEW_NEW_FORM;
	}

	@RequestMapping(value=ResponsibilityRoutes.NEW_PATH,method=RequestMethod.POST,params="_cancel")
	public String cancelNewForm(HttpServletRequest request, @ModelAttribute(MODEL_ATTR_RESPONSIBILITY) @Valid ResponsibilityForm form, BindingResult result) {
		return routes.redirectRoute(ResponsibilityRoutes.STANDARD_LIST);
	}
	

	@RequestMapping(value=ResponsibilityRoutes.NEW_PATH,method=RequestMethod.POST,params="_save")
	public String submitNewForm(HttpServletRequest request, @ModelAttribute(MODEL_ATTR_RESPONSIBILITY) @Valid ResponsibilityForm form, BindingResult result) {
		if (result.hasErrors()) {
			return VIEW_NEW_FORM;
		}
		
		Responsibility ss = new Responsibility();
		ss.setResponsiblePersonId(form.getPerson());
		ss.setRuleNumberType(form.getRuleNumberType());
		ss.setRuleNumberFrom(form.getRuleNumberFrom());
		ss.setRuleNumberThru(form.getRuleNumberThru());
		ss.setPrecedence(form.getPrecedence());
		ss.setSourceSystem(form.getSourceSystem());
		ss.setResponsiblePersonType(form.getPersonType());
		ss.setNoteText(form.getNote());
		service.save(ss);
		
		FlashMessages.add(request, MESSAGE_SUCCESFULLYADDED, 
				new Object[]{ss.getId(), ss.getRuleNumber()}, 
				MESSAGE_SUCCESSFULLY_ADDED_DEFAULT);

		
		return redirectToList();
	}


	@RequestMapping(value=ResponsibilityRoutes.DELETE_PATH,method=RequestMethod.GET)
	public String delete(HttpServletRequest request, Model model, 
			@PathVariable(ResponsibilityRoutes.PATH_VAR_ID) Long id) {
		Responsibility ss = this.service.get(id);
		this.service.delete(ss);
		
		FlashMessages.add(request, MESSAGE_SUCCESFULLYDELETED, 
				new Object[]{ss.getId(), ss.getRuleNumber()}, 
				MESSAGE_SUCCESSFULLY_DELETED_DEFAULT);

		
		
		return redirectToList();
	}
	
	@RequestMapping(value=ResponsibilityRoutes.UNDELETE_PATH,method=RequestMethod.GET)
	public String undelete(HttpServletRequest request, Model model,
			@PathVariable(ResponsibilityRoutes.PATH_VAR_ID) Long id) {
		Responsibility ss = this.service.get(id);
		this.service.undelete(ss);
		
		FlashMessages.add(request, MESSAGE_SUCCESFULLYDELETED, 
				new Object[]{ss.getId(), ss.getRuleNumber()}, 
				MESSAGE_SUCCESSFULLY_DELETED_DEFAULT);
		
		return redirectToList();
	}
	

	@RequestMapping(value=ResponsibilityRoutes.EDIT_PATH,method=RequestMethod.GET)
	public String editForm(Model model, @PathVariable(ResponsibilityRoutes.PATH_VAR_ID) Long id) {
		Responsibility ss = this.service.get(id);
		model.addAttribute(MODEL_ATTR_RESPONSIBILITY, TO_FORM.apply(ss));
		return VIEW_EDIT_FORM;
	}
	
	
	
	@RequestMapping(value=ResponsibilityRoutes.EDIT_PATH,method=RequestMethod.POST,params="_cancel")
	public String cancelEditForm(HttpServletRequest request, 
			@ModelAttribute(MODEL_ATTR_RESPONSIBILITY) @Valid ResponsibilityForm form, BindingResult result, 
			@PathVariable(ResponsibilityRoutes.PATH_VAR_ID) String id) {
		
		return routes.redirectRoute(ResponsibilityRoutes.STANDARD_LIST);
	}

	@RequestMapping(value=ResponsibilityRoutes.EDIT_PATH,method=RequestMethod.POST,params="_save")
	public String submitEditForm(HttpServletRequest request, 
			@Valid @ModelAttribute(MODEL_ATTR_RESPONSIBILITY) ResponsibilityForm form, BindingResult result, 
			@PathVariable(ResponsibilityRoutes.PATH_VAR_ID) Long id) {
		
		if (result.hasErrors()) {
			return VIEW_EDIT_FORM;
		}
		
		Responsibility ss = this.service.get(id);
		ss.setSourceSystem(form.getSourceSystem());
		ss.setRuleNumberType(form.getRuleNumberType());
		ss.setRuleNumberFrom(form.getRuleNumberFrom());
		ss.setRuleNumberThru(form.getRuleNumberThru());
		ss.setPrecedence(form.getPrecedence());
		ss.setResponsiblePersonId(form.getPerson());
		ss.setResponsiblePersonType(form.getPersonType());
		ss.setNoteText(form.getNote());
		this.service.save(ss);
		
		FlashMessages.add(request, MESSAGE_SUCCESFULLYUPDATED, 
				new Object[]{ss.getId(), ss.getRuleNumber()}, 
				MESSAGE_SUCCESSFULLY_UPDATED_DEFAULT);

		return redirectToList();
	}

	private String redirectToList() {
		return routes.redirectRoute(ResponsibilityRoutes.STANDARD_LIST);
	}



	private static final Function<Responsibility, ResponsibilityForm> TO_FORM = new Function<Responsibility, ResponsibilityForm>() {
		@Override
		public ResponsibilityForm apply(Responsibility arg0) {
			ResponsibilityForm f = new ResponsibilityForm();
			f.setAuditData(arg0.getAuditData());
			f.setDeleted(arg0.isDeleted());
			f.setId(arg0.getId());
			f.setPerson(arg0.getResponsiblePersonId());
			f.setPersonType(arg0.getResponsiblePersonType());
			f.setRuleNumber(arg0.getRuleNumber());
			f.setPrecedence(arg0.getPrecedence());
			f.setSourceSystem(arg0.getSourceSystem());
			f.setVersion(arg0.getVersion());
			f.setNote(arg0.getNote().getText());
			return f;
		}
	};
		
	
	@Required
	public void setService(ResponsibilityService service) {
		this.service = service;
	}

	@Required
	public void setSourceSystemService(SourceSystemService sourceSystemService) {
		this.sourceSystemService = sourceSystemService;
	}
}
