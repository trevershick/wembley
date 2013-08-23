package com.railinc.r2dq.messages;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Function;
import com.google.gson.GsonBuilder;
import com.railinc.r2dq.domain.GenericMessage;
import com.railinc.r2dq.domain.InboundMessage;
import com.railinc.r2dq.domain.OutboundMessage;
import com.railinc.r2dq.domain.views.RawInboundMessageView;
import com.railinc.r2dq.integration.Queue;
import com.railinc.r2dq.integration.msg.InboundMDMExceptionMessage;
import com.railinc.r2dq.util.PagedCollection;
import com.railinc.r2dq.web.FlashMessages;

@Controller
@RequestMapping("/s/support/message")
public class MessageController {

	private static final String VIEW_MESSAGE_SUBMISSION_FORM = "support/message/form";
	private static final String VIEW_MESSAGE_SUBMISSION_CRUD_FORM = "support/message/edit";
	private static final String VIEW_MESSAGE_SEARCH_FORM = "support/message/list";
	
	MessageService service;

	Queue processQueue;
	
	
	@Required
	public void setService(MessageService service) {
		this.service = service;
	}



	public static final String redirectToSearch() {
		return "redirect:/s/support/message/list";
	}

	
	
	@RequestMapping
	public String landing() {
		return redirectToSearch();
	}
	
	
	private static final Function<GenericMessage,MessageForm> TO_FORM = new Function<GenericMessage, MessageForm>() {
		public MessageForm apply(GenericMessage m) {
			MessageForm f = new MessageForm();
			f.setAuditData(m.getAuditData());
			f.setData(m.getData());
			f.setIdentifier(m.getIdentifier());
			f.setProcessed(m.isProcessed());
			if(m instanceof InboundMessage){
				f.setSource(((InboundMessage)m).getSource().toString());
				f.setType("I");
			}
			if(m instanceof OutboundMessage){
				f.setSource(((OutboundMessage)m).getOutbound());
				f.setType("O");
			}
			return f;
		}
	};
	


	@RequestMapping(value = "/list")
	public String showSearchForm(
			@Valid
			@ModelAttribute MessageSearchForm form,
			BindingResult result) {
		PagedCollection<RawInboundMessageView> all = this.service.all(form.getCriteria());
		form.setResults(new PagedCollection<RawInboundMessageView>(all, all.getPaging()));
		return VIEW_MESSAGE_SEARCH_FORM;
	}
	
	


	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public String editForm(Model model, @PathVariable("id") Long id) {
		GenericMessage ss = this.service.get(id);
		model.addAttribute("message", TO_FORM.apply(ss));
		return VIEW_MESSAGE_SUBMISSION_CRUD_FORM;
	}
	
	
	
	@RequestMapping(value="/{id}",method=RequestMethod.POST,params="_cancel")
	public String cancelEditForm(HttpServletRequest request, @ModelAttribute("message") @Valid MessageForm form, BindingResult result, @PathVariable("id") Long id) {
		return "redirect:list";
	}

	@RequestMapping(value="/{id}",method=RequestMethod.POST,params="_save")
	public String submitEditForm(HttpServletRequest request, @ModelAttribute("message") @Valid MessageForm form, BindingResult result, @PathVariable("id") Long id) {
		if (result.hasErrors()) {
			return VIEW_MESSAGE_SUBMISSION_CRUD_FORM;
		}
		
		GenericMessage ss = this.service.get(id);
		ss.setData(form.getData());
		if(ss instanceof InboundMessage){
			this.service.save((InboundMessage)ss);
		}
		if(ss instanceof OutboundMessage){
			this.service.save((OutboundMessage)ss);
		}
		
		
		FlashMessages.add(request, "message.succesfullyupdated", 
				new Object[]{ss.getIdentifier() }, 
				"Successfully updated the Message");

		return "redirect:list";
	}

	

	@RequestMapping(value = "/send", method = RequestMethod.GET)
	public String showSubmissionForm(
			@ModelAttribute MessageSubmissionForm form,
			BindingResult result) {
		return VIEW_MESSAGE_SUBMISSION_FORM;
	}

	@RequestMapping(value = "/{messageId}/reprocess")
	public String cancelNewForm(HttpServletRequest request,
			@PathVariable("messageId") Long messageId) {
		
		GenericMessage message = this.service.get(messageId);
		if (message == null) {
			FlashMessages.addError(request, "info.rawinboundmessage.missing", new Object[]{messageId}, "Can't load message id " + messageId);
		} else if(message instanceof InboundMessage){
			this.processQueue.sendMessage(message);
			FlashMessages.add(request, "info.rawinboundmessage.sent", new Object[]{messageId}, "Message sent.");
		}else if( message instanceof OutboundMessage){
			service.resend((OutboundMessage)message);
			FlashMessages.add(request, "info.rawinboundmessage.sent", new Object[]{messageId}, "Message sent.");
		}
		return redirectToSearch();
	}

