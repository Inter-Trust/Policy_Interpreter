//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2014.05.22 a las 12:32:54 PM CEST 
//


package es.umu.intertrust.policyinterpreter.sds.xml;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the es.umu.intertrust.policyinterpreter.sds.xml package. 
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

    private final static QName _Sds_QNAME = new QName("http://inter-trust.eu/schema/interpreter/sds", "sds");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: es.umu.intertrust.policyinterpreter.sds.xml
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Sds }
     * 
     */
    public Sds createSds() {
        return new Sds();
    }

    /**
     * Create an instance of {@link Configuration }
     * 
     */
    public Configuration createConfiguration() {
        return new Configuration();
    }

    /**
     * Create an instance of {@link SecurityFeature }
     * 
     */
    public SecurityFeature createSecurityFeature() {
        return new SecurityFeature();
    }

    /**
     * Create an instance of {@link SecurityParameter }
     * 
     */
    public SecurityParameter createSecurityParameter() {
        return new SecurityParameter();
    }

    /**
     * Create an instance of {@link SecurityParameters }
     * 
     */
    public SecurityParameters createSecurityParameters() {
        return new SecurityParameters();
    }

    /**
     * Create an instance of {@link UndeploySecurityFeature }
     * 
     */
    public UndeploySecurityFeature createUndeploySecurityFeature() {
        return new UndeploySecurityFeature();
    }

    /**
     * Create an instance of {@link Functionality }
     * 
     */
    public Functionality createFunctionality() {
        return new Functionality();
    }

    /**
     * Create an instance of {@link Category }
     * 
     */
    public Category createCategory() {
        return new Category();
    }

    /**
     * Create an instance of {@link Target }
     * 
     */
    public Target createTarget() {
        return new Target();
    }

    /**
     * Create an instance of {@link Deploy }
     * 
     */
    public Deploy createDeploy() {
        return new Deploy();
    }

    /**
     * Create an instance of {@link Undeploy }
     * 
     */
    public Undeploy createUndeploy() {
        return new Undeploy();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Sds }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://inter-trust.eu/schema/interpreter/sds", name = "sds")
    public JAXBElement<Sds> createSds(Sds value) {
        return new JAXBElement<Sds>(_Sds_QNAME, Sds.class, null, value);
    }

}
