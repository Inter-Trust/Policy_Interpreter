package es.umu.intertrust.policyinterpreter.orbacsds.commons;

import es.umu.intertrust.policyinterpreter.orbac.ObligationPrecondition;
import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandlerConfiguration;
import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandlerException;
import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandlerInitializationException;
import es.umu.intertrust.policyinterpreter.sds.SDSTarget;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Juanma
 */
public abstract class ConfiguredOperatorBasedSDSHandler extends OperatorBasedSDSHandler {

    SDSHandlerConfigurationParser parser;

    @Override
    public void initialize(PolicyHandlerConfiguration configuration) throws PolicyHandlerInitializationException {
        try {
            parser = new SDSHandlerConfigurationParser();
            customizeParser(parser);
            parser.parse(getConfigurationFile(configuration.getConfigurationLocation()));
            super.initialize(configuration);
        } catch (ConfigurationParsingException ex) {
            throw new PolicyHandlerInitializationException(ex.getMessage(), ex);
        }
    }

    @Override
    public List<ObligationPrecondition> getObligationPreconditions() throws PolicyHandlerException {
        return getObligationPreconditions(parser);
    }

    @Override
    public String getCategory() throws PolicyHandlerException {
        return getCategory(parser);
    }

    @Override
    public String getType() throws PolicyHandlerException {
        return getType(parser);
    }

    @Override
    public Map<String, SDSTarget> getTargetsMap() throws PolicyHandlerException {
        return getTargetsMap(parser);
    }

    @Override
    public SecurityFeatureOperator getSecurityFeatureOperator() throws PolicyHandlerException {
        return getSecurityFeatureOperator(parser);
    }

    public abstract List<ObligationPrecondition> getObligationPreconditions(SDSHandlerConfigurationParser parser) throws PolicyHandlerException;

    public abstract String getCategory(SDSHandlerConfigurationParser parser) throws PolicyHandlerException;

    public abstract String getType(SDSHandlerConfigurationParser parser) throws PolicyHandlerException;

    public abstract Map<String, SDSTarget> getTargetsMap(SDSHandlerConfigurationParser parser) throws PolicyHandlerException;

    public abstract SecurityFeatureOperator getSecurityFeatureOperator(SDSHandlerConfigurationParser parser) throws PolicyHandlerException;

    public void customizeParser(SDSHandlerConfigurationParser parser) {
        // Overridable method to customize parser options by subclasses.
    }
}
