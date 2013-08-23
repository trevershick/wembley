package com.railinc.wembley.api.event;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;



@XmlRegistry
public class ObjectFactory {

    private final static QName _Body_QNAME = new QName("http://events.notifserv.railinc.com", "body");

    @XmlElementDecl(namespace = "http://events.notifserv.railinc.com", name = "TextBodyType", substitutionHeadNamespace = "http://events.notifserv.railinc.com", substitutionHeadName = "body")
    public JAXBElement<TextEventBodyVo> createTextBody(TextEventBodyVo value) {
        return new JAXBElement<TextEventBodyVo>(_Body_QNAME, TextEventBodyVo.class, null, value);
    }

    @XmlElementDecl(namespace = "http://events.notifserv.railinc.com", name = "XmlBodyType", substitutionHeadNamespace = "http://events.notifserv.railinc.com", substitutionHeadName = "body")
    public JAXBElement<XmlEventBodyVo> createXmlBody(XmlEventBodyVo value) {
        return new JAXBElement<XmlEventBodyVo>(_Body_QNAME, XmlEventBodyVo.class, null, value);
    }

    @XmlElementDecl(namespace = "http://events.notifserv.railinc.com", name = "body")
    public JAXBElement<Object> createBody(Object value) {
        return new JAXBElement<Object>(_Body_QNAME, Object.class, null, value);
    }
}
