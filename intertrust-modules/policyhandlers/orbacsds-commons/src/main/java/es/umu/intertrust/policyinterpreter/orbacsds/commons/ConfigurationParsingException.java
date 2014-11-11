package es.umu.intertrust.policyinterpreter.orbacsds.commons;

import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandlerException;

/**
 *
 * @author Juanma
 */
public class ConfigurationParsingException extends PolicyHandlerException {

    /**
     * Creates a new instance of <code>ConfigurationParsingException</code>
     * without detail message.
     */
    public ConfigurationParsingException() {
    }

    /**
     * Constructs an instance of <code>ConfigurationParsingException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public ConfigurationParsingException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>ConfigurationParsingException</code> with
     * the specified cause.
     *
     * @param cause the cause.
     */
    public ConfigurationParsingException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs an instance of <code>ConfigurationParsingException</code> with
     * the specified detail message and cause.
     *
     * @param msg the detail message.
     * @param cause the cause.
     */
    public ConfigurationParsingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
