package es.umu.intertrust.policyinterpreter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 *
 * @author Juanma
 */
public class ConfigurationParser {

    static final String POLICY_HANDLERS_DIR_XPATH = "policyHandlersDir";
    static final String POLICY_HANDLERS_CONF_XPATH = "policyHandlersConf";
    static final String POLICY_NOTIFICATION_HANDLER_XPATH = "policy/policyNotificationHandler";
    static final String DEPLOYMENT_ENFORCER_XPATH = "deployment/deploymentEnforcer";
    static final String POLICY_QUERY_MANAGERS_XPATH = "policy/queryManagers/queryManager";
    static final String DEPLOYMENT_QUERY_MANAGERS_XPATH = "deployment/queryManagers/queryManager";
    static final String POLICY_PRECONDITION_AVAILABLE_CLASSES_XPATH = "policy/preconditionAvailableClasses/class";
    static final String DEPLOYMENT_PRECONDITION_AVAILABLE_CLASSES_XPATH = "deployment/preconditionAvailableClasses/class";
    static final String COMMON_PROPERTY_ENTRIES_XPATH = "properties/entry";
    static final String POLICY_PROPERTY_ENTRIES_XPATH = "policy/properties/entry";
    static final String DEPLOYMENT_PROPERTY_ENTRIES_XPATH = "deployment/properties/entry";
    static final String PROPERTY_ENTRY_KEY_ATTRIBUTE_NAME = "key";

    public void parse(File configurationFile, Configuration configuration) throws ConfigurationParsingException {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(configurationFile);
            Element configElement = document.getDocumentElement();
            XPath xPath = XPathFactory.newInstance().newXPath();

            String policyHandlersDirectory = xPath.evaluate(POLICY_HANDLERS_DIR_XPATH, configElement);
            if (policyHandlersDirectory.isEmpty()) {
                throw new ConfigurationParsingException("Element not found in " + configurationFile.getName() + ": " + POLICY_HANDLERS_DIR_XPATH);
            } else {
                configuration.policyHandlersDirectory = new File(policyHandlersDirectory);
            }
            String policyHandlersConf = xPath.evaluate(POLICY_HANDLERS_CONF_XPATH, configElement);
            if (!policyHandlersConf.isEmpty()) {
                configuration.policyHandlersConfLocation = new File(policyHandlersConf);
            }
            configuration.policyNotificationHandlerClass = xPath.evaluate(POLICY_NOTIFICATION_HANDLER_XPATH, configElement);
            if (configuration.policyNotificationHandlerClass.isEmpty()) {
                throw new ConfigurationParsingException("Element not found in " + configurationFile.getName() + ": " + POLICY_NOTIFICATION_HANDLER_XPATH);
            }
            configuration.deploymentEnforcerClass = xPath.evaluate(DEPLOYMENT_ENFORCER_XPATH, configElement);
            if (configuration.deploymentEnforcerClass.isEmpty()) {
                throw new ConfigurationParsingException("Element not found in " + configurationFile.getName() + ": " + DEPLOYMENT_ENFORCER_XPATH);
            }
            configuration.policyQueryManagerClasses = parseList((NodeList) xPath.evaluate(POLICY_QUERY_MANAGERS_XPATH, configElement, XPathConstants.NODESET));
            configuration.deploymentQueryManagerClasses = parseList((NodeList) xPath.evaluate(DEPLOYMENT_QUERY_MANAGERS_XPATH, configElement, XPathConstants.NODESET));

            configuration.preconditionAvailableClasses = parseList((NodeList) xPath.evaluate(POLICY_PRECONDITION_AVAILABLE_CLASSES_XPATH, configElement, XPathConstants.NODESET));
            configuration.preconditionAvailableClasses.addAll(parseList((NodeList) xPath.evaluate(DEPLOYMENT_PRECONDITION_AVAILABLE_CLASSES_XPATH, configElement, XPathConstants.NODESET)));

            Properties commonProperties;
            try {
                commonProperties = parseProperties((NodeList) xPath.evaluate(COMMON_PROPERTY_ENTRIES_XPATH, configElement, XPathConstants.NODESET));
            } catch (ConfigurationParsingException ex) {
                throw new ConfigurationParsingException("Invalid property found in " + configurationFile.getName() + "(" + COMMON_PROPERTY_ENTRIES_XPATH + "): " + ex.getMessage());
            }
            try {
                configuration.policyProperties = parseProperties(commonProperties, (NodeList) xPath.evaluate(POLICY_PROPERTY_ENTRIES_XPATH, configElement, XPathConstants.NODESET));
            } catch (ConfigurationParsingException ex) {
                throw new ConfigurationParsingException("Invalid property found in " + configurationFile.getName() + "(" + POLICY_PROPERTY_ENTRIES_XPATH + "): " + ex.getMessage());
            }
            try {
                configuration.deploymentProperties = parseProperties(commonProperties, (NodeList) xPath.evaluate(DEPLOYMENT_PROPERTY_ENTRIES_XPATH, configElement, XPathConstants.NODESET));
            } catch (ConfigurationParsingException ex) {
                throw new ConfigurationParsingException("Invalid property found in " + configurationFile.getName() + "(" + DEPLOYMENT_PROPERTY_ENTRIES_XPATH + "): " + ex.getMessage());
            }
        } catch (ParserConfigurationException ex) {
            throw new ConfigurationParsingException("Unable to parse configuration: " + ex.getMessage(), ex);
        } catch (SAXException ex) {
            throw new ConfigurationParsingException("Unable to parse configuration: " + ex.getMessage(), ex);
        } catch (FileNotFoundException ex) {
            throw new ConfigurationParsingException("Configuration file not found: " + ex.getMessage(), ex);
        } catch (IOException ex) {
            throw new ConfigurationParsingException("Unable to parse configuration: " + ex.getMessage(), ex);
        } catch (XPathExpressionException ex) {
            throw new ConfigurationParsingException("Unable to parse configuration: " + ex.getMessage(), ex);
        }
    }

    private List<String> parseList(NodeList nodeList) {
        List<String> elements = new ArrayList<String>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            elements.add(nodeList.item(i).getTextContent());
        }
        return elements;
    }

    private Properties parseProperties(NodeList nodeList) throws ConfigurationParsingException {
        return parseProperties(null, nodeList);
    }

    private Properties parseProperties(Properties defaults, NodeList nodeList) throws ConfigurationParsingException {
        Properties properties;
        if (defaults == null) {
            properties = new Properties();
        } else {
            properties = new Properties(defaults);
        }
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node entry = nodeList.item(i);
            Node key = entry.getAttributes().getNamedItem(PROPERTY_ENTRY_KEY_ATTRIBUTE_NAME);
            if (key == null) {
                throw new ConfigurationParsingException("No attribute found: " + PROPERTY_ENTRY_KEY_ATTRIBUTE_NAME);
            }
            if (!hasTextContent(entry)) {
                throw new ConfigurationParsingException("Invalid property entry.");
            }
            properties.setProperty(key.getTextContent(), entry.getTextContent());
        }
        return properties;
    }

    private boolean hasTextContent(Node node) {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i) instanceof Text) {
                return true;
            }
        }
        return false;
    }
}
