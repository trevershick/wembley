package com.railinc.r2dq.implementation;

import static com.google.common.collect.Collections2.transform;

import java.util.ArrayList;
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
import com.railinc.r2dq.domain.Implementation;
import com.railinc.r2dq.domain.ImplementationType;
import com.railinc.r2dq.domain.SourceSystem;
import com.railinc.r2dq.sourcesystem.SourceSystemService;
import com.railinc.r2dq.util.PagedCollection;
import com.railinc.r2dq.util.WebFormConstants;
import com.railinc.r2dq.web.FlashMessages;
import com.railinc.r2dq.web.Routes;

@Controller
@RequestMapping(ImplementationRoutes.ROOT_PATH)
public class ImplementationController {

	private static final String MODEL_ATTR_IMPLEMENTATION_TYPES = "implementationTypes";
	private static final String MODEL_ATTR_SOURCE_SYSTEMS = "sourceSystems";
	private static final String MODEL_ATTR_IMPLEMENTATION = "implementation";

	private static final String MESSAGE_SUCCESSFULLY_ADDED_DEFAULT = "Successfully added the Implementation";
	private static final String MESSAGE_SUCCESFULLYADDED = "implementation.succesfullyadded";
	private static final String MESSAGE_SUCCESSFULLY_UPDATED_DEFAULT = "Successfully updated the Implementation";
	private static final String MESSAGE_SUCCESFULLYUPDATED = "implementation.succesfullyupdated";
	private static final String MESSAGE_SUCCESSFULLY_DELETED_DEFAULT = "Successfully deleted the Implementation";
	private static final String MESSAGE_SUCCESFULLYDELETED = "implementation.succesfullydeleted";

	private static final String VIEW_EDIT_FORM = "implementation/edit";
	private static final String VIEW_NEW_FORM = "implementation/new";
	private static final String VIEW_LIST = "implementation/list";
	private final Routes routes = new ImplementationRoutes();

	private ImplementationService service;
	
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
	
	@ModelAttribute(MODEL_ATTR_IMPLEMENTATION_TYPES)
	public Collection<String> implementationTypes() {
		Collection<String> implementationTypes = new ArrayList<String>();
		for(ImplementationType  type:ImplementationType.values()){
			if(ImplementationType.ForceStormDrain != type){
				implementationTypes.add(type.toString());
			}
		}
		return implementationTypes;
	}
	

	@RequestMapping(value=ImplementationRoutes.LIST_PATH)
	public String list(@ModelAttribute(ImplementationSearchForm.DEFAULT_FORM_NAME) @Valid ImplementationSearchForm form, BindingResult result) {
		
		PagedCollection<Implementation> all = service.all(form.getCriteria());
		form.setResults(new PagedCollection<ImplementationForm>(transform(all, TO_FORM), all.getPaging()));
		
		return VIEW_LIST;
	}
	
	@RequestMapping(value=ImplementationRoutes.NEW_PATH,method=RequestMethod.GET)
	public String newForm(Model model) {
		model.addAttribute(MODEL_ATTR_IMPLEMENTATION, new ImplementationForm());
		return VIEW_NEW_FORM;
	}

	@RequestMapping(value=ImplementationRoutes.NEW_PATH,method=RequestMethod.POST,params="_cancel")
	public String cancelNewForm(HttpServletRequest request, @ModelAttribute(MODEL_ATTR_IMPLEMENTATION) @Valid ImplementationForm form, BindingResult result) {
		return routes.redirectRoute(ImplementationRoutes.STANDARD_LIST);
	}
	

	@RequestMapping(value=ImplementationRoutes.NEW_PATH,method=RequestMethod.POST,params="_save")
	public String submitNewForm(HttpServletRequest request, @ModelAttribute(MODEL_ATTR_IMPLEMENTATION) @Valid ImplementationForm form, BindingResult result) {
		if (result.hasErrors()) {
			return VIEW_NEW_FORM;
		}
		
		Implementation implementation = new Implementation();
		implementation.setRuleNumberType(form.getRuleNumberType());
		implementation.setRuleNumberFrom(form.getRuleNumberFrom());
		implementation.setRuleNumberThru(form.getRuleNumberThru());
		implementation.setPrecedence(form.getPrecedence());
		implementation.setSourceSystem(form.getSourceSystem());
		implementation.setType(form.getImplementationType());
		implementation.setNoteText(form.getNote());
		service.save(implementation);
		
		FlashMessages.add(request, MESSAGE_SUCCESFULLYADDED, 
				new Object[]{implementation.getId(), implementation.getRuleNumber()}, 
				MESSAGE_SUCCESSFULLY_ADDED_DEFAULT);

		
		return redirectToList();
	}


