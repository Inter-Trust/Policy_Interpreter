package es.umu.intertrust.policyinterpreter.accesscontrol;

import es.umu.intertrust.policyinterpreter.orbacsds.commons.ConfigurationParsingException;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.ConfiguredOperatorSDSHandler;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.SecurityFeatureOperator;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.SDSHandlerConfigurationParser;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.SecurityFeatureOperatorBuilder;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Juanma
 */
public class AuthenticationHandler extends ConfiguredOperatorSDSHandler {

    @Override
    public Collection<SecurityFeatureOperator> getPropertyOperators(SDSHandlerConfigurationParser parser) throws ConfigurationParsingException {
        Collection<SecurityFeatureOperator> operators = new ArrayList<SecurityFeatureOperator>();

        SDSHandlerConfigurationParser.ConfigurationProperty authnTypes = parser.parseProperty("authnTypes", "authnType");
        operators.add(new SecurityFeatureOperatorBuilder(authnTypes).setOperatorTypes(0, SecurityFeatureOperatorBuilder.OperatorType.FUNCTIONALITY, SecurityFeatureOperatorBuilder.OperatorType.PARAMETER_WITH_SHORT_VALUES).setParameterNames("authnType").build());

        return operators;
    }

}
