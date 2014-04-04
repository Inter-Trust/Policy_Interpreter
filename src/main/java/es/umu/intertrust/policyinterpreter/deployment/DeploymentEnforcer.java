package es.umu.intertrust.policyinterpreter.deployment;

/**
 *
 * @author Juanma
 */
public interface DeploymentEnforcer {

    public void enforce(DeploymentManager manager) throws DeploymentEnforcingException;
}
