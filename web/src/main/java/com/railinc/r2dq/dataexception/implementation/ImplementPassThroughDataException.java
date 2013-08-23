package com.railinc.r2dq.dataexception.implementation;

import com.railinc.r2dq.integration.FromJson;
import com.railinc.r2dq.integration.ToJson;
import com.railinc.r2dq.util.GsonUtil;

public class ImplementPassThroughDataException extends ImplementDataException {
	private static final long serialVersionUID = 1L;
	
	public ImplementPassThroughDataException(){
		super("PassThrough");
	}

	@ToJson
	public String toJsonString(){
		return GsonUtil.toJson(this, toJsonIgnoreFields());
	}
	
	@FromJson
	public ImplementDataException fromJson(String jsonString){
		return GsonUtil.fromJson(jsonString, ImplementDataException.class);
	}
}
