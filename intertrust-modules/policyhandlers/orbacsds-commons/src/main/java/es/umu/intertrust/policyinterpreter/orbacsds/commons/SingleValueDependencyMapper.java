package es.umu.intertrust.policyinterpreter.orbacsds.commons;

import es.umu.intertrust.policyinterpreter.orbac.Obligation;
import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandlerException;
import es.umu.intertrust.policyinterpreter.sds.SecurityFeature;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Juanma
 */
public class SingleValueDependencyMapper implements DependencyMapper {

    public enum ObligationField {

        SUBJECT, ACTION, OBJECT, PARAM
    }

    SecurityFeatureOperator mainOperator;
    Map<String, SecurityFeatureOperator> dependentOperators;
    ObligationField mainOperatorObligationField;
    String defaultMainValue;
    Map<SecurityFeatureOperator, String> mainValuesByDependent;

    public SingleValueDependencyMapper(Map<String, SecurityFeatureOperator> dependentOperators, String defaultMainValue, ObligationField mainOperatorObligationField) {
        this.dependentOperators = dependentOperators;
        this.defaultMainValue = defaultMainValue;
        this.mainOperatorObligationField = mainOperatorObligationField;
        this.mainValuesByDependent = generateMainValuesByDependent();
    }

    public SecurityFeatureOperator getDependentForMain(List<Obligation> obligations) {
        return dependentOperators.get(getValueForMain(obligations.get(obligations.size())));
    }

    public SecurityFeatureOperator getDependentForDefaultMain() {
        return dependentOperators.get(defaultMainValue);
    }

    public void processMainForDependent(SecurityFeatureOperator dependent, SecurityFeature feature) throws PolicyHandlerException {
        mainOperator.processActivation(generateObligationForMain(mainValuesByDependent.get(dependent)), feature);
    }

    private Map<SecurityFeatureOperator, String> generateMainValuesByDependent() {
        Map<SecurityFeatureOperator, String> mainValuesByDep = new HashMap<SecurityFeatureOperator, String>();
        for (Map.Entry<String, SecurityFeatureOperator> operator : dependentOperators.entrySet()) {
            mainValuesByDep.put(operator.getValue(), operator.getKey());
        }
        return mainValuesByDep;
    }

    private String getValueForMain(Obligation obligation) {
        switch (mainOperatorObligationField) {
            case SUBJECT:
                return obligation.getSubject();
            case ACTION:
                return obligation.getAction();
            case OBJECT:
                return obligation.getObject();
            case PARAM:
                return obligation.getParameter();
            default:
                throw new IllegalStateException("Unsupported obligation field: " + mainOperatorObligationField);
        }
    }

    private Obligation generateObligationForMain(String value) {
        switch (mainOperatorObligationField) {
            case SUBJECT:
                return new Obligation("", value, "", "");
            case ACTION:
                return new Obligation("", "", value, "");
            case OBJECT:
                return new Obligation("", "", "", value);
            case PARAM:
                return new Obligation("", "", "", "", value);
            default:
                throw new IllegalStateException("Unsupported obligation field: " + mainOperatorObligationField);
        }
    }

}