	@RequestMapping(value = "/send", method = RequestMethod.POST, params = "_cancel")
	public String cancelNewForm(HttpServletRequest request,
			@ModelAttribute("form") MessageSubmissionForm form,
			BindingResult result) {
		
		return redirectToSearch();
	}

	@RequestMapping(value = "/send", method = RequestMethod.POST, params = {"_upload"})
	public String uploadFile(HttpServletRequest request,
			@ModelAttribute MessageSubmissionForm form,
			BindingResult result) {
		if (result.hasErrors()) {
			return VIEW_MESSAGE_SUBMISSION_FORM;
		}
		
		MultipartFile file = form.getFile();
		FlatFileItemReader<InboundMDMExceptionMessage> itemReader = new FlatFileItemReader<InboundMDMExceptionMessage>();
		itemReader.setLinesToSkip(1);
		InputStream is = null;
		try {
			is = file.getInputStream();
		} catch (Exception e) {
			FlashMessages.addError(request, "error.exception", new Object[]{e}, e.getMessage());
			return VIEW_MESSAGE_SUBMISSION_FORM;
		}

		itemReader.setResource(new InputStreamResource(is));

		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[]{"EXEPTION_ID","EXCEPTION_CREATED_TS","EDW_SRC_ID","EDW_SRC_NAME","EXCPTN_CD","EXCPTN_DESC","MDM_EXCPTN_TYPE","MDM_EXCPTN_COL_NAME","EDW_SRC_VALUE","EDW_SRC_KEY_COLUMN","EDW_SRC_KEY","EDW_SRC_INFO","MDM_VALUE"});
		//DelimitedLineTokenizer defaults to comma as its delimiter
		DefaultLineMapper<InboundMDMExceptionMessage> lineMapper = new DefaultLineMapper<InboundMDMExceptionMessage>();
		
		lineMapper.setLineTokenizer(tokenizer);
		
		lineMapper.setFieldSetMapper(new FieldSetMapper<InboundMDMExceptionMessage>() {
			@Override
			public InboundMDMExceptionMessage mapFieldSet(FieldSet fieldSet) throws BindException {
				InboundMDMExceptionMessage m = new InboundMDMExceptionMessage();
				m.setCreated(fieldSet.readDate("EXCEPTION_CREATED_TS","ddMMMyyyy:HH:mm:ss"));
				m.setSourceSystem(fieldSet.readRawString("EDW_SRC_NAME"));
			    m.setSourceSystemKeyColumn(fieldSet.readRawString("EDW_SRC_KEY_COLUMN"));
				m.setSourceSystemKeyValue(fieldSet.readRawString("EDW_SRC_KEY"));
			    m.setSourceSystemValue(fieldSet.readRawString("EDW_SRC_VALUE"));
			    m.setSourceSystemObjectData(fieldSet.readRawString("EDW_SRC_INFO"));
				m.setCode(fieldSet.readLong("EXCPTN_CD"));
				m.setDescription(fieldSet.readRawString("EXCPTN_DESC"));
				m.setMdmObjectType(fieldSet.readRawString("MDM_EXCPTN_TYPE"));
				m.setMdmObjectAttribute(fieldSet.readRawString("MDM_EXCPTN_COL_NAME"));
				m.setMdmAttributevalue(fieldSet.readRawString("MDM_VALUE"));
				m.setMdmExceptionId(fieldSet.readLong("EXEPTION_ID"));
				return m;
			}
		});
		itemReader.setLineMapper(lineMapper);
		itemReader.open(new ExecutionContext());
		InboundMDMExceptionMessage m = null;
		try {
			while ((m =itemReader.read()) != null) {
				String json = new GsonBuilder()
					.setDateFormat("yyyy-mm-dd HH:MM")
					.create()
					.toJson(m);
				service.sendMessage(json);
				FlashMessages.add(request, "support.message.fileuploaded",new Object[]{form.getData(), form.getTimes()}, "Successfully uploaded the file.");
			}
		} catch (Exception e) {
			FlashMessages.addError(request, "error.exception", new Object[]{e}, e.getMessage());
		}


		return VIEW_MESSAGE_SUBMISSION_FORM;
	}

	@RequestMapping(value = "/send", method = RequestMethod.POST, params = "_submit")
	public String submitNewForm(HttpServletRequest request,
			@ModelAttribute @Valid MessageSubmissionForm form,
			BindingResult result) {
		if (result.hasErrors()) {
			return VIEW_MESSAGE_SUBMISSION_FORM;
		}
		for (int i=0;i<form.getTimes();i++) {
			service.sendMessage(form.getData());
		}

		FlashMessages.add(request, "support.message.successfullysent",new Object[]{form.getData(), form.getTimes()}, "Successfully sent the message.");

		return VIEW_MESSAGE_SUBMISSION_FORM;
	}

	

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		MessageSearchForm.initBinder(binder);
	}



	public Queue getProcessQueue() {
		return processQueue;
	}


	@Required
	public void setProcessQueue(Queue processQueue) {
		this.processQueue = processQueue;
	}
}
