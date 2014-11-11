package es.umu.intertrust.policyinterpreter.query;

import es.umu.intertrust.policyinterpreter.policy.PolicyManager;
import es.umu.intertrust.policyinterpreter.policyhandlers.QueryManager;

/**
 *
 * @author Juanma
 */
public interface PolicyQueryManager extends QueryManager {

    public void initialize(PolicyManager policyManager);

}
