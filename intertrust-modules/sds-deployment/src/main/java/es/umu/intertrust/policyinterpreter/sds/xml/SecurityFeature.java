//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2014.05.22 a las 12:32:54 PM CEST 
//


package es.umu.intertrust.policyinterpreter.sds.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para securityFeature complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="securityFeature">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="target" type="{http://inter-trust.eu/schema/interpreter/sds}target" minOccurs="0"/>
 *         &lt;element name="functionality" type="{http://inter-trust.eu/schema/interpreter/sds}functionality" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="configuration" type="{http://inter-trust.eu/schema/interpreter/sds}configuration" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "securityFeature", propOrder = {
    "target",
    "functionality",
    "configuration"
})
public class SecurityFeature {

    protected Target target;
    protected List<Functionality> functionality;
    protected Configuration configuration;
    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "type")
    @XmlSchemaType(name = "anyURI")
    protected String type;

    /**
     * Obtiene el valor de la propiedad target.
     * 
     * @return
     *     possible object is
     *     {@link Target }
     *     
     */
    public Target getTarget() {
        return target;
    }

    /**
     * Define el valor de la propiedad target.
     * 
     * @param value
     *     allowed object is
     *     {@link Target }
     *     
     */
    public void setTarget(Target value) {
        this.target = value;
    }

    /**
     * Gets the value of the functionality property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the functionality property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFunctionality().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Functionality }
     * 
     * 
     */
    public List<Functionality> getFunctionality() {
        if (functionality == null) {
            functionality = new ArrayList<Functionality>();
        }
        return this.functionality;
    }

    /**
     * Obtiene el valor de la propiedad configuration.
     * 
     * @return
     *     possible object is
     *     {@link Configuration }
     *     
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Define el valor de la propiedad configuration.
     * 
     * @param value
     *     allowed object is
     *     {@link Configuration }
     *     
     */
    public void setConfiguration(Configuration value) {
        this.configuration = value;
    }

    /**
     * Obtiene el valor de la propiedad id.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Define el valor de la propiedad id.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Obtiene el valor de la propiedad type.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Define el valor de la propiedad type.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

}
