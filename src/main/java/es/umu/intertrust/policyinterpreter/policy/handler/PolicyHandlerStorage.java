package es.umu.intertrust.policyinterpreter.policy.handler;

import es.umu.intertrust.policyinterpreter.deployment.DeploymentFeature;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Juanma
 */
public class PolicyHandlerStorage {

    Map<Precondition, List<DeploymentFeature>> deploymentFeaturesByPrecondition;

    public PolicyHandlerStorage() {
        this.deploymentFeaturesByPrecondition = new HashMap<Precondition, List<DeploymentFeature>>();
    }

    public void setDeploymentFeatureOfPrecondition(DeploymentFeature deploymentFeature, Precondition precondition) {
        List<DeploymentFeature> deploymentFeatures = deploymentFeaturesByPrecondition.get(precondition);
        if (deploymentFeatures == null) {
            deploymentFeatures = new ArrayList<DeploymentFeature>();
            deploymentFeaturesByPrecondition.put(precondition, deploymentFeatures);
        }
        deploymentFeatures.add(deploymentFeature);
    }

    public List<DeploymentFeature> getDeploymentFeaturesByPrecondition(Precondition precondition) {
        List<DeploymentFeature> deploymentFeatures = deploymentFeaturesByPrecondition.get(precondition);
        if (deploymentFeatures == null) {
            deploymentFeatures = new ArrayList<DeploymentFeature>();
        }
        return deploymentFeatures;
    }

    public void removeDeploymentFeatureOfPrecondition(DeploymentFeature deploymentFeature, Precondition precondition) {
        List<DeploymentFeature> deploymentFeatures = deploymentFeaturesByPrecondition.get(precondition);
        if (deploymentFeatures != null) {
            deploymentFeatures.remove(deploymentFeature);
        }
    }

}
