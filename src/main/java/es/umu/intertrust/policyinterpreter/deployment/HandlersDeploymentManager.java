package es.umu.intertrust.policyinterpreter.deployment;

/**
 *
 * @author Juanma
 */
public class HandlersDeploymentManager implements es.umu.intertrust.policyinterpreter.policy.handler.DeploymentManager {

    DeploymentManager manager;

    public HandlersDeploymentManager(DeploymentManager manager) {
        this.manager = manager;
    }

    public void addDeploymentFeature(DeploymentFeature feature) {
        manager.addDeploymentFeature(feature);
    }

    public void removeDeploymentFeature(DeploymentFeature feature) {
        manager.removeDeploymentFeature(feature);
    }

    public void updateDeploymentFeature(DeploymentFeature feature) {
        manager.updateDeploymentFeature(feature);
    }

}
