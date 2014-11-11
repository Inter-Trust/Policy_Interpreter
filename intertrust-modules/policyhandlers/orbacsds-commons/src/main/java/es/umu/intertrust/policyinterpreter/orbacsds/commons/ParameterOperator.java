package es.umu.intertrust.policyinterpreter.orbacsds.commons;

import es.umu.intertrust.policyinterpreter.orbac.ObligationField;
import es.umu.intertrust.policyinterpreter.sds.SecurityFeature;
import es.umu.intertrust.policyinterpreter.sds.SecurityParameter;

/**
 *
 * @author Juanma
 */
public abstract class ParameterOperator extends AbstractSecurityFeatureOperator {

    String name;
    String defaultParameter;

    public ParameterOperator(String name, ObligationField obligationField) {
        this(name, null, obligationField);
    }

    public ParameterOperator(String name, String defaultParameter, ObligationField obligationField) {
        super(obligationField);
        this.name = name;
        this.defaultParameter = defaultParameter;
    }

    public String getName() {
        return name;
    }

    public String getDefaultParameter() {
        return defaultParameter;
    }

    public void setDefaultParameter(String defaultParameter) {
        this.defaultParameter = defaultParameter;
    }

    @Override
    protected String getDefaultValue() {
        return defaultParameter;
    }

    @Override
    protected String getCurrentValue(SecurityFeature feature) {
        SecurityParameter currentParameter = feature.getParameter(name);
        if (currentParameter == null) {
            return null;
        } else {
            return currentParameter.getValue();
        }
    }

    @Override
    protected void setValue(SecurityFeature feature, String currentValue, String newValue) {
        feature.setParameter(name, newValue);
    }

    @Override
    protected void removeValue(SecurityFeature feature, String currentValue) {
        feature.removeParameter(name);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " <" + name + "> (default=" + defaultParameter + ")";
    }

}
