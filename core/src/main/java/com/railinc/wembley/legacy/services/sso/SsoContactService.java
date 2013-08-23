package com.railinc.wembley.legacy.services.sso;

import com.railinc.wembley.api.core.NotificationService;

public interface SsoContactService extends NotificationService {

	SsoContactResponse getSsoContactByRole( SsoContactRequest request );
	
	SsoContactResponse getSsoContactByRoleAndMark( SsoContactRequest request );

	SsoContactResponse getSsoContactByUserId( SsoContactRequest request );
}
