package es.umu.intertrust.policyinterpreter.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 *
 * @author Juanma
 */
public class XMLParser {

    org.w3c.dom.Element mainElement;
    XPath xPath = XPathFactory.newInstance().newXPath();

    public XMLParser(File file) throws ParsingException {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
            mainElement = document.getDocumentElement();
        } catch (ParserConfigurationException ex) {
            throw new ParsingException(ex.getMessage(), ex);
        } catch (SAXException ex) {
            throw new ParsingException(ex.getMessage(), ex);
        } catch (IOException ex) {
            throw new ParsingException(ex.getMessage(), ex);
        }
    }

    public XMLParser(org.w3c.dom.Element mainElement) {
        this.mainElement = mainElement;
    }

    public String parseElementValue(String path) throws ParsingException {
        try {
            NodeList elements = (NodeList) xPath.evaluate(path, mainElement, XPathConstants.NODESET);
            if (elements.getLength() == 0) {
                throw new ParsingException("Value not found: " + path);
            }
            if (elements.getLength() > 1) {
                throw new ParsingException("Multiple values found: " + path);
            }
            Node element = elements.item(0);
            if (hasTextContent(element)) {
                return element.getTextContent();
            } else {
                throw new ParsingException("Element content is not a text string: " + path);
            }
        } catch (XPathExpressionException ex) {
            throw new ParsingException(ex.getMessage(), ex);
        }
    }

    public Element parseElement(String path) throws ParsingException {
        List<Element> elements = parseElements(path);
        if (elements.isEmpty()) {
            throw new ParsingException("Value not found: " + path);
        }
        if (elements.size() > 1) {
            throw new ParsingException("Multiple values found: " + path);
        }
        return elements.get(0);
    }

    public List<Element> parseElements(String... paths) throws ParsingException {
        return parseElements(mainElement, paths);
    }

    private List<Element> parseElements(org.w3c.dom.Element initialElement, String... paths) throws ParsingException {
        try {
            List<Element> elements = new ArrayList<Element>();
            if (paths.length == 0) {
                throw new IllegalArgumentException("No element names specified for parsing.");
            }
            NodeList xmlElements = (NodeList) xPath.evaluate(paths[0], initialElement, XPathConstants.NODESET);
            if (xmlElements.getLength() == 0) {
                throw new ParsingException("Element not found: " + paths[0]);
            }
            for (int i = 0; i < xmlElements.getLength(); i++) {
                org.w3c.dom.Element xmlElement = toElement(xmlElements.item(i));
                Element element = new Element();
                element.name = xmlElement.getNodeName();
                element.attributes = new HashMap<String, String>();
                NamedNodeMap xmlAttributes = xmlElement.getAttributes();
                for (int j = 0; j < xmlAttributes.getLength(); j++) {
                    Attr xmlAttribute = toAttribute(xmlAttributes.item(j));
                    element.attributes.put(xmlAttribute.getName(), xmlAttribute.getValue());
                }

                if (paths.length > 1) {
                    if (hasTextContent(xmlElement)) {
                        throw new ParsingException("No child elements found: " + paths[1]);
                    }
                    element.children = parseElements(xmlElement, Arrays.copyOfRange(paths, 1, paths.length));
                } else {
                    if (hasTextContent(xmlElement)) {
                        element.value = xmlElement.getTextContent();
                    }
                }
                elements.add(element);
            }
            return elements;
        } catch (XPathExpressionException ex) {
            throw new ParsingException(ex.getMessage(), ex);
        }
    }

    private org.w3c.dom.Element toElement(Node node) throws ParsingException {
        if (!(node instanceof org.w3c.dom.Element)) {
            throw new ParsingException("Not an element: " + node.getNodeName());
        }
        return (org.w3c.dom.Element) node;
    }

    private org.w3c.dom.Attr toAttribute(Node node) throws ParsingException {
        if (!(node instanceof org.w3c.dom.Attr)) {
            throw new ParsingException("Not an attribute: " + node.getNodeName());
        }
        return (org.w3c.dom.Attr) node;
    }

    private boolean hasTextContent(Node node) {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (!(children.item(i) instanceof Text)) {
                return false;
            }
        }
        return true;
    }

    public class Element {

        String name;
        Map<String, String> attributes;
        List<Element> children;
        String value;

        public String getName() {
            return name;
        }

        public Map<String, String> getAttributes() {
            return attributes;
        }

        public List<Element> getChildren() {
            return children;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Element{" + "name=" + name + ", attributes=" + attributes + ", children=" + children + ", value=" + value + '}';
        }
    }

    public class ParsingException extends Exception {

        public ParsingException(String message) {
            super(message);
        }

        public ParsingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
