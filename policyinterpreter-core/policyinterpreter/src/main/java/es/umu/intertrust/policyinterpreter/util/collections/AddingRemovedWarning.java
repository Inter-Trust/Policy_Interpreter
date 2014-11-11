package es.umu.intertrust.policyinterpreter.util.collections;

/**
 *
 * @author Juanma
 */
public class AddingRemovedWarning extends TrackChangesListWarning {

    /**
     * Creates a new instance of <code>AddingRemovedWarning</code> without
     * detail message.
     */
    public AddingRemovedWarning() {
    }

    /**
     * Constructs an instance of <code>AddingRemovedWarning</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public AddingRemovedWarning(String msg) {
        super(msg);
    }
}
