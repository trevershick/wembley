package com.railinc.r2dq.implementation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Assert;
import org.junit.Test;

import com.railinc.r2dq.domain.ImplementationType;
import com.railinc.r2dq.domain.SourceSystem;

public class ImplementationFormTest {
	
	@Test
	public void validation_cant_be_empty() {
		ImplementationForm form = new ImplementationForm();
		form.setImplementationType(ImplementationType.Manual);
		Validator v = Validation.buildDefaultValidatorFactory().getValidator();
		
		Set<ConstraintViolation<ImplementationForm>> violations = v.validate(form);
		Assert.assertFalse("should have some violations", violations.isEmpty());
		
		
	}
	
	@Test
	public void validation_withSourceSystemNotAutomatedAndPassThroughImplementation_fail() {
		ImplementationForm form = new ImplementationForm();
		SourceSystem sourceSystem = new SourceSystem();
		form.setImplementationType(ImplementationType.PassThrough);
		form.setSourceSystem(sourceSystem);
		form.setRuleNumber("1,5");
		form.setNote("a note");
		form.setPrecedence(10);
		Validator v = Validation.buildDefaultValidatorFactory().getValidator();
		
		Set<ConstraintViolation<ImplementationForm>> violations = v.validate(form);
		System.out.println(violations);
		Assert.assertFalse(violations.isEmpty());
		
	}
	
	@Test
	public void validation_withSourceSystemAutomatedAndPassThroughImplementation_pass() {
		ImplementationForm form = new ImplementationForm();
		SourceSystem sourceSystem = new SourceSystem();
		sourceSystem.setOutboundQueue("AART.SSO");
		form.setImplementationType(ImplementationType.PassThrough);
		form.setSourceSystem(sourceSystem);
		form.setRuleNumber("1,5");
		form.setNote("a note");
		form.setPrecedence(10);
		Validator v = Validation.buildDefaultValidatorFactory().getValidator();
		
		
		
		Set<ConstraintViolation<ImplementationForm>> violations = v.validate(form);
		System.out.println(violations);
		Assert.assertTrue("passes validation", violations.isEmpty());
		
	}
	@Test
	public void validation_passes() {
		ImplementationForm form = new ImplementationForm();
		SourceSystem sourceSystem = new SourceSystem();
		form.setImplementationType(ImplementationType.Manual);
		form.setSourceSystem(sourceSystem);
		form.setRuleNumber("1,5");
		form.setNote("a note");
		form.setPrecedence(10);
		Validator v = Validation.buildDefaultValidatorFactory().getValidator();
		
		
		
		Set<ConstraintViolation<ImplementationForm>> violations = v.validate(form);
		System.out.println(violations);
		Assert.assertTrue("passes validation", violations.isEmpty());
		
	}
}
