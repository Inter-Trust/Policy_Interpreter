package es.umu.intertrust.policyinterpreter.privacy;

import es.umu.intertrust.policyinterpreter.orbacsds.commons.ConfigurationParsingException;
import es.umu.intertrust.policyinterpreter.util.XMLParser;
import es.umu.intertrust.policyinterpreter.util.XMLParser.Element;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Juanma
 */
public class PseudonymityHandlerConfigurationParser {

    static final String ACTION_ELEMENT_NAME = "policyAction";
    static final String CATEGORY_ELEMENT_NAME = "category";
    static final String TYPE_ELEMENT_NAME = "type";
    static final String MAIN_FUNCTIONALITY_ELEMENT_NAME = "functionality";
    static final String PROTECTED_ITEMS_XPATH_EXPRESSION = "protectedItems/item";
    static final String POLICY_REF_ATTR_NAME = "policy-ref";
    static final String DATA_TARGET_ELEMENT_NAME = "dataTarget";
    static final String ENCRYPTION_TARGET_ELEMENT_NAME = "encryptionTarget";
    static final String SIGNATURE_TARGET_ELEMENT_NAME = "signatureTarget";
    static final String ID_ATTR_NAME = "id";
    static final String PSEUDONYM_ATTR_NAME = "pseudonym";

    static final String ENCRYPTION_MAIN_FUNCTIONALITY_XPATH_EXPRESSION = "encryption/functionality";
    static final String ENCRYPTION_PARAMETER_XPATH_EXPRESSION = "encryption/parameter";
    static final String SIGNATURE_MAIN_FUNCTIONALITY_XPATH_EXPRESSION = "signature/functionality";
    static final String SIGNATURE_PARAMETER_XPATH_EXPRESSION = "signature/parameter";
    static final String NAME_ATTR_NAME = "name";

    String action;
    String category;
    String type;
    String mainFunctionality;

    String encryptionMainFunctionality;
    String encryptionParameterName;
    String encryptionParameterValue;
    String signatureMainFunctionality;
    String signatureParameterName;
    String signatureParameterValue;

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

    public String getEncryptionMainFunctionality() {
        return encryptionMainFunctionality;
    }

    public String getEncryptionParameterName() {
        return encryptionParameterName;
    }

    public String getEncryptionParameterValue() {
        return encryptionParameterValue;
    }

    public String getSignatureMainFunctionality() {
        return signatureMainFunctionality;
    }

    public String getSignatureParameterName() {
        return signatureParameterName;
    }

