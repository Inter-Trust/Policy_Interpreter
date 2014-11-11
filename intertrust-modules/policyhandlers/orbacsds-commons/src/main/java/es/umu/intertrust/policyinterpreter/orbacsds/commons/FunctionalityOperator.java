package es.umu.intertrust.policyinterpreter.orbacsds.commons;

import es.umu.intertrust.policyinterpreter.orbac.ObligationField;
import es.umu.intertrust.policyinterpreter.orbac.Obligation;
import es.umu.intertrust.policyinterpreter.sds.SecurityFeature;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Juanma
 */
public class FunctionalityOperator extends AbstractSecurityFeatureOperator {

    String defaultFunctionality;
    Map<String, String> functionalities;

    public FunctionalityOperator(ObligationField obligationField) {
        this(null, obligationField);
    }

    public FunctionalityOperator(String defaultFunctionality, ObligationField obligationField) {
        this(defaultFunctionality, new HashMap<String, String>(), obligationField);
    }

    public FunctionalityOperator(String defaultFunctionality, Map<String, String> functionalities, ObligationField obligationField) {
        super(obligationField);
        this.defaultFunctionality = defaultFunctionality;
        this.functionalities = functionalities;
    }

    public String getDefaultFunctionality() {
        return defaultFunctionality;
    }

    public void setDefaultFunctionality(String defaultFunctionality) {
        this.defaultFunctionality = defaultFunctionality;
    }

    public Map<String, String> getFunctionalities() {
        return functionalities;
    }

    public void setFunctionalities(Map<String, String> functionalities) {
        this.functionalities = functionalities;
    }

    public String getFunctionalityForValue(String value) {
        String parameter = functionalities.get(value);
        if (parameter == null) {
            throw new IllegalArgumentException("Unsupported value: " + value);
        }
        return parameter;
    }

    public void setFunctionalityForValue(String value, String functionality) {
        this.functionalities.put(value, functionality);
    }

    public void removeFunctionalityForValue(String value) {
        this.functionalities.remove(value);
    }

    public boolean isOperable(Obligation obligation) {
        String value = obligationField.getValue(obligation);
        return (functionalities.keySet().contains(value));
    }

    @Override
    protected String getDefaultValue() {
        return defaultFunctionality;
    }

    @Override
    protected String getValueForObligationValue(String value) {
        return getFunctionalityForValue(value);
    }

    @Override
    protected String getCurrentValue(SecurityFeature feature) {
        Collection<String> values = this.functionalities.values();
        for (String functionality : feature.getFunctionalities()) {
            if (values.contains(functionality)) {
                return functionality;
            }
        }
        return null;
    }

    @Override
    protected void setValue(SecurityFeature feature, String currentValue, String newValue) {
        feature.removeFunctionality(currentValue);
        feature.addFunctionality(newValue);
    }

    @Override
    protected void removeValue(SecurityFeature feature, String currentValue) {
        feature.removeFunctionality(currentValue);
    }

    @Override
    public String toString() {
        return "FunctionalityOperator (default=" + defaultFunctionality + "): " + functionalities;
    }

}
