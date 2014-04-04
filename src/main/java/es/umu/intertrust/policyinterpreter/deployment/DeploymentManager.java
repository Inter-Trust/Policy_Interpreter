package es.umu.intertrust.policyinterpreter.deployment;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Juanma
 */
public class DeploymentManager {

    List<DeploymentFeature> deploymentFeatures;
    List<DeploymentFeature> addedFeatures;
    List<DeploymentFeature> updatedFeatures;
    List<DeploymentFeature> removedFeatures;
    DeploymentEnforcer enforcer;
    List<DeploymentChangeListener> listeners;

    public DeploymentManager(DeploymentEnforcer enforcer) {
        this.enforcer = enforcer;
        this.deploymentFeatures = new ArrayList<DeploymentFeature>();
        this.addedFeatures = new ArrayList<DeploymentFeature>();
        this.updatedFeatures = new ArrayList<DeploymentFeature>();
        this.removedFeatures = new ArrayList<DeploymentFeature>();
        this.listeners = new ArrayList<DeploymentChangeListener>();
    }

    public void addDeploymentFeature(DeploymentFeature feature) {
        if (deploymentFeatures.contains(feature)) {
            throw new IllegalStateException("Adding already present feature: " + feature);
        } else if (removedFeatures.contains(feature)) {
            throw new IllegalStateException("Adding feature already set to be removed in current enforcing operation: " + feature);
        }
        deploymentFeatures.add(feature);
        addedFeatures.add(feature);
    }

    public void updateDeploymentFeature(DeploymentFeature feature) {
        if (!deploymentFeatures.contains(feature)) {
            throw new IllegalStateException("Updating nonexistent feature: " + feature);
        }
        // Removing and adding should be done in case equals is overriden by the feature
        deploymentFeatures.remove(feature);
        deploymentFeatures.add(feature);
        updatedFeatures.add(feature);
    }

    public void removeDeploymentFeature(DeploymentFeature feature) {
        if (!deploymentFeatures.contains(feature)) {
            throw new IllegalStateException("Removing nonexistent feature: " + feature);
        } else if (addedFeatures.contains(feature)) {
            throw new IllegalStateException("Removing feature already set to be added in current enforcing operation: " + feature);
        }
        deploymentFeatures.remove(feature);
        removedFeatures.add(feature);
    }

    public boolean deploymentChanged() {
        return !addedFeatures.isEmpty() || !updatedFeatures.isEmpty() || !removedFeatures.isEmpty();
    }

    public void enforce() throws DeploymentEnforcingException {
        enforcer.enforce(this);
        addedFeatures.clear();
        updatedFeatures.clear();
        removedFeatures.clear();
    }

    public List<DeploymentFeature> getDeploymentFeatures() {
        return deploymentFeatures;
    }

    public List<DeploymentFeature> getAddedFeatures() {
        return addedFeatures;
    }

    public List<DeploymentFeature> getUpdatedFeatures() {
        return updatedFeatures;
    }

    public List<DeploymentFeature> getRemovedFeatures() {
        return removedFeatures;
    }

    public void addDeploymentChangeListener(DeploymentChangeListener listener) {
        listeners.add(listener);
    }

    public void removeDeploymentChangeListener(DeploymentChangeListener listener) {
        listeners.remove(listener);
    }

    protected void fireDeploymentFeatureAdded(DeploymentFeature feature) {
        for (DeploymentChangeListener listener : listeners) {
            listener.deploymentFeatureAdded(feature);
        }
    }

    protected void fireDeploymentFeatureUpdated(DeploymentFeature feature) {
        for (DeploymentChangeListener listener : listeners) {
            listener.deploymentFeatureUpdated(feature);
        }
    }

    protected void fireDeploymentFeatureRemoved(DeploymentFeature feature) {
        for (DeploymentChangeListener listener : listeners) {
            listener.deploymentFeatureRemoved(feature);
        }
    }

}
