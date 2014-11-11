package es.umu.intertrust.policyinterpreter.orbacsds.commons;

import es.umu.intertrust.policyinterpreter.sds.SDSTarget;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.DOMException;
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
public class SDSHandlerConfigurationParser {

    static final String PRECONDITIONS_ACTION_ELEMENT_NAME = "policyAction";
    static final String CATEGORY_ELEMENT_NAME = "category";
    static final String TYPE_ELEMENT_NAME = "type";
    static final String MAIN_FUNCTIONALITY_ELEMENT_NAME = "functionality";
    static final String TARGETS_XPATH_EXPRESSION = "targets/target";
    static final String NAME_ATTR_NAME = "name";
    static final String POLICY_REF_ATTR_NAME = "policy-ref";
    static final String ID_ATTR_NAME = "id";
    static final String DEFAULT_ELEMENT_NAME = "default";
    String preconditionsAction;
    String category;
    String type;
    String mainFunctionality;
    Map<String, SDSTarget> targetsMap;

    boolean parsePreconditionsAction = true;
    boolean parseMainFunctionality = true;
    boolean requirePropertyElementsId = true;

    Element configElement;
    XPath xPath;

    public boolean getParsePreconditionsAction() {
        return parsePreconditionsAction;
    }

    public void setParsePreconditionsAction(boolean parsePreconditionsAction) {
        this.parsePreconditionsAction = parsePreconditionsAction;
    }

    public boolean getParseMainFunctionality() {
        return parseMainFunctionality;
    }

    public void setParseMainFunctionality(boolean parseMainFunctionality) {
        this.parseMainFunctionality = parseMainFunctionality;
    }

    public boolean isRequiredPropertyElementsId() {
        return requirePropertyElementsId;
    }

    public void setRequirePropertyElementsId(boolean requirePropertyElementsId) {
        this.requirePropertyElementsId = requirePropertyElementsId;
    }

