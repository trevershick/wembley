package com.railinc.r2dq.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.railinc.r2dq.domain.tasks.ExceptionRemediationTask;
import com.railinc.r2dq.domain.tasks.Task;
import com.railinc.r2dq.util.GsonUtil;

public class GsonUtilTest {
	
	@Test
	public void testToJson_withPropertyInCollection(){
		DataException dataException = new DataException();
		dataException.setSource(buildRawInboundMessage());
		Task task = new ExceptionRemediationTask();
		task.addDataException(dataException);
		assertNotNull(GsonUtil.toJson(task, "exceptions.task"));
	}
	
	@Test
	public void testToJson_withProperty(){
		DataException dataException = new DataException();
		dataException.setSource(buildRawInboundMessage());
		Task task = new ExceptionRemediationTask();
		task.addDataException(dataException);
		assertNotNull(GsonUtil.toJson(dataException, "task.exceptions, auditData, task.auditData"));
		
	}
	
	private InboundMessage buildRawInboundMessage() {
		InboundMessage inboundMessage = new InboundMessage();
		inboundMessage.setSource(InboundSource.MDMException);
		inboundMessage.setProcessed(YesNo.N);
		inboundMessage.setData("{created:\"2013-06-03 13:09\",sourcesystem:\"fur\",sourcekeycol:\"keycol1\",sourcekeyvalue:\"keyval1\",sourcevalue:\"srcvalue1\",sourceinfo:\"srcinfo1\",sourcerecordid:\"srcid1\","+
				"code:1034,description:\"mdmdesc1\",type:\"mdmobjtype\",attr:\"mdmattrtype\",value:\"mdmattrvalue\"}");
		return inboundMessage;
	}
	
}
