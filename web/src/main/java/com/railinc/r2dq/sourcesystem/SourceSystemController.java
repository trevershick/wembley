package com.railinc.r2dq.sourcesystem;

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

import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.IdentityType;
import com.railinc.r2dq.domain.SourceSystem;
import com.railinc.r2dq.util.WebFormConstants;
import com.railinc.r2dq.web.FlashMessages;

@Controller
@RequestMapping("/s/admin/sourcesystem")
public class SourceSystemController {

	
	private SourceSystemService service;
	
	
	
	@Required
	public void setService(SourceSystemService service) {
		this.service = service;
	}


	@InitBinder
	public void initBinder(WebDataBinder b) {
		b.registerCustomEditor(Date.class, WebFormConstants.timestampPropertyEditor());
		b.registerCustomEditor(IdentityType.class, WebFormConstants.identityTypeEditor());
	}
	
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(Model model, @RequestParam(value="q",required=false) String q) {
		model.addAttribute("results", service.all(q));
		return "sourcesystem/list";
	}
	
	@RequestMapping(value="/new",method=RequestMethod.GET)
	public String newForm(Model model) {
		model.addAttribute("sourcesystem", new SourceSystemForm());
		return "sourcesystem/new";
	}

	@RequestMapping(value="/new",method=RequestMethod.POST,params="_cancel")
	public String cancelNewForm(HttpServletRequest request, @ModelAttribute("sourcesystem") @Valid SourceSystemForm form, BindingResult result) {
		return "redirect:list";
	}
	

	@RequestMapping(value="/new",method=RequestMethod.POST,params="_save")
	public String submitNewForm(HttpServletRequest request, 
			@ModelAttribute("sourcesystem") @Valid SourceSystemForm form, 
			BindingResult result) {
		
		if (result.hasErrors()) {
			return "sourcesystem/new";
		}
		
		SourceSystem ss = new SourceSystem();
		ss.setIdentifier(form.getId());
		ss.setName(form.getName());
		ss.setOutboundQueue(form.getOutboundQueue());
		ss.setDataSteward(new Identity(form.getPersonType(), form.getPerson()));
		try{
			service.save(ss);
		}catch(IllegalStateException ise){
			FlashMessages.addError(request, "", new Object[]{ss.getOutboundQueue()}, "Invalid Outbound Queue. Either its not defined in MQConnectionFactory or its invalid name");
			return "sourcesystem/new";
		}
		
		FlashMessages.add(request, "sourcesystem.succesfullyadded", 
				new Object[]{ss.getIdentifier(), ss.getName()}, 
				"Successfully added the Source System");

		
		return "redirect:list";
	}


	@RequestMapping(value="/{id}/delete",method=RequestMethod.GET)
	public String delete(HttpServletRequest request, Model model, @PathVariable("id") String id) {
		SourceSystem ss = this.service.get(id);
		this.service.delete(ss);
		
		FlashMessages.add(request, "sourcesystem.succesfullydeleted", 
				new Object[]{ss.getIdentifier(), ss.getName()}, 
				"Successfully deleted the Source System");

		
		
		return "redirect:../list";
	}
	
	@RequestMapping(value="/{id}/undelete",method=RequestMethod.GET)
	public String undelete(HttpServletRequest request, Model model, @PathVariable("id") String id) {
		SourceSystem ss = this.service.get(id);
		this.service.undelete(ss);
		
		FlashMessages.add(request, "sourcesystem.succesfullydeleted", 
				new Object[]{ss.getIdentifier(), ss.getName()}, 
				"Successfully deleted the Source System");

		
		
		return "redirect:../list";
	}
	

	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public String editForm(Model model, @PathVariable("id") String id) {
		SourceSystem ss = this.service.get(id);
		model.addAttribute("sourcesystem", new SourceSystemForm(ss));
		return "sourcesystem/edit";
	}
	
	
	
	@RequestMapping(value="/{id}",method=RequestMethod.POST,params="_cancel")
	public String cancelEditForm(HttpServletRequest request, @ModelAttribute("sourcesystem") @Valid SourceSystemForm form, BindingResult result, @PathVariable("id") String id) {
		return "redirect:list";
	}

	@RequestMapping(value="/{id}",method=RequestMethod.POST,params="_save")
	public String submitEditForm(HttpServletRequest request, @ModelAttribute("sourcesystem") @Valid SourceSystemForm form, BindingResult result, @PathVariable("id") String id) {
		if (result.hasErrors()) {
			return "sourcesystem/edit";
		}
		
		SourceSystem ss = this.service.get(id);
		ss.setName(form.getName());
		ss.setOutboundQueue(form.getOutboundQueue());
		ss.setDataSteward(new Identity(form.getPersonType(), form.getPerson()));
		
		try{
			service.save(ss);
		}catch(IllegalStateException ise){
			FlashMessages.addError(request, "", new Object[]{ss.getOutboundQueue()}, "Invalid Outbound Queue. Either its not defined in MQConnectionFactory or its invalid name");
			return "sourcesystem/edit";
		}
		
		FlashMessages.add(request, "sourcesystem.succesfullyupdated", 
				new Object[]{ss.getIdentifier(), ss.getName()}, 
				"Successfully updated the Source System");

		return "redirect:list";
	}

}
