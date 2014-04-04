package es.umu.intertrust.policyinterpreter.query;

/**
 *
 * @author Juanma
 */
public class UnsupportedQueryManagerException extends Exception {

    /**
     * Creates a new instance of <code>UnsupportedQueryManagerException</code>
     * without detail message.
     */
    public UnsupportedQueryManagerException() {
    }

    /**
     * Constructs an instance of <code>UnsupportedQueryManagerException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public UnsupportedQueryManagerException(String msg) {
        super(msg);
    }
}
