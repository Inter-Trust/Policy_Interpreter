package es.umu.intertrust.policyinterpreter.manager;

/**
 *
 * @author Juanma
 */
public class InterpreterManagerException extends Exception {

    /**
     * Creates a new instance of <code>InterpreterManagerException</code>
     * without detail message.
     */
    public InterpreterManagerException() {
    }

    /**
     * Constructs an instance of <code>InterpreterManagerException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public InterpreterManagerException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>InterpreterManagerException</code> with
     * the specified cause.
     *
     * @param cause the cause.
     */
    public InterpreterManagerException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs an instance of <code>InterpreterManagerException</code> with
     * the specified detail message and cause.
     *
     * @param msg the detail message.
     * @param cause the cause.
     */
    public InterpreterManagerException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
