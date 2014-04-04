package es.umu.intertrust.policyinterpreter.manager;

/**
 *
 * @author Juanma
 */
public class EngineRuntimeException extends RuntimeException {

    /**
     * Creates a new instance of <code>EngineRuntimeException</code> without
     * detail message.
     */
    public EngineRuntimeException() {
    }

    /**
     * Constructs an instance of <code>EngineRuntimeException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public EngineRuntimeException(String msg) {
        super(msg);
    }
}
