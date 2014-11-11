package es.umu.intertrust.policyinterpreter.policy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Juanma
 */
public abstract class AbstractPolicyNotificationHandler implements PolicyNotificationHandler {

    Properties policyProperties;
    List<PolicyChangeListener> policyChangeListeners;
    List<PolicyNotificationHandlerListener> notificationHandlerListeners;
    List<ExceptionListener> exceptionListeners;

    public AbstractPolicyNotificationHandler() {
        this.policyChangeListeners = new ArrayList<PolicyChangeListener>();
        this.notificationHandlerListeners = new ArrayList<PolicyNotificationHandlerListener>();
        this.exceptionListeners = new ArrayList<ExceptionListener>();
    }

    public void initialize(Properties policyProperties) throws PolicyNotificationHandlerException {
        this.policyProperties = policyProperties;
    }

    public void addPolicyChangeListener(PolicyChangeListener listener) {
        policyChangeListeners.add(listener);
    }

    public void removePolicyChangeListener(PolicyChangeListener listener) {
        policyChangeListeners.remove(listener);
    }

    public void addPolicyNotificationHandlerListener(PolicyNotificationHandlerListener listener) {
        notificationHandlerListeners.add(listener);
    }

    public void removePolicyNotificationHandlerListener(PolicyNotificationHandlerListener listener) {
        notificationHandlerListeners.remove(listener);
    }

    public void addExceptionListener(ExceptionListener listener) {
        exceptionListeners.add(listener);
    }

    public void removeExceptionListener(ExceptionListener listener) {
        exceptionListeners.remove(listener);
    }

    protected Properties getPolicyProperties() {
        return policyProperties;
    }

    protected void firePolicyStatusChanged(PolicyRuleChange policyRuleChange) {
        firePolicyStatusChanged(Arrays.asList(new PolicyRuleChange[]{policyRuleChange}));
    }

    protected void firePolicyStatusChanged(List<PolicyRuleChange> policyRuleChanges) {
        for (PolicyChangeListener listener : policyChangeListeners) {
            listener.policyStatusChanged(policyRuleChanges);
        }
    }

    protected void fireNotificationHandlerStarted() {
        for (PolicyNotificationHandlerListener listener : notificationHandlerListeners) {
            listener.notificationHandlerStarted();
        }
    }

    protected void fireNotificationHandlerStopped() {
        for (PolicyNotificationHandlerListener listener : notificationHandlerListeners) {
            listener.notificationHandlerStopped();
        }
    }

    protected void fireExceptionOccured(Exception exception) {
        for (ExceptionListener listener : exceptionListeners) {
            listener.exceptionOccured(exception);
        }
    }

}
