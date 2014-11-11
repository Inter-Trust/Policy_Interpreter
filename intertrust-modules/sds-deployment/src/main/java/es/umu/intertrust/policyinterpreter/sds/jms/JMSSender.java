package es.umu.intertrust.policyinterpreter.sds.jms;

import java.util.Properties;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Juanma
 */
public class JMSSender {

    static final String DEFAULT_CONNECTION_FACTORY_NAME = "ConnectionFactory";
    static final String DEFAULT_DESTINATION_NAME = JMSSender.class.getName() + ".destination";

    Connection connection;
    Session session;
    MessageProducer producer;

    public JMSSender() throws NamingException, JMSException {
        this(new InitialContext(), DEFAULT_CONNECTION_FACTORY_NAME, DEFAULT_DESTINATION_NAME);
    }

    public JMSSender(Properties jndiContextEnvironment) throws NamingException, JMSException {
        this(new InitialContext(jndiContextEnvironment), DEFAULT_CONNECTION_FACTORY_NAME, DEFAULT_DESTINATION_NAME);
    }

    public JMSSender(Context jndiContext, String connectionFactoryName, String destinationName) throws NamingException, JMSException {
        ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext.lookup(connectionFactoryName);
        Destination destination = (Destination) jndiContext.lookup(destinationName);
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        producer = session.createProducer(destination);
    }

    public void sendMessage(String message) throws JMSException {
        TextMessage textMessage = session.createTextMessage();
        textMessage.setText(message);
        producer.send(textMessage);
    }

    public void close() throws JMSException {
        producer.close();
        session.close();
        connection.close();
    }

}
