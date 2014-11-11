package es.umu.intertrust.policyinterpreter.orbac;

import es.umu.intertrust.policyinterpreter.policy.PolicyManager;
import es.umu.intertrust.policyinterpreter.query.PolicyQueryManager;

/**
 *
 * @author Juanma
 */
public class OrbacQueryManager implements PolicyQueryManager {

    public void initialize(PolicyManager policyManager) {
        // Nothing to initialize
    }

    public boolean isPermitted(String subject, String action, String object) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isProhibited(String subject, String action, String object) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isObliged(String subject, String action, String object) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
