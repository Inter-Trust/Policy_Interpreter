package es.umu.intertrust.policyinterpreter.query;

import es.umu.intertrust.policyinterpreter.deployment.DeploymentFeature;
import es.umu.intertrust.policyinterpreter.deployment.DeploymentManager;
import java.util.List;

/**
 *
 * @author Juanma
 */
public abstract class AbstractDeploymentQueryManager extends AbstractMatchQueryManager<DeploymentFeature> implements DeploymentQueryManager {

    DeploymentManager manager;

    public void initialize(DeploymentManager deploymentManager) {
        this.manager = deploymentManager;
    }

    public <T extends DeploymentFeature> List<T> getDeploymentFeatures(Filter<T> filter) {
        return getMatchingElements(manager.getDeploymentFeatures(), filter);
    }

    public <T extends DeploymentFeature> T getDeploymentFeature(Filter<T> filter) {
        return getMatchingElement(manager.getDeploymentFeatures(), filter);
    }

}
