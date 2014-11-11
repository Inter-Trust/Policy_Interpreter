package es.umu.intertrust.policyinterpreter.preconditions;

/**
 *
 * @author Juanma
 */
public class PreconditionManagementException extends Exception {

    /**
     * Creates a new instance of <code>PreconditionException</code> without
     * detail message.
     */
    public PreconditionManagementException() {
    }

    /**
     * Constructs an instance of <code>PreconditionException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PreconditionManagementException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>PreconditionException</code> with the
     * specified cause.
     *
     * @param cause the cause.
     */
    public PreconditionManagementException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs an instance of <code>PreconditionException</code> with the
     * specified detail message and cause.
     *
     * @param msg the detail message.
     * @param cause the cause.
     */
    public PreconditionManagementException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
