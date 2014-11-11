package es.umu.intertrust.policyinterpreter.manager;

/**
 *
 * @author Juanma
 */
public class EngineException extends Exception {

    /**
     * Creates a new instance of <code>EngineException</code> without detail
     * message.
     */
    public EngineException() {
    }

    /**
     * Constructs an instance of <code>EngineException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public EngineException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>EngineException</code> with the specified
     * cause.
     *
     * @param cause the cause.
     */
    public EngineException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs an instance of <code>EngineException</code> with the specified
     * detail message and cause.
     *
     * @param msg the detail message.
     * @param cause the cause.
     */
    public EngineException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
