//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2014.07.10 a las 10:49:27 AM CEST 
//
package org.example.obligationnotification;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Clase Java para ObligationsType complex type.
 *
 * <p>
 * El siguiente fragmento de esquema especifica el contenido que se espera que
 * haya en esta clase.
 *
 * <pre>
 * &lt;complexType name="ObligationsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NotifyObligation" type="{http://www.example.org/ObligationNotification}NotifyObligationType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlRootElement(name = "Obligations")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ObligationsType", propOrder = {
    "notifyObligation"
})
public class ObligationsType {

    @XmlElement(name = "NotifyObligation", required = true)
    protected List<NotifyObligationType> notifyObligation;

    /**
     * Gets the value of the notifyObligation property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the notifyObligation property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNotifyObligation().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NotifyObligationType }
     *
     *
     */
    public List<NotifyObligationType> getNotifyObligation() {
        if (notifyObligation == null) {
            notifyObligation = new ArrayList<NotifyObligationType>();
        }
        return this.notifyObligation;
    }

}
