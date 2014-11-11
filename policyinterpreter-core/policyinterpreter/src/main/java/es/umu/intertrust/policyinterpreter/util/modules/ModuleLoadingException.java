package es.umu.intertrust.policyinterpreter.util.modules;

/**
 *
 * @author Juanma
 */
public class ModuleLoadingException extends Exception {

    /**
     * Creates a new instance of <code>ModuleLoadingException</code> without
     * detail message.
     */
    public ModuleLoadingException() {
    }

    /**
     * Constructs an instance of <code>ModuleLoadingException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ModuleLoadingException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>ModuleLoadingException</code> with the
     * specified cause.
     *
     * @param cause the cause.
     */
    public ModuleLoadingException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs an instance of <code>ModuleLoadingException</code> with the
     * specified detail message and cause.
     *
     * @param msg the detail message.
     * @param cause the cause.
     */
    public ModuleLoadingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
