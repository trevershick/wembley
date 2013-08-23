package com.railinc.wembley.api;

import com.railinc.wembley.api.address.Address;

public interface Notification {
	NotificationSource source();
	Iterable<Intent> intents();
	Iterable<Address> addresses();
}