    public void parse(File config) throws ConfigurationParsingException {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(config);
            configElement = document.getDocumentElement();
            xPath = XPathFactory.newInstance().newXPath();
            if (parsePreconditionsAction) {
                preconditionsAction = xPath.evaluate(PRECONDITIONS_ACTION_ELEMENT_NAME, configElement);
                if (preconditionsAction.isEmpty()) {
                    throw new ConfigurationParsingException("Element not found in " + config.getName() + ": " + PRECONDITIONS_ACTION_ELEMENT_NAME);
                }
            }
            category = xPath.evaluate(CATEGORY_ELEMENT_NAME, configElement);
            if (category.isEmpty()) {
                throw new ConfigurationParsingException("Element not found in " + config.getName() + ": " + CATEGORY_ELEMENT_NAME);
            }
            type = xPath.evaluate(TYPE_ELEMENT_NAME, configElement);
            if (type.isEmpty()) {
                throw new ConfigurationParsingException("Element not found in " + config.getName() + ": " + TYPE_ELEMENT_NAME);
            }
            if (parseMainFunctionality) {
                mainFunctionality = xPath.evaluate(MAIN_FUNCTIONALITY_ELEMENT_NAME, configElement);
                if (mainFunctionality.isEmpty()) {
                    throw new ConfigurationParsingException("Element not found in " + config.getName() + ": " + MAIN_FUNCTIONALITY_ELEMENT_NAME);
                }
            }

            NodeList xmlTargets = (NodeList) xPath.evaluate(TARGETS_XPATH_EXPRESSION, configElement, XPathConstants.NODESET);
            targetsMap = new HashMap<String, SDSTarget>();
            for (int i = 0; i < xmlTargets.getLength(); i++) {
                Node xmlTarget = xmlTargets.item(i);
                String policyRef;
                Node xmlPolicyRef = xmlTarget.getAttributes().getNamedItem(POLICY_REF_ATTR_NAME);
                if (xmlPolicyRef != null) {
                    policyRef = xmlPolicyRef.getTextContent();
                } else {
                    Node xmlName = xmlTarget.getAttributes().getNamedItem(NAME_ATTR_NAME);
                    if (xmlName != null) {
                        policyRef = xmlName.getTextContent();
                    } else {
                        throw new ConfigurationParsingException("No attribute found for target in " + config.getName() + ": " + POLICY_REF_ATTR_NAME + " or " + NAME_ATTR_NAME);
                    }
                }
                targetsMap.put(policyRef, new SDSTarget(xmlTarget.getTextContent()));
            }
        } catch (IOException ex) {
            throw new ConfigurationParsingException("Unable to parse handler configuration: " + ex.getMessage(), ex);
        } catch (ParserConfigurationException ex) {
            throw new ConfigurationParsingException("Unable to parse handler configuration: " + ex.getMessage(), ex);
        } catch (XPathExpressionException ex) {
            throw new ConfigurationParsingException("Unable to parse handler configuration: " + ex.getMessage(), ex);
        } catch (DOMException ex) {
            throw new ConfigurationParsingException("Unable to parse handler configuration: " + ex.getMessage(), ex);
        } catch (SAXException ex) {
            throw new ConfigurationParsingException("Unable to parse handler configuration: " + ex.getMessage(), ex);
        }
    }

    public ConfigurationProperty parseProperty(String... elementNames) throws ConfigurationParsingException {
        try {
            if (elementNames.length < 2) {
                throw new IllegalArgumentException("No element names specified for parsing.");
            }
            Element propertyElement = getPropertyElement(elementNames[0]);
            return parseProperty(propertyElement, Arrays.copyOfRange(elementNames, 1, elementNames.length));
        } catch (XPathExpressionException ex) {
            throw new ConfigurationParsingException("Unable to parse handler configuration: " + ex.getMessage(), ex);
        }
    }

    private ConfigurationProperty parseProperty(Element propertyElement, String... elementNames) throws ConfigurationParsingException, XPathExpressionException {
        if (elementNames.length == 0) {
            throw new IllegalArgumentException("No element names specified for parsing.");
        }
        ConfigurationProperty property = new ConfigurationProperty();
        Map<String, String> valuesByName = new HashMap<String, String>();
        Map<String, ConfigurationProperty> dependentPropertiesByValue = new HashMap<String, ConfigurationProperty>();
        NodeList xmlElements = (NodeList) xPath.evaluate(elementNames[0], propertyElement, XPathConstants.NODESET);
        if (xmlElements.getLength() == 0) {
            return null;
        }
        for (int i = 0; i < xmlElements.getLength(); i++) {
            Node xmlValue = xmlElements.item(i);
            Node xmlName = xmlValue.getAttributes().getNamedItem(NAME_ATTR_NAME);
            Node xmlPolicyRef = xmlValue.getAttributes().getNamedItem(POLICY_REF_ATTR_NAME);
            Node xmlId = xmlValue.getAttributes().getNamedItem(ID_ATTR_NAME);
            String value;
            if (xmlId != null) {
                value = xmlId.getTextContent();
            } else {
                if (!hasTextContent(xmlValue)) {
                    if (requirePropertyElementsId) {
                        throw new ConfigurationParsingException("No attribute found for " + elementNames[0] + " : " + ID_ATTR_NAME);
                    } else {
                        value = null;
                    }
                } else {
                    value = xmlValue.getTextContent();
                }
            }
            if (elementNames.length == 1) {
                processElementValue(property, xmlName, xmlPolicyRef, value, valuesByName);
            } else {
                if (xmlValue instanceof Element) {
                    String policyRef = processElementValue(property, xmlName, xmlPolicyRef, value, valuesByName);
                    ConfigurationProperty dependentProperty = parseProperty((Element) xmlValue, Arrays.copyOfRange(elementNames, 1, elementNames.length));
                    if (dependentProperty != null) {
                        property.putDependentProperty(policyRef, dependentProperty);
                        dependentPropertiesByValue.put(value, dependentProperty);
                    }
                } else {
                    throw new ConfigurationParsingException("Unable to parse configuration: " + elementNames[1]);
                }
            }
        }
        NodeList xmlDefaultElements = (NodeList) xPath.evaluate(DEFAULT_ELEMENT_NAME, propertyElement, XPathConstants.NODESET);
        if (xmlDefaultElements.getLength() > 0) {
            String defaultValue = xmlDefaultElements.item(0).getTextContent();
            processDefaultElementValue(property, defaultValue, valuesByName);
            ConfigurationProperty defaultDependentProperty = dependentPropertiesByValue.get(property.getDefaultValue());
            if (defaultDependentProperty != null) {
                property.setDefaultDependentProperty(defaultDependentProperty);
            }
        }
        return property;
    }

    private Element getPropertyElement(String propertyName) throws ConfigurationParsingException, XPathExpressionException {
        if (configElement == null) {
            throw new IllegalStateException("Configuration not parsed.");
        }
        Element propertyElement = (Element) xPath.evaluate(propertyName, configElement, XPathConstants.NODE);
        if (propertyElement == null) {
            throw new ConfigurationParsingException("Configuration property not found: " + propertyName);
        } else {
            return propertyElement;
        }
    }

    private String processElementValue(ConfigurationProperty property, Node xmlName, Node xmlPolicyRef, String value, Map<String, String> valuesByName) {
        String policyRef;
        if (xmlPolicyRef != null) {
            policyRef = xmlPolicyRef.getTextContent();
        } else if (xmlName != null) {
            policyRef = xmlName.getTextContent();
        } else {
            policyRef = value;
        }
        String shortValue;
        if (xmlName != null) {
            String name = xmlName.getTextContent();
            shortValue = name;
            valuesByName.put(name, value);
        } else if (xmlPolicyRef != null) {
            shortValue = xmlPolicyRef.getTextContent();
        } else {
            shortValue = value;
        }
        property.putValue(policyRef, value);
        property.putShortValue(policyRef, shortValue);
        return policyRef;
    }

    private void processDefaultElementValue(ConfigurationProperty property, String defaultValue, Map<String, String> valuesByName) throws ConfigurationParsingException {
        Map<String, String> values = property.getValues();
        Map<String, String> shortValues = property.getShortValues();
        if (valuesByName.containsKey(defaultValue)) {
            property.setDefaultValue(valuesByName.get(defaultValue));
            property.setDefaultShortValue(defaultValue);
        } else if (values.containsKey(defaultValue)) {
            property.setDefaultValue(values.get(defaultValue));
            property.setDefaultShortValue(shortValues.get(defaultValue));
        } else if (values.containsValue(defaultValue)) {
            property.setDefaultValue(defaultValue);
            if (Collections.frequency(shortValues.values(), defaultValue) == 1) {
                for (Map.Entry<String, String> entry : shortValues.entrySet()) {
                    if (entry.getValue().equals(defaultValue)) {
                        property.setDefaultShortValue(entry.getKey());
                    }
                }
            } else {
                property.setDefaultShortValue(defaultValue);
            }
        } else {
            throw new ConfigurationParsingException("Default value does not point to a valid value: " + defaultValue);
        }
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

    public String getPreconditionsAction() {
        return preconditionsAction;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public String getMainFunctionality() {
        return mainFunctionality;
    }

    public Map<String, SDSTarget> getTargetsMap() {
        return targetsMap;
    }

    public class ConfigurationProperty {

        Map<String, String> values;
        Map<String, String> shortValues;
        Map<String, ConfigurationProperty> dependentProperties;
        String defaultValue;
        String defaultShortValue;
        ConfigurationProperty defaultDependentProperty;

        private ConfigurationProperty() {
            values = new HashMap<String, String>();
            shortValues = new HashMap<String, String>();
            dependentProperties = new HashMap<String, ConfigurationProperty>();
            defaultValue = null;
            defaultShortValue = null;
            defaultDependentProperty = null;
        }

        public Map<String, String> getValues() {
            return values;
        }

        public Map<String, String> getShortValues() {
            return shortValues;
        }

        public Map<String, ConfigurationProperty> getDependentProperties() {
            return dependentProperties;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public String getDefaultShortValue() {
            return defaultShortValue;
        }

        public ConfigurationProperty getDefaultDependentProperty() {
            return defaultDependentProperty;
        }

        private void putValue(String policyRef, String value) {
            this.values.put(policyRef, value);
        }

        private void putShortValue(String policyRef, String value) {
            this.shortValues.put(policyRef, value);
        }

        private void putDependentProperty(String policyRef, ConfigurationProperty property) {
            this.dependentProperties.put(policyRef, property);
        }

        private void setDefaultValue(String value) {
            this.defaultValue = value;
        }

        private void setDefaultShortValue(String value) {
            this.defaultShortValue = value;
        }

        private void setDefaultDependentProperty(ConfigurationProperty property) {
            this.defaultDependentProperty = property;
        }
    }
}
