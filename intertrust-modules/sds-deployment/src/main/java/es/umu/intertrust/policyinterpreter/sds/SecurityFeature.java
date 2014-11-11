package es.umu.intertrust.policyinterpreter.sds;

import es.umu.intertrust.policyinterpreter.deployment.DeploymentFeature;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author Juanma
 */
public class SecurityFeature implements DeploymentFeature {

    String category;
    String id;
    String type;
    List<String> functionalities;
    Map<String, SecurityParameter> parameters;

    public SecurityFeature(String category, String type) {
        this(category, UUID.randomUUID().toString(), type);
    }

    public SecurityFeature(String category, String id, String type) {
        this.category = category;
        this.id = id;
        this.type = type;
        this.functionalities = new ArrayList<String>();
        this.parameters = new HashMap<String, SecurityParameter>();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

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

    public List<String> getFunctionalities() {
        return functionalities;
    }

    public void setFunctionalities(List<String> functionalities) {
        this.functionalities = functionalities;
    }

    public void addFunctionality(String functionality) {
        this.functionalities.add(functionality);
    }

    public void removeFunctionality(String functionality) {
        this.functionalities.remove(functionality);
    }

    public boolean containsFunctionality(String functionality) {
        return this.functionalities.contains(functionality);
    }

    public Collection<SecurityParameter> getParameters() {
        return parameters.values();
    }

    public SecurityParameter getParameter(String name) {
        return this.parameters.get(name);
    }

    public String getParameterValue(String name) {
        SecurityParameter parameter = this.parameters.get(name);
        return (parameter != null) ? parameter.getValue() : null;
    }

    public void setParameters(Collection<SecurityParameter> parameters) {
        this.parameters.clear();
        for (SecurityParameter parameter : parameters) {
            this.parameters.put(parameter.getName(), parameter);
        }
    }

    public void setParameter(String name, String value) {
        this.parameters.put(name, new SecurityParameter(name, value));
    }

    public void setParameter(SecurityParameter parameter) {
        this.parameters.put(parameter.getName(), parameter);
    }

    public void removeParameter(String name) {
        this.parameters.remove(name);
    }

    public boolean containsParameter(String name) {
        return this.parameters.containsKey(name);
    }

    @Override
    public String toString() {
        String str = "Security Feature (id = " + id + "): " + type + "\n";
        for (String functionality : functionalities) {
            str += "   Functionality: " + functionality + "\n";
        }
        str += "   Parameters: " + parameters.values().toString();
        return str;
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
