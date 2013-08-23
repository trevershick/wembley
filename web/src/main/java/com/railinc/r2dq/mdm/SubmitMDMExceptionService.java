package com.railinc.r2dq.mdm;

import com.railinc.r2dq.integration.msg.InboundMDMExceptionMessage;


public interface SubmitMDMExceptionService {
	void submit(InboundMDMExceptionMessage message);

}
