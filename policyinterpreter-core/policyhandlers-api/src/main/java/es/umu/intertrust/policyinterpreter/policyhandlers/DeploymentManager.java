package es.umu.intertrust.policyinterpreter.policyhandlers;

import es.umu.intertrust.policyinterpreter.deployment.DeploymentFeature;
import es.umu.intertrust.policyinterpreter.deployment.Target;
import java.util.List;

/**
 *
 * @author Juanma
 */
public interface DeploymentManager {

    public void addDeploymentFeature(DeploymentFeature feature, Target target);

    public void addDeploymentFeature(DeploymentFeature feature, List<Target> targets);

    public void updateDeploymentFeature(DeploymentFeature feature);

    public void removeDeploymentFeature(DeploymentFeature feature);

    public void removeDeploymentFeature(DeploymentFeature feature, Target target);

    public void removeDeploymentFeature(DeploymentFeature feature, List<Target> targets);

}
