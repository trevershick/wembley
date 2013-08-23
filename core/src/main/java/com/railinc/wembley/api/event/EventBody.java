package com.railinc.wembley.api.event;

import java.io.Serializable;

public interface EventBody extends Serializable, Cloneable {

	Object getBodyRoot();
	Object clone() throws CloneNotSupportedException;
}
