package es.umu.intertrust.policyinterpreter.deployment;

import es.umu.intertrust.policyinterpreter.util.collections.AddingRemovedWarning;
import es.umu.intertrust.policyinterpreter.util.collections.AlreadyAddedException;
import es.umu.intertrust.policyinterpreter.util.collections.RemovingAddedWarning;
import es.umu.intertrust.policyinterpreter.util.collections.ListHashMap;
import es.umu.intertrust.policyinterpreter.util.collections.ListMap;
import es.umu.intertrust.policyinterpreter.util.collections.NotFoundException;
import es.umu.intertrust.policyinterpreter.util.collections.NotFoundWarning;
import es.umu.intertrust.policyinterpreter.util.collections.TrackChangesList;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juanma
 */
public class DeploymentManager {

    public static final Logger logger = Logger.getLogger(DeploymentManager.class.getName());

    Properties properties;
    TrackChangesList<DeploymentFeature> deploymentFeatures;
    TrackChangesList<TargetAssignment> targetAssignments;
    ListMap<Target, DeploymentFeature> featuresByTarget;
    ListMap<DeploymentFeature, Target> targetsByFeature;
    DeploymentEnforcer enforcer;
    List<DeploymentChangeListener> listeners;

    public DeploymentManager(Properties properties) {
        this.properties = properties;
        this.deploymentFeatures = new TrackChangesList<DeploymentFeature>();
        this.targetAssignments = new TrackChangesList<TargetAssignment>();
        this.targetsByFeature = new ListHashMap<DeploymentFeature, Target>();
        this.featuresByTarget = new ListHashMap<Target, DeploymentFeature>();
        this.listeners = new ArrayList<DeploymentChangeListener>();
    }

    public Properties getDeploymentProperties() {
        return properties;
    }

    public DeploymentEnforcer getEnforcer() {
        if (enforcer == null) {
            throw new IllegalStateException("Enforcer not set.");
        }
        return enforcer;
    }

    public void setEnforcer(DeploymentEnforcer enforcer) {
        this.enforcer = enforcer;
    }

    public void addDeploymentFeature(DeploymentFeature feature, Target target) {
        if (!deploymentFeatures.contains(feature)) {
            addDeploymentFeatureInternal(feature);
        }
        addFeatureToTarget(feature, target);
    }

    public void addDeploymentFeature(DeploymentFeature feature, List<Target> targets) {
        if (!deploymentFeatures.contains(feature)) {
            addDeploymentFeatureInternal(feature);
        }
        addFeatureToTargets(feature, targets);
    }

    private void addDeploymentFeatureInternal(DeploymentFeature feature) {
        try {
            deploymentFeatures.add(feature);
            logger.log(Level.FINE, "Deployment feature added: {0}", feature);
            fireDeploymentFeatureAdded(feature);
        } catch (AlreadyAddedException ex) {
            throw new IllegalStateException("Adding already present feature: " + feature);
        } catch (AddingRemovedWarning ex) {
            logger.log(Level.WARNING, "Adding feature already set to be removed in current enforcing operation: {0}", feature);
        }
    }

    public void updateDeploymentFeature(DeploymentFeature feature) {
        try {
            deploymentFeatures.update(feature);
            logger.log(Level.FINE, "Deployment feature updated: {0}", feature);
            fireDeploymentFeatureUpdated(feature);
        } catch (NotFoundException ex) {
            throw new IllegalStateException("Updating nonexistent feature: " + feature);
        }
    }

    public void removeDeploymentFeature(DeploymentFeature feature, Target target) {
        removeFeatureFromTarget(feature, target);
        if (getTargetsByFeature(feature).isEmpty()) {
            removeDeploymentFeature(feature);
        }
    }

    public void removeDeploymentFeature(DeploymentFeature feature, List<Target> targets) {
        removeFeatureFromTargets(feature, targets);
        if (getTargetsByFeature(feature).isEmpty()) {
            removeDeploymentFeature(feature);
        }
    }

    public void removeDeploymentFeature(DeploymentFeature feature) {
        removeFeatureFromTargets(feature, getTargetsByFeature(feature));
        removeDeploymentFeatureInternal(feature);
    }

