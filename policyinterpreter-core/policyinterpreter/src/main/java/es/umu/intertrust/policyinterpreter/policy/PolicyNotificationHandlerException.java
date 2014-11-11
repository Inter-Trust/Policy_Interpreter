package es.umu.intertrust.policyinterpreter.policy;

/**
 *
 * @author Juanma
 */
public class PolicyNotificationHandlerException extends Exception {

    /**
     * Creates a new instance of <code>PolicyNotificationHandlerException</code>
     * without detail message.
     */
    public PolicyNotificationHandlerException() {
    }

    /**
     * Constructs an instance of <code>PolicyNotificationHandlerException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public PolicyNotificationHandlerException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>PolicyNotificationHandlerException</code>
     * with the specified cause.
     *
     * @param cause the cause.
     */
    public PolicyNotificationHandlerException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs an instance of <code>PolicyNotificationHandlerException</code>
     * with the specified detail message and cause.
     *
     * @param msg the detail message.
     * @param cause the cause.
     */
    public PolicyNotificationHandlerException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
