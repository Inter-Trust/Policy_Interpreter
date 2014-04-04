package es.umu.intertrust.policyinterpreter.query;

import es.umu.intertrust.policyinterpreter.deployment.DeploymentFeature;
import es.umu.intertrust.policyinterpreter.deployment.DeploymentManager;
import java.util.List;

/**
 *
 * @author Juanma
 */
public class DefaultDeploymentQueryManager extends AbstractMatchQueryManager<DeploymentFeature> {

    DeploymentManager manager;

    public DefaultDeploymentQueryManager(DeploymentManager manager) {
        this.manager = manager;
    }

    public <T extends DeploymentFeature> List<T> getDeploymentFeatures(Filter<T> filter) {
        return getMatchingElements(manager.getDeploymentFeatures(), filter);
    }

    public <T extends DeploymentFeature> T getDeploymentFeature(Filter<T> filter) {
        return getMatchingElement(manager.getDeploymentFeatures(), filter);
    }
}
