package es.umu.intertrust.policyinterpreter.sds;

import es.umu.intertrust.policyinterpreter.deployment.DeploymentFeature;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Juanma
 */
public class SecurityFeature implements DeploymentFeature {

    String id;
    String type;
    String target;
    List<String> functionalities;
    Map<String, String> parameters;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public List<String> getFunctionalities() {
        return functionalities;
    }

    public void setFunctionalities(List<String> functionalities) {
        this.functionalities = functionalities;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "Security Feature"; // TODO
    }

    @Override
    public int hashCode() {
        return 217 + (this.id != null ? this.id.hashCode() : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final SecurityFeature other = (SecurityFeature) obj;
        return (this.id != null) ? this.id.equals(other.id) : (other.id == null);
    }

}
