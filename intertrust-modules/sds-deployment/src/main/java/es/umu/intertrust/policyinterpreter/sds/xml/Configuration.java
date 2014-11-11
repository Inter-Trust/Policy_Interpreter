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
 * <p>Clase Java para configuration complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="configuration">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="securityParameters" type="{http://inter-trust.eu/schema/interpreter/sds}securityParameters"/>
 *         &lt;element name="securityDescription" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "configuration", propOrder = {
    "securityParameters",
    "securityDescription"
})
public class Configuration {

    protected SecurityParameters securityParameters;
    protected Object securityDescription;

    /**
     * Obtiene el valor de la propiedad securityParameters.
     * 
     * @return
     *     possible object is
     *     {@link SecurityParameters }
     *     
     */
    public SecurityParameters getSecurityParameters() {
        return securityParameters;
    }

    /**
     * Define el valor de la propiedad securityParameters.
     * 
     * @param value
     *     allowed object is
     *     {@link SecurityParameters }
     *     
     */
    public void setSecurityParameters(SecurityParameters value) {
        this.securityParameters = value;
    }

    /**
     * Obtiene el valor de la propiedad securityDescription.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getSecurityDescription() {
        return securityDescription;
    }

    /**
     * Define el valor de la propiedad securityDescription.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setSecurityDescription(Object value) {
        this.securityDescription = value;
    }

}
