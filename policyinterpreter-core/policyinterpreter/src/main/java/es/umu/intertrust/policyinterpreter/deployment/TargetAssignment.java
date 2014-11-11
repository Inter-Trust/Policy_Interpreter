package es.umu.intertrust.policyinterpreter.deployment;

/**
 *
 * @author Juanma
 */
public class TargetAssignment {

    Target target;
    DeploymentFeature feature;

    public TargetAssignment(Target target, DeploymentFeature feature) {
        this.target = target;
        this.feature = feature;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public DeploymentFeature getFeature() {
        return feature;
    }

    public void setFeature(DeploymentFeature feature) {
        this.feature = feature;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (this.target != null ? this.target.hashCode() : 0);
        hash = 89 * hash + (this.feature != null ? this.feature.hashCode() : 0);
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
        final TargetAssignment other = (TargetAssignment) obj;
        if (this.target != other.target && (this.target == null || !this.target.equals(other.target))) {
            return false;
        }
        if (this.feature != other.feature && (this.feature == null || !this.feature.equals(other.feature))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "(" + target + ", " + feature + ")";
    }

}
