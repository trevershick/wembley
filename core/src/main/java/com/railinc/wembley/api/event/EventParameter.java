package com.railinc.wembley.api.event;

import java.io.Serializable;

public interface EventParameter extends Serializable, Cloneable {

	String getParamName();
	String getParamValue();
}
