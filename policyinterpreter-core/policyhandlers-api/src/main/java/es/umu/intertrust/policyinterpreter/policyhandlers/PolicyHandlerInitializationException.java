package es.umu.intertrust.policyinterpreter.policyhandlers;

/**
 *
 * @author Juanma
 */
public class PolicyHandlerInitializationException extends PolicyHandlerException {

    /**
     * Creates a new instance of
     * <code>PolicyHandlerInitializationException</code> without detail message.
     */
    public PolicyHandlerInitializationException() {
    }

    /**
     * Constructs an instance of
     * <code>PolicyHandlerInitializationException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public PolicyHandlerInitializationException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of
     * <code>PolicyHandlerInitializationException</code> with the specified
     * cause.
     *
     * @param cause the cause.
     */
    public PolicyHandlerInitializationException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs an instance of
     * <code>PolicyHandlerInitializationException</code> with the specified
     * detail message and cause.
     *
     * @param msg the detail message.
     * @param cause the cause.
     */
    public PolicyHandlerInitializationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
