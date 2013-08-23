package com.railinc.r2dq.dataexception;

import static com.google.common.collect.Collections2.transform;

import java.beans.PropertyEditorSupport;
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
import com.google.common.base.Functions;
import com.railinc.r2dq.domain.ApprovalDisposition;
import com.railinc.r2dq.domain.DataException;
import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.IdentityType;
import com.railinc.r2dq.domain.ImplementationDisposition;
import com.railinc.r2dq.domain.ImplementationType;
import com.railinc.r2dq.domain.SourceSystem;
import com.railinc.r2dq.domain.tasks.Task;
import com.railinc.r2dq.sourcesystem.SourceSystemService;
import com.railinc.r2dq.util.PagedCollection;
import com.railinc.r2dq.util.WebFormConstants;
import com.railinc.r2dq.web.FlashMessages;

@Controller
@RequestMapping("/s/support/dataexception")
public class DataExceptionController {

	private DataExceptionService service;
	
	private SourceSystemService sourceSystemService;
	

	@Required
	public void setService(DataExceptionService service) {
		this.service = service;
	}

	@Required
	public void setSourceSystemService(SourceSystemService sourceSystemService) {
		this.sourceSystemService = sourceSystemService;
	}

	private static final Function<DataException, DataExceptionForm> TO_FORM = new Function<DataException, DataExceptionForm>() {
		@Override
		public DataExceptionForm apply(DataException arg0) {
			DataExceptionForm f = new DataExceptionForm();
			f.setId(arg0.getId());
			f.setAuditData(arg0.getAuditData());
			f.setDeleted(arg0.isDeleted());
			f.setRuleNumber(arg0.getRuleNumber());
			f.setPerson(arg0.getResponsiblePerson().getId());
			f.setPersonType(arg0.getResponsiblePerson().getType());
			f.setSourceSystem(arg0.getSourceSystem());
			f.setBundleName(arg0.getBundleName());
			f.setDescription(arg0.getDescription());
			f.setExceptionCreated(arg0.getExceptionCreated());
			f.setMdmAttributevalue(arg0.getMdmAttributevalue());
			f.setMdmObjectAttribute(arg0.getMdmObjectAttribute());
			f.setMdmObjectType(arg0.getMdmObjectType());
			f.setSourceSystemKeyColumn(arg0.getSourceSystemKeyColumn());
			f.setSourceSystemKey(arg0.getSourceSystemKey());
			f.setSourceSystemObjectData(arg0.getSourceSystemObjectData());
			f.setSourceSystemValue(arg0.getSourceSystemValue());
			f.setUserComment(arg0.getUserComment());
			f.setApprovalDisposition(arg0.getApprovalDisposition());
			f.setImplementationDisposition(arg0.getImplementationDisposition());
			f.setImplementationType(arg0.getImplementationType());
			Task t = arg0.getTask();
			if (t != null) {
				f.setTaskId(t.getId());
			}
			return f;
		}
	};
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, WebFormConstants.timestampPropertyEditor());
		binder.registerCustomEditor(IdentityType.class, WebFormConstants.identityTypeEditor());
		binder.registerCustomEditor(ImplementationType.class, WebFormConstants.implementationTypeEditor());
		binder.registerCustomEditor(ImplementationDisposition.class, WebFormConstants.implementationDispositionEditor());
		binder.registerCustomEditor(ApprovalDisposition.class, WebFormConstants.approvalDispositionTypeEditor());
		binder.registerCustomEditor(SourceSystem.class, new PropertyEditorSupport() {
			@Override
			public String getAsText() {
				return String.valueOf(getValue());
			}
			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				setValue(sourceSystemService.get(text));
			}
		});
	}
	
	public Collection<SourceSystem> sourceSystems() {
		return sourceSystemService.active();
	}
	public Collection<String> personTypes() {
		return transform(Arrays.asList(IdentityType.values()), Functions.toStringFunction());
	}
	public Collection<String> implementationTypes() {
		return transform(Arrays.asList(ImplementationType.values()), Functions.toStringFunction());
	}
	public Collection<String> implementationDispositions() {
		return transform(Arrays.asList(ImplementationDisposition.values()), Functions.toStringFunction());
	}
	public Collection<String> approvalDispositions() {
		return transform(Arrays.asList(ApprovalDisposition.values()), Functions.toStringFunction());
	}

	@RequestMapping
	public String landing() {
		return "redirect:dataexception/list";
	}
	
	
	@RequestMapping(value="/list")
	public String list(Model model, @ModelAttribute("dataexceptionsearch") @Valid DataExceptionSearchForm form, BindingResult result) {
		PagedCollection<DataException> all = service.all(form.getCriteria());
		form.setResults(new PagedCollection<DataExceptionForm>(transform(all, TO_FORM), all.getPaging()));
		form.setSourceSystems(this.sourceSystems());

		
		return "dataexception/list";
	}
	
	@RequestMapping(value="/new",method=RequestMethod.GET)
	public String newForm(Model model) {
		model.addAttribute("dataexception", new DataExceptionForm());
		return "dataexception/new";
	}

	@RequestMapping(value="/new",method=RequestMethod.POST,params="_cancel")
	public String cancelNewForm(HttpServletRequest request, @ModelAttribute("dataexception") @Valid DataExceptionForm form, BindingResult result) {
		return "redirect:list";
	}
	
