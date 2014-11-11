package es.umu.intertrust.policyinterpreter.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Juanma
 */
public class XMLUtils {

    public static Marshaller getMarshaller(Class jaxbClass) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(jaxbClass);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        return marshaller;
    }

    public static Unmarshaller getUnmarshaller(Class jaxbClass) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(jaxbClass);
        return jaxbContext.createUnmarshaller();
    }
}
