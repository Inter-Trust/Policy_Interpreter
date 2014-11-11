package es.umu.intertrust.policyinterpreter.util.modules;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Juanma
 */
public class ModuleLoadingExceptions extends Exception {

    Collection<ModuleLoadingException> causes;

    /**
     * Creates a new instance of <code>ModuleLoadingExceptions</code> without
     * detail message.
     */
    public ModuleLoadingExceptions() {
        this.causes = new ArrayList<ModuleLoadingException>();
    }

    /**
     * Constructs an instance of <code>ModuleLoadingExceptions</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ModuleLoadingExceptions(String msg) {
        super(msg);
        this.causes = new ArrayList<ModuleLoadingException>();
    }

    /**
     * Constructs an instance of <code>ModuleLoadingExceptions</code> with the
     * specified cause.
     *
     * @param cause the cause.
     */
    public ModuleLoadingExceptions(ModuleLoadingException cause) {
        this.causes = new ArrayList<ModuleLoadingException>();
        this.causes.add(cause);
    }

    /**
     * Constructs an instance of <code>ModuleLoadingExceptions</code> with the
     * specified detail message and cause.
     *
     * @param msg the detail message.
     * @param cause the cause.
     */
    public ModuleLoadingExceptions(String msg, ModuleLoadingException cause) {
        super(msg);
        this.causes = new ArrayList<ModuleLoadingException>();
        this.causes.add(cause);
    }

    /**
     * Constructs an instance of <code>ModuleLoadingExceptions</code> with the
     * specified causes.
     *
     * @param causes the causes.
     */
    public ModuleLoadingExceptions(Collection<ModuleLoadingException> causes) {
        this.causes = causes;
    }

    /**
     * Constructs an instance of <code>ModuleLoadingExceptions</code> with the
     * specified detail message and causes.
     *
     * @param msg the detail message.
     * @param causes the causes.
     */
    public ModuleLoadingExceptions(String msg, Collection<ModuleLoadingException> causes) {
        super(msg);
        this.causes = causes;
    }

    public void addCause(ModuleLoadingException cause) {
        this.causes.add(cause);
    }

    public Collection<ModuleLoadingException> getCauses() {
        return causes;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage() + " Reasons:";
        for (ModuleLoadingException cause : causes) {
            message += "\n - " + cause.getMessage();
        }
        return message;
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
        for (ModuleLoadingException cause : causes) {
            cause.printStackTrace();
        }
    }

}
