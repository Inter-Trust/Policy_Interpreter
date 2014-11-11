package es.umu.intertrust.policyinterpreter.util.collections;

/**
 *
 * @author Juanma
 */
public class TrackChangesListWarning extends TrackChangesListException {

    /**
     * Creates a new instance of <code>TrackChangesListWarning</code> without
     * detail message.
     */
    public TrackChangesListWarning() {
    }

    /**
     * Constructs an instance of <code>TrackChangesListWarning</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public TrackChangesListWarning(String msg) {
        super(msg);
    }
}
