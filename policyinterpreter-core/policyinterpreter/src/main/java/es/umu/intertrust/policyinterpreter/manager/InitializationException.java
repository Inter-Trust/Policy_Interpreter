package es.umu.intertrust.policyinterpreter.manager;

/**
 *
 * @author Juanma
 */
public class InitializationException extends Exception {

    /**
     * Creates a new instance of <code>InitializationException</code> without
     * detail message.
     */
    public InitializationException() {
    }

    /**
     * Constructs an instance of <code>InitializationException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InitializationException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>InitializationException</code> with the
     * specified cause.
     *
     * @param cause the cause.
     */
    public InitializationException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs an instance of <code>InitializationException</code> with the
     * specified detail message and cause.
     *
     * @param msg the detail message.
     * @param cause the cause.
     */
    public InitializationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
