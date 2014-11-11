//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2014.07.10 a las 10:49:27 AM CEST 
//


package org.example.obligationnotification;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.example.obligationnotification package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Obligations_QNAME = new QName("http://www.example.org/ObligationNotification", "Obligations");
    private final static QName _NotifyObligation_QNAME = new QName("http://www.example.org/ObligationNotification", "NotifyObligation");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.example.obligationnotification
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ObligationsType }
     * 
     */
    public ObligationsType createObligationsType() {
        return new ObligationsType();
    }

    /**
     * Create an instance of {@link NotifyObligationType }
     * 
     */
    public NotifyObligationType createNotifyObligationType() {
        return new NotifyObligationType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObligationsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/ObligationNotification", name = "Obligations")
    public JAXBElement<ObligationsType> createObligations(ObligationsType value) {
        return new JAXBElement<ObligationsType>(_Obligations_QNAME, ObligationsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NotifyObligationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/ObligationNotification", name = "NotifyObligation")
    public JAXBElement<NotifyObligationType> createNotifyObligation(NotifyObligationType value) {
        return new JAXBElement<NotifyObligationType>(_NotifyObligation_QNAME, NotifyObligationType.class, null, value);
    }

}
