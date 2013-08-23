package com.railinc.r2dq.integration;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.transformer.Transformer;

import com.google.common.base.Function;
import com.railinc.r2dq.domain.DataException;
import com.railinc.r2dq.domain.InboundMessage;
import com.railinc.r2dq.domain.SourceSystem;
import com.railinc.r2dq.integration.msg.InboundMDMExceptionMessage;
import com.railinc.r2dq.sourcesystem.SourceSystemService;


public class InboundMessageToDataExceptionXFormer implements Transformer, Function<InboundMDMExceptionMessage, DataException> {
	private SourceSystemService sourceSystemService;
	
	/**
	 * translate the inbound JSON to a DataException
	 */
	@Override
	public Message<?> transform(Message<?> message) {
		InboundMessage m = (InboundMessage) message.getPayload();
		
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

		InboundMDMExceptionMessage in = InboundMDMExceptionMessage.fromJson(m.getData());
		
		Set<ConstraintViolation<InboundMDMExceptionMessage>> violations = validator.validate(in);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		
		
		DataException dataException = this.apply(in);
		dataException.setSource(m);
		
		
		Set<ConstraintViolation<DataException>> validate = validator.validate(dataException);
		if (!validate.isEmpty()) {
			throw new ConstraintViolationException(validate);
		}

		return buildMessage(dataException, message);
	}
	
	private Message<?> buildMessage(Object payload, Message<?> originalMessage){
		return MessageBuilder
				.withPayload(payload)
				.copyHeaders(originalMessage.getHeaders())
				.build();
	}

	public DataException apply(InboundMDMExceptionMessage in) {
		DataException d = new DataException();
		SourceSystem sourceSystem = sourceSystemService.getOrCreate(in.getSourceSystem());
		d.setSourceSystem(sourceSystem);
		d.setRuleNumber(in.getCode());
		d.setDescription(in.getDescription());
		d.setExceptionCreated(in.getCreated());
		d.setMdmExceptionId(in.getMdmExceptionId());
		d.setMdmAttributeValue(in.getMdmAttributevalue());
		d.setMdmExceptionId(in.getMdmExceptionId());
		d.setMdmObjectAttribute(in.getMdmObjectAttribute());
		d.setMdmObjectType(in.getMdmObjectType());
		d.setSourceSystemKeyColumn(in.getSourceSystemKeyColumn());
		d.setSourceSystemKey(in.getSourceSystemKeyValue());
		d.setSourceSystemObjectData(in.getSourceSystemObjectData());
		d.setSourceSystemValue(in.getSourceSystemValue());
		d.setMdmExceptionId(in.getMdmExceptionId());
		return d;
	}
	
	@Required
	public void setSourceSystemService(SourceSystemService sourceSystemService) {
		this.sourceSystemService = sourceSystemService;
	}
}