    public String getSignatureParameterValue() {
        return signatureParameterValue;
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

            boolean encryptionTargets = false;
            boolean signatureTargets = false;
            protectedItems = new HashMap<String, ProtectedItem>();
            List<Element> xmlProtectedItems = parser.parseElements(PROTECTED_ITEMS_XPATH_EXPRESSION, DATA_TARGET_ELEMENT_NAME + " | " + ENCRYPTION_TARGET_ELEMENT_NAME + " | " + SIGNATURE_TARGET_ELEMENT_NAME);
            for (XMLParser.Element xmlProtectedItem : xmlProtectedItems) {
                ProtectedItem protectedItem = new ProtectedItem();
                protectedItem.dataTargets = new ArrayList<DataTarget>();
                protectedItem.encryptionTargets = new ArrayList<String>();
                protectedItem.signatureTargets = new ArrayList<String>();

                String policyRef = xmlProtectedItem.getAttributes().get(POLICY_REF_ATTR_NAME);
                if (policyRef != null) {
                    protectedItems.put(policyRef, protectedItem);
                } else {
                    String name = xmlProtectedItem.getAttributes().get(NAME_ATTR_NAME);
                    if (name != null) {
                        protectedItems.put(name, protectedItem);
                    } else {
                        throw new ConfigurationParsingException("No name or policy reference specified for protected item.");
                    }
                }
                for (Element xmlTarget : xmlProtectedItem.getChildren()) {
                    if (xmlTarget.getName().equals(DATA_TARGET_ELEMENT_NAME)) {
                        DataTarget dataTarget = new DataTarget();
                        dataTarget.id = xmlTarget.getAttributes().get(ID_ATTR_NAME);
                        if (dataTarget.id == null) {
                            throw new ConfigurationParsingException("No id specified for data target.");
                        }
                        dataTarget.pseudonym = xmlTarget.getAttributes().get(PSEUDONYM_ATTR_NAME);
                        if (dataTarget.pseudonym == null) {
                            throw new ConfigurationParsingException("No pseudonym specified for data target: " + dataTarget.id);
                        }
                        dataTarget.target = xmlTarget.getValue();
                        if (dataTarget.target == null) {
                            throw new ConfigurationParsingException("No target specified for data target: " + dataTarget.id);
                        }
                        protectedItem.dataTargets.add(dataTarget);
                    } else if (xmlTarget.getName().equals(ENCRYPTION_TARGET_ELEMENT_NAME)) {
                        String encryptionTarget = xmlTarget.getValue();
                        if (encryptionTarget == null) {
                            throw new ConfigurationParsingException("No value specified for element: " + ENCRYPTION_TARGET_ELEMENT_NAME);
                        }
                        protectedItem.encryptionTargets.add(encryptionTarget);
                    } else if (xmlTarget.getName().equals(SIGNATURE_TARGET_ELEMENT_NAME)) {
                        String signatureTarget = xmlTarget.getValue();
                        if (signatureTarget == null) {
                            throw new ConfigurationParsingException("No value specified for element: " + SIGNATURE_TARGET_ELEMENT_NAME);
                        }
                        protectedItem.signatureTargets.add(signatureTarget);
                    }
                }
                if (!protectedItem.encryptionTargets.isEmpty()) {
                    encryptionTargets = true;
                }
                if (!protectedItem.signatureTargets.isEmpty()) {
                    signatureTargets = true;
                }
            }
            if (encryptionTargets) {
                encryptionMainFunctionality = parser.parseElementValue(ENCRYPTION_MAIN_FUNCTIONALITY_XPATH_EXPRESSION);
                Element xmlEncryptionParameter = parser.parseElement(ENCRYPTION_PARAMETER_XPATH_EXPRESSION);
                encryptionParameterName = xmlEncryptionParameter.getAttributes().get(NAME_ATTR_NAME);
                if (encryptionParameterName == null) {
                    throw new ConfigurationParsingException("No name defined for encryption parameter.");
                }
                encryptionParameterValue = xmlEncryptionParameter.getValue();
                if (encryptionParameterValue == null) {
                    throw new ConfigurationParsingException("No value defined for encryption parameter: " + encryptionParameterName);
                }

            }
            if (signatureTargets) {
                signatureMainFunctionality = parser.parseElementValue(SIGNATURE_MAIN_FUNCTIONALITY_XPATH_EXPRESSION);
                Element xmlSignatureParameter = parser.parseElement(SIGNATURE_PARAMETER_XPATH_EXPRESSION);
                signatureParameterName = xmlSignatureParameter.getAttributes().get(NAME_ATTR_NAME);
                if (signatureParameterName == null) {
                    throw new ConfigurationParsingException("No name defined for signature parameter.");
                }
                signatureParameterValue = xmlSignatureParameter.getValue();
                if (signatureParameterValue == null) {
                    throw new ConfigurationParsingException("No value defined for signature parameter: " + signatureParameterName);
                }
            }
        } catch (XMLParser.ParsingException ex) {
            throw new ConfigurationParsingException(ex.getMessage(), ex);
        }
    }

    public class ProtectedItem {

        List<DataTarget> dataTargets;
        List<String> encryptionTargets;
        List<String> signatureTargets;

        public List<DataTarget> getDataTargets() {
            return dataTargets;
        }

        public List<String> getEncryptionTargets() {
            return encryptionTargets;
        }

        public List<String> getSignatureTargets() {
            return signatureTargets;
        }
    }

    public class DataTarget {

        String target;
        String id;
        String pseudonym;

        public String getTarget() {
            return target;
        }

        public String getId() {
            return id;
        }

        public String getPseudonym() {
            return pseudonym;
        }
    }

}
