package es.umu.intertrust.policyinterpreter.policyhandlers;

/**
 *
 * @author Juanma
 */
public class PolicyHandlerException extends Exception {

    /**
     * Creates a new instance of <code>PolicyHandlerException</code> without
     * detail message.
     */
    public PolicyHandlerException() {
    }

    /**
     * Constructs an instance of <code>PolicyHandlerException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PolicyHandlerException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>PolicyHandlerException</code> with the
     * specified cause.
     *
     * @param cause the cause.
     */
    public PolicyHandlerException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs an instance of <code>PolicyHandlerException</code> with the
     * specified detail message and cause.
     *
     * @param msg the detail message.
     * @param cause the cause.
     */
    public PolicyHandlerException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
