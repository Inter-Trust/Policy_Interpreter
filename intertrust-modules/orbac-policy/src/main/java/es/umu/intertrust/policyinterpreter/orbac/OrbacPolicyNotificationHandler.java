package es.umu.intertrust.policyinterpreter.orbac;

import es.umu.intertrust.policyinterpreter.Configuration;
import es.umu.intertrust.policyinterpreter.PolicyInterpreter;
import es.umu.intertrust.policyinterpreter.policy.AbstractPolicyNotificationHandler;
import es.umu.intertrust.policyinterpreter.policy.PolicyNotificationHandlerException;
import es.umu.intertrust.policyinterpreter.policy.PolicyRuleChange;
import es.umu.intertrust.policyinterpreter.util.XMLUtils;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.example.obligationnotification.NotifyObligationType;
import org.example.obligationnotification.ObligationsType;

/**
 *
 * @author Juanma
 */
public class OrbacPolicyNotificationHandler extends AbstractPolicyNotificationHandler {

    public static final Logger logger = Logger.getLogger(PolicyInterpreter.class.getName());

    JMSReceiver receiver;

    public void start() throws PolicyNotificationHandlerException {
        try {
            Properties policyProperties = getPolicyProperties();
            logger.log(Level.INFO, "Setting up notification listener for topic ''{0}'' on {1}", new String[]{policyProperties.getProperty(Constants.JMS_TOPIC_PROPERTY), policyProperties.getProperty(Constants.JMS_BROKER_URL_PROPERTY)});
            receiver = new JMSReceiver(generateJMSProperties(policyProperties));
            receiver.setMessageListener(new JMSReceiver.Listener() {

                public void onTextMessage(String message) {
                    logger.log(Level.FINE, "Policy change notification received:\n{0}", message);
                    String notificationNamePrefix = Configuration.getInstance().getPolicyProperties().getProperty(Constants.NOTIFICATION_NAME_PREFIX_PROPERTY);
                    ObligationsType filteredNotifications = new ObligationsType();
                    List<NotifyObligationType> filteredNotifyObligations = filteredNotifications.getNotifyObligation();
                    try {
                        Unmarshaller unmarshaller = XMLUtils.getUnmarshaller(ObligationsType.class);
                        ObligationsType notifications = (ObligationsType) unmarshaller.unmarshal(new StringReader(message));
                        List<PolicyRuleChange> policyRuleChanges = new ArrayList<PolicyRuleChange>();
                        for (NotifyObligationType notification : notifications.getNotifyObligation()) {
                            Obligation obligation;
                            String action = notification.getAction();
                            if (action == null) {
                                fireExceptionOccured(new PolicyNotificationHandlerException("Received obligation notification with no action."));
                            } else {
                                if ((notificationNamePrefix == null) || notification.getName().startsWith(notificationNamePrefix)) {
                                    int index = action.indexOf('.');
                                    if (index == -1) {
                                        obligation = new Obligation(notification.getName(), notification.getSubject(), action, notification.getObject());
                                    } else {
                                        obligation = new Obligation(notification.getName(), notification.getSubject(), action.substring(0, index), notification.getObject(), action.substring(index + 1));
                                    }
                                    filteredNotifyObligations.add(notification);
                                    policyRuleChanges.add(new PolicyRuleChange(obligation, Boolean.parseBoolean(notification.getStatus())));
                                }
                            }
                        }
                        if (!policyRuleChanges.isEmpty()) {
                            if (logger.isLoggable(Level.INFO)) {
                                Marshaller marshaller = XMLUtils.getMarshaller(ObligationsType.class);
                                StringWriter output = new StringWriter();
                                marshaller.marshal(filteredNotifications, output);
                                logger.log(Level.INFO, "Processing policy change notification:\n{0}", output.toString());
                            }
                            firePolicyStatusChanged(policyRuleChanges);
                        }
                    } catch (JAXBException ex) {
                        fireExceptionOccured(new PolicyNotificationHandlerException("Error while parsing policy notification: " + ex.getMessage(), ex));
                    } catch (Throwable ex) {
                        fireExceptionOccured(new PolicyNotificationHandlerException("Unexpected exception while processing policy notification: " + ex.getClass().getSimpleName() + ": " + ex.getMessage()));
                    }
                }

                public void onException(JMSException exception) {
                    fireExceptionOccured(new PolicyNotificationHandlerException("Error while receiving notifications: " + exception.getClass().getSimpleName() + ": " + exception.getMessage()));
                }
            });
            fireNotificationHandlerStarted();
        } catch (NamingException ex) {
            throw new PolicyNotificationHandlerException("Error starting notification handler: " + ex.getMessage(), ex);
        } catch (JMSException ex) {
            throw new PolicyNotificationHandlerException("Error starting notification handler: " + ex.getMessage(), ex);
        }
    }

    public void stop() throws PolicyNotificationHandlerException {
        try {
            if (receiver != null) {
                receiver.close();
            }
            fireNotificationHandlerStopped();
        } catch (JMSException ex) {
            throw new PolicyNotificationHandlerException("Error stopping notification handler", ex);
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
