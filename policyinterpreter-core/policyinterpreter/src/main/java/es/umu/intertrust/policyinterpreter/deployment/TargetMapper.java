package es.umu.intertrust.policyinterpreter.deployment;

import es.umu.intertrust.policyinterpreter.util.collections.ListHashMap;
import es.umu.intertrust.policyinterpreter.util.collections.ListMap;
import java.util.List;

/**
 * Maps policy rule targets to deployment targets
 *
 * @author Juanma
 */
public class TargetMapper {

    ListMap<String, String> policyToDeployment;
    ListMap<String, String> deploymentToPolicy;

    public TargetMapper() {
        this.policyToDeployment = new ListHashMap<String, String>();
        this.deploymentToPolicy = new ListHashMap<String, String>();
    }

    public List<String> getDeploymentTargets(String policyTarget) {
        return policyToDeployment.get(policyTarget);
    }

    public void addDeploymentTarget(String policyTarget, String deploymentTarget) {
        addIfNotExists(policyToDeployment.getList(policyTarget), deploymentTarget);
        addIfNotExists(deploymentToPolicy.getList(deploymentTarget), policyTarget);
    }

    public void addDeploymentTargets(String policyTarget, List<String> deploymentTargets) {
        List<String> currentTargets = policyToDeployment.getList(policyTarget);
        for (String deploymentTarget : deploymentTargets) {
            addIfNotExists(currentTargets, deploymentTarget);
            addIfNotExists(deploymentToPolicy.getList(deploymentTarget), policyTarget);
        }
    }

    public List<String> getPolicyTargets(String deploymentTarget) {
        return deploymentToPolicy.get(deploymentTarget);
    }

    public void addPolicyTarget(String deploymentTarget, String policyTarget) {
        addIfNotExists(deploymentToPolicy.getList(deploymentTarget), policyTarget);
        addIfNotExists(policyToDeployment.getList(policyTarget), deploymentTarget);
    }

    public void addPolicyTargets(String deploymentTarget, List<String> policyTargets) {
        List<String> currentTargets = deploymentToPolicy.getList(deploymentTarget);
        for (String policyTarget : policyTargets) {
            addIfNotExists(currentTargets, policyTarget);
            addIfNotExists(policyToDeployment.getList(policyTarget), deploymentTarget);
        }
    }

    private void addIfNotExists(List<String> list, String value) {
        if (!list.contains(value)) {
            list.add(value);
        }
    }
}
