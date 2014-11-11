package es.umu.intertrust.policyinterpreter.orbacsds.commons;

import es.umu.intertrust.policyinterpreter.orbac.ObligationField;
import es.umu.intertrust.policyinterpreter.orbac.Obligation;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Juanma
 */
public class MappedParameterOperator extends ParameterOperator {

    Map<String, String> parameters;

    public MappedParameterOperator(String name, ObligationField obligationField) {
        this(name, null, new HashMap<String, String>(), obligationField);
    }

    public MappedParameterOperator(String name, Map<String, String> parameters, ObligationField obligationField) {
        this(name, null, parameters, obligationField);
    }

    public MappedParameterOperator(String name, String defaultParameter, ObligationField obligationField) {
        this(name, defaultParameter, new HashMap<String, String>(), obligationField);
    }

    public MappedParameterOperator(String name, String defaultParameter, Map<String, String> parameters, ObligationField obligationField) {
        super(name, defaultParameter, obligationField);
        this.parameters = parameters;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getParameterForValue(String value) {
        String parameter = parameters.get(value);
        if (parameter == null) {
            throw new IllegalArgumentException("Unsupported value: " + value);
        }
        return parameter;
    }

    public void setParameterForValue(String value, String parameter) {
        this.parameters.put(value, parameter);
    }

    public void removeParameterForValue(String value) {
        this.parameters.remove(value);
    }

    public boolean isOperable(Obligation obligation) {
        String value = obligationField.getValue(obligation);
        return parameters.keySet().contains(value);
    }

    @Override
    protected String getValueForObligationValue(String value) {
        return getParameterForValue(value);
    }

    @Override
    public String toString() {
        return super.toString() + ": " + parameters;
    }

}
