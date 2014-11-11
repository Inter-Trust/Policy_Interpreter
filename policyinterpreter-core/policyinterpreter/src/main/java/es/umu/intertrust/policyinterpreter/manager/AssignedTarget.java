package es.umu.intertrust.policyinterpreter.manager;

import es.umu.intertrust.policyinterpreter.deployment.DeploymentFeature;
import es.umu.intertrust.policyinterpreter.deployment.Target;

/**
 *
 * @author Juanma
 */
public class AssignedTarget {

    public DeploymentFeature feature;
    public Target target;

    public AssignedTarget(DeploymentFeature feature, Target target) {
        this.feature = feature;
        this.target = target;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + (this.feature != null ? this.feature.hashCode() : 0);
        hash = 19 * hash + (this.target != null ? this.target.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AssignedTarget other = (AssignedTarget) obj;
        if (this.feature != other.feature && (this.feature == null || !this.feature.equals(other.feature))) {
            return false;
        }
        return this.target == other.target || (this.target != null && this.target.equals(other.target));
    }

}
