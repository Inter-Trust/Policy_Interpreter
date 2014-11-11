package es.umu.intertrust.policyinterpreter.util.collections;

/**
 *
 * @author Juanma
 */
public class NotFoundException extends TrackChangesListException {

    /**
     * Creates a new instance of <code>NotFoundException</code> without detail
     * message.
     */
    public NotFoundException() {
    }

    /**
     * Constructs an instance of <code>NotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NotFoundException(String msg) {
        super(msg);
    }
}
