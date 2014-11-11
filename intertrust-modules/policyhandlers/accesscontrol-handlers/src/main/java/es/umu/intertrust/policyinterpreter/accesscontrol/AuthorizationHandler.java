package es.umu.intertrust.policyinterpreter.accesscontrol;

import es.umu.intertrust.policyinterpreter.orbacsds.commons.ConfigurationParsingException;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.ConfiguredOperatorSDSHandler;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.SecurityFeatureOperator;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.SDSHandlerConfigurationParser;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Juanma
 */
public class AuthorizationHandler extends ConfiguredOperatorSDSHandler {

    @Override
    public Collection<SecurityFeatureOperator> getPropertyOperators(SDSHandlerConfigurationParser parser) throws ConfigurationParsingException {
        Collection<SecurityFeatureOperator> operators = new ArrayList<SecurityFeatureOperator>();
        // TODO - Define needed parameters to connect to the Policy Engine for Inter-Trust authorization
        return operators;
    }

}
