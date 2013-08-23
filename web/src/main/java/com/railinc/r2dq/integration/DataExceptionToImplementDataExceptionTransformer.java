package com.railinc.r2dq.integration;

import com.google.common.base.Function;
import com.railinc.r2dq.dataexception.implementation.ImplementApprovedDataException;
import com.railinc.r2dq.dataexception.implementation.ImplementDataException;
import com.railinc.r2dq.dataexception.implementation.ImplementPassThroughDataException;
import com.railinc.r2dq.domain.DataException;

public class DataExceptionToImplementDataExceptionTransformer implements
		Function<DataException, ImplementDataException> {

	@Override
	public ImplementDataException apply(DataException input) {
		ImplementDataException implement = null;
		
		if(input.isApprovedToImplement()){
			implement = new ImplementApprovedDataException();
		} else if( input.isPassThrough()){
			implement = new ImplementPassThroughDataException();
		}else{
			throw new RuntimeException("Not supported to implement by source system");
		}
		
		
		implement.setSourceSystem(input.getSourceSystem());
		implement.setRuleNumber(input.getRuleNumber());
		implement.setSourceSystemKeyIdentifier(input.getSourceSystemRecordIdentifier());
		implement.setSourceSystemColumnName(input.getSourceSystemKeyColumn());
		implement.setMdmValue(input.getMdmAttributeValue());
		implement.setDescription(input.getDescription());
		return implement;
	}

}
