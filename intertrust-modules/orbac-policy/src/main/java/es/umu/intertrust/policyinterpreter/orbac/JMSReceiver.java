package es.umu.intertrust.policyinterpreter.orbac;

import java.util.Properties;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Juanma
 */
public class JMSReceiver {

    static final String DEFAULT_CONNECTION_FACTORY_NAME = "ConnectionFactory";
    static final String DEFAULT_DESTINATION_NAME = JMSReceiver.class.getName() + ".destination";

    Connection connection;
    Session session;
    MessageConsumer consumer;

    public JMSReceiver() throws NamingException, JMSException {
        this(new InitialContext(), DEFAULT_CONNECTION_FACTORY_NAME, DEFAULT_DESTINATION_NAME);
    }

    public JMSReceiver(Properties jndiContextEnvironment) throws NamingException, JMSException {
        this(new InitialContext(jndiContextEnvironment), DEFAULT_CONNECTION_FACTORY_NAME, DEFAULT_DESTINATION_NAME);
    }

    public JMSReceiver(Context jndiContext, String connectionFactoryName, String destinationName) throws NamingException, JMSException {
        ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext.lookup(connectionFactoryName);
        Destination destination = (Destination) jndiContext.lookup(destinationName);
        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        consumer = session.createConsumer(destination);
    }

    public void setMessageListener(final Listener listener) throws JMSException {
        connection.setExceptionListener(listener);
        consumer.setMessageListener(new MessageListener() {

            public void onMessage(Message message) {
                try {
                    if (message instanceof TextMessage) {
                        TextMessage textMessage = (TextMessage) message;
                        listener.onTextMessage(textMessage.getText());
                    } else {
                        listener.onException(new JMSException("Unsupported message type: " + message.getJMSType()));
                    }
                } catch (JMSException ex) {
                    listener.onException(ex);
                }
            }
        });
    }

    public void close() throws JMSException {
        consumer.close();
        session.close();
        connection.close();
    }

    public interface Listener extends ExceptionListener {

        public void onTextMessage(String message);
    }
}
