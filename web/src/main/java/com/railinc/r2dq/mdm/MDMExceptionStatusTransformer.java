package com.railinc.r2dq.mdm;

import com.google.common.base.Function;

public class MDMExceptionStatusTransformer implements Function<String, MDMExceptionStatus>{

	@Override
	public MDMExceptionStatus apply(String input) {
		return MDMExceptionStatus.fromJson(input);
	}

}
