package es.umu.intertrust.policyinterpreter.util.collections;

/**
 *
 * @author Juanma
 */
public class RemovingAddedWarning extends TrackChangesListWarning {

    /**
     * Creates a new instance of <code>RemovingAddedWarning</code> without
     * detail message.
     */
    public RemovingAddedWarning() {
    }

    /**
     * Constructs an instance of <code>RemovingAddedWarning</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RemovingAddedWarning(String msg) {
        super(msg);
    }
}
