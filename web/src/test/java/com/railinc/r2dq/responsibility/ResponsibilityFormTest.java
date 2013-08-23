package com.railinc.r2dq.responsibility;

 import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Assert;
import org.junit.Test;

import com.railinc.r2dq.domain.IdentityType;

public class ResponsibilityFormTest {

	@Test
	public void validation_cant_be_empty() {
		ResponsibilityForm form = new ResponsibilityForm();
		Validator v = Validation.buildDefaultValidatorFactory().getValidator();
		
		Set<ConstraintViolation<ResponsibilityForm>> violations = v.validate(form);
		Assert.assertFalse("should have some violations", violations.isEmpty());
		
		
	}
	
	@Test
	public void validation_minimum_passes() {
		ResponsibilityForm form = new ResponsibilityForm();
		form.setRuleNumber("1,5");
		form.setPersonType(IdentityType.SsoId);
		form.setNote("a note");
		form.setPrecedence(10);
		Validator v = Validation.buildDefaultValidatorFactory().getValidator();
		
		
		
		Set<ConstraintViolation<ResponsibilityForm>> violations = v.validate(form);
		System.out.println(violations);
		Assert.assertTrue("passes validation", violations.isEmpty());
		
	}

}
