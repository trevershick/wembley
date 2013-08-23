package com.railinc.wembley.legacy.services.findusrail;

import com.railinc.wembley.api.core.NotificationService;
import com.railinc.wembley.legacy.services.findusrail.model.FindUsRailContactRequest;
import com.railinc.wembley.legacy.services.findusrail.model.FindUsRailContactResponse;

public interface FindUsRailContactService extends NotificationService {

	FindUsRailContactResponse getFindUsRailContacts(FindUsRailContactRequest request);
}
