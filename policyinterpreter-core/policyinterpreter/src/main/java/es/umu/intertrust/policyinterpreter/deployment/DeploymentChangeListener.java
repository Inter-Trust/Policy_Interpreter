package es.umu.intertrust.policyinterpreter.deployment;

/**
 *
 * @author Juanma
 */
public interface DeploymentChangeListener {

    public void deploymentFeatureAdded(DeploymentFeature feature);

    public void deploymentFeatureRemoved(DeploymentFeature feature);

    public void deploymentFeatureUpdated(DeploymentFeature feature);

    public void deploymentFeatureAddedToTarget(DeploymentFeature feature, Target target);

    public void deploymentFeatureRemovedFromTarget(DeploymentFeature feature, Target target);

}