	@RequestMapping(value=ImplementationRoutes.DELETE_PATH,method=RequestMethod.GET)
	public String delete(HttpServletRequest request, Model model, 
			@PathVariable(ImplementationRoutes.PATH_VAR_ID) Long id) {
		Implementation implementation = this.service.get(id);
		this.service.delete(implementation);
		
		FlashMessages.add(request, MESSAGE_SUCCESFULLYDELETED, 
				new Object[]{implementation.getId(), implementation.getRuleNumber()}, 
				MESSAGE_SUCCESSFULLY_DELETED_DEFAULT);

		
		
		return redirectToList();
	}
	
	@RequestMapping(value=ImplementationRoutes.UNDELETE_PATH,method=RequestMethod.GET)
	public String undelete(HttpServletRequest request, Model model,
			@PathVariable(ImplementationRoutes.PATH_VAR_ID) Long id) {
		Implementation implementation = this.service.get(id);
		this.service.undelete(implementation);
		
		FlashMessages.add(request, MESSAGE_SUCCESFULLYDELETED, 
				new Object[]{implementation.getId(), implementation.getRuleNumber()}, 
				MESSAGE_SUCCESSFULLY_DELETED_DEFAULT);
		
		return redirectToList();
	}
	

	@RequestMapping(value=ImplementationRoutes.EDIT_PATH,method=RequestMethod.GET)
	public String editForm(Model model, @PathVariable(ImplementationRoutes.PATH_VAR_ID) Long id) {
		Implementation implementation = this.service.get(id);
		model.addAttribute(MODEL_ATTR_IMPLEMENTATION, TO_FORM.apply(implementation));
		return VIEW_EDIT_FORM;
	}
	
	
	
	@RequestMapping(value=ImplementationRoutes.EDIT_PATH,method=RequestMethod.POST,params="_cancel")
	public String cancelEditForm(HttpServletRequest request, 
			@ModelAttribute(MODEL_ATTR_IMPLEMENTATION) @Valid ImplementationForm form, BindingResult result, 
			@PathVariable(ImplementationRoutes.PATH_VAR_ID) String id) {
		
		return routes.redirectRoute(ImplementationRoutes.STANDARD_LIST);
	}

	@RequestMapping(value=ImplementationRoutes.EDIT_PATH,method=RequestMethod.POST,params="_save")
	public String submitEditForm(HttpServletRequest request, 
			@Valid @ModelAttribute(MODEL_ATTR_IMPLEMENTATION) ImplementationForm form, BindingResult result, 
			@PathVariable(ImplementationRoutes.PATH_VAR_ID) Long id) {
		
		if (result.hasErrors()) {
			return VIEW_EDIT_FORM;
		}
		
		Implementation implementation = this.service.get(id);
		implementation.setSourceSystem(form.getSourceSystem());
		implementation.setRuleNumberType(form.getRuleNumberType());
		implementation.setRuleNumberFrom(form.getRuleNumberFrom());
		implementation.setRuleNumberThru(form.getRuleNumberThru());
		implementation.setPrecedence(form.getPrecedence());
		implementation.setType(form.getImplementationType());
		implementation.setNoteText(form.getNote());
		this.service.save(implementation);
		
		FlashMessages.add(request, MESSAGE_SUCCESFULLYUPDATED, 
				new Object[]{implementation.getId(), implementation.getRuleNumber()}, 
				MESSAGE_SUCCESSFULLY_UPDATED_DEFAULT);

		return redirectToList();
	}

	private String redirectToList() {
		return routes.redirectRoute(ImplementationRoutes.STANDARD_LIST);
	}



	private static final Function<Implementation, ImplementationForm> TO_FORM = new Function<Implementation, ImplementationForm>() {
		@Override
		public ImplementationForm apply(Implementation implementation) {
			ImplementationForm form = new ImplementationForm();
			form.setAuditData(implementation.getAuditData());
			form.setDeleted(implementation.isDeleted());
			form.setId(implementation.getId());
			form.setRuleNumber(implementation.getRuleNumber());
			form.setPrecedence(implementation.getPrecedence());
			form.setSourceSystem(implementation.getSourceSystem());
			form.setImplementationType(implementation.getType());
			form.setVersion(implementation.getVersion());
			form.setNote(implementation.getNoteText());
			return form;
		}
	};
		
	
	@Required
	public void setService(ImplementationService service) {
		this.service = service;
	}

	@Required
	public void setSourceSystemService(SourceSystemService sourceSystemService) {
		this.sourceSystemService = sourceSystemService;
	}
}
