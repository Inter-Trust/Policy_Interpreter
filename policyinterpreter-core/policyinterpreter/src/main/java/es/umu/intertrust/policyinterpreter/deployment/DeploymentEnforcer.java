package es.umu.intertrust.policyinterpreter.deployment;

/**
 *
 * @author Juanma
 */
public interface DeploymentEnforcer {

    public void initialize(DeploymentManager manager) throws DeploymentEnforcingException;

    public void enforce() throws DeploymentEnforcingException;
}
