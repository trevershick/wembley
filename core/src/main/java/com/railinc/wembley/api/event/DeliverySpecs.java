package com.railinc.wembley.api.event;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(DeliverySpecsVo.Adapter.class)
public interface DeliverySpecs extends Serializable {

	List<? extends DeliverySpec> getDeliverySpecs();
	void addDeliverySpec(AbstractDeliverySpecVo deliverySpec);
}
