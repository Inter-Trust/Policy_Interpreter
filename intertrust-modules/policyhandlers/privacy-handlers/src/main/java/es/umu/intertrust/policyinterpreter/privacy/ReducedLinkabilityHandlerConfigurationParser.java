package es.umu.intertrust.policyinterpreter.privacy;

import es.umu.intertrust.policyinterpreter.orbacsds.commons.ConfigurationParsingException;
import es.umu.intertrust.policyinterpreter.util.XMLParser;
import es.umu.intertrust.policyinterpreter.util.XMLParser.Element;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Juanma
 */
public class ReducedLinkabilityHandlerConfigurationParser {

    static final String ACTION_ELEMENT_NAME = "policyAction";
    static final String CATEGORY_ELEMENT_NAME = "category";
    static final String TYPE_ELEMENT_NAME = "type";
    static final String MAIN_FUNCTIONALITY_ELEMENT_NAME = "functionality";
    static final String PROTECTED_ITEMS_XPATH_EXPRESSION = "protectedItems/item";
    static final String ID_ATTR_NAME = "id";
    static final String POLICY_REF_ATTR_NAME = "policy-ref";
    static final String TARGET_ELEMENT_NAME = "target";
    static final String EVENT_ATTR_NAME = "event";

    String action;
    String category;
    String type;
    String mainFunctionality;
    Map<String, ProtectedItem> protectedItems;

    public String getAction() {
        return action;
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

    public Map<String, ProtectedItem> getProtectedItems() {
        return protectedItems;
    }

    public void parse(File config) throws ConfigurationParsingException {
        try {
            XMLParser parser = new XMLParser(config);
            action = parser.parseElementValue(ACTION_ELEMENT_NAME);
            category = parser.parseElementValue(CATEGORY_ELEMENT_NAME);
            type = parser.parseElementValue(TYPE_ELEMENT_NAME);
            mainFunctionality = parser.parseElementValue(MAIN_FUNCTIONALITY_ELEMENT_NAME);
            protectedItems = new HashMap<String, ProtectedItem>();
            List<Element> xmlProtectedItems = parser.parseElements(PROTECTED_ITEMS_XPATH_EXPRESSION, TARGET_ELEMENT_NAME);
            for (Element xmlProtectedItem : xmlProtectedItems) {
                ProtectedItem protectedItem = new ProtectedItem();
                protectedItem.id = xmlProtectedItem.getAttributes().get(ID_ATTR_NAME);
                if (protectedItem.id == null) {
                    throw new ConfigurationParsingException("No id defined for protected item.");
                }
                String policyRef = xmlProtectedItem.getAttributes().get(POLICY_REF_ATTR_NAME);
                if (policyRef != null) {
                    protectedItems.put(policyRef, protectedItem);
                } else {
                    protectedItems.put(protectedItem.id, protectedItem);
                }
                protectedItem.targets = new HashMap<String, String>();
                for (Element xmlTarget : xmlProtectedItem.getChildren()) {
                    String event = xmlTarget.getAttributes().get(EVENT_ATTR_NAME);
                    if (event == null) {
                        throw new ConfigurationParsingException("No event defined for protected item target.");
                    }
                    String target = xmlTarget.getValue();
                    if (target == null) {
                        throw new ConfigurationParsingException("No target defined for event: " + event);
                    }
                    protectedItem.targets.put(event, target);
                }
            }
        } catch (XMLParser.ParsingException ex) {
            throw new ConfigurationParsingException(ex.getMessage(), ex);
        }
    }

    public class ProtectedItem {

        String id;
        Map<String, String> targets;

        public String getId() {
            return id;
        }

        public Map<String, String> getTargets() {
            return targets;
        }
    }

}
