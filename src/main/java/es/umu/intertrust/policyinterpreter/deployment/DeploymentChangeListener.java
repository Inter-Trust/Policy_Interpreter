package es.umu.intertrust.policyinterpreter.deployment;

/**
 *
 * @author Juanma
 */
public interface DeploymentChangeListener {

    public void deploymentFeatureAdded(DeploymentFeature feature);

    public void deploymentFeatureRemoved(DeploymentFeature feature);

    public void deploymentFeatureUpdated(DeploymentFeature feature);
}
