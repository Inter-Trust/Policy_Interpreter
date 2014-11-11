package es.umu.intertrust.policyinterpreter.messagesecurity;

import es.umu.intertrust.policyinterpreter.orbacsds.commons.ConfigurationParsingException;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.ConfiguredOperatorSDSHandler;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.SecurityFeatureOperator;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.SDSHandlerConfigurationParser;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.SDSHandlerConfigurationParser.ConfigurationProperty;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.SecurityFeatureOperatorBuilder;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.SecurityFeatureOperatorBuilder.OperatorType;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Juanma
 */
public class MessageDecryptionHandler extends ConfiguredOperatorSDSHandler {

    @Override
    public Collection<SecurityFeatureOperator> getPropertyOperators(SDSHandlerConfigurationParser parser) throws ConfigurationParsingException {
        Collection<SecurityFeatureOperator> operators = new ArrayList<SecurityFeatureOperator>();

        ConfigurationProperty formats = parser.parseProperty("formats", "format");
        operators.add(new SecurityFeatureOperatorBuilder(formats).setOperatorTypes(0, OperatorType.FUNCTIONALITY, OperatorType.PARAMETER_WITH_SHORT_VALUES).setParameterNames("format").build());

        ConfigurationProperty keys = parser.parseProperty("keys", "keyType", "key");
        operators.add(new SecurityFeatureOperatorBuilder(keys).setOperatorTypes(0, OperatorType.FUNCTIONALITY, OperatorType.PARAMETER_WITH_SHORT_VALUES).setOperatorTypes(1, OperatorType.PARAMETER).setParameterNames("key-type", "key").build());

        return operators;
    }

}
