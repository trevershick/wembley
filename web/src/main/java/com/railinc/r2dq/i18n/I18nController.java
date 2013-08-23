package com.railinc.r2dq.i18n;

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

import com.railinc.r2dq.domain.I18nMessage;
import com.railinc.r2dq.util.PagedCollection;
import com.railinc.r2dq.util.WebFormConstants;
import com.railinc.r2dq.web.FlashMessages;
import com.railinc.r2dq.web.Routes;

@Controller
@RequestMapping(I18nRoutes.ROOT_PATH)
public class I18nController {

	
	private static final String MESSAGE_SUCCESSFULLY_UPDATED_DEFAULT = "Successfully updated the Property";
	private static final String MESSAGE_SUCCESFULLYUPDATED_CODE = "i18n.succesfullyupdated";
	private static final String MESSAGE_SUCCESSFULLY_DELETED_DEFAULT = "Successfully deleted the Property";
	private static final String MESSAGE_SUCCESFULLYDELETED_CODE = "r2dqproeprty.succesfullydeleted";
	private static final String MESSAGE_SUCCESSFULLY_ADDED_THE_PROPERTY_CODE = "i18n.succesfullyadded";
	private static final String MESSAGE_SUCCESSFULLY_ADDED_THE_PROPERTY_DEFAULT = "Successfully added the Property";
	private final I18nRoutes routes = new I18nRoutes();


	private static final String VIEW_EDIT = "i18n/edit";
	private static final String VIEW_NEW = "i18n/new";
	private static final String VIEW_LIST = "i18n/list";
	
	
	private I18nSearchableService service;
	

	private String redirectToList() {
		return routes.redirectRoute(Routes.STANDARD_LIST);
	}

	
	@Required
	public void setService(I18nSearchableService service) {
		this.service = service;
	}


	@InitBinder
	public void initBinder(WebDataBinder b) {
		b.registerCustomEditor(Date.class, WebFormConstants.timestampPropertyEditor());
	}
	
	@ModelAttribute("routes")
	public I18nRoutes routes() {
		return routes;
	}
	
	
	@RequestMapping(method=RequestMethod.GET)
	public String defaultLandingPage() {
		return routes.redirectRoute(Routes.STANDARD_LIST);
	}

	@RequestMapping(value=I18nRoutes.LIST_PATH)
	public String list(@ModelAttribute(I18nSearchForm.DEFAULT_FORM_NAME) @Valid I18nSearchForm form, BindingResult result) {
		PagedCollection<I18nMessage> all = service.all(form.getCriteria());
		form.setResults(new PagedCollection<I18nForm>(transform(all, I18nForm.PROPERTY_TO_FORM), all.getPaging()));
		return VIEW_LIST;
	}
	
	@RequestMapping(value=I18nRoutes.NEW_PATH,method=RequestMethod.GET)
	public String newForm(@ModelAttribute(I18nForm.DEFAULT_FORM_NAME) I18nForm form) {
		return VIEW_NEW;
	}

	@RequestMapping(value=I18nRoutes.NEW_PATH,method=RequestMethod.POST,params="_cancel")
	public String cancelNewForm(HttpServletRequest request, @ModelAttribute(I18nForm.DEFAULT_FORM_NAME) @Valid I18nForm form, BindingResult result) {
		return routes.redirectRoute(Routes.STANDARD_LIST);
	}
	

	@RequestMapping(value=I18nRoutes.NEW_PATH,method=RequestMethod.POST,params="_save")
	public String submitNewForm(HttpServletRequest request, 
			@ModelAttribute(I18nForm.DEFAULT_FORM_NAME) @Valid I18nForm form, 
			BindingResult result) {
		
		if (result.hasErrors()) {
			return VIEW_NEW;
		}
		
		I18nMessage newProperty = I18nForm.FORM_TO_NEW_PROPERTY.apply(form);
		service.save(newProperty);
		
		FlashMessages.add(request, 
				MESSAGE_SUCCESSFULLY_ADDED_THE_PROPERTY_CODE, 
				new Object[]{newProperty.getCode()}, 
				MESSAGE_SUCCESSFULLY_ADDED_THE_PROPERTY_DEFAULT);

		
		return redirectToList();
	}


	@RequestMapping(value=I18nRoutes.DELETE_PATH,method=RequestMethod.GET)
	public String delete(HttpServletRequest request, Model model, @PathVariable("id") Long id) {
		I18nMessage ss = this.service.get(id);
		this.service.delete(ss);
		
		FlashMessages.add(request, 
				MESSAGE_SUCCESFULLYDELETED_CODE, 
				new Object[]{ss.getCode()}, 
				MESSAGE_SUCCESSFULLY_DELETED_DEFAULT);

		
		
		return redirectToList();
	}
	

	

	@RequestMapping(value=I18nRoutes.EDIT_PATH,method=RequestMethod.GET)
	public String editForm(Model model, @PathVariable("id") Long id) {
		I18nMessage ss = this.service.get(id);
		model.addAttribute(I18nForm.DEFAULT_FORM_NAME, I18nForm.PROPERTY_TO_FORM.apply(ss));
		return VIEW_EDIT;
	}
	
	
	
	@RequestMapping(value=I18nRoutes.EDIT_PATH,method=RequestMethod.POST,params="_cancel")
	public String cancelEditForm(HttpServletRequest request, @ModelAttribute(I18nForm.DEFAULT_FORM_NAME) @Valid I18nForm form, BindingResult result, @PathVariable("id") Long id) {
		return redirectToList();
	}


	@RequestMapping(value=I18nRoutes.EDIT_PATH,method=RequestMethod.POST,params="_save")
	public String submitEditForm(HttpServletRequest request, @ModelAttribute(I18nForm.DEFAULT_FORM_NAME) @Valid I18nForm form, BindingResult result, @PathVariable("id") Long id) {
		if (result.hasErrors()) {
			return VIEW_EDIT;
		}
		
		I18nMessage ss = this.service.get(id);
		ss.setText(form.getText());
		this.service.save(ss);
		
		FlashMessages.add(request, MESSAGE_SUCCESFULLYUPDATED_CODE, 
				new Object[]{ss.getCode(), ss.getCode()}, 
				MESSAGE_SUCCESSFULLY_UPDATED_DEFAULT);

		return redirectToList();
	}

}
