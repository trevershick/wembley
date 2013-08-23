package com.railinc.r2dq.task.notify;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.IdentityType;

public final class IdentityFieldSetMapper implements FieldSetMapper<Identity> {
	@Override
	public Identity mapFieldSet(FieldSet fieldSet) throws BindException {
		String type = fieldSet.readString("type");
		String id = fieldSet.readString("id");
		return new Identity(IdentityType.find(type),id);
	}
}