    private void removeDeploymentFeatureInternal(DeploymentFeature feature) {
        try {
            deploymentFeatures.remove(feature);
            logger.log(Level.FINE, "Deployment feature removed: {0}", feature);
            fireDeploymentFeatureRemoved(feature);
        } catch (NotFoundWarning ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Removal of nonexistent feature: {0}", feature);
        } catch (RemovingAddedWarning ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Removing feature already set to be added in current enforcing operation: {0}", feature);
        }
    }

    private void addFeatureToTarget(DeploymentFeature feature, Target target) {
        TargetAssignment assignment = new TargetAssignment(target, feature);
        try {
            targetAssignments.add(assignment);
            logger.log(Level.FINER, "Target added to deployment feature: {0} - {1}", new Object[]{target, feature});
            fireDeploymentFeatureAddedToTarget(feature, target);
        } catch (AlreadyAddedException ex) {
            throw new IllegalStateException("Adding already added feature to target: " + assignment);
        } catch (AddingRemovedWarning ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Adding feature to target already set to be removed in current enforcing operation: {0}", feature);
        }
        featuresByTarget.addToList(target, feature);
        targetsByFeature.addToList(feature, target);
    }

    private void addFeatureToTargets(DeploymentFeature feature, List<Target> targets) {
        for (Target target : targets) {
            addFeatureToTarget(feature, target);
        }
    }

    private void removeFeatureFromTarget(DeploymentFeature feature, Target target) {
        TargetAssignment assignment = new TargetAssignment(target, feature);
        try {
            targetAssignments.remove(assignment);
            logger.log(Level.FINER, "Target removed from deployment feature: {0} - {1}", new Object[]{target, feature});
            fireDeploymentFeatureRemovedFromTarget(feature, target);
        } catch (NotFoundWarning ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Removal of unasigned feature from target: {0}", assignment);
        } catch (RemovingAddedWarning ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Removing feature from target already set to be added in current enforcing operation: {0}", assignment);
        }
        featuresByTarget.removeFromList(target, feature);
        targetsByFeature.removeFromList(feature, target);
    }

    private void removeFeatureFromTargets(DeploymentFeature feature, List<Target> targets) {
        for (Target target : new ArrayList<Target>(targets)) {
            removeFeatureFromTarget(feature, target);
        }
    }

    public boolean deploymentChanged() {
        return deploymentFeatures.hasChanges() || targetAssignments.hasChanges();
    }

    public void enforce() throws DeploymentEnforcingException {
        logger.log(Level.INFO, "Enforcing deployment features.");
        getEnforcer().enforce();
        deploymentFeatures.cleanChanges();
        targetAssignments.cleanChanges();
    }

    public List<DeploymentFeature> getDeploymentFeatures() {
        return deploymentFeatures.getElements();
    }

    public List<DeploymentFeature> getAddedFeatures() {
        return deploymentFeatures.getAddedElements();
    }

    public List<DeploymentFeature> getUpdatedFeatures() {
        return deploymentFeatures.getUpdatedElements();
    }

    public List<DeploymentFeature> getRemovedFeatures() {
        return deploymentFeatures.getRemovedElements();
    }

    public List<Target> getTargetsByFeature(DeploymentFeature deploymentFeature) {
        return targetsByFeature.getList(deploymentFeature);
    }

    public List<DeploymentFeature> getFeaturesByTarget(Target target) {
        return featuresByTarget.getList(target);
    }

    public List<TargetAssignment> getTargetAssignments() {
        return targetAssignments.getElements();
    }

    public List<TargetAssignment> getAddedTargetAssignments() {
        return targetAssignments.getAddedElements();
    }

    public List<TargetAssignment> getRemovedTargetAssignments() {
        return targetAssignments.getRemovedElements();
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

    protected void fireDeploymentFeatureAddedToTarget(DeploymentFeature feature, Target target) {
        for (DeploymentChangeListener listener : listeners) {
            listener.deploymentFeatureAddedToTarget(feature, target);
        }
    }

    protected void fireDeploymentFeatureRemovedFromTarget(DeploymentFeature feature, Target target) {
        for (DeploymentChangeListener listener : listeners) {
            listener.deploymentFeatureRemovedFromTarget(feature, target);
        }
    }

}
