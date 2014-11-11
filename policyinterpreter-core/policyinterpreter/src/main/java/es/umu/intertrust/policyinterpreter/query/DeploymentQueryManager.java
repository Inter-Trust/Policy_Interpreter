package es.umu.intertrust.policyinterpreter.query;

import es.umu.intertrust.policyinterpreter.deployment.DeploymentManager;
import es.umu.intertrust.policyinterpreter.policyhandlers.QueryManager;

/**
 *
 * @author Juanma
 */
public interface DeploymentQueryManager extends QueryManager {

    public void initialize(DeploymentManager deploymentManager);

}
