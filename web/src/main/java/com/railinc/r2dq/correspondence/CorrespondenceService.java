package com.railinc.r2dq.correspondence;

public interface CorrespondenceService {

	public void send(Correspondence correspondence);
	
	public void convertAndSend(CorrespondenceTemplate template);
}
