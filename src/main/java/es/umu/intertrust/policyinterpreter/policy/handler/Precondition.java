package es.umu.intertrust.policyinterpreter.policy.handler;

/**
 *
 * @author Juanma
 */
public class Precondition {

    String evaluationCondition;

    public Precondition(String evaluationCondition) {
        this.evaluationCondition = evaluationCondition;
    }

    public String getEvaluationCondition() {
        return evaluationCondition;
    }

    @Override
    public String toString() {
        return "Precondition { " + getEvaluationCondition() + " }";
    }

}
