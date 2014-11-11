package es.umu.intertrust.policyinterpreter.orbacsds.commons;

import es.umu.intertrust.policyinterpreter.orbac.Obligation;
import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandlerException;
import es.umu.intertrust.policyinterpreter.sds.SecurityFeature;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Juanma
 */
public class MultipleOperator implements SecurityFeatureOperator {

    List<SecurityFeatureOperator> operators;

    public MultipleOperator() {
        this(new ArrayList<SecurityFeatureOperator>());
    }

    public MultipleOperator(List<SecurityFeatureOperator> operators) {
        this.operators = operators;
    }

    public List<SecurityFeatureOperator> getOperators() {
        return operators;
    }

    public void setOperators(List<SecurityFeatureOperator> operators) {
        this.operators = operators;
    }

    public void add(SecurityFeatureOperator operator) {
        this.operators.add(operator);
    }

    public void addAll(Collection<SecurityFeatureOperator> operators) {
        this.operators.addAll(operators);
    }

    public void remove(SecurityFeatureOperator operator) {
        this.operators.remove(operator);
    }

    public boolean isOperable(Obligation obligation) {
        for (SecurityFeatureOperator operator : operators) {
            if (operator.isOperable(obligation)) {
                return true;
            }
        }
        return false;
    }

    public boolean processDefaults(SecurityFeature feature) throws PolicyHandlerException {
        boolean modified = false;
        for (SecurityFeatureOperator operator : operators) {
            modified = modified | operator.processDefaults(feature);
        }
        return modified;
    }

    public boolean processActivation(Obligation obligation, SecurityFeature feature) throws PolicyHandlerException {
        boolean modified = false;
        for (SecurityFeatureOperator operator : operators) {
            if (operator.isOperable(obligation)) {
                modified = modified | operator.processActivation(obligation, feature);
            }
        }
        return modified;
    }

    public boolean processDeactivation(Obligation obligation, SecurityFeature feature) throws PolicyHandlerException {
        boolean modified = false;
        for (SecurityFeatureOperator operator : operators) {
            if (operator.isOperable(obligation)) {
                modified = modified | operator.processDeactivation(obligation, feature);
            }
        }
        return modified;
    }

    public boolean clean(SecurityFeature feature) throws PolicyHandlerException {
        boolean modified = false;
        for (SecurityFeatureOperator operator : operators) {
            modified = modified | operator.clean(feature);
        }
        return modified;
    }

    @Override
    public String toString() {
        String string = "MultipleOperator {";
        for (SecurityFeatureOperator operator : operators) {
            string += "\n    - " + operator;
        }
        string += "\n}";
        return string;
    }

}
