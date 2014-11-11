package es.umu.intertrust.policyinterpreter.util.collections;

/**
 *
 * @author Juanma
 */
public class AlreadyAddedException extends TrackChangesListException {

    /**
     * Creates a new instance of <code>AlreadyPresentException</code> without
     * detail message.
     */
    public AlreadyAddedException() {
    }

    /**
     * Constructs an instance of <code>AlreadyPresentException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public AlreadyAddedException(String msg) {
        super(msg);
    }
}
