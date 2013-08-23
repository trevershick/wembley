package com.railinc.wembley.api.event;

import java.io.Serializable;
import java.util.List;

public interface DeliverySpec extends Serializable
{
	List<String> validate();
}
