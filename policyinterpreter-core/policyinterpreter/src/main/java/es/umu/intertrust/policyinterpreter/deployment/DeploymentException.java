package es.umu.intertrust.policyinterpreter.deployment;

/**
 *
 * @author Juanma
 */
public class DeploymentException extends Exception {

    /**
     * Creates a new instance of <code>DeploymentException</code> without detail
     * message.
     */
    public DeploymentException() {
    }

    /**
     * Constructs an instance of <code>DeploymentException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeploymentException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>DeploymentException</code> with the
     * specified cause.
     *
     * @param cause the cause.
     */
    public DeploymentException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs an instance of <code>DeploymentException</code> with the
     * specified detail message and cause.
     *
     * @param msg the detail message.
     * @param cause the cause.
     */
    public DeploymentException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
