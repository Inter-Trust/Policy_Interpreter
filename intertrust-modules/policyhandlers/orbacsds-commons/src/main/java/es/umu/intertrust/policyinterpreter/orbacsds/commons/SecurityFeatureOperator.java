package es.umu.intertrust.policyinterpreter.orbacsds.commons;

import es.umu.intertrust.policyinterpreter.orbac.Obligation;
import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandlerException;
import es.umu.intertrust.policyinterpreter.sds.SecurityFeature;

/**
 *
 * @author Juanma
 */
public interface SecurityFeatureOperator {

    /**
     * Returns whether the obligation specifies a value that is handled by this
     * operator.
     *
     * @param obligation the obligation.
     * @return true if the obligation is processable by this operator. False
     * otherwise.
     */
    public boolean isOperable(Obligation obligation);

    /**
     * Establishes default values in the specified security feature.
     *
     * @param feature the security feature.
     * @return true if the security feature has been changed. False otherwise.
     * @throws PolicyHandlerException if an error occurs.
     */
    public boolean processDefaults(SecurityFeature feature) throws PolicyHandlerException;

    /**
     * Processes the activation of the obligation to set the corresponding
     * values in the specified security feature.
     *
     * @param obligation the obligation.
     * @param feature the security feature.
     * @return true if the security feature has been changed. False otherwise.
     * @throws PolicyHandlerException if an error occurs.
     */
    public boolean processActivation(Obligation obligation, SecurityFeature feature) throws PolicyHandlerException;

    /**
     * Processes the deactivation of the obligation to set the corresponding
     * values in the specified security feature.
     *
     * @param obligation the obligation.
     * @param feature the security feature.
     * @return true if the security feature has been changed. False otherwise.
     * @throws PolicyHandlerException if an error occurs.
     */
    public boolean processDeactivation(Obligation obligation, SecurityFeature feature) throws PolicyHandlerException;

    /**
     * Unsets any value related to this operator in the specified security
     * feature.
     *
     * @param feature the security feature.
     * @return true if the security feature has been changed. False otherwise.
     * @throws PolicyHandlerException if an error occurs.
     */
    public boolean clean(SecurityFeature feature) throws PolicyHandlerException;

}
