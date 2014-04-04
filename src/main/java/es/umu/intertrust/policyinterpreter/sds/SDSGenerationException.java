package es.umu.intertrust.policyinterpreter.sds;

import es.umu.intertrust.policyinterpreter.deployment.DeploymentEnforcingException;

/**
 *
 * @author Juanma
 */
public class SDSGenerationException extends DeploymentEnforcingException {

    /**
     * Creates a new instance of <code>SDSGenerationException</code> without
     * detail message.
     */
    public SDSGenerationException() {
    }

    /**
     * Constructs an instance of <code>SDSGenerationException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public SDSGenerationException(String msg) {
        super(msg);
    }
}
