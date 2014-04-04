package es.umu.intertrust.policyinterpreter.deployment;

/**
 *
 * @author Juanma
 */
public class DeploymentEnforcingException extends Exception {

    /**
     * Creates a new instance of <code>DeploymentEnforcingException</code>
     * without detail message.
     */
    public DeploymentEnforcingException() {
    }

    /**
     * Constructs an instance of <code>DeploymentEnforcingException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public DeploymentEnforcingException(String msg) {
        super(msg);
    }
}
