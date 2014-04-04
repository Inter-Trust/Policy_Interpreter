package es.umu.intertrust.policyinterpreter.policy.handler;

import es.umu.intertrust.policyinterpreter.deployment.DeploymentFeature;

/**
 *
 * @author Juanma
 */
public interface DeploymentManager {

    public void addDeploymentFeature(DeploymentFeature feature);

    public void removeDeploymentFeature(DeploymentFeature feature);

    public void updateDeploymentFeature(DeploymentFeature feature);

}
