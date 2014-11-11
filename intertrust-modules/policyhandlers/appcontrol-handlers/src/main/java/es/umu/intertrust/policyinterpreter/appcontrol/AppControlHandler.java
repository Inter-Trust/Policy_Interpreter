package es.umu.intertrust.policyinterpreter.appcontrol;

import es.umu.intertrust.policyinterpreter.orbacsds.commons.SecurityFeatureOperator;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.SDSHandlerConfigurationParser;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.ConfigurationParsingException;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.ConfiguredOperatorBasedSDSHandler;
import es.umu.intertrust.policyinterpreter.orbac.ObligationField;
import es.umu.intertrust.policyinterpreter.orbac.ObligationPrecondition;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.SDSHandlerConfigurationParser.ConfigurationProperty;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.SecurityFeatureOperatorBuilder;
import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandlerException;
import es.umu.intertrust.policyinterpreter.sds.SDSTarget;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Juanma
 */
public class AppControlHandler extends ConfiguredOperatorBasedSDSHandler {

    ConfigurationProperty actionsProperty;

    @Override
    public void customizeParser(SDSHandlerConfigurationParser parser) {
        parser.setParsePreconditionsAction(false);
        parser.setParseMainFunctionality(false);
    }

    @Override
    public List<ObligationPrecondition> getObligationPreconditions(SDSHandlerConfigurationParser parser) throws PolicyHandlerException {
        try {
            List<ObligationPrecondition> obligationPreconditions = new ArrayList<ObligationPrecondition>();
            for (String action : getActionsProperty(parser).getValues().keySet()) {
                obligationPreconditions.add(new ObligationPrecondition(action));
            }
            return obligationPreconditions;
        } catch (ConfigurationParsingException ex) {
            throw new PolicyHandlerException(ex.getMessage(), ex);
        }
    }

    @Override
    public String getCategory(SDSHandlerConfigurationParser parser) {
        return parser.getCategory();
    }

    @Override
    public String getType(SDSHandlerConfigurationParser parser) {
        return parser.getType();
    }

    @Override
    public Map<String, SDSTarget> getTargetsMap(SDSHandlerConfigurationParser parser) {
        return parser.getTargetsMap();
    }

    @Override
    public SecurityFeatureOperator getSecurityFeatureOperator(SDSHandlerConfigurationParser parser) throws PolicyHandlerException {
        return SecurityFeatureOperatorBuilder.newFunctionalityOperator(getActionsProperty(parser), ObligationField.ACTION);
    }

    private ConfigurationProperty getActionsProperty(SDSHandlerConfigurationParser parser) throws PolicyHandlerException {
        if (actionsProperty == null) {
            try {
                actionsProperty = parser.parseProperty("actions", "action");
            } catch (ConfigurationParsingException ex) {
                throw new PolicyHandlerException(ex.getMessage(), ex);
            }
        }
        return actionsProperty;
    }

}
