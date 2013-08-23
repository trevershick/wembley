package com.railinc.r2dq.domain;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class ImplementationTest {
	
	@Test
	public void testGetDerivedType_withImplementationTypeAutomatedAndSouceSystemNotAutomatic_returnForceStromDrain(){
		Implementation implementation = new Implementation();
		SourceSystem sourceSystem = mock(SourceSystem.class);
		implementation.setSourceSystem(sourceSystem);
		when(sourceSystem.isAutomatic()).thenReturn(false);
		implementation.setType(ImplementationType.Automated);
		assertEquals(ImplementationType.ForceStormDrain, implementation.getDerivedType());
	}
	
	@Test
	public void testGetDerivedType_withImplementationTypePassthroughAndSouceSystemNotAutomatic_returnForceStromDrain(){
		Implementation implementation = new Implementation();
		SourceSystem sourceSystem = mock(SourceSystem.class);
		implementation.setSourceSystem(sourceSystem);
		when(sourceSystem.isAutomatic()).thenReturn(false);
		implementation.setType(ImplementationType.PassThrough);
		assertEquals(ImplementationType.ForceStormDrain, implementation.getDerivedType());
	}
	
	@Test
	public void testGetDerivedType_withImplementationTypeAutomatedAndSouceSystemAutomatic_returnAutomated(){
		Implementation implementation = new Implementation();
		SourceSystem sourceSystem = mock(SourceSystem.class);
		implementation.setSourceSystem(sourceSystem);
		when(sourceSystem.isAutomatic()).thenReturn(true);
		implementation.setType(ImplementationType.Automated);
		assertEquals(ImplementationType.Automated, implementation.getDerivedType());
	}
	
	@Test
	public void testGetDerivedType_withImplementationTypePassthroughAndSouceSystemAutomatic_returnPassthrough(){
		Implementation implementation = new Implementation();
		SourceSystem sourceSystem = mock(SourceSystem.class);
		implementation.setSourceSystem(sourceSystem);
		when(sourceSystem.isAutomatic()).thenReturn(true);
		implementation.setType(ImplementationType.PassThrough);
		assertEquals(ImplementationType.PassThrough, implementation.getDerivedType());
	}

	@Test
	public void testGetDerivedType_withImplementationTypeManulAndSouceSystemAutomatic_returnManual(){
		Implementation implementation = new Implementation();
		SourceSystem sourceSystem = mock(SourceSystem.class);
		implementation.setSourceSystem(sourceSystem);
		when(sourceSystem.isAutomatic()).thenReturn(true);
		implementation.setType(ImplementationType.Manual);
		assertEquals(ImplementationType.Manual, implementation.getDerivedType());
	}
	
	@Test
	public void testGetDerivedType_withNoImplementationType_returnStromDrain(){
		Implementation implementation = new Implementation();
		SourceSystem sourceSystem = mock(SourceSystem.class);
		implementation.setSourceSystem(sourceSystem);
		when(sourceSystem.isAutomatic()).thenReturn(true);
		assertEquals(ImplementationType.StormDrain, implementation.getDerivedType());
	}
}
