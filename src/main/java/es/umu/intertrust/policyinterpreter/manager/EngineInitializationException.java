package es.umu.intertrust.policyinterpreter.manager;

/**
 *
 * @author Juanma
 */
public class EngineInitializationException extends Exception {

    /**
     * Creates a new instance of <code>EngineInitializationException</code>
     * without detail message.
     */
    public EngineInitializationException() {
    }

    /**
     * Constructs an instance of <code>EngineInitializationException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public EngineInitializationException(String msg) {
        super(msg);
    }
}
