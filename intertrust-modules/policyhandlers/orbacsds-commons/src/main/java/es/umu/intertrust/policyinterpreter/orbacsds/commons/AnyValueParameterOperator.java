package es.umu.intertrust.policyinterpreter.orbacsds.commons;

import es.umu.intertrust.policyinterpreter.orbac.ObligationField;
import es.umu.intertrust.policyinterpreter.orbac.Obligation;

/**
 *
 * @author Juanma
 */
public class AnyValueParameterOperator extends ParameterOperator {

    public AnyValueParameterOperator(String name, ObligationField obligationField) {
        super(name, obligationField);
    }

    public AnyValueParameterOperator(String name, String defaultParameter, ObligationField obligationField) {
        super(name, defaultParameter, obligationField);
    }

    @Override
    protected String getValueForObligationValue(String value) {
        return value;
    }

    @Override
    public boolean isOperable(Obligation obligation) {
        return obligationField.getValue(obligation) != null;
    }

}
