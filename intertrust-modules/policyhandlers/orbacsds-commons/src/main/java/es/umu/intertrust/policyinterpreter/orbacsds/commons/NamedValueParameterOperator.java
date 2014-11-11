package es.umu.intertrust.policyinterpreter.orbacsds.commons;

import es.umu.intertrust.policyinterpreter.orbac.ObligationField;
import es.umu.intertrust.policyinterpreter.orbac.Obligation;

/**
 *
 * @author Juanma
 */
public class NamedValueParameterOperator extends ParameterOperator {

    static final char SEPARATOR = '=';

    public NamedValueParameterOperator(String name, ObligationField obligationField) {
        super(name, obligationField);
    }

    public NamedValueParameterOperator(String name, String defaultParameter, ObligationField obligationField) {
        super(name, defaultParameter, obligationField);
    }

    @Override
    protected String getValueForObligationValue(String value) {
        return parseValue(value);
    }

    public boolean isOperable(Obligation obligation) {
        String value = obligationField.getValue(obligation);
        return name.equals(parseName(value));
    }

    private String parseName(String value) {
        int index = value.indexOf(SEPARATOR);
        if (index != -1) {
            return value.substring(0, index);
        } else {
            return null;
        }
    }

    private String parseValue(String value) {
        int index = value.indexOf(SEPARATOR);
        if (index != -1) {
            return value.substring(index + 1);
        } else {
            return null;
        }
    }

}
