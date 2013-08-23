package com.railinc.r2dq.integration;


import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.railinc.r2dq.domain.DataException;
import com.railinc.r2dq.domain.SourceSystem;
import com.railinc.r2dq.integration.msg.InboundMDMExceptionMessage;
import com.railinc.r2dq.sourcesystem.SourceSystemService;

@RunWith(MockitoJUnitRunner.class)
public class RawInboundMessageToExceptionXFormerTest {
	@Mock
	private SourceSystemService sourceSystemService;
	
	@InjectMocks
	private InboundMessageToDataExceptionXFormer transformer = new InboundMessageToDataExceptionXFormer(); 
	
	@Test
	public void testApply(){
		// Assemble
		SourceSystem sourceSystem = mock(SourceSystem.class);
		when(sourceSystemService.getOrCreate(anyString())).thenReturn(sourceSystem);
		

		InboundMDMExceptionMessage in = new InboundMDMExceptionMessage();
		in.setCode(123L);
		in.setDescription("description");
		in.setCreated(new Date());
		in.setMdmAttributevalue("mdmAttributevalue");
		in.setMdmObjectAttribute("mdmObjectAttribute");
		in.setMdmObjectType("mdmObjectType");
		in.setSourceSystemKeyColumn("SSOUSER");
		in.setSourceSystemKeyValue("ITVXM01");
		in.setSourceSystemObjectData("TESTME");

		// Act
		DataException d = transformer.apply(in);
		assertEquals(sourceSystem, d.getSourceSystem());
		assertEquals(in.getCode(), d.getRuleNumber());
		assertEquals(in.getDescription(), d.getDescription());
		assertEquals(in.getCreated(), d.getExceptionCreated());
		assertEquals(in.getMdmAttributevalue(), d.getMdmAttributevalue());
		assertEquals(in.getMdmObjectAttribute(), d.getMdmObjectAttribute());
		assertEquals(in.getMdmObjectType(), d.getMdmObjectType());
		assertEquals(in.getSourceSystemKeyColumn(), d.getSourceSystemKeyColumn());
		assertEquals(in.getSourceSystemObjectData(), d.getSourceSystemObjectData());
		assertEquals(in.getSourceSystemValue(), d.getSourceSystemValue());
		
	}

}
