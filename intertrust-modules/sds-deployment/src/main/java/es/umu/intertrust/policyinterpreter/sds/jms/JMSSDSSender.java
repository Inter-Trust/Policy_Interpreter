package es.umu.intertrust.policyinterpreter.sds.jms;

import es.umu.intertrust.policyinterpreter.sds.SDSSenderException;
import es.umu.intertrust.policyinterpreter.sds.SDSSender;
import es.umu.intertrust.policyinterpreter.sds.xml.ObjectFactory;
import es.umu.intertrust.policyinterpreter.sds.xml.Sds;
import es.umu.intertrust.policyinterpreter.util.XMLUtils;
import java.io.StringWriter;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 *
 * @author Juanma
 */
public class JMSSDSSender implements SDSSender {

    public static final Logger logger = Logger.getLogger(JMSSDSSender.class.getName());

    JMSSender sender;

    public void initialize(Properties deploymentProperties) throws SDSSenderException {
        try {
            logger.log(Level.FINE, "Setting up SDS sender for topic ''{0}'' on {1}", new String[]{deploymentProperties.getProperty(Constants.JMS_TOPIC_PROPERTY), deploymentProperties.getProperty(Constants.JMS_BROKER_URL_PROPERTY)});
            sender = new JMSSender(generateJMSProperties(deploymentProperties));
        } catch (NamingException ex) {
            throw new SDSSenderException("Unable to initialize SDS sender: " + ex.getMessage(), ex);
        } catch (JMSException ex) {
            throw new SDSSenderException("Unable to initialize SDS sender: " + ex.getMessage(), ex);
        }
    }

    public void send(Sds sds) throws SDSSenderException {
        try {
            StringWriter output = new StringWriter();
            Marshaller marshaller = XMLUtils.getMarshaller(Sds.class);
            marshaller.marshal(new ObjectFactory().createSds(sds), output);
            String xmlSDS = output.toString();
            logger.log(Level.INFO, "Deploying SDS:\n{0}", xmlSDS);
            sender.sendMessage(xmlSDS);
        } catch (JAXBException ex) {
            throw new SDSSenderException("Unable to send SDS: " + ex.getMessage(), ex);
        } catch (JMSException ex) {
            throw new SDSSenderException("Unable to send SDS: " + ex.getMessage(), ex);
        }
    }

    private Properties generateJMSProperties(Properties properties) {
        Properties jndiContextEnvironment = new Properties();
        String jmsBrokerURL = properties.getProperty(Constants.JMS_BROKER_URL_PROPERTY);
        if (jmsBrokerURL != null) {
            jndiContextEnvironment.setProperty(Constants.JMS_BROKER_URL_JNDI_NAME, jmsBrokerURL);
        }
        String jmsTopic = properties.getProperty(Constants.JMS_TOPIC_PROPERTY);
        if (jmsTopic != null) {
            jndiContextEnvironment.setProperty(Constants.JMS_TOPIC_JNDI_NAME, jmsTopic);
        }
        return jndiContextEnvironment;
    }

}