//
//	@RequestMapping(value="/new",method=RequestMethod.POST,params="_save")
//	public String submitNewForm(HttpServletRequest request, @ModelAttribute("dataexception") @Valid DataExceptionForm form, BindingResult result) {
//		if (result.hasErrors()) {
//			return "dataexception/new";
//		}
//		
//		DataException ss = new DataException();
//		ss.setSourceSystem(form.getSourceSystem());
//		ss.setResponsiblePerson(new Identity(form.getPersonType(), form.getPerson()));
//		service.save(ss);
//		
//		FlashMessages.add(request, "dataexception.succesfullyadded", 
//				new Object[]{ss.getId(), ss.getRuleNumber()}, 
//				"Successfully added the DataException");
//
//		
//		return "redirect:list";
//	}


	@RequestMapping(value="/{id}/delete",method=RequestMethod.GET)
	public String delete(HttpServletRequest request, Model model, @PathVariable("id") Long id) {
		DataException ss = this.service.get(id);
		this.service.delete(ss);
		
		FlashMessages.add(request, "dataexception.succesfullydeleted", 
				new Object[]{ss.getId(), ss.getRuleNumber()}, 
				"Successfully deleted the DataException");

		
		
		return "redirect:../list";
	}


	@RequestMapping(value="/{id}/undelete",method=RequestMethod.GET)
	public String undelete(HttpServletRequest request, Model model, @PathVariable("id") Long id) {
		DataException ss = this.service.get(id);
		this.service.undelete(ss);
		
		FlashMessages.add(request, "dataexception.succesfullydeleted", 
				new Object[]{ss.getId(), ss.getRuleNumber()}, 
				"Successfully deleted the Responsibility");
		
		return "redirect:../list";
	}
	

	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public String editForm(Model model, @PathVariable("id") Long id) {
		DataException ss = this.service.get(id);
		DataExceptionForm f = TO_FORM.apply(ss);
		f.setSourceSystems(this.sourceSystems());
		f.setApprovalDispositions(this.approvalDispositions());
		f.setImplementationTypes(this.implementationTypes());
		f.setImplementationDispositions(this.implementationDispositions());
		f.setPersonTypes(this.personTypes());
		
		model.addAttribute(DataExceptionForm.DEFAULT_FORM_NAME, f);
		return "dataexception/edit";
	}
	
	
	
	@RequestMapping(value="/{id}",method=RequestMethod.POST,params="_cancel")
	public String cancelEditForm(HttpServletRequest request, @ModelAttribute("dataexception") @Valid DataExceptionForm form, BindingResult result, @PathVariable("id") String id) {
		return "redirect:list";
	}

	@RequestMapping(value="/{id}",method=RequestMethod.POST,params="_save")
	public String submitEditForm(HttpServletRequest request, @Valid @ModelAttribute("dataexception") DataExceptionForm form, BindingResult result, @PathVariable("id") Long id) {
		if (result.hasErrors()) {
			form.setSourceSystems(this.sourceSystems());
			form.setApprovalDispositions(this.approvalDispositions());
			form.setImplementationTypes(this.implementationTypes());
			form.setImplementationDispositions(this.implementationDispositions());
			form.setPersonTypes(this.personTypes());

			return "dataexception/edit";
		}
		
		DataException ss = this.service.get(id);
		ss.setSourceSystem(form.getSourceSystem());
		ss.setSourceSystemKeyColumn(form.getSourceSystemKeyColumn());
		ss.setSourceSystemKey(form.getSourceSystemKey());
		ss.setSourceSystemObjectData(form.getSourceSystemObjectData());
		ss.setSourceSystemValue(form.getSourceSystemValue());
		
		ss.setMdmAttributeValue(form.getMdmAttributevalue());
		ss.setMdmObjectAttribute(form.getMdmObjectAttribute());
		ss.setMdmObjectType(form.getMdmObjectType());
		
		ss.setRuleNumber(form.getRuleNumber());
		ss.setDescription(form.getDescription());
		ss.setResponsiblePerson(new Identity(form.getPersonType(), form.getPerson()));
		ss.setImplementationType(form.getImplementationType());
		ss.setImplementationDisposition(form.getImplementationDisposition());
		ss.setApprovalDisposition(form.getApprovalDisposition());
		ss.setUserComment(form.getUserComment());

		this.service.save(ss);
		
		FlashMessages.add(request, "dataexception.succesfullyupdated", 
				new Object[]{ss.getId(), ss.getRuleNumber()}, 
				"Successfully updated the DataException");

		return "redirect:list";
	}

}
