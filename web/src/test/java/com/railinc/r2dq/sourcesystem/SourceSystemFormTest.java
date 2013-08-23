package com.railinc.r2dq.sourcesystem;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Assert;
import org.junit.Test;

public class SourceSystemFormTest {
	
	@Test
	public void validate_withOutId_doFail(){
		SourceSystemForm form = new SourceSystemForm();
		form.setName("Single Sign On");
		form.setOutboundQueue("AART.SANDBOX.ITVXM01.R2DQ.MDMEXCEPTION.SSO");
		Validator v = Validation.buildDefaultValidatorFactory().getValidator();
		
		Set<ConstraintViolation<SourceSystemForm>> violations = v.validate(form);
		Assert.assertFalse("should have some violations", violations.isEmpty());
	}

	@Test
	public void validate_withOutName_doFail(){
		SourceSystemForm form = new SourceSystemForm();
		form.setId("SSO");
		form.setOutboundQueue("AART.SANDBOX.ITVXM01.R2DQ.MDMEXCEPTION.SSO");
		Validator v = Validation.buildDefaultValidatorFactory().getValidator();
		
		Set<ConstraintViolation<SourceSystemForm>> violations = v.validate(form);
		Assert.assertFalse("should have some violations", violations.isEmpty());
	}
	
	@Test
	public void validate_WithOutOutboundQueueName_doNotFail(){
		SourceSystemForm form = new SourceSystemForm();
		form.setId("SSO");
		form.setName("Single Sign On");
		Validator v = Validation.buildDefaultValidatorFactory().getValidator();
		
		Set<ConstraintViolation<SourceSystemForm>> violations = v.validate(form);
		Assert.assertTrue(violations.isEmpty());
	}
	
	@Test
	public void validate_WithOutboundQueueAlphaNumeric_doNotFail(){
		SourceSystemForm form = new SourceSystemForm();
		form.setId("SSO");
		form.setName("Single Sign On");
		form.setOutboundQueue("AART.SANDBOX.ITVXM01.R2DQ.MDMEXCEPTION.SSO");
		Validator v = Validation.buildDefaultValidatorFactory().getValidator();
		
		Set<ConstraintViolation<SourceSystemForm>> violations = v.validate(form);
		Assert.assertTrue(violations.isEmpty());
	}

}
