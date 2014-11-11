package es.umu.intertrust.policyinterpreter.privacy;

import es.umu.intertrust.policyinterpreter.orbac.Obligation;
import es.umu.intertrust.policyinterpreter.orbac.ObligationField;
import es.umu.intertrust.policyinterpreter.orbac.ObligationPrecondition;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.AbstractDependencyOperatorBuilder;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.AnyValueParameterOperator;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.ConfigurationParsingException;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.ConfiguredOperatorBasedSDSHandler;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.MultipleOperator;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.NamedValueParameterOperator;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.SDSHandlerConfigurationParser;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.SDSHandlerConfigurationParser.ConfigurationProperty;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.SecurityFeatureOperator;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.SecurityFeatureOperatorBuilder;
import es.umu.intertrust.policyinterpreter.orbacsds.commons.SecurityFeatureOperatorBuilder.OperatorType;
import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandlerException;
import es.umu.intertrust.policyinterpreter.sds.SDSTarget;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Juanma
 */
public class ObfuscationHandler extends ConfiguredOperatorBasedSDSHandler {

    ConfigurationProperty proceduresProperty;

    @Override
    public void customizeParser(SDSHandlerConfigurationParser parser) {
        parser.setParsePreconditionsAction(false);
        parser.setParseMainFunctionality(false);
        parser.setRequirePropertyElementsId(false);
    }

    @Override
    public List<ObligationPrecondition> getObligationPreconditions(SDSHandlerConfigurationParser parser) throws PolicyHandlerException {
        List<ObligationPrecondition> obligationPreconditions = new ArrayList<ObligationPrecondition>();
        for (String procedure : getProceduresProperty(parser).getValues().keySet()) {
            obligationPreconditions.add(new ObligationPrecondition(procedure));
        }
        return obligationPreconditions;
    }

    @Override
    public String getCategory(SDSHandlerConfigurationParser parser) throws PolicyHandlerException {
        return parser.getCategory();
    }

    @Override
    public String getType(SDSHandlerConfigurationParser parser) throws PolicyHandlerException {
        return parser.getType();
    }

    @Override
    public Map<String, SDSTarget> getTargetsMap(SDSHandlerConfigurationParser parser) throws PolicyHandlerException {
        return parser.getTargetsMap();
    }

    @Override
    public SecurityFeatureOperator getSecurityFeatureOperator(SDSHandlerConfigurationParser parser) throws PolicyHandlerException {

        return new AbstractDependencyOperatorBuilder(getProceduresProperty(parser), ObligationField.ACTION) {

            @Override
            protected SecurityFeatureOperator generateMainOperator(ConfigurationProperty mainProperty) {
                return new SecurityFeatureOperatorBuilder(mainProperty).setOperatorTypes(0, OperatorType.FUNCTIONALITY, OperatorType.PARAMETER_WITH_SHORT_VALUES).setParameterNames("procedure").setObligationFields(ObligationField.ACTION).build();
            }

            @Override
            protected SecurityFeatureOperator generateDependentOperator(String mainValue, ConfigurationProperty dependentProperty) {
                final String procedureName = mainValue;
                Map<String, String> params = dependentProperty.getValues();
                if (params.size() == 1) {
                    Map.Entry<String, String> param = params.entrySet().iterator().next();
                    return new AnyValueParameterOperator(param.getKey(), param.getValue(), ObligationField.PARAMETER) {

                        @Override
                        public boolean isOperable(Obligation obligation) {
                            return obligation.getAction().equals(procedureName) && super.isOperable(obligation);
                        }
                    };
                } else if (params.size() > 1) {
                    MultipleOperator parameterOperators = new MultipleOperator();
                    for (Map.Entry<String, String> param : params.entrySet()) {
                        parameterOperators.add(new NamedValueParameterOperator(param.getKey(), param.getValue(), ObligationField.PARAMETER) {

                            @Override
                            public boolean isOperable(Obligation obligation) {
                                return obligation.getAction().equals(procedureName) && super.isOperable(obligation);
                            }
                        });
                    }
                    return parameterOperators;
                } else {
                    return null;
                }
            }
        }.build();
    }

    private ConfigurationProperty getProceduresProperty(SDSHandlerConfigurationParser parser) throws PolicyHandlerException {
        if (proceduresProperty == null) {
            try {
                proceduresProperty = parser.parseProperty("procedures", "procedure", "param");
            } catch (ConfigurationParsingException ex) {
                throw new PolicyHandlerException(ex.getMessage(), ex);
            }
        }
        return proceduresProperty;
    }

}
