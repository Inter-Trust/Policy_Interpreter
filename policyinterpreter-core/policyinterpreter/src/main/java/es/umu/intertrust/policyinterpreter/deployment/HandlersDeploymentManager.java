package es.umu.intertrust.policyinterpreter.deployment;

import java.util.List;

/**
 *
 * @author Juanma
 */
public class HandlersDeploymentManager implements es.umu.intertrust.policyinterpreter.policyhandlers.DeploymentManager {

    DeploymentManager manager;

    public HandlersDeploymentManager(DeploymentManager manager) {
        this.manager = manager;
    }

    public void addDeploymentFeature(DeploymentFeature feature, Target target) {
        manager.addDeploymentFeature(feature, target);
    }

    public void addDeploymentFeature(DeploymentFeature feature, List<Target> targets) {
        manager.addDeploymentFeature(feature, targets);
    }

    public void updateDeploymentFeature(DeploymentFeature feature) {
        manager.updateDeploymentFeature(feature);
    }

    public void removeDeploymentFeature(DeploymentFeature feature) {
        manager.removeDeploymentFeature(feature);
    }

    public void removeDeploymentFeature(DeploymentFeature feature, Target target) {
        manager.removeDeploymentFeature(feature, target);
    }

    public void removeDeploymentFeature(DeploymentFeature feature, List<Target> targets) {
        manager.removeDeploymentFeature(feature, targets);
    }

}
