package com.railinc.r2dq.domain.views;

import com.google.common.base.Function;
import com.railinc.r2dq.domain.DataException;

public class DataExceptionToView implements Function<DataException, DataExceptionView> {


	@Override
	public DataExceptionView apply(DataException input) {
		DataExceptionView v = new DataExceptionView();
		v.setDescription(input.getDescription());
		v.setId(input.getId());
		v.setMdmAttributeValue(input.getMdmAttributevalue());
		v.setMdmObjectAttribute(input.getMdmObjectAttribute());
		v.setMdmObjectType(input.getMdmObjectType());

		v.setSourceSystem(input.getSourceSystem());
		v.setSourceSystemKeyColumn(input.getSourceSystemKeyColumn());
		v.setSourceSystemKey(input.getSourceSystemKey());
		v.setSourceSystemObjectData(input.getSourceSystemObjectDataMap());
		v.setSourceSystemValue(input.getSourceSystemValue());
		v.setRuleNumber(input.getRuleNumber());
		return v;
	}
}
