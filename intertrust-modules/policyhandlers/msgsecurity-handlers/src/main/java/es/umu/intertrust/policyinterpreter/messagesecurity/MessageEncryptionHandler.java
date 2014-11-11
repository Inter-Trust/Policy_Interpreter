package es.umu.intertrust.policyinterpreter.messagesecurity;

import es.umu.intertrust.policyinterpreter.orbacsds.commons.ConfigurationParsingException;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.ConfiguredOperatorSDSHandler;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.DependencyOperator;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.SecurityFeatureOperator;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.SDSHandlerConfigurationParser;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.SDSHandlerConfigurationParser.ConfigurationProperty;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.MultipleOperator;
import es.umu.intertrust.policyinterpreter.orbac.ObligationField;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.SecurityFeatureOperatorBuilder;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.SecurityFeatureOperatorBuilder.OperatorType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Juanma
 */
public class MessageEncryptionHandler extends ConfiguredOperatorSDSHandler {

    static final String ENCRYPT_METHOD = "encrypt";

    @Override
    public void customizeParser(SDSHandlerConfigurationParser parser) {
        parser.setParseMainFunctionality(false);
    }

    @Override
    public Collection<SecurityFeatureOperator> getPropertyOperators(SDSHandlerConfigurationParser parser) throws ConfigurationParsingException {
        List<SecurityFeatureOperator> encryptOperators = new ArrayList<SecurityFeatureOperator>();

        ConfigurationProperty formats = parser.parseProperty("formats", "format");
        encryptOperators.add(new SecurityFeatureOperatorBuilder(formats).setOperatorTypes(0, OperatorType.FUNCTIONALITY, OperatorType.PARAMETER_WITH_SHORT_VALUES).setParameterNames("format").build());

        ConfigurationProperty ciphers = parser.parseProperty("ciphers", "algorithm", "padding");
        encryptOperators.add(new SecurityFeatureOperatorBuilder(ciphers).setOperatorTypes(0, OperatorType.FUNCTIONALITY, OperatorType.PARAMETER_WITH_SHORT_VALUES).setParameterNames("algorithm", "padding").build());

        ConfigurationProperty keys = parser.parseProperty("keys", "keyType", "key");
        encryptOperators.add(new SecurityFeatureOperatorBuilder(keys).setOperatorTypes(0, OperatorType.FUNCTIONALITY, OperatorType.PARAMETER_WITH_SHORT_VALUES).setOperatorTypes(1, OperatorType.PARAMETER).setParameterNames("key-type", "key").build());

        List<SecurityFeatureOperator> operators = new ArrayList<SecurityFeatureOperator>();

        ConfigurationProperty methods = parser.parseProperty("methods", "method");
        SecurityFeatureOperator methodsOperator = new SecurityFeatureOperatorBuilder(methods).build();

        MultipleOperator encryptOperator = new MultipleOperator(encryptOperators);
        Map<String, SecurityFeatureOperator> dependentOperators = new HashMap<String, SecurityFeatureOperator>();
        dependentOperators.put(ENCRYPT_METHOD, encryptOperator);
        SecurityFeatureOperator defaultOperator = (methods.getValues().get(ENCRYPT_METHOD).equals(methods.getDefaultValue())) ? encryptOperator : null;
        operators.add(new DependencyOperator(methodsOperator, ObligationField.PARAMETER, dependentOperators, defaultOperator));

        return operators;
    }
}
