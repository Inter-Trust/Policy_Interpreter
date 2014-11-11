package es.umu.intertrust.policyinterpreter.util.collections;

/**
 *
 * @author Juanma
 */
public class NotFoundWarning extends TrackChangesListWarning {

    /**
     * Creates a new instance of <code>NotFoundWarning</code> without detail
     * message.
     */
    public NotFoundWarning() {
    }

    /**
     * Constructs an instance of <code>NotFoundWarning</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public NotFoundWarning(String msg) {
        super(msg);
    }
}
