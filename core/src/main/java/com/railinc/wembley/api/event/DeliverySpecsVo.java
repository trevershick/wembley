package com.railinc.wembley.api.event;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DeliverySpecsVo implements DeliverySpecs {

	private static final long serialVersionUID = -8871082655568257745L;
	private List<AbstractDeliverySpecVo> deliverySpecs;

	@XmlElement(name = "deliverySpec", namespace = "http://events.notifserv.railinc.com")
	public List<AbstractDeliverySpecVo> getDeliverySpecs() {
		return deliverySpecs;
	}

	public void setDeliverySpecs(List<AbstractDeliverySpecVo> deliverySpecs) {
		this.deliverySpecs = deliverySpecs;
	}

	public void addDeliverySpec(AbstractDeliverySpecVo deliverySpec) {
		if(this.deliverySpecs == null) {
			this.deliverySpecs = new ArrayList<AbstractDeliverySpecVo>();
		}
		this.deliverySpecs.add(deliverySpec);
	}

	public static class Adapter extends XmlAdapter<DeliverySpecsVo, DeliverySpecs> {
		public DeliverySpecs unmarshal(DeliverySpecsVo v) {
			return v;
		}

		public DeliverySpecsVo marshal(DeliverySpecs v) {
			return (DeliverySpecsVo) v;
		}
	}
}
