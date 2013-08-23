package com.railinc.r2dq.configuration;

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

import com.railinc.common.configuration.PropertyService;
import com.railinc.r2dq.domain.ConfigurationProperty;
import com.railinc.r2dq.util.WebFormConstants;
import com.railinc.r2dq.web.FlashMessages;
import com.railinc.r2dq.web.Routes;

@Controller
@RequestMapping(R2DQPropertyRoutes.ROOT_PATH)
public class R2DQPropertyController {

	
	private static final String MESSAGE_SUCCESSFULLY_UPDATED_DEFAULT = "Successfully updated the Property";
	private static final String MESSAGE_SUCCESFULLYUPDATED_CODE = "r2dqproperty.succesfullyupdated";
	private static final String MESSAGE_SUCCESSFULLY_DELETED_DEFAULT = "Successfully deleted the Property";
	private static final String MESSAGE_SUCCESFULLYDELETED_CODE = "r2dqproeprty.succesfullydeleted";
	private static final String MESSAGE_SUCCESSFULLY_ADDED_THE_PROPERTY_CODE = "r2dqproperty.succesfullyadded";
	private static final String MESSAGE_SUCCESSFULLY_ADDED_THE_PROPERTY_DEFAULT = "Successfully added the Property";
	private final R2DQPropertyRoutes routes = new R2DQPropertyRoutes();


	private static final String VIEW_EDIT = "r2dqproperty/edit";
	private static final String VIEW_NEW = "r2dqproperty/new";
	private static final String VIEW_LIST = "r2dqproperty/list";
	
	
	private PropertyService<ConfigurationProperty> service;
	

	private String redirectToList() {
		return routes.redirectRoute(Routes.STANDARD_LIST);
	}

	
	@Required
	public void setService(PropertyService<ConfigurationProperty> service) {
		this.service = service;
	}


	@InitBinder
	public void initBinder(WebDataBinder b) {
		b.registerCustomEditor(Date.class, WebFormConstants.timestampPropertyEditor());
	}
	
	@ModelAttribute("routes")
	public R2DQPropertyRoutes routes() {
		return routes;
	}
	
	
	@RequestMapping(method=RequestMethod.GET)
	public String defaultLandingPage() {
		return routes.redirectRoute(Routes.STANDARD_LIST);
	}

	@RequestMapping(value=R2DQPropertyRoutes.LIST_PATH,method=RequestMethod.GET)
	public String list(Model model, @RequestParam(value="q",required=false) String q) {
		model.addAttribute("results", service.all(q));
		return VIEW_LIST;
	}
	
	@RequestMapping(value=R2DQPropertyRoutes.NEW_PATH,method=RequestMethod.GET)
	public String newForm(Model model) {
		model.addAttribute(R2DQPropertyForm.DEFAULT_FORM_NAME, new R2DQPropertyForm());
		return VIEW_NEW;
	}

	@RequestMapping(value=R2DQPropertyRoutes.NEW_PATH,method=RequestMethod.POST,params="_cancel")
	public String cancelNewForm(HttpServletRequest request, @ModelAttribute(R2DQPropertyForm.DEFAULT_FORM_NAME) @Valid R2DQPropertyForm form, BindingResult result) {
		return routes.redirectRoute(Routes.STANDARD_LIST);
	}
	

	@RequestMapping(value=R2DQPropertyRoutes.NEW_PATH,method=RequestMethod.POST,params="_save")
	public String submitNewForm(HttpServletRequest request, 
			@ModelAttribute(R2DQPropertyForm.DEFAULT_FORM_NAME) @Valid R2DQPropertyForm form, 
			BindingResult result) {
		
		if (result.hasErrors()) {
			return VIEW_NEW;
		}
		
		ConfigurationProperty newProperty = R2DQPropertyForm.FORM_TO_NEW_PROPERTY.apply(form);
		service.save(newProperty);
		
		FlashMessages.add(request, 
				MESSAGE_SUCCESSFULLY_ADDED_THE_PROPERTY_CODE, 
				new Object[]{newProperty.getCode(), newProperty.getName()}, 
				MESSAGE_SUCCESSFULLY_ADDED_THE_PROPERTY_DEFAULT);

		
		return redirectToList();
	}


	@RequestMapping(value=R2DQPropertyRoutes.DELETE_PATH,method=RequestMethod.GET)
	public String delete(HttpServletRequest request, Model model, @PathVariable("id") Long id) {
		ConfigurationProperty ss = this.service.get(id);
		this.service.delete(ss);
		
		FlashMessages.add(request, 
				MESSAGE_SUCCESFULLYDELETED_CODE, 
				new Object[]{ss.getCode(), ss.getName()}, 
				MESSAGE_SUCCESSFULLY_DELETED_DEFAULT);

		
		
		return redirectToList();
	}
	

	

	@RequestMapping(value=R2DQPropertyRoutes.EDIT_PATH,method=RequestMethod.GET)
	public String editForm(Model model, @PathVariable("id") Long id) {
		ConfigurationProperty ss = this.service.get(id);
		model.addAttribute(R2DQPropertyForm.DEFAULT_FORM_NAME, R2DQPropertyForm.PROPERTY_TO_FORM.apply(ss));
		return VIEW_EDIT;
	}
	
	
	
	@RequestMapping(value=R2DQPropertyRoutes.EDIT_PATH,method=RequestMethod.POST,params="_cancel")
	public String cancelEditForm(HttpServletRequest request, @ModelAttribute(R2DQPropertyForm.DEFAULT_FORM_NAME) @Valid R2DQPropertyForm form, BindingResult result, @PathVariable("id") String id) {
		return redirectToList();
	}


	@RequestMapping(value=R2DQPropertyRoutes.EDIT_PATH,method=RequestMethod.POST,params="_save")
	public String submitEditForm(HttpServletRequest request, 
			@ModelAttribute(R2DQPropertyForm.DEFAULT_FORM_NAME) @Valid R2DQPropertyForm form, 
			BindingResult result, 
			@PathVariable("id") Long id) {
		if (result.hasErrors()) {
			return VIEW_EDIT;
		}
		
		ConfigurationProperty ss = this.service.get(id);
		ss.setCode(form.getCode());
		ss.setName(form.getName());
		ss.setValue(form.getValue());
		ss.setDescription(form.getDescription());
		this.service.save(ss);
		
		FlashMessages.add(request, MESSAGE_SUCCESFULLYUPDATED_CODE, 
				new Object[]{ss.getCode(), ss.getName()}, 
				MESSAGE_SUCCESSFULLY_UPDATED_DEFAULT);

		return redirectToList();
	}

}
