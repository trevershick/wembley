package com.railinc.wembley.legacy.services.findusrail;

import java.io.StringWriter;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.wembley.api.core.NotificationServiceException;
import com.railinc.wembley.legacy.services.findusrail.model.FindUsRailContactRequest;

public class FindUsRailContactServiceRequestMarshaller {

	private static final Logger log = LoggerFactory.getLogger(FindUsRailContactServiceRequestMarshaller.class);
	private JAXBContext jaxbCtx;

	public FindUsRailContactServiceRequestMarshaller() {
		try {
			this.jaxbCtx = JAXBContext.newInstance(ContactInformationRequestDocument.class);
		} catch (JAXBException e) {
			log.error("JAXBException trying to create JAXBContext!", e);
			throw new NotificationServiceException("Error creating the JAXB Context", e);
		}
	}

	public String marshalFindUsRailContactServiceRequest(Long msgId, FindUsRailContactRequest contactRequest) {

		StringWriter xml = new StringWriter();

		ContactInformationRequestDocument doc = new ContactInformationRequestDocument();

		doc.setMessageHeader(new MessageHeader());

		ControlInformation conInf = new ControlInformation();
		doc.getMessageHeader().setControlInformation(conInf);
		conInf.setMessageId(msgId);
		conInf.setMessageTimestamp(new Date());

		SenderSoftwareComponent softwareComp = new SenderSoftwareComponent();
		doc.getMessageHeader().setSenderSoftwareComponent(softwareComp);
		softwareComp.setSoftwareVersion("1.0.0");
		softwareComp.setSoftwareComponentId(new SoftwareComponentId());
		softwareComp.getSoftwareComponentId().setIdSchemeName("J");
		softwareComp.getSoftwareComponentId().setSoftwareComponentId("NOTIF_SERV");

		doc.setContactRequestDetail(new ContactRequestDetail());
		doc.getContactRequestDetail().setContactQuery(new ContactQuery());

		Parameters params = new Parameters();
		doc.getContactRequestDetail().getContactQuery().setParameters(params);
		params.setCategoryId(contactRequest.getCategory());
		params.setCategoryRole(contactRequest.getCategoryRole() == null ? null : contactRequest.getCategoryRole().toString());
		params.setCategoryFunction(contactRequest.getCategoryFunction());
		Company comp = new Company();
		params.setCompany(comp);
		comp.setCompanyType(contactRequest.getCompanyType() == null ? null : contactRequest.getCompanyType().toString());
		comp.setCompanyId(contactRequest.getCompanyId());
		if(contactRequest.isIncludeParent()) {
			comp.addCompanyInclude(FindUsRailCompanyInclude.PARENT.name());
		}
		if(contactRequest.isIncludeChildren()) {
			comp.addCompanyInclude(FindUsRailCompanyInclude.CHILDREN.name());
		}
		if(contactRequest.isIncludeAgents()) {
			comp.addCompanyInclude(FindUsRailCompanyInclude.AGENTS.name());
		}
		try {
			Marshaller m = this.jaxbCtx.createMarshaller();
			m.marshal(doc, xml);
		} catch (JAXBException e) {
			log.error("Exception trying to marshal FindUsRail request", e);
		}

		return xml.getBuffer().toString();
	}
}
