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

    /**
     * Constructs an instance of <code>DeploymentEnforcingException</code> with
     * the specified cause.
     *
     * @param cause the cause.
     */
    public DeploymentEnforcingException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs an instance of <code>DeploymentEnforcingException</code> with
     * the specified detail message and cause.
     *
     * @param msg the detail message.
     * @param cause the cause.
     */
    public DeploymentEnforcingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
