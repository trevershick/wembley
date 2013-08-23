package com.railinc.r2dq.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DataExceptionTest {
	
	@Test
	public void testToJson(){
		DataException dataException = new DataException();
		dataException.setSource(buildRawInboundMessage());
		assertNotNull(dataException.toJsonString());
	}
	
	@Test
	public void testIsAvailableForSourceSystemToImplement_withManaulImplementation_returnFalse(){
		DataException exception = new DataException();
		exception.setImplementationType(ImplementationType.Manual);
		assertFalse(exception.isAvailableForSourceSystemToImplement());
	}
	
	@Test
	public void testIsAvailableForSourceSystemToImplement_withAutomaticImplementationAndInitialApprovalDisposition_returnFalse(){
		DataException exception = new DataException();
		exception.setImplementationType(ImplementationType.Automated);
		exception.setApprovalDisposition(ApprovalDisposition.Initial);
		assertFalse(exception.isAvailableForSourceSystemToImplement());
	}
	
	@Test
	public void testIsAvailableForSourceSystemToImplement_withStromDrainImplementation_returnFalse(){
		DataException exception = new DataException();
		exception.setImplementationType(ImplementationType.StormDrain);
		assertFalse(exception.isAvailableForSourceSystemToImplement());
	}
	
	@Test
	public void testIsAvailableForSourceSystemToImplement_withForceStromDrainImplementation_returnFalse(){
		DataException exception = new DataException();
		exception.setImplementationType(ImplementationType.ForceStormDrain);
		assertFalse(exception.isAvailableForSourceSystemToImplement());
	}
	
	@Test
	public void testIsAvailableForSourceSystemToImplement_withAutomatedImplementationAndApprovedApprovalDisposition_returnTrue(){
		DataException exception = new DataException();
		exception.setImplementationType(ImplementationType.Automated);
		exception.setApprovalDisposition(ApprovalDisposition.Approved);
		assertTrue(exception.isAvailableForSourceSystemToImplement());
	}
	
	@Test
	public void testIsAvailableForSourceSystemToImplement_withAutomatedImplementationAndInitialApprovalDisposition_returnFalse(){
		DataException exception = new DataException();
		exception.setImplementationType(ImplementationType.Automated);
		exception.setApprovalDisposition(ApprovalDisposition.Initial);
		assertFalse(exception.isAvailableForSourceSystemToImplement());
	}
	
	@Test
	public void testIsAvailableForSourceSystemToImplement_withAutomatedImplementationAndDisapprovedApprovalDisposition_returnFalse(){
		DataException exception = new DataException();
		exception.setImplementationType(ImplementationType.Automated);
		exception.setApprovalDisposition(ApprovalDisposition.Disapproved);
		assertFalse(exception.isAvailableForSourceSystemToImplement());
	}
	
	@Test
	public void testIsAvailableForSourceSystemToImplement_withAutomatedImplementationAndIgnoredApprovalDisposition_returnFalse(){
		DataException exception = new DataException();
		exception.setImplementationType(ImplementationType.Automated);
		exception.setApprovalDisposition(ApprovalDisposition.Ignored);
		assertFalse(exception.isAvailableForSourceSystemToImplement());
	}
	
	@Test
	public void testIsApprovalDispositionNeedToNotify_withManaulImplementation_returnTrue(){
		DataException exception = new DataException();
		exception.setImplementationType(ImplementationType.Manual);
		assertTrue(exception.isApprovalDispositionNeedToNotify());
	}
	
	@Test
	public void testIsApprovalDispositionNeedToNotify_withAutomaticImplementationAndInitialApprovalDisposition_returnTrue(){
		DataException exception = new DataException();
		exception.setImplementationType(ImplementationType.Automated);
		exception.setApprovalDisposition(ApprovalDisposition.Initial);
		assertTrue(exception.isApprovalDispositionNeedToNotify());
	}
	
	@Test
	public void testIsApprovalDispositionNeedToNotify_withAutomatedImplementationAndApprovedApprovalDisposition_returnFalse(){
		DataException exception = new DataException();
		exception.setImplementationType(ImplementationType.Automated);
		exception.setApprovalDisposition(ApprovalDisposition.Approved);
		assertFalse(exception.isApprovalDispositionNeedToNotify());
	}
	
	@Test
	public void testIsApprovalDispositionNeedToNotify_withAutomatedImplementationAndDisapprovedApprovalDisposition_returnTrue(){
		DataException exception = new DataException();
		exception.setImplementationType(ImplementationType.Automated);
		exception.setApprovalDisposition(ApprovalDisposition.Disapproved);
		assertTrue(exception.isApprovalDispositionNeedToNotify());
	}
	
	@Test
	public void testIsApprovalDispositionNeedToNotify_withAutomatedImplementationAndIgnoredApprovalDisposition_returnTrue(){
		DataException exception = new DataException();
		exception.setImplementationType(ImplementationType.Automated);
		exception.setApprovalDisposition(ApprovalDisposition.Ignored);
		assertTrue(exception.isApprovalDispositionNeedToNotify());
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
