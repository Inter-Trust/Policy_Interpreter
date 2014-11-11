package es.umu.intertrust.policyinterpreter.orbacsds.commons;

import es.umu.intertrust.policyinterpreter.orbac.ObligationField;
import es.umu.intertrust.policyinterpreter.orbac.ObligationPrecondition;
import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandlerException;
import es.umu.intertrust.policyinterpreter.sds.SDSTarget;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Juanma
 */
public abstract class ConfiguredOperatorSDSHandler extends ConfiguredOperatorBasedSDSHandler {

    @Override
    public List<ObligationPrecondition> getObligationPreconditions(SDSHandlerConfigurationParser parser) {
        String preconditionsAction = parser.getPreconditionsAction();
        if (preconditionsAction == null) {
            throw new IllegalStateException("No preconditions action specified.");
        }
        ObligationPrecondition precondition = new ObligationPrecondition(preconditionsAction);
        return Arrays.asList(new ObligationPrecondition[]{precondition});
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
        MultipleOperator multipleOperator = new MultipleOperator();
        String mainFunctionality = parser.getMainFunctionality();
        if (mainFunctionality != null) {
            multipleOperator.add(new FunctionalityOperator(parser.getMainFunctionality(), ObligationField.ACTION));
        }
        multipleOperator.addAll(getPropertyOperators(parser));
        return multipleOperator;
    }

    public abstract Collection<SecurityFeatureOperator> getPropertyOperators(SDSHandlerConfigurationParser parser) throws PolicyHandlerException;

}
