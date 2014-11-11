//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2014.05.22 a las 12:32:54 PM CEST 
//


package es.umu.intertrust.policyinterpreter.sds.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Security Deployment Specification
 * 
 * <p>Clase Java para sds complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="sds">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="deploy" type="{http://inter-trust.eu/schema/interpreter/sds}deploy" minOccurs="0"/>
 *         &lt;element name="undeploy" type="{http://inter-trust.eu/schema/interpreter/sds}undeploy" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sds", propOrder = {
    "deploy",
    "undeploy"
})
public class Sds {

    protected Deploy deploy;
    protected Undeploy undeploy;

    /**
     * Obtiene el valor de la propiedad deploy.
     * 
     * @return
     *     possible object is
     *     {@link Deploy }
     *     
     */
    public Deploy getDeploy() {
        return deploy;
    }

    /**
     * Define el valor de la propiedad deploy.
     * 
     * @param value
     *     allowed object is
     *     {@link Deploy }
     *     
     */
    public void setDeploy(Deploy value) {
        this.deploy = value;
    }

    /**
     * Obtiene el valor de la propiedad undeploy.
     * 
     * @return
     *     possible object is
     *     {@link Undeploy }
     *     
     */
    public Undeploy getUndeploy() {
        return undeploy;
    }

    /**
     * Define el valor de la propiedad undeploy.
     * 
     * @param value
     *     allowed object is
     *     {@link Undeploy }
     *     
     */
    public void setUndeploy(Undeploy value) {
        this.undeploy = value;
    }

}
