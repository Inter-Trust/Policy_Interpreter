package es.umu.intertrust.policyinterpreter.sds;

/**
 *
 * @author Juanma
 */
public class SDSSenderException extends Exception {

    /**
     * Creates a new instance of <code>SDSSenderException</code> without detail
     * message.
     */
    public SDSSenderException() {
    }

    /**
     * Constructs an instance of <code>SDSSenderException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public SDSSenderException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>SDSSenderException</code> with the
     * specified cause.
     *
     * @param cause the cause.
     */
    public SDSSenderException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs an instance of <code>SDSSenderException</code> with the
     * specified detail message and cause.
     *
     * @param msg the detail message.
     * @param cause the cause.
     */
    public SDSSenderException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
