package es.umu.intertrust.policyinterpreter.orbacsds.commons;

import es.umu.intertrust.policyinterpreter.orbac.Obligation;
import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandlerException;
import es.umu.intertrust.policyinterpreter.sds.SecurityFeature;

/**
 *
 * @author Juanma
 */
public class EmptyOperator implements SecurityFeatureOperator {

    public boolean isOperable(Obligation obligation) {
        return false;
    }

    public boolean processDefaults(SecurityFeature feature) throws PolicyHandlerException {
        return false;
    }

    public boolean processActivation(Obligation obligation, SecurityFeature feature) throws PolicyHandlerException {
        return false;
    }

    public boolean processDeactivation(Obligation obligation, SecurityFeature feature) throws PolicyHandlerException {
        return false;
    }

    public boolean clean(SecurityFeature feature) throws PolicyHandlerException {
        return false;
    }

}